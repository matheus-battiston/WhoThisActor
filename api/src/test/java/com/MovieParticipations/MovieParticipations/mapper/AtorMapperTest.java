package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves;

@ExtendWith(MockitoExtension.class)
public class AtorMapperTest {

    @Test
    @DisplayName("Deve transformar dto em entidade")
    void transformarEmEntidade() {
        AtorTMDBDtoPesquisaId dto = com.MovieParticipations.MovieParticipations.factories.AtorTMDBDtoPesquisaIdFactory.getKeanuReeves();

        Ator response = AtorMapper.toEntity(dto);

        Assertions.assertEquals(dto.getId(), response.getIdTmdb());
        Assertions.assertEquals(dto.getNome(), response.getNome());
        Assertions.assertEquals("keanu reeves", response.getNomeNormalizado());
        Assertions.assertEquals(dto.getFotoDePerfil(), response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularity());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar opcoes parecidas em entidade")
    void transformarOpcoesParecidasEmEntidade() {
        OpcoesAtoresParecidosResponse dto = getKeanuReeves();
        dto.setImagem("keanu.jpg");
        dto.setIdTmdb(6384L);
        dto.setPopularidade(10.0);

        Ator response = AtorMapper.toEntityFromOpcoesParecidos(dto);

        Assertions.assertEquals(dto.getIdTmdb(), response.getIdTmdb());
        Assertions.assertEquals(dto.getIdentidade(), response.getNome());
        Assertions.assertEquals("keanu reeves", response.getNomeNormalizado());
        Assertions.assertEquals("/keanu.jpg", response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularity());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em info")
    void transformarEmInfo() {
        Ator ator = getKeanuReevesComId();

        InfoAtorResponse response = AtorMapper.toInfo(ator);

        Assertions.assertEquals(ator.getId(), response.getId());
        Assertions.assertEquals(ator.getNome(), response.getNome());
        Assertions.assertEquals("https://image.tmdb.org/t/p/w200" + ator.getImagem(), response.getUrlImagem());
        Assertions.assertEquals(ator.getPopularity(), response.getPopularity());
    }
}
