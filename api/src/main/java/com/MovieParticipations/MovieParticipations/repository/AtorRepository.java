package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtorRepository extends JpaRepository<Ator, Long> {

    Optional<Ator> findByNomeIgnoreCase(String nome);
    boolean existsAtorByNomeIgnoreCase(String nome);

    List<Ator> findBySeriesId(Long serieId); // A consulta será gerada automaticamente
    List<Ator> findAtorByNomeIn(List<String> nomes);
}
