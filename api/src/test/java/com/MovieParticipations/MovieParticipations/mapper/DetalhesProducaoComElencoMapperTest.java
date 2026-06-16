package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcaoPesquisaElencoResponseFactory.getNeo;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;

@ExtendWith(MockitoExtension.class)
public class DetalhesProducaoComElencoMapperTest {

    @Test
    @DisplayName("Deve transformar filme em response com elenco")
    void transformarFilmeEmResponseComElenco() {
        Filme filme = getMatrixComId();
        List<OpcaoPesquisaElencoResponse> elenco = List.of(getNeo());
        List<ProviderDto> providers = List.of(getNetflix());

        DetalhesProducaoComElenco response = DetalhesProducaoComElencoMapper.toResponse(elenco, filme, providers, true);

        Assertions.assertEquals(filme.getId(), response.getId());
        Assertions.assertEquals(filme.getTitulo(), response.getNome());
        Assertions.assertEquals(filme.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        Assertions.assertEquals(elenco, response.getElenco());
        Assertions.assertEquals(providers, response.getProviders());
        Assertions.assertEquals(true, response.getEstaFavoritado());
    }

    @Test
    @DisplayName("Deve transformar serie em response com elenco")
    void transformarSerieEmResponseComElenco() {
        Serie serie = getBreakingBadComId();
        List<OpcaoPesquisaElencoResponse> elenco = List.of(getNeo());
        List<ProviderDto> providers = List.of(getNetflix());

        DetalhesProducaoComElenco response = DetalhesProducaoComElencoMapper.toResponse(elenco, serie, providers, false);

        Assertions.assertEquals(serie.getId(), response.getId());
        Assertions.assertEquals(serie.getTitulo(), response.getNome());
        Assertions.assertEquals(serie.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.TV, response.getTipoMidia());
        Assertions.assertEquals(elenco, response.getElenco());
        Assertions.assertEquals(providers, response.getProviders());
        Assertions.assertEquals(false, response.getEstaFavoritado());
    }
}
