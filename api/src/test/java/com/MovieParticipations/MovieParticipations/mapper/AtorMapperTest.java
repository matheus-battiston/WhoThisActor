package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcoesAtoresParecidosResponseFactory.getKeanuReeves;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AtorMapperTest {

    @Test
    @DisplayName("Deve transformar dto em entidade")
    void transformarEmEntidade() {
        AtorTMDBDtoPesquisaId dto = com.MovieParticipations.MovieParticipations.factories.AtorTMDBDtoPesquisaIdFactory.getKeanuReeves();

        Ator response = AtorMapper.toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getNome());
        assertEquals("keanu reeves", response.getNomeNormalizado());
        assertEquals(dto.getFotoDePerfil(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularity());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar opcoes parecidas em entidade")
    void transformarOpcoesParecidasEmEntidade() {
        OpcoesAtoresParecidosResponse dto = getKeanuReeves();
        dto.setImagem("keanu.jpg");
        dto.setIdTmdb(6384L);
        dto.setPopularidade(10.0);

        Ator response = AtorMapper.toEntityFromOpcoesParecidos(dto);

        assertEquals(dto.getIdTmdb(), response.getIdTmdb());
        assertEquals(dto.getIdentidade(), response.getNome());
        assertEquals("keanu reeves", response.getNomeNormalizado());
        assertEquals("/keanu.jpg", response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularity());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em info")
    void transformarEmInfo() {
        Ator ator = getKeanuReevesComId();

        InfoAtorResponse response = AtorMapper.toInfo(ator);

        assertEquals(ator.getId(), response.getId());
        assertEquals(ator.getNome(), response.getNome());
        assertEquals("https://image.tmdb.org/t/p/w200" + ator.getImagem(), response.getUrlImagem());
        assertEquals(ator.getPopularity(), response.getPopularity());
    }
}
