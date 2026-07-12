import json
import logging
import os
import threading

import pika
import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel
from pipeline import EmbeddingEngine, PDFProcessor, Summarizer, VectorStore, extract_topics

logging.basicConfig(level=logging.INFO)
log = logging.getLogger("processor")

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://postgres:postgres@postgres:5432/drag_db")
EMBEDDING_MODEL = os.getenv("EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2")
SUMMARIZATION_MODEL = os.getenv("SUMMARIZATION_MODEL", "sshleifer/distilbart-cnn-12-6")
UPLOAD_DIR = os.getenv("FILE_UPLOAD_DIR", "/data/uploads")
RABBITMQ_URL = os.getenv("RABBITMQ_URL", "amqp://guest:guest@rabbitmq:5672/%2F")
HTTP_PORT = int(os.getenv("HTTP_PORT", "8000"))
EXCHANGE = "documents.exchange"
QUEUE = "pdf-processing-queue"
ROUTING_KEY = "document.uploaded"

processor = PDFProcessor(chunk_size=400, chunk_overlap=50)
embedder = EmbeddingEngine(model_name=EMBEDDING_MODEL)
summarizer = Summarizer(model_name=SUMMARIZATION_MODEL)
vector_store = VectorStore(db_url=DATABASE_URL, vector_dim=embedder.dimension)


def publish(channel, routing_key: str, payload: dict) -> None:
    channel.basic_publish(
        exchange=EXCHANGE,
        routing_key=routing_key,
        body=json.dumps(payload).encode(),
        properties=pika.BasicProperties(content_type="application/json", delivery_mode=2),
    )


def process(document_id: str, filename: str, storage_path: str) -> dict:
    path = os.path.join(UPLOAD_DIR, storage_path)
    text = processor.clean_text(processor.extract_from_file(path))
    chunks = processor.chunk_text(text)

    if chunks:
        embeddings = embedder.generate_embeddings(chunks)
        vector_store.save_chunks(document_id, chunks, embeddings)

    summary = summarizer.summarize(text) if text else ""
    topics = extract_topics(summary) if summary else []

    return {
        "eventId": "",
        "documentId": document_id,
        "filename": filename,
        "chunkCount": len(chunks),
        "vectorCount": len(chunks),
        "storagePath": storage_path,
        "summary": summary,
        "topics": topics,
    }


def on_message(channel, method, _properties, body: bytes) -> None:
    event = json.loads(body)
    document_id = event.get("documentId")

    try:
        result = process(document_id, event.get("filename"), event.get("storagePath"))
        publish(channel, "document.processed", result)
        log.info(
            "processed document %s (%d chunks, %d topics)",
            document_id, result["chunkCount"], len(result["topics"]),
        )
    except Exception as exc:
        log.exception("failed to process document %s", document_id)
        publish(channel, "document.failed", {"eventId": "", "documentId": document_id, "reason": str(exc)})

    channel.basic_ack(delivery_tag=method.delivery_tag)


def consume_forever() -> None:
    """Runs the blocking RabbitMQ consume loop; called on a background thread
    so the HTTP server (see below) can run in the foreground."""
    connection = pika.BlockingConnection(pika.URLParameters(RABBITMQ_URL))
    channel = connection.channel()
    channel.exchange_declare(exchange=EXCHANGE, exchange_type="topic", durable=True)
    channel.queue_declare(queue=QUEUE, durable=True)
    channel.queue_bind(queue=QUEUE, exchange=EXCHANGE, routing_key=ROUTING_KEY)
    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue=QUEUE, on_message_callback=on_message)

    log.info("listening on %s (routing key '%s')", QUEUE, ROUTING_KEY)
    channel.start_consuming()


# Internal-only HTTP surface (reachable on the Docker network, never published
# to the host or routed through the gateway): lets spring-app embed a chat
# query with the exact same model used on stored chunks, without duplicating
# the model in the JVM.
app = FastAPI()


class EmbedRequest(BaseModel):
    text: str


class EmbedResponse(BaseModel):
    embedding: list[float]
    dimension: int


@app.post("/embed", response_model=EmbedResponse)
def embed(request: EmbedRequest) -> EmbedResponse:
    vector = embedder.embed_query(request.text)
    return EmbedResponse(embedding=vector, dimension=len(vector))


@app.get("/health")
def health() -> dict:
    return {"status": "ok"}


def main() -> None:
    consumer_thread = threading.Thread(target=consume_forever, name="rabbitmq-consumer", daemon=True)
    consumer_thread.start()

    uvicorn.run(app, host="0.0.0.0", port=HTTP_PORT, log_level="info")


if __name__ == "__main__":
    main()
