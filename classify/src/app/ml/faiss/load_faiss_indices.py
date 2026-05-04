import pickle

import faiss

from app.core.config import settings
from app.ml.embeddings.pca_projection import load_projection

CACHE_INDICES = None


def _load_index_bundle(index_path, labels_path):
    if not index_path.exists() or not labels_path.exists():
        print(f"Indice FAISS ou labels nao encontrados para {index_path}.")
        return None

    index_individual = faiss.read_index(str(index_path))

    with open(labels_path, "rb") as f:
        labels_individual = pickle.load(f)

    return {
        "index_individual": index_individual,
        "labels_individual": tuple(labels_individual),
    }


def load_indices():
    global CACHE_INDICES

    if CACHE_INDICES is not None:
        return CACHE_INDICES

    projection = load_projection(settings.pca_projection_path)
    pca_bundle = _load_index_bundle(
        settings.faiss_index_pca_path,
        settings.faiss_labels_pca_path,
    )
    raw_bundle = _load_index_bundle(
        settings.faiss_index_raw_path,
        settings.faiss_labels_raw_path,
    )

    if pca_bundle is None or raw_bundle is None or projection is None:
        return None

    CACHE_INDICES = {
        "pca": pca_bundle,
        "raw": raw_bundle,
        "pca_projection": projection,
    }
    return CACHE_INDICES
