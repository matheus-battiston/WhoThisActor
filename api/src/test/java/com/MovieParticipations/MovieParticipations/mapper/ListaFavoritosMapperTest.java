package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.mapper.ListaFavoritosMapper.toResponse;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ListaFavoritosMapperTest {

    @Test
    @DisplayName("Deve transformar listas de favoritos em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadComId();
        Filme filme = getMatrixComId();

        ProducoesFavoritasResponse response = toResponse(of(serie), of(filme));

        assertEquals(1, response.getSeries().size());
        assertEquals(serie.getId(), response.getSeries().get(0).getId());
        assertEquals(serie.getTitulo(), response.getSeries().get(0).getNome());
        assertEquals(serie.getImagem(), response.getSeries().get(0).getImagem());
        assertEquals(1, response.getFilmes().size());
        assertEquals(filme.getId(), response.getFilmes().get(0).getId());
        assertEquals(filme.getTitulo(), response.getFilmes().get(0).getNome());
        assertEquals(filme.getImagem(), response.getFilmes().get(0).getImagem());
    }
}
