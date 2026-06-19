package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.ListaFavoritosMapper.toResponse;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ListaFavoritosMapperTest {
    private static final int QUANTIDADE_FAVORITOS = 1;
    private static final int PRIMEIRO_ITEM = 0;

    @Test
    @DisplayName("Deve transformar listas de favoritos em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadSerieEntityComId();
        Filme filme = getMatrixFilmeEntityComId();

        var series = of(serie);
        var filmes = of(filme);

        ProducoesFavoritasResponse response = toResponse(series, filmes);

        assertEquals(QUANTIDADE_FAVORITOS, response.getSeries().size());
        assertEquals(serie.getId(), response.getSeries().get(PRIMEIRO_ITEM).getId());
        assertEquals(serie.getTitulo(), response.getSeries().get(PRIMEIRO_ITEM).getNome());
        assertEquals(serie.getImagem(), response.getSeries().get(PRIMEIRO_ITEM).getImagem());
        assertEquals(QUANTIDADE_FAVORITOS, response.getFilmes().size());
        assertEquals(filme.getId(), response.getFilmes().get(PRIMEIRO_ITEM).getId());
        assertEquals(filme.getTitulo(), response.getFilmes().get(PRIMEIRO_ITEM).getNome());
        assertEquals(filme.getImagem(), response.getFilmes().get(PRIMEIRO_ITEM).getImagem());
    }
}
