package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieAtorRepository extends CrudRepository<SerieAtor, Long> {

    @Query("SELECT new com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse" +
            "(a.nome, a.imagem, sa.personagem, a.popularity) " +
            "FROM SerieAtor sa " +
            "JOIN sa.ator a " +
            "JOIN sa.serie s " +
            "WHERE s.id = :serieId " +
            "AND LOWER(sa.personagem) LIKE LOWER(CONCAT('%', :nomePersonagem, '%'))")
    Page<OpcaoPesquisaElencoResponse> findElencoPorSerieIdComPersonagem(@Param("serieId") Long serieId,
                                                                        @Param("nomePersonagem") String nomePersonagem,
                                                                        Pageable pageable);

}