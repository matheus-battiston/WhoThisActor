package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.PesquisaFilmeCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesquisaFilmeCacheRepository extends JpaRepository<PesquisaFilmeCache, Long> {

    boolean existsByTermoNormalizado(String termoNormalizado);
}
