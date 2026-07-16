package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.GeneroTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.util.NormalizadorDeString;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;

public final class FilmeMapper {

    public static Filme toEntity(ProducaoTMDBDto producao) {
        return criarFilme(
                producao.getTitulo(),
                producao.getImagemPoster(),
                producao.getId(),
                producao.getPopularidade(),
                producao.getDataLancamento()
        );
    }

    public static Filme toEntity(FilmeTMDBDto producao) {
        return criarFilme(
                producao.getTitulo(),
                producao.getImagemPoster(),
                producao.getId(),
                producao.getPopularidade(),
                producao.getDataLancamento()
        );
    }

    public static ProducaoInfoResponse toResponse(Filme filme, List<ProviderDto> providerDtos) {
        return ProducaoInfoResponse.builder()
                .nome(filme.getTitulo())
                .imagem(filme.getImagem())
                .backdropPath(filme.getBackdropPath())
                .dataLancamento(filme.getDataLancamento())
                .genero(filme.getGenero())
                .overview(filme.getOverview())
                .tipoMidia(TipoMidia.MOVIE)
                .providers(providerDtos)
                .id(filme.getId())
                .build();
    }

    public static void atualizarComDetalhes(Filme filme, FilmeDetalhesTMDBDto detalhes) {
        if (detalhes == null) return;

        filme.setTitulo(detalhes.getTitulo());
        filme.setTituloNormalizado(NormalizadorDeString.normalizar(detalhes.getTitulo()));
        filme.setImagem(detalhes.getImagemPoster());
        filme.setPopularidade(detalhes.getPopularidade());
        filme.setDataLancamento(detalhes.getDataLancamento());
        filme.setBackdropPath(detalhes.getBackdropPath());
        filme.setGenero(primeiroGenero(detalhes.getGenres()));
        filme.setOverview(detalhes.getOverview());
        filme.setUltimaAtualizacao(now());
        filme.setInfoAtualizado(true);
    }

    private static Filme criarFilme(
            String titulo,
            String imagem,
            Long idTmdb,
            Double popularidade,
            LocalDate dataLancamento
    ) {
        return Filme.builder()
                .imagem(imagem)
                .titulo(titulo)
                .tituloNormalizado(NormalizadorDeString.normalizar(titulo))
                .idTmdb(idTmdb)
                .ultimaAtualizacao(now())
                .dataLancamento(dataLancamento)
                .popularidade(popularidade)
                .elencoInicializado(false)
                .infoAtualizado(false)
                .build();
    }

    private static String primeiroGenero(List<GeneroTMDBDto> generos) {
        if (generos == null || generos.isEmpty()) return null;
        return generos.get(0).getName();
    }
}
