import json
import logging
import os

import pika
from pipeline import EmbeddingEngine, PDFProcessor, VectorStore

logging.basicConfig(level=logging.INFO)
log = logging.getLogger("processor")

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://postgres:postgres@postgres:5432/drag_db")
EMBEDDING_MODEL = os.getenv("EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2")
UPLOAD_DIR = os.getenv("FILE_UPLOAD_DIR", "/data/uploads")
RABBITMQ_URL = os.getenv("RABBITMQ_URL", "amqp://guest:guest@rabbitmq:5672/%2F")
EXCHANGE = "documents.exchange"
QUEUE = "pdf-processing-queue"
ROUTING_KEY = "document.uploaded"

processor = PDFProcessor(chunk_size=400, chunk_overlap=50)
embedder = EmbeddingEngine(model_name=EMBEDDING_MODEL)
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

    return {
        "eventId": "",
        "documentId": document_id,
        "filename": filename,
        "chunkCount": len(chunks),
        "vectorCount": len(chunks),
        "storagePath": storage_path,
    }


def on_message(channel, method, _properties, body: bytes) -> None:
    event = json.loads(body)
    document_id = event.get("documentId")

    try:
        result = process(document_id, event.get("filename"), event.get("storagePath"))
        publish(channel, "document.processed", result)
        log.info("processed document %s (%d chunks)", document_id, result["chunkCount"])
    except Exception as exc:
        log.exception("failed to process document %s", document_id)
        publish(channel, "document.failed", {"eventId": "", "documentId": document_id, "reason": str(exc)})

    channel.basic_ack(delivery_tag=method.delivery_tag)


def main() -> None:
    connection = pika.BlockingConnection(pika.URLParameters(RABBITMQ_URL))
    channel = connection.channel()
    channel.exchange_declare(exchange=EXCHANGE, exchange_type="topic", durable=True)
    channel.queue_declare(queue=QUEUE, durable=True)
    channel.queue_bind(queue=QUEUE, exchange=EXCHANGE, routing_key=ROUTING_KEY)
    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue=QUEUE, on_message_callback=on_message)

    log.info("listening on %s (routing key '%s')", QUEUE, ROUTING_KEY)
    channel.start_consuming()


if __name__ == "__main__":
    main()
