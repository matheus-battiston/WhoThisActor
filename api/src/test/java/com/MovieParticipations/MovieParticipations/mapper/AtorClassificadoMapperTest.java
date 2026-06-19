package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.AtorTMDBDtoPesquisaIdFactory.getKeanuReevesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReevesOpcoesAtoresParecidosResponse;
import static com.MovieParticipations.MovieParticipations.mapper.AtorClassificadoMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AtorClassificadoMapperTest {
    private static final String LINK_IMAGEM = "https://image.tmdb.org/t/p/w200";

    @Test
    @DisplayName("Deve transformar classificacao e ator em response")
    void transformarClassificacaoEAtorEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        Ator ator = getKeanuReevesAtorEntityComId();

        OpcoesAtoresParecidosResponse response = toResponse(classificacao, ator);

        assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        assertEquals(ator.getId(), response.getId());
        assertEquals(LINK_IMAGEM + ator.getImagem(), response.getImagem());
        assertEquals(ator.getIdTmdb(), response.getIdTmdb());
        assertEquals(ator.getPopularity(), response.getPopularidade());
    }

    @Test
    @DisplayName("Deve transformar classificacao e ator tmdb em response")
    void transformarClassificacaoEAtorTmdbEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        AtorTMDBDtoPesquisaId ator = getKeanuReevesTMDBDto();

        OpcoesAtoresParecidosResponse response = toResponse(classificacao, ator);

        assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        assertEquals(LINK_IMAGEM + ator.getFotoDePerfil(), response.getImagem());
        assertEquals(ator.getId(), response.getIdTmdb());
        assertEquals(ator.getPopularidade(), response.getPopularidade());
    }
}
