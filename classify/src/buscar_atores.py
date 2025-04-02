import psycopg2

def buscar_atores_por_serie(nome_serie):
    try:
        # Conectar ao banco de dados
        conn = psycopg2.connect(
                    dbname="postgres",
                    user="matheus",
                    password="Lun@fumble1",
                    host="whothisactorapi-db.postgres.database.azure.com",
                    port="5432",
                    sslmode="require"
                )
        cursor = conn.cursor()
        
        # Consulta SQL para buscar os atores da série
        query = """
        SELECT ator.nome 
        FROM ator
        JOIN serie_ator ON ator.id = serie_ator.ator
        JOIN serie ON serie.id = serie_ator.serie
        WHERE serie.titulo = %s;
        """
        cursor.execute(query, (nome_serie,))
        
        # Obter os nomes dos atores
        atores = [row[0] for row in cursor.fetchall()]
        
        # Fechar conexão
        cursor.close()
        conn.close()
        
        return atores
    
    except Exception as e:
        print(f"Erro ao buscar atores: {e}")
        return []