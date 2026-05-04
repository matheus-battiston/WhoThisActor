package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeAtorRepository extends JpaRepository<FilmeAtor, Long> {
    List<FilmeAtor> findByAtorId(Long id);

    @Query("""
    SELECT new com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse(
        f.id,
        f.titulo,
        fa.personagem,
        f.imagem
    )
    FROM FilmeAtor fa
    JOIN fa.filme f
    WHERE fa.ator.id = :atorId
      AND fa.personagem IS NOT NULL
      AND TRIM(fa.personagem) <> ''
      AND LOWER(TRIM(fa.personagem)) <> 'self'
      AND LOWER(TRIM(fa.personagem)) NOT LIKE 'self -%'
      AND LOWER(TRIM(fa.personagem)) NOT LIKE 'self (%'
    ORDER BY f.popularidade DESC
""")
    Page<ProducaoComPersonagemResponse> findProducoesResponsePorAtor(
            @Param("atorId") Long atorId,
            Pageable pageable
    );

    @Query("""
        SELECT fa.ator.idTmdb
        FROM FilmeAtor fa
        WHERE fa.filme.id = :producaoId
        """)
    List<Long> findAtorIdsTmdbByProducaoId(@Param("producaoId") Long producaoId);

    @Query("""
    SELECT new com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse(
        a.nome,
        a.imagem,
        fa.personagem,
        a.popularity,
        a.id
    )
    FROM FilmeAtor fa
    JOIN fa.ator a
    JOIN fa.filme p
    WHERE p.id = :id
      AND fa.personagem IS NOT NULL
      AND TRIM(fa.personagem) <> ''
      AND LOWER(TRIM(fa.personagem)) <> 'self'
      AND LOWER(TRIM(fa.personagem)) NOT LIKE 'self -%'
      AND LOWER(TRIM(fa.personagem)) NOT LIKE 'self (%'
      AND LOWER(fa.personagem) LIKE LOWER(CONCAT('%', :nomePersonagem, '%'))
    ORDER BY fa.creditOrder ASC
""")
    Page<OpcaoPesquisaElencoResponse> findElencoPorIdComPersonagem(
            @Param("id") Long id,
            @Param("nomePersonagem") String nomePersonagem,
            Pageable pageable
    );}
