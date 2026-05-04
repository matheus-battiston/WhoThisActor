import os
import pickle
import numpy as np
import faiss
import requests

from app.core.config import settings
from app.ml.embeddings.pca_projection import fit_pca, save_projection, transform_embeddings


EMBEDDINGS_FILE = settings.base_dir / "embeddings" / "emb.pkl"
FAISS_INDEX_PCA_FILE = settings.faiss_index_pca_path
FAISS_LABELS_PCA_FILE = settings.faiss_labels_pca_path
FAISS_INDEX_RAW_FILE = settings.faiss_index_raw_path
FAISS_LABELS_RAW_FILE = settings.faiss_labels_raw_path

def download_embeddings_from_oracle():
    EMBEDDINGS_FILE.parent.mkdir(parents=True, exist_ok=True)

    download_from_oracle(
        object_name="emb.pkl",
        output_path=EMBEDDINGS_FILE
    )

def download_from_oracle(object_name: str, output_path: str):
    base_url = os.getenv("OCI_PAR_BASE_URL")

    if not base_url:
        raise ValueError("OCI_PAR_BASE_URL não definida")

    if not base_url.endswith("/"):
        base_url += "/"

    url = base_url + object_name

    print(url)

    print(f"Baixando {object_name}...")

    with requests.get(url, stream=True) as r:
        r.raise_for_status()

        with open(output_path, "wb") as f:
            for chunk in r.iter_content(1024 * 1024):
                f.write(chunk)

    print("Download OK ✅")

def load_embeddings():
    combined_embeddings = {}

    with open(EMBEDDINGS_FILE, "rb") as f:
        data = pickle.load(f)

    if isinstance(data, tuple) and len(data) == 2:
        embeddings, _ = data
    else:
        embeddings = data

    total_embeddings = 0

    for label, emb_list in embeddings.items():
        if label not in combined_embeddings:
            combined_embeddings[label] = []

        combined_embeddings[label].extend(emb_list)
        total_embeddings += len(emb_list)

    if total_embeddings == 0:
        raise ValueError("Nenhum embedding foi carregado para gerar os indices.")

    print(f"Labels: {len(combined_embeddings)}")
    print(f"Embeddings: {total_embeddings}")
    return combined_embeddings

def build_faiss(data, pca_enabled, index_path, labels_path):
    embeddings = []
    labels = []

    for classe, vetor_list in data.items():
        for vetor in vetor_list:
            embeddings.append(vetor)
            labels.append(classe)

    embeddings = np.array(embeddings).astype(np.float32)
    if embeddings.size == 0:
        raise ValueError("Nenhum embedding foi informado para gerar o indice FAISS.")

    if pca_enabled:
        fit_pca(embeddings)
        save_projection(settings.pca_projection_path)
        embeddings = transform_embeddings(embeddings)
    else:
        faiss.normalize_L2(embeddings)
    d = embeddings.shape[1]
    index = faiss.IndexFlatL2(d)
    index.add(embeddings)

    faiss.write_index(index, str(index_path))
    with open(labels_path, "wb") as f:
        pickle.dump(labels, f)

    modo = "PCA ENABLED" if pca_enabled else "PCA DISABLED"
    print(f"Índice {modo} criado com {index.ntotal} embeddings em {index_path}")

def preparar_faiss():
    download_embeddings_from_oracle()
    embeddings = load_embeddings()
    build_faiss(
        embeddings,
        pca_enabled=True,
        index_path=FAISS_INDEX_PCA_FILE,
        labels_path=FAISS_LABELS_PCA_FILE,
    )
    build_faiss(
        embeddings,
        pca_enabled=False,
        index_path=FAISS_INDEX_RAW_FILE,
        labels_path=FAISS_LABELS_RAW_FILE,
    )

if __name__ == "__main__":
    preparar_faiss()
