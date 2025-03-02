package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Serie findByTituloIgnoreCaseAndTipo(String titulo, TipoMidia midia);
    boolean existsSerieByTituloIgnoreCaseAndTipo(String titulo, TipoMidia tipo);
}
