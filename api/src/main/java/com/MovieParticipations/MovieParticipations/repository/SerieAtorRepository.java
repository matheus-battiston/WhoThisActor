package com.MovieParticipations.MovieParticipations.repository;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieAtorRepository extends JpaRepository<SerieAtor, Long> {

    @Query("""
    SELECT new com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse(
        a.nome,
        a.imagem,
        sa.personagem,
        a.popularity,
        a.id
    )
    FROM SerieAtor sa
    JOIN sa.ator a
    JOIN sa.serie p
    WHERE p.id = :id
      AND sa.personagem IS NOT NULL
      AND TRIM(sa.personagem) <> ''
      AND LOWER(TRIM(sa.personagem)) <> 'self'
      AND LOWER(TRIM(sa.personagem)) NOT LIKE 'self -%'
      AND LOWER(TRIM(sa.personagem)) NOT LIKE 'self (%'
      AND LOWER(sa.personagem) LIKE LOWER(CONCAT('%', :nomePersonagem, '%'))
    ORDER BY sa.numeroEpisodios DESC
""")
    Page<OpcaoPesquisaElencoResponse> findElencoPorIdComPersonagem(
            @Param("id") Long id,
            @Param("nomePersonagem") String nomePersonagem,
            Pageable pageable
    );

    @Query("""
    SELECT new com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse(
        s.id,
        s.titulo,
        sa.personagem,
        s.imagem
    )
    FROM SerieAtor sa
    JOIN sa.serie s
    WHERE sa.ator.id = :atorId
      AND sa.personagem IS NOT NULL
      AND TRIM(sa.personagem) <> ''
      AND LOWER(TRIM(sa.personagem)) <> 'self'
      AND LOWER(TRIM(sa.personagem)) NOT LIKE 'self -%'
      AND LOWER(TRIM(sa.personagem)) NOT LIKE 'self (%'
    ORDER BY s.popularidade DESC
""")
    Page<ProducaoComPersonagemResponse> findProducoesResponsePorAtor(
            @Param("atorId") Long atorId,
            Pageable pageable
    );

    @Query("""
SELECT pa.ator.idTmdb
FROM SerieAtor pa
WHERE pa.serie.id = :producaoId
""")
    List<Long> findAtorIdsTmdbByProducaoId(@Param("producaoId") Long producaoId);

    List<SerieAtor> findByAtorId(Long id);
}
