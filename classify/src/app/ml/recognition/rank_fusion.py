import numpy as np


FUSION_TOP_N = 40
FUSION_RRF_K = 3
FUSION_MISSING_RANK_PENALTY = 0.10
FUSION_MIN_WEIGHT = 0.35
FUSION_GAP_EPSILON = 1e-12
FUSION_TOP1_CONSENSUS_BONUS = 0.20
FUSION_MAX_SCORE = (1.0 / (FUSION_RRF_K + 1)) + FUSION_TOP1_CONSENSUS_BONUS
FUSION_SINGLE_MIN_SCORE = 0.70
FUSION_SINGLE_MIN_SCORE_GAP = 0.12
FUSION_SINGLE_MAX_RANK = 5


def _indexar_ranking(resultados):
    ranking = {}
    for rank, resultado in enumerate(resultados, start=1):
        ator_id, distancia = resultado
        if ator_id not in ranking:
            ranking[ator_id] = {
                "rank": rank,
                "distance": float(distancia),
            }
    return ranking


def _bonus_consenso(rank_pca, rank_raw):
    if rank_pca is None or rank_raw is None:
        return 0.0

    if rank_pca == 1 and rank_raw == 1:
        return FUSION_TOP1_CONSENSUS_BONUS

    if rank_pca <= 3 and rank_raw <= 3:
        return 0.12

    if rank_pca <= 5 and rank_raw <= 5:
        return 0.06

    return 0.0


def _gap_normalizado_por_std(ranking):
    ordenado = sorted(ranking.values(), key=lambda item: item["rank"])
    if len(ordenado) < 2:
        return 0.0

    distancias = [item["distance"] for item in ordenado]
    std_distancias = max(float(np.std(distancias)), FUSION_GAP_EPSILON)
    gap_absoluto = max(0.0, ordenado[-1]["distance"] - ordenado[0]["distance"])
    return gap_absoluto / std_distancias


def _calcular_pesos_por_gap(ranking_pca, ranking_raw):
    gap_pca = _gap_normalizado_por_std(ranking_pca)
    gap_raw = _gap_normalizado_por_std(ranking_raw)
    total_gap = gap_pca + gap_raw

    if total_gap <= 0:
        return 0.50, 0.50, gap_pca, gap_raw

    peso_pca = gap_pca / total_gap
    peso_raw = gap_raw / total_gap

    peso_pca = min(max(peso_pca, FUSION_MIN_WEIGHT), 1.0 - FUSION_MIN_WEIGHT)
    peso_raw = 1.0 - peso_pca
    return peso_pca, peso_raw, gap_pca, gap_raw


def _calcular_scores_fusao(resultados_pca, resultados_raw):
    scores = {}
    ranking_pca = _indexar_ranking(resultados_pca)
    ranking_raw = _indexar_ranking(resultados_raw)
    peso_pca, peso_raw, _, _ = _calcular_pesos_por_gap(ranking_pca, ranking_raw)

    def add(resultados, peso):
        vistos = set()
        for rank, resultado in enumerate(resultados, start=1):
            ator_id = resultado[0]
            if ator_id in vistos:
                continue
            vistos.add(ator_id)
            scores[ator_id] = scores.get(ator_id, 0) + peso / (FUSION_RRF_K + rank)

    add(resultados_pca, peso_pca)
    add(resultados_raw, peso_raw)

    for ator_id in set(ranking_pca) | set(ranking_raw):
        rank_pca = ranking_pca.get(ator_id, {}).get("rank")
        rank_raw = ranking_raw.get(ator_id, {}).get("rank")
        scores[ator_id] = scores.get(ator_id, 0.0) + _bonus_consenso(rank_pca, rank_raw)
        if rank_pca is None or rank_raw is None:
            scores[ator_id] = max(0.0, scores[ator_id] - FUSION_MISSING_RANK_PENALTY)

    return scores, ranking_pca, ranking_raw


def _normalizar_score(score):
    return float(min(score / FUSION_MAX_SCORE, 1.0))


def _deve_retornar_apenas_vencedor(vencedor, ranking_pca, ranking_raw, resultados_ordenados):
    rank_pca = ranking_pca.get(vencedor, {}).get("rank")
    rank_raw = ranking_raw.get(vencedor, {}).get("rank")
    if rank_pca is None or rank_raw is None:
        return False

    if rank_pca > FUSION_SINGLE_MAX_RANK or rank_raw > FUSION_SINGLE_MAX_RANK:
        return False

    score_vencedor = _normalizar_score(resultados_ordenados[0][1])
    score_segundo = _normalizar_score(resultados_ordenados[1][1]) if len(resultados_ordenados) > 1 else 0.0

    return (
        score_vencedor >= FUSION_SINGLE_MIN_SCORE
        and score_vencedor - score_segundo >= FUSION_SINGLE_MIN_SCORE_GAP
    )


def montar_resultados_fusionados(resultados_pca, resultados_raw, output_limit):
    scores, ranking_pca, ranking_raw = _calcular_scores_fusao(
        resultados_pca[:FUSION_TOP_N],
        resultados_raw[:FUSION_TOP_N],
    )
    if not scores:
        return []

    resultados_ordenados = sorted(scores.items(), key=lambda item: item[1], reverse=True)
    vencedor = resultados_ordenados[0][0]
    if _deve_retornar_apenas_vencedor(vencedor, ranking_pca, ranking_raw, resultados_ordenados):
        resultados_ordenados = resultados_ordenados[:1]
    else:
        resultados_ordenados = resultados_ordenados[:output_limit]

    resultado_normalizado = [
        (identity, _normalizar_score(score))
        for identity, score in resultados_ordenados
    ]

    if not resultado_normalizado:
        return []

    top_score = resultado_normalizado[0][1]

    return [
        (identity, score)
        for identity, score in resultado_normalizado
        if score >= top_score - 0.10
    ]
