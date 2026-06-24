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

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.response.OpcaoPesquisaElencoResponseFactory.getNeoOpcaoPesquisaElencoResponse;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getNetflixProviderDto;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.DetalhesProducaoComElencoMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DetalhesProducaoComElencoMapperTest {
    private static final boolean ESTA_FAVORITADO = true;
    private static final boolean NAO_ESTA_FAVORITADO = false;

    @Test
    @DisplayName("Deve transformar filme em response com elenco")
    void transformarFilmeEmResponseComElenco() {
        Filme filme = getMatrixFilmeEntityComId();
        List<OpcaoPesquisaElencoResponse> elenco = of(getNeoOpcaoPesquisaElencoResponse());
        List<ProviderDto> providers = of(getNetflixProviderDto());

        DetalhesProducaoComElenco response = toResponse(elenco, filme, providers, ESTA_FAVORITADO);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        assertEquals(elenco, response.getElenco());
        assertEquals(providers, response.getProviders());
        assertEquals(ESTA_FAVORITADO, response.getEstaFavoritado());
    }

    @Test
    @DisplayName("Deve transformar serie em response com elenco")
    void transformarSerieEmResponseComElenco() {
        Serie serie = getBreakingBadSerieEntityComId();
        List<OpcaoPesquisaElencoResponse> elenco = of(getNeoOpcaoPesquisaElencoResponse());
        List<ProviderDto> providers = of(getNetflixProviderDto());

        DetalhesProducaoComElenco response = toResponse(elenco, serie, providers, NAO_ESTA_FAVORITADO);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
        assertEquals(TipoMidia.TV, response.getTipoMidia());
        assertEquals(elenco, response.getElenco());
        assertEquals(providers, response.getProviders());
        assertEquals(NAO_ESTA_FAVORITADO, response.getEstaFavoritado());
    }
}
