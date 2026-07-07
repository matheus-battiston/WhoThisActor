package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialPessoaResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.response.BuscaInicialResponseFactory.getBreakingBadBuscaInicialProducaoResponse;
import static com.MovieParticipations.MovieParticipations.factories.response.BuscaInicialResponseFactory.getKeanuReevesBuscaInicialPessoaResponse;
import static com.MovieParticipations.MovieParticipations.factories.response.BuscaInicialResponseFactory.getMatrixBuscaInicialProducaoResponse;
import static com.MovieParticipations.MovieParticipations.mapper.BuscaInicialMapper.toPessoaResponse;
import static com.MovieParticipations.MovieParticipations.mapper.BuscaInicialMapper.toProducaoResponse;
import static com.MovieParticipations.MovieParticipations.mapper.BuscaInicialMapper.toResponse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BuscaInicialMapper")
class BuscaInicialMapperTest {
    private static final String URL_IMAGEM_KEANU_REEVES = "https://image.tmdb.org/t/p/w200/keanu.jpg";
    private static final String URL_IMAGEM_MATRIX = "https://image.tmdb.org/t/p/w200/matrix.jpg";
    private static final String URL_IMAGEM_BREAKING_BAD = "https://image.tmdb.org/t/p/w200/breaking-bad.jpg";

    @Test
    @DisplayName("Deve transformar ator em response de pessoa")
    void deveTransformarAtorEmResponseDePessoa() {
        Ator ator = getKeanuReevesAtorEntityComId();

        BuscaInicialPessoaResponse response = toPessoaResponse(ator);

        assertThat(response.getId()).isEqualTo(ator.getId());
        assertThat(response.getNome()).isEqualTo(ator.getNome());
        assertThat(response.getUrlImagem()).isEqualTo(URL_IMAGEM_KEANU_REEVES);
    }

    @Test
    @DisplayName("Deve transformar filme em response de produção")
    void deveTransformarFilmeEmResponseDeProducao() {
        Filme filme = getMatrixFilmeEntityComId();

        BuscaInicialProducaoResponse response = toProducaoResponse(filme);

        assertThat(response.getId()).isEqualTo(filme.getId());
        assertThat(response.getNomeProducao()).isEqualTo(filme.getTitulo());
        assertThat(response.getUrlImagem()).isEqualTo(URL_IMAGEM_MATRIX);
        assertThat(response.getOverview()).isEqualTo(filme.getOverview());
    }

    @Test
    @DisplayName("Deve transformar série em response de produção")
    void deveTransformarSerieEmResponseDeProducao() {
        Serie serie = getBreakingBadSerieEntityComId();

        BuscaInicialProducaoResponse response = toProducaoResponse(serie);

        assertThat(response.getId()).isEqualTo(serie.getId());
        assertThat(response.getNomeProducao()).isEqualTo(serie.getTitulo());
        assertThat(response.getUrlImagem()).isEqualTo(URL_IMAGEM_BREAKING_BAD);
        assertThat(response.getOverview()).isEqualTo(serie.getOverview());
    }

    @Test
    @DisplayName("Deve transformar listas em response da busca inicial")
    void deveTransformarListasEmResponseDaBuscaInicial() {
        BuscaInicialPessoaResponse pessoa = getKeanuReevesBuscaInicialPessoaResponse();
        BuscaInicialProducaoResponse serie = getBreakingBadBuscaInicialProducaoResponse();
        BuscaInicialProducaoResponse filme = getMatrixBuscaInicialProducaoResponse();

        BuscaInicialResponse response = toResponse(of(pessoa), of(serie), of(filme));

        assertThat(response.getPessoas()).containsExactly(pessoa);
        assertThat(response.getSeries()).containsExactly(serie);
        assertThat(response.getFilmes()).containsExactly(filme);
    }
}
