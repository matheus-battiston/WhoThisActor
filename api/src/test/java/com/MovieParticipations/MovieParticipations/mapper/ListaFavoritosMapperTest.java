package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;

@ExtendWith(MockitoExtension.class)
public class ListaFavoritosMapperTest {

    @Test
    @DisplayName("Deve transformar listas de favoritos em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadComId();
        Filme filme = getMatrixComId();

        ProducoesFavoritasResponse response = ListaFavoritosMapper.toResponse(List.of(serie), List.of(filme));

        Assertions.assertEquals(1, response.getSeries().size());
        Assertions.assertEquals(serie.getId(), response.getSeries().get(0).getId());
        Assertions.assertEquals(serie.getTitulo(), response.getSeries().get(0).getNome());
        Assertions.assertEquals(serie.getImagem(), response.getSeries().get(0).getImagem());
        Assertions.assertEquals(1, response.getFilmes().size());
        Assertions.assertEquals(filme.getId(), response.getFilmes().get(0).getId());
        Assertions.assertEquals(filme.getTitulo(), response.getFilmes().get(0).getNome());
        Assertions.assertEquals(filme.getImagem(), response.getFilmes().get(0).getImagem());
    }
}
