package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.ProducaoMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ProducaoMapperTest {
    private static final String GENERO_MATRIX = "Ficcao cientifica";

    @Test
    @DisplayName("Deve transformar serie em response")
    void transformarSerieEmResponse() {
        Serie serie = getBreakingBadSerieEntityComId();

        ProducaoResponse response = toResponse(serie);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
        assertEquals(serie.getGenero(), response.getGenero());
        assertEquals(serie.getOverview(), response.getOverview());
        assertEquals(serie.getAnoPrimeiraTemporada(), response.getAno());
    }

    @Test
    @DisplayName("Deve transformar filme em response")
    void transformarFilmeEmResponse() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setGenero(GENERO_MATRIX);

        ProducaoResponse response = toResponse(filme);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(filme.getGenero(), response.getGenero());
        assertEquals(filme.getOverview(), response.getOverview());
        assertEquals(filme.getDataLancamento().getYear(), response.getAno());
    }

    @Test
    @DisplayName("Deve transformar filme sem ano quando data de lançamento for nula")
    void transformarFilmeSemAnoQuandoDataLancamentoForNula() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setDataLancamento(null);

        ProducaoResponse response = toResponse(filme);

        assertNull(response.getAno());
    }
}
