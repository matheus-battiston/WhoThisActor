import psycopg2

from app.core.config import settings

def _get_connection():
    return psycopg2.connect(
        dbname=settings.postgres_db,
        user=settings.postgres_user,
        password=settings.postgres_password,
        host=settings.postgres_host,
        port=settings.postgres_port,
        sslmode=settings.postgres_sslmode,
    )

def buscar_atores_por_ids_producoes(ids_series=None, ids_filmes=None):
    if not ids_series and not ids_filmes:
        return []

    conn = None
    cursor = None

    try:
        conn = _get_connection()
        cursor = conn.cursor()

        query = """
        SELECT DISTINCT ator.nome
        FROM ator
        LEFT JOIN serie_ator ON ator.id = serie_ator.ator
        LEFT JOIN filme_ator ON ator.id = filme_ator.ator
        WHERE serie_ator.serie = ANY(%s)
           OR filme_ator.filme = ANY(%s);
        """
        cursor.execute(query, (ids_series or [], ids_filmes or []))

        atores = [row[0] for row in cursor.fetchall()]

        return atores

    except Exception as e:
        print(f"Erro ao buscar atores por ids de producoes: {e}")
        return []

    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()
