package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcaoPesquisaElencoResponseFactory.getNeo;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.mapper.DetalhesProducaoComElencoMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DetalhesProducaoComElencoMapperTest {

    @Test
    @DisplayName("Deve transformar filme em response com elenco")
    void transformarFilmeEmResponseComElenco() {
        Filme filme = getMatrixComId();
        List<OpcaoPesquisaElencoResponse> elenco = of(getNeo());
        List<ProviderDto> providers = of(getNetflix());

        DetalhesProducaoComElenco response = toResponse(elenco, filme, providers, true);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        assertEquals(elenco, response.getElenco());
        assertEquals(providers, response.getProviders());
        assertEquals(true, response.getEstaFavoritado());
    }

    @Test
    @DisplayName("Deve transformar serie em response com elenco")
    void transformarSerieEmResponseComElenco() {
        Serie serie = getBreakingBadComId();
        List<OpcaoPesquisaElencoResponse> elenco = of(getNeo());
        List<ProviderDto> providers = of(getNetflix());

        DetalhesProducaoComElenco response = toResponse(elenco, serie, providers, false);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
        assertEquals(TipoMidia.TV, response.getTipoMidia());
        assertEquals(elenco, response.getElenco());
        assertEquals(providers, response.getProviders());
        assertEquals(false, response.getEstaFavoritado());
    }
}
