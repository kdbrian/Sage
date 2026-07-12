import re
from collections import Counter
from typing import List
from pypdf import PdfReader
from sentence_transformers import SentenceTransformer
from transformers import pipeline
import psycopg
from pgvector import Vector
from pgvector.psycopg import register_vector

# Small hardcoded stopword list -- topic extraction only needs to filter out
# noise words, not full linguistic accuracy, so no extra NLP dependency.
_STOPWORDS = {
    "the", "and", "for", "are", "but", "not", "you", "all", "any", "can",
    "had", "her", "was", "one", "our", "out", "day", "get", "has", "him",
    "his", "how", "man", "new", "now", "old", "see", "two", "way", "who",
    "boy", "did", "its", "let", "put", "say", "she", "too", "use", "with",
    "this", "that", "from", "have", "will", "your", "they", "been", "were",
    "into", "than", "them", "then", "these", "those", "which", "about",
    "would", "there", "their", "what", "when", "where", "while", "such",
    "also", "some", "each", "more", "most", "other", "over", "after",
    "being", "both", "could", "should", "does", "doing", "during",
    "further", "here", "itself", "just", "only", "same", "very", "within",
}


# 1. PREPROCESSING CLASS

class PDFProcessor:
    def __init__(self, chunk_size: int = 500, chunk_overlap: int = 50):
        self.chunk_size = chunk_size
        self.chunk_overlap = chunk_overlap

    def extract_from_file(self, path: str) -> str:
        """Extracts raw text from a PDF already on the shared uploads volume."""
        reader = PdfReader(path)
        return "\n".join(filter(None, (page.extract_text() for page in reader.pages)))

    def clean_text(self, text: str) -> str:
        """Cleans whitespaces, removes null bytes, and normalizes text structure."""
        text = text.replace("\x00", "")  # Remove null characters for Postgres safety
        text = re.sub(r'\s+', ' ', text)  # Normalize whitespaces/newlines
        return text.strip()

    def chunk_text(self, text: str) -> List[str]:
        """Splits raw text into overlapping chunks based on words."""
        words = text.split(" ")
        chunks = []

        i = 0
        while i < len(words):
            chunk_words = words[i : i + self.chunk_size]
            chunks.append(" ".join(chunk_words))
            if i + self.chunk_size >= len(words):
                break
            i += self.chunk_size - self.chunk_overlap

        return chunks


# 2. EMBEDDING CLASS

class EmbeddingEngine:
    def __init__(self, model_name: str = None):
        # Fallback to a default if env variable is missing
        self.model_name = model_name or "sentence-transformers/all-MiniLM-L6-v2"
        self.model = SentenceTransformer(self.model_name)
        # Dynamic dimension retrieval based on the model chosen
        self.dimension = self.model.get_sentence_embedding_dimension()

    def generate_embeddings(self, texts: List[str]) -> List[List[float]]:
        """Converts an array of text chunks into raw float embeddings."""
        if not texts:
            return []
        embeddings = self.model.encode(texts, convert_to_numpy=True)
        return embeddings.tolist()

    def embed_query(self, text: str) -> List[float]:
        """Embeds a single piece of text (a chat query) into the same vector space."""
        return self.model.encode([text], convert_to_numpy=True)[0].tolist()


# 2b. SUMMARIZATION CLASS
# Runs during processing only (not on the query path) -- one summarization per
# uploaded document, not per chat request.

class Summarizer:
    # Input text is truncated before summarizing: distilbart's encoder has a
    # 1024-token limit, and a single-pass summary over the opening of a
    # document is a reasonable tradeoff against full map-reduce summarization
    # for documents that exceed it.
    MAX_INPUT_CHARS = 4000

    def __init__(self, model_name: str = None):
        self.model_name = model_name or "sshleifer/distilbart-cnn-12-6"
        self.pipeline = pipeline("summarization", model=self.model_name)

    def summarize(self, text: str) -> str:
        """Produces a short abstractive summary of the document's opening text."""
        truncated = text[: self.MAX_INPUT_CHARS].strip()
        if len(truncated) < 200:
            # Too little material for the summarizer to do anything useful with.
            return truncated
        result = self.pipeline(truncated, max_length=120, min_length=30, do_sample=False)
        return result[0]["summary_text"].strip()


def extract_topics(summary: str, max_topics: int = 3) -> List[str]:
    """Derives short topic labels from a summary via word frequency, filtering
    stopwords and short/non-alphabetic tokens. Deliberately simple (no extra
    NLP model) since these are meant as coarse document categories, not
    precision keyword extraction."""
    words = re.findall(r"[A-Za-z][A-Za-z\-]{3,}", summary.lower())
    significant = [w for w in words if w not in _STOPWORDS]
    if not significant:
        return []
    counts = Counter(significant)
    return [word.title() for word, _ in counts.most_common(max_topics)]


# 3. VECTOR STORAGE CLASS (PGVECTOR)
# Shares the same Postgres instance/database as the Spring ingestion service so
# chunks stay tied to the `documents` rows it owns via `document_id`.

class VectorStore:
    def __init__(self, db_url: str, vector_dim: int):
        self.db_url = db_url
        self.vector_dim = vector_dim
        self._init_db()

    def _get_connection(self):
        """Returns a standard psycopg connection."""
        return psycopg.connect(self.db_url)

    def _init_db(self):
        """Creates the extension and necessary table schemas if missing."""
        with self._get_connection() as conn:
            with conn.cursor() as cur:
                # Enable pgvector extension
                cur.execute("CREATE EXTENSION IF NOT EXISTS vector;")
                # Register vector type dynamically for psycopg map handling
                register_vector(conn)

                # Build table schema. No FK to `documents` on purpose: Hibernate
                # (the Spring app) owns and migrates that table independently, and
                # this service must not depend on it existing first at boot.
                cur.execute(f"""
                    CREATE TABLE IF NOT EXISTS pdf_document_chunks (
                        id SERIAL PRIMARY KEY,
                        document_id TEXT NOT NULL,
                        chunk_index INT NOT NULL,
                        chunk_content TEXT NOT NULL,
                        embedding vector({self.vector_dim}),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """)
                cur.execute(
                    "CREATE INDEX IF NOT EXISTS idx_pdf_chunks_document_id "
                    "ON pdf_document_chunks (document_id);"
                )
                conn.commit()

    def save_chunks(self, document_id: str, chunks: List[str], embeddings: List[List[float]]):
        """Persists text chunks and their related vectors, replacing any prior run for this document."""
        with self._get_connection() as conn:
            register_vector(conn)  # Register vector support on this session
            with conn.cursor() as cur:
                cur.execute("DELETE FROM pdf_document_chunks WHERE document_id = %s", (document_id,))
                with cur.copy(
                    "COPY pdf_document_chunks (document_id, chunk_index, chunk_content, embedding) FROM STDIN"
                ) as copy:
                    for index, (chunk, embedding) in enumerate(zip(chunks, embeddings)):
                        # COPY resolves each value's serializer by its Python type,
                        # not by the target column's type -- a plain list gets
                        # psycopg's generic array dumper ("{...}"), not pgvector's
                        # bracket format ("[...]"), even with register_vector() on
                        # the connection. Wrapping in Vector forces the right one.
                        copy.write_row((document_id, index, chunk, Vector(embedding)))
                conn.commit()
