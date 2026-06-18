package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.AtorTMDBDtoPesquisaIdFactory.getKeanuReeves;
import static com.MovieParticipations.MovieParticipations.mapper.AtorClassificadoMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AtorClassificadoMapperTest {

    @Test
    @DisplayName("Deve transformar classificacao e ator em response")
    void transformarClassificacaoEAtorEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves();
        Ator ator = getKeanuReevesComId();

        OpcoesAtoresParecidosResponse response = toResponse(classificacao, ator);

        assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        assertEquals(ator.getId(), response.getId());
        assertEquals("https://image.tmdb.org/t/p/w200" + ator.getImagem(), response.getImagem());
        assertEquals(ator.getIdTmdb(), response.getIdTmdb());
        assertEquals(ator.getPopularity(), response.getPopularidade());
    }

    @Test
    @DisplayName("Deve transformar classificacao e ator tmdb em response")
    void transformarClassificacaoEAtorTmdbEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves();
        AtorTMDBDtoPesquisaId ator = getKeanuReeves();

        OpcoesAtoresParecidosResponse response = toResponse(classificacao, ator);

        assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        assertEquals("https://image.tmdb.org/t/p/w200" + ator.getFotoDePerfil(), response.getImagem());
        assertEquals(ator.getId(), response.getIdTmdb());
        assertEquals(ator.getPopularidade(), response.getPopularidade());
    }
}
