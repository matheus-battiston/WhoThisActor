package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
