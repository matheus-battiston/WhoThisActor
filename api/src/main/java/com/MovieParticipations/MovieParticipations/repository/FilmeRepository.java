package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByIdTmdbIn(List<Long> idsTmdb);
    List<Filme> findByTituloNormalizadoOrderByPopularidadeDesc(String normalizar);
}
