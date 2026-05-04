package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.valueOf;
import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;

public class SerieMapper {
    public static Serie toEntity(ProducaoTMDBDto producaoDto) {
        TipoMidia tipoProducao = valueOf(producaoDto.getTipoMidia().toUpperCase());
        String titulo = tipoProducao.equals(MOVIE) ? producaoDto.getTitulo() : producaoDto.getNome();

        return Serie.builder()
                .imagem(producaoDto.getImagemPoster())
                .titulo(titulo)
                .tituloNormalizado(normalizar(titulo))
                .idTmdb(producaoDto.getId())
                .ultimaAtualizacao(LocalDate.now())
                .popularidade(producaoDto.getPopularidade())
                .inicializado(false)
                .build();
    }

    public static Serie toEntity(SerieTMDBDto producao){
        return Serie.builder()
                .imagem(producao.getImagemPoster())
                .titulo(producao.getNome())
                .tituloNormalizado(normalizar(producao.getNome()))
                .idTmdb(producao.getId())
                .ultimaAtualizacao(LocalDate.now())
                .popularidade(producao.getPopularidade())
                .inicializado(false)
                .build();
    }


    public static ProducaoInfoResponse toResponse(Serie serie, List<ProviderDto> providers) {
        return ProducaoInfoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(serie.getImagem())
                .providers(providers)
                .id(serie.getId())
                .build();
    }
}
