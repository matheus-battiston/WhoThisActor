package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {
    boolean existsSerieById(Long idSerie);
    List<Serie> findByIdTmdbIn(List<Long> longs);
    List<Serie> findByTituloNormalizadoOrderByPopularidadeDesc(String nome);
}
