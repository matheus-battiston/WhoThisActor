# --- Stage 1: Builder ---
FROM python:3.9-slim AS builder
WORKDIR /app

ARG OCI_PAR_BASE_URL
ENV OCI_PAR_BASE_URL=$OCI_PAR_BASE_URL
ENV PYTHONPATH=/app

COPY requirements.txt /app/

RUN pip install --upgrade pip && \
    pip install --no-cache-dir --no-compile -r requirements.txt && \
    pip cache purge

COPY src/ /app/

RUN python -m app.pipeline.gerar_faiss


# --- Stage 2: Runtime ---
FROM python:3.9-slim
WORKDIR /app

ENV PYTHONPATH=/app

RUN apt-get update && apt-get install -y \
    libgl1 \
    libglib2.0-0 \
    libatlas3-base \
    libsm6 \
    libxext6 \
    libxrender1 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /usr/local/lib/python3.9/site-packages /usr/local/lib/python3.9/site-packages
COPY --from=builder /usr/local/bin /usr/local/bin

COPY src/ /app/

COPY --from=builder /app/faiss_index_pca.bin /app/faiss_index_pca.bin
COPY --from=builder /app/faiss_labels_pca.pkl /app/faiss_labels_pca.pkl
COPY --from=builder /app/faiss_index_raw.bin /app/faiss_index_raw.bin
COPY --from=builder /app/faiss_labels_raw.pkl /app/faiss_labels_raw.pkl
COPY --from=builder /app/pca_projection.pkl /app/pca_projection.pkl

EXPOSE 8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
