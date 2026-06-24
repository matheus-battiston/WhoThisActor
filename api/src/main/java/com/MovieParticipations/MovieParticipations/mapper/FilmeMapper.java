package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.util.NormalizadorDeString;

import java.util.List;

import static java.time.LocalDate.now;

public final class FilmeMapper {

    public static Filme toEntity(ProducaoTMDBDto producao) {
        return criarFilme(
                producao.getTitulo(),
                producao.getImagemPoster(),
                producao.getId(),
                producao.getPopularidade()
        );
    }

    public static Filme toEntity(FilmeTMDBDto producao) {
        return criarFilme(
                producao.getTitulo(),
                producao.getImagemPoster(),
                producao.getId(),
                producao.getPopularidade()
        );
    }

    public static ProducaoInfoResponse toResponse(Filme filme, List<ProviderDto> providerDtos) {
        return ProducaoInfoResponse.builder()
                .nome(filme.getTitulo())
                .imagem(filme.getImagem())
                .tipoMidia(TipoMidia.MOVIE)
                .providers(providerDtos)
                .id(filme.getId())
                .build();
    }

    private static Filme criarFilme(
            String titulo,
            String imagem,
            Long idTmdb,
            Double popularidade
    ) {
        return Filme.builder()
                .imagem(imagem)
                .titulo(titulo)
                .tituloNormalizado(NormalizadorDeString.normalizar(titulo))
                .idTmdb(idTmdb)
                .ultimaAtualizacao(now())
                .popularidade(popularidade)
                .inicializado(false)
                .build();
    }
}

