package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByIdTmdbIn(List<Long> idsTmdb);
    List<Filme> findByTituloNormalizadoOrderByPopularidadeDesc(String normalizar);

    @Query(value = """
    SELECT *
    FROM (
        SELECT *
        FROM filme
        WHERE popularidade IS NOT NULL
            AND imagem IS NOT NULL
        ORDER BY popularidade DESC
        LIMIT 100
    ) top_100
    ORDER BY RANDOM()
    LIMIT 2
    """, nativeQuery = true)

    List<Filme> buscarDoisAleatoriosDoTop100();
}
