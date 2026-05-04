from collections import defaultdict
import logging
import time
from typing import List, Optional

import psycopg2

from app.core.config import settings

logger = logging.getLogger(__name__)

serie_para_atores: dict[int, set[str]] = {}
filme_para_atores: dict[int, set[str]] = {}


def _get_connection():
    return psycopg2.connect(
        dbname=settings.postgres_db,
        user=settings.postgres_user,
        password=settings.postgres_password,
        host=settings.postgres_host,
        port=settings.postgres_port,
        sslmode=settings.postgres_sslmode,
    )


def carregar_cache_atores_por_producao():
    global serie_para_atores, filme_para_atores

    tempo_inicio = time.perf_counter()

    conn = None
    cursor = None

    try:
        conn = _get_connection()
        cursor = conn.cursor()

        query_series = """
        SELECT serie_ator.serie, ator.nome
        FROM serie_ator
        JOIN ator ON ator.id = serie_ator.ator;
        """

        query_filmes = """
        SELECT filme_ator.filme, ator.nome
        FROM filme_ator
        JOIN ator ON ator.id = filme_ator.ator;
        """

        cursor.execute(query_series)
        rows_series = cursor.fetchall()

        cursor.execute(query_filmes)
        rows_filmes = cursor.fetchall()

        temp_series = defaultdict(set)
        temp_filmes = defaultdict(set)

        for serie_id, nome_ator in rows_series:
            temp_series[serie_id].add(nome_ator)

        for filme_id, nome_ator in rows_filmes:
            temp_filmes[filme_id].add(nome_ator)

        serie_para_atores = dict(temp_series)
        filme_para_atores = dict(temp_filmes)

        tempo_total = time.perf_counter() - tempo_inicio
        total_relacoes_series = len(rows_series)
        total_relacoes_filmes = len(rows_filmes)
        total_series = len(serie_para_atores)
        total_filmes = len(filme_para_atores)

        logger.info(
            "[cache] cache de atores por produção carregado em %.4fs | series=%s | filmes=%s | relacoes_series=%s | relacoes_filmes=%s",
            tempo_total,
            total_series,
            total_filmes,
            total_relacoes_series,
            total_relacoes_filmes,
        )

    except Exception:
        logger.exception("[cache] erro ao carregar cache de atores por produção")
        serie_para_atores = {}
        filme_para_atores = {}

    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()


def buscar_atores_por_ids_producoes_em_memoria(
    ids_series: Optional[List[int]],
    ids_filmes: Optional[List[int]],
) -> list[str]:
    if not ids_series and not ids_filmes:
        return []

    atores = set()

    for serie_id in ids_series or []:
        atores.update(serie_para_atores.get(serie_id, set()))

    for filme_id in ids_filmes or []:
        atores.update(filme_para_atores.get(filme_id, set()))

    return list(atores)
