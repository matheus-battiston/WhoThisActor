package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.GeneroTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;

import java.util.List;

public class SerieDetalhesTMDBDtoFactory {
    private static final Long ID_BREAKING_BAD = 456L;
    private static final Long ID_GENERO_DRAMA = 18L;
    private static final String NOME_BREAKING_BAD = "Breaking Bad";
    private static final String IMAGEM_POSTER_BREAKING_BAD = "/breaking-bad.jpg";
    private static final String BACKDROP_BREAKING_BAD = "/breaking-bad-backdrop.jpg";
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;
    private static final String GENERO_DRAMA = "Drama";
    private static final String OVERVIEW_BREAKING_BAD = "Um professor de quimica muda de vida apos um diagnostico.";
    private static final String PRIMEIRA_DATA_EXIBICAO_BREAKING_BAD = "2008-01-20";
    private static final String ULTIMA_DATA_EXIBICAO_BREAKING_BAD = "2013-09-29";
    private static final Integer QUANTIDADE_TEMPORADAS_BREAKING_BAD = 5;
    private static final String DATA_INVALIDA_TEXTO = "abc";
    private static final String DATA_INVALIDA_CURTA = "12";
    private static final String PRIMEIRA_DATA_EXIBICAO_COM_ANO_NAO_NUMERICO = "abcd-01-01";
    private static final String ULTIMA_DATA_EXIBICAO_COM_ANO_NAO_NUMERICO = "wxyz-01-01";

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesTMDBDto() {
        return SerieDetalhesTMDBDto.builder()
                .id(ID_BREAKING_BAD)
                .nome(NOME_BREAKING_BAD)
                .nomeOriginal(NOME_BREAKING_BAD)
                .imagemPoster(IMAGEM_POSTER_BREAKING_BAD)
                .backdropPath(BACKDROP_BREAKING_BAD)
                .popularidade(POPULARIDADE_BREAKING_BAD)
                .primeiraDataExibicao(PRIMEIRA_DATA_EXIBICAO_BREAKING_BAD)
                .ultimaDataExibicao(ULTIMA_DATA_EXIBICAO_BREAKING_BAD)
                .quantidadeTemporadas(QUANTIDADE_TEMPORADAS_BREAKING_BAD)
                .genres(List.of(GeneroTMDBDto.builder().id(ID_GENERO_DRAMA).name(GENERO_DRAMA).build()))
                .overview(OVERVIEW_BREAKING_BAD)
                .build();
    }

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesSemGenerosTMDBDto() {
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();
        detalhes.setGenres(List.of());
        return detalhes;
    }

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesComDatasInvalidasTMDBDto() {
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();
        detalhes.setPrimeiraDataExibicao(DATA_INVALIDA_TEXTO);
        detalhes.setUltimaDataExibicao(DATA_INVALIDA_CURTA);
        return detalhes;
    }

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesComAnoNaoNumericoTMDBDto() {
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();
        detalhes.setPrimeiraDataExibicao(PRIMEIRA_DATA_EXIBICAO_COM_ANO_NAO_NUMERICO);
        detalhes.setUltimaDataExibicao(ULTIMA_DATA_EXIBICAO_COM_ANO_NAO_NUMERICO);
        return detalhes;
    }

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesComDatasNulasTMDBDto() {
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();
        detalhes.setPrimeiraDataExibicao(null);
        detalhes.setUltimaDataExibicao(null);
        return detalhes;
    }

    public static SerieDetalhesTMDBDto getBreakingBadSerieDetalhesComGenerosNulosTMDBDto() {
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();
        detalhes.setGenres(null);
        return detalhes;
    }
}
