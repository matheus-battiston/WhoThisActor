package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;

public class DetalhesProducaoComElencoMapper {
    public static DetalhesProducaoComElenco toResponse(
            List<OpcaoPesquisaElencoResponse> elenco,
            Filme filme,
            List<ProviderDto> providers,
            Boolean estaFavoritado) {
        return DetalhesProducaoComElenco.builder()
                .elenco(elenco)
                .nome(filme.getTitulo())
                .imagem(normalizar(filme.getImagem()))
                .backdropPath(normalizar(filme.getBackdropPath()))
                .dataLancamento(filme.getDataLancamento())
                .genero(filme.getGenero())
                .overview(filme.getOverview())
                .tipoMidia(MOVIE)
                .providers(providers)
                .id(filme.getId())
                .estaFavoritado(estaFavoritado)
                .build();
    }

    public static DetalhesProducaoComElenco toResponse(
            List<OpcaoPesquisaElencoResponse> elenco,
            Serie serie,
            List<ProviderDto> providers,
            Boolean estaFavoritado) {
        return DetalhesProducaoComElenco.builder()
                .elenco(elenco)
                .nome(serie.getTitulo())
                .imagem(normalizar(serie.getImagem()))
                .backdropPath(normalizar(serie.getBackdropPath()))
                .anoPrimeiraTemporada(serie.getAnoPrimeiraTemporada())
                .anoUltimaTemporada(serie.getAnoUltimaTemporada())
                .quantidadeTemporadas(serie.getQuantidadeTemporadas())
                .genero(serie.getGenero())
                .overview(serie.getOverview())
                .tipoMidia(TV)
                .providers(providers)
                .id(serie.getId())
                .estaFavoritado(estaFavoritado)
                .build();
    }
}
