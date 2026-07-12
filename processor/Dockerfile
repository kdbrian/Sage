FROM ghcr.io/astral-sh/uv:python3.11-bookworm-slim

WORKDIR /app

COPY pyproject.toml uv.lock ./

RUN --mount=type=cache,target=/root/.cache/uv \
    uv sync --frozen --no-dev

ENV HF_HOME=/app/models/.huggingface

# Ungated by default so the image builds with no HuggingFace auth. Override
# with --build-arg EMBEDDING_MODEL=... (and set EMBEDDING_MODEL in .env to
# match) to pre-bake a different model; gated models additionally need
# HF_TOKEN set in the build environment.
ARG EMBEDDING_MODEL=sentence-transformers/all-MiniLM-L6-v2
ARG SUMMARIZATION_MODEL=sshleifer/distilbart-cnn-12-6

RUN .venv/bin/python -c "\
from sentence_transformers import SentenceTransformer; \
SentenceTransformer('${EMBEDDING_MODEL}')"

RUN .venv/bin/python -c "\
from transformers import pipeline; \
pipeline('summarization', model='${SUMMARIZATION_MODEL}')"

COPY . .

# Internal-only: reachable on the Docker network for spring-app's chat query
# embedding calls, never published to the host or routed through the gateway.
EXPOSE 8000

CMD [".venv/bin/python", "main.py"]