# Etapa 1: Construção (Builder)
FROM python:3.9-slim AS builder

ARG CONNECTIONSTRING

ENV CONNECTIONSTRING=$CONNECTIONSTRING
WORKDIR /app

# Copiar o arquivo requirements.txt para o diretório de trabalho
COPY requirements.txt /app/

# Instalar as dependências Python
RUN pip install --upgrade pip
RUN pip install --no-cache-dir -r requirements.txt

# Etapa 2: Imagem Final (Alvo)
FROM python:3.9-slim

ARG CONNECTIONSTRING

ENV CONNECTIONSTRING=$CONNECTIONSTRING
WORKDIR /app

RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libatlas-base-dev \
    && rm -rf /var/lib/apt/lists/*

# Copiar os pacotes instalados da etapa de construção
COPY --from=builder /app /app
# this is for sure required
COPY --from=builder /usr/local/lib/python3.9/site-packages /usr/local/lib/python3.9/site-packages
# this depends on what you are installing
COPY --from=builder /usr/local/bin /usr/local/bin

# Copiar o restante dos arquivos da aplicação para o diretório de trabalho
COPY src/ /app/
COPY weights/ /root/.deepface/weights

# Expor a porta 8000
EXPOSE 8000

# Comando para rodar o servidor FastAPI com Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]