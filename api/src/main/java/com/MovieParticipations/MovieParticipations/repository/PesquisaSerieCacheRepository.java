package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.domain.PesquisaSerieCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesquisaSerieCacheRepository extends JpaRepository<PesquisaSerieCache, Long> {

    boolean existsByTermoNormalizado(String termoNormalizado);
}
