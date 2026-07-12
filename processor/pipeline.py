import re
from typing import List
from pypdf import PdfReader
from sentence_transformers import SentenceTransformer
import psycopg
from pgvector.psycopg import register_vector


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
                        copy.write_row((document_id, index, chunk, embedding))
                conn.commit()
