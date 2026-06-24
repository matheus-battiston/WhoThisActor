from app.core.config import settings
from app.ml.embeddings.embedding_model import gerar_embeddings_consulta
from app.ml.faiss.index_search import buscar_no_indice, buscar_no_indice_filtrado
from app.ml.faiss.load_faiss_indices import load_indices


def recognize_face_with_faiss(
    image,
    image_flip,
    top_n=settings.top_n_search,
    mode="pca",
):
    try:
        data = load_indices()
        if data is None or mode not in data:
            return []

        bundle = data[mode]
        projection = data["pca_projection"] if mode == "pca" else None
        emb_original, emb_flip = gerar_embeddings_consulta(image, image_flip, projection=projection)

        return buscar_no_indice(
            index=bundle["index_individual"],
            labels=bundle["labels_individual"],
            emb_original=emb_original,
            emb_flip=emb_flip,
            top_n=top_n,
            search_multiplier=10,
            min_search=100,
        )

    except Exception as e:
        print(f"Erro ao reconhecer rosto com Faiss: {e}")
        return []


def recognize_face_with_faiss_filtrado(
    image,
    image_flip,
    valid_labels,
    top_n=settings.top_n_search,
    mode="pca",
):
    try:
        data = load_indices()
        if data is None or mode not in data:
            return []

        bundle = data[mode]
        projection = data["pca_projection"] if mode == "pca" else None
        emb_original, emb_flip = gerar_embeddings_consulta(image, image_flip, projection=projection)

        return buscar_no_indice_filtrado(
            index=bundle["index_individual"],
            labels=bundle["labels_individual"],
            emb_original=emb_original,
            emb_flip=emb_flip,
            valid_labels=valid_labels,
            top_n=top_n,
        )

    except Exception as e:
        print(f"Erro ao reconhecer rosto com Faiss filtrado: {e}")
        return []
