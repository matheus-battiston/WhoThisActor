package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBDtoPesquisaIdFactory.getKeanuReevesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.response.OpcoesAtoresParecidosResponseFactory.getKeanuReevesOpcoesAtoresParecidosResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AtorMapperTest {
    private static final String NOME_NORMALIZADO_KEANU_REEVES = "keanu reeves";
    private static final boolean FALSO = false;
    private static final String IMAGEM_KEANU_REEVES_SEM_BARRA = "keanu.jpg";
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final Double POPULARIDADE_KEANU_REEVES = 10.0;
    private static final String IMAGEM_KEANU_REEVES = "/keanu.jpg";
    private static final String LINK_IMAGEM = "https://image.tmdb.org/t/p/w200";

    @Test
    @DisplayName("Deve transformar dto em entidade")
    void transformarEmEntidade() {
        AtorTMDBDtoPesquisaId dto = getKeanuReevesTMDBDto();

        Ator response = AtorMapper.toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(NOME_NORMALIZADO_KEANU_REEVES, response.getNomeNormalizado());
        assertEquals(dto.getFotoDePerfil(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularity());
        assertEquals(FALSO, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar opcoes parecidas em entidade")
    void transformarOpcoesParecidasEmEntidade() {
        OpcoesAtoresParecidosResponse dto = getKeanuReevesOpcoesAtoresParecidosResponse();
        dto.setImagem(IMAGEM_KEANU_REEVES_SEM_BARRA);
        dto.setIdTmdb(ID_TMDB_KEANU_REEVES);
        dto.setPopularidade(POPULARIDADE_KEANU_REEVES);

        Ator response = AtorMapper.toEntityFromOpcoesParecidos(dto);

        assertEquals(dto.getIdTmdb(), response.getIdTmdb());
        assertEquals(dto.getIdentidade(), response.getNome());
        assertEquals(NOME_NORMALIZADO_KEANU_REEVES, response.getNomeNormalizado());
        assertEquals(IMAGEM_KEANU_REEVES, response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularity());
        assertEquals(FALSO, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em info")
    void transformarEmInfo() {
        Ator ator = getKeanuReevesAtorEntityComId();

        InfoAtorResponse response = AtorMapper.toInfo(ator);

        assertEquals(ator.getId(), response.getId());
        assertEquals(ator.getNome(), response.getNome());
        assertEquals(LINK_IMAGEM + ator.getImagem(), response.getUrlImagem());
        assertEquals(ator.getPopularity(), response.getPopularity());
    }
}
