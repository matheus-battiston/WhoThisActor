package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtorRepository extends JpaRepository<Ator, Long> {

    boolean existsAtorById(Long id);
    List<Ator> findByIdTmdbIn(List<Long> idsTmdb);
    Optional<Ator> findAtorById(Long idSerie);
    List<Ator> findAtorByNomeNormalizado(String normalizar);
    List<Ator> findByNomeNormalizadoIn(List<String> nomesNormalizados);
    @Query(value = """
        SELECT *
        FROM (
            SELECT *
            FROM ator
            WHERE popularity IS NOT NULL
              AND imagem IS NOT NULL
            ORDER BY popularity DESC, id
            LIMIT 100
        ) top_100
        ORDER BY RANDOM()
        LIMIT 2
        """, nativeQuery = true)
    List<Ator> buscarDoisAtoresAleatoriosDoTop100();
}
