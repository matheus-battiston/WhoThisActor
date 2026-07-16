package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.GeneroTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
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
                .elencoInicializado(false)
                .infoAtualizado(false)
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
                .elencoInicializado(false)
                .infoAtualizado(false)
                .build();
    }


    public static ProducaoInfoResponse toResponse(Serie serie, List<ProviderDto> providers) {
        return ProducaoInfoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(serie.getImagem())
                .backdropPath(serie.getBackdropPath())
                .anoPrimeiraTemporada(serie.getAnoPrimeiraTemporada())
                .anoUltimaTemporada(serie.getAnoUltimaTemporada())
                .quantidadeTemporadas(serie.getQuantidadeTemporadas())
                .genero(serie.getGenero())
                .overview(serie.getOverview())
                .tipoMidia(TV)
                .providers(providers)
                .id(serie.getId())
                .build();
    }

    public static void atualizarComDetalhes(Serie serie, SerieDetalhesTMDBDto detalhes) {
        if (detalhes == null) return;

        serie.setTitulo(detalhes.getNome());
        serie.setTituloNormalizado(normalizar(detalhes.getNome()));
        serie.setImagem(detalhes.getImagemPoster());
        serie.setPopularidade(detalhes.getPopularidade());
        serie.setBackdropPath(detalhes.getBackdropPath());
        serie.setAnoPrimeiraTemporada(extrairAno(detalhes.getPrimeiraDataExibicao()));
        serie.setAnoUltimaTemporada(extrairAno(detalhes.getUltimaDataExibicao()));
        serie.setQuantidadeTemporadas(detalhes.getQuantidadeTemporadas());
        serie.setGenero(primeiroGenero(detalhes.getGenres()));
        serie.setOverview(detalhes.getOverview());
        serie.setUltimaAtualizacao(LocalDate.now());
        serie.setInfoAtualizado(true);
    }

    private static String primeiroGenero(List<GeneroTMDBDto> generos) {
        if (generos == null || generos.isEmpty()) return null;
        return generos.get(0).getName();
    }

    private static Integer extrairAno(String data) {
        if (data == null || data.length() < 4) return null;

        try {
            return Integer.valueOf(data.substring(0, 4));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
