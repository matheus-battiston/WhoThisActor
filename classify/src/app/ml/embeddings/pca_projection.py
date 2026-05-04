import pickle

import faiss
import numpy as np

from app.core.config import settings


PCA_CACHE = None
EPSILON = 1e-12


def _normalize_rows(vectors):
    normalized = np.asarray(vectors, dtype=np.float32).copy()
    faiss.normalize_L2(normalized)
    return normalized


def fit_pca(vectors, n_components=None):
    global PCA_CACHE

    matrix = _normalize_rows(vectors)
    n_samples, n_features = matrix.shape
    requested_components = settings.pca_components if n_components is None else n_components
    n_components = min(requested_components, n_samples, n_features)

    if n_components <= 0:
        raise ValueError("PCA_COMPONENTS deve ser maior que zero para habilitar PCA.")

    mean = matrix.mean(axis=0, dtype=np.float32)
    centered = matrix - mean

    _, singular_values, vt = np.linalg.svd(centered, full_matrices=False)
    components = vt[:n_components].astype(np.float32)

    scales = None
    if settings.pca_whiten:
        explained_variance = (singular_values[:n_components] ** 2) / max(n_samples - 1, 1)
        scales = np.sqrt(np.maximum(explained_variance, EPSILON)).astype(np.float32)

    PCA_CACHE = {
        "mean": mean.astype(np.float32),
        "components": components,
        "scales": scales,
        "whiten": settings.pca_whiten,
        "n_components": int(n_components),
    }

    return PCA_CACHE


def save_projection(path=settings.pca_projection_path):
    if PCA_CACHE is None:
        raise ValueError("Nenhuma projecao PCA foi ajustada para salvar.")

    with open(path, "wb") as f:
        pickle.dump(PCA_CACHE, f)


def load_projection(path=settings.pca_projection_path):
    global PCA_CACHE

    if not path.exists():
        PCA_CACHE = None
        return None

    with open(path, "rb") as f:
        PCA_CACHE = pickle.load(f)

    return PCA_CACHE


def transform_embeddings(vectors, projection=None):
    projection = PCA_CACHE if projection is None else projection

    if projection is None:
        return np.asarray(vectors, dtype=np.float32)

    matrix = _normalize_rows(vectors)
    centered = matrix - projection["mean"]
    projected = centered @ projection["components"].T

    if projection["scales"] is not None:
        projected = projected / projection["scales"]

    projected = projected.astype(np.float32)
    faiss.normalize_L2(projected)
    return projected


def transform_query(embedding, projection=None):
    return transform_embeddings(embedding, projection=projection)
