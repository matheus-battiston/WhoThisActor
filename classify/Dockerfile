FROM python:3.9-slim

ARG CONNECTIONSTRING

ENV CONNECTIONSTRING=$CONNECTIONSTRING
WORKDIR /app

RUN apt-get update && apt-get install -y \
    build-essential \
    python3-dev \
    gcc \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libatlas-base-dev \
    && rm -rf /var/lib/apt/lists/*

# Copiar o arquivo requirements.txt para o diretório de trabalho
COPY requirements.txt /app/

RUN pip install --upgrade pip
RUN pip install --no-cache-dir -r requirements.txt

# Copiar o arquivo embeddings_combinados.pkl baixado da Azure Blob para o diretório raiz do container
COPY embeddings_combinados.pkl /app/embeddings_combinados.pkl

# Copiar o restante dos arquivos da aplicação para o diretório de trabalho
COPY . /app/

EXPOSE 8000

# Comando para rodar o servidor FastAPI com Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]