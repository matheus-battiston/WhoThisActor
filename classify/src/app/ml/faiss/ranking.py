from app.core.config import settings


def merge_results(resultados_orig, resultados_flip):
    resultados_finais = dict(resultados_orig)

    for label, dist in resultados_flip.items():
        if label in resultados_finais:
            resultados_finais[label] = min(resultados_finais[label], dist)
        else:
            resultados_finais[label] = dist

    return resultados_finais

def melhores_resultados_adaptativo(distances, indices, labels):
    results = {}

    for idx, i in enumerate(indices[0]):
        if i < 0 or i >= len(labels):
            continue

        label = labels[i]
        dist = float(distances[0][idx])

        if label not in results or dist < results[label]:
            results[label] = dist

    return results


def ordenar_e_filtrar_resultados(
    resultados_finais,
    top_n=settings.top_n_search,
):
    if not resultados_finais:
        return []

    resultado_ordenado = sorted(resultados_finais.items(), key=lambda x: x[1])
    return resultado_ordenado[:top_n]

def melhores_resultados_filtrados(distances, indices, labels, valid_labels_set):
    results = {}

    for idx, i in enumerate(indices[0]):
        if i < 0 or i >= len(labels):
            continue

        label = labels[i]
        if label not in valid_labels_set:
            continue

        dist = float(distances[0][idx])
        if label not in results or dist < results[label]:
            results[label] = dist

    return results
