from app.core.config import settings
from app.ml.faiss.ranking import melhores_resultados_adaptativo, ordenar_e_filtrar_resultados, merge_results, \
    melhores_resultados_filtrados


def buscar_no_indice(
    index,
    labels,
    emb_original,
    emb_flip,
    top_n=settings.top_n_search,
    search_multiplier=3,
    min_search=20,
):
    if index is None or not labels:
        return []

    search_n = min(max(top_n * search_multiplier, min_search), len(labels))
    distances_orig, indices_orig = index.search(emb_original, search_n)
    distances_flip, indices_flip = index.search(emb_flip, search_n)

    resultados_orig = melhores_resultados_adaptativo(distances_orig, indices_orig, labels)
    resultados_flip = melhores_resultados_adaptativo(distances_flip, indices_flip, labels)

    return ordenar_e_filtrar_resultados(
        merge_results(resultados_orig, resultados_flip),
        top_n=top_n,
    )

def buscar_no_indice_filtrado(
    index,
    labels,
    emb_original,
    emb_flip,
    valid_labels,
    top_n=settings.top_n_search,
):
    if index is None or not labels or not valid_labels:
        return []

    valid_labels_set = set(valid_labels)

    search_n = len(labels)
    distances_orig, indices_orig = index.search(emb_original, search_n)
    distances_flip, indices_flip = index.search(emb_flip, search_n)

    resultados_orig = melhores_resultados_filtrados(
        distances_orig,
        indices_orig,
        labels,
        valid_labels_set,
    )

    resultados_flip = melhores_resultados_filtrados(
        distances_flip,
        indices_flip,
        labels,
        valid_labels_set,
    )

    return ordenar_e_filtrar_resultados(
        merge_results(resultados_orig, resultados_flip),
        top_n=top_n,
    )
