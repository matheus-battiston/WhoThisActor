FROM python:3.9-slim AS builder

ARG CONNECTIONSTRING

ENV CONNECTIONSTRING=$CONNECTIONSTRING
WORKDIR /app

COPY requirements.txt /app/

RUN pip install --upgrade pip && \
    pip install --no-cache-dir --no-compile -r requirements.txt \
    && pip cache purge

FROM python:3.9-slim

ARG CONNECTIONSTRING

ENV CONNECTIONSTRING=$CONNECTIONSTRING
WORKDIR /app

RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libatlas-base-dev \
    && rm -rf /var/lib/apt/lists/*


# COPY --from=builder /app /app
COPY --from=builder /usr/local/lib/python3.9/site-packages /usr/local/lib/python3.9/site-packages
COPY --from=builder /usr/local/bin /usr/local/bin

COPY src/ /app/

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]