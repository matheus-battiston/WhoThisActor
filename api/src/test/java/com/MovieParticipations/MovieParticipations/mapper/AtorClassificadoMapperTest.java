package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.AtorTMDBDtoPesquisaIdFactory.getKeanuReeves;

@ExtendWith(MockitoExtension.class)
public class AtorClassificadoMapperTest {

    @Test
    @DisplayName("Deve transformar classificacao e ator em response")
    void transformarClassificacaoEAtorEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves();
        Ator ator = getKeanuReevesComId();

        OpcoesAtoresParecidosResponse response = AtorClassificadoMapper.toResponse(classificacao, ator);

        Assertions.assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        Assertions.assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        Assertions.assertEquals(ator.getId(), response.getId());
        Assertions.assertEquals("https://image.tmdb.org/t/p/w200" + ator.getImagem(), response.getImagem());
        Assertions.assertEquals(ator.getIdTmdb(), response.getIdTmdb());
        Assertions.assertEquals(ator.getPopularity(), response.getPopularidade());
    }

    @Test
    @DisplayName("Deve transformar classificacao e ator tmdb em response")
    void transformarClassificacaoEAtorTmdbEmResponse() {
        OpcoesAtoresParecidosResponse classificacao = com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves();
        AtorTMDBDtoPesquisaId ator = getKeanuReeves();

        OpcoesAtoresParecidosResponse response = AtorClassificadoMapper.toResponse(classificacao, ator);

        Assertions.assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        Assertions.assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
        Assertions.assertEquals("https://image.tmdb.org/t/p/w200" + ator.getFotoDePerfil(), response.getImagem());
        Assertions.assertEquals(ator.getId(), response.getIdTmdb());
        Assertions.assertEquals(ator.getPopularidade(), response.getPopularidade());
    }
}
