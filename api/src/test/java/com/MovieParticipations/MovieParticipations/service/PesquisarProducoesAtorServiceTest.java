package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.response.ProducaoComPersonagemResponseFactory.getBreakingBadProducaoComPersonagemResponse;
import static com.MovieParticipations.MovieParticipations.factories.response.ProducaoComPersonagemResponseFactory.getMatrixProducaoComPersonagemResponse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarProducoesAtorService")
class PesquisarProducoesAtorServiceTest {

    private static final Long ID_ATOR = 99L;
    private static final String NOME_BUSCADO = "Keanu Reeves";
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";
    private static final String CONHECIDO_POR_ATUACAO = "Acting";
    private static final String CONHECIDO_POR_DIRECAO = "Directing";
    private static final String URL_FOTO_KEANU = "https://image.tmdb.org/t/p/w200/keanu.jpg";
    private static final int TAMANHO_LISTA = 30;

    @Mock
    private SerieAtorRepository serieAtorRepository;

    @Mock
    private FilmeAtorRepository filmeAtorRepository;

    @InjectMocks
    private PesquisarProducoesAtorService service;

    @Test
    @DisplayName("Deve buscar filmes e séries na primeira página de trinta produções")
    void deveBuscarFilmesESeriesNaPrimeiraPaginaDeTrintaProducoes() {
        ProducaoComPersonagemResponse matrix = getMatrixProducaoComPersonagemResponse();
        ProducaoComPersonagemResponse breakingBad = getBreakingBadProducaoComPersonagemResponse();
        Pageable pageable = of(0, TAMANHO_LISTA);

        when(filmeAtorRepository.findProducoesResponsePorAtor(ID_ATOR, pageable))
                .thenReturn(new PageImpl<>(of(matrix)));
        when(serieAtorRepository.findProducoesResponsePorAtor(ID_ATOR, pageable))
                .thenReturn(new PageImpl<>(of(breakingBad)));

        ProducaoAtorResponse resultado = service.pesquisarProducoesDeAtorPorId(ID_ATOR);

        assertThat(resultado.getFilmes()).containsExactly(matrix);
        assertThat(resultado.getSeries()).containsExactly(breakingBad);
        verify(filmeAtorRepository).findProducoesResponsePorAtor(ID_ATOR, pageable);
        verify(serieAtorRepository).findProducoesResponsePorAtor(ID_ATOR, pageable);
    }

    @Test
    @DisplayName("Deve priorizar correspondência exata de nome ignorando acentos e maiúsculas")
    void devePriorizarCorrespondenciaExataDeNomeIgnorandoAcentosEMaiusculas() {
        AtorTMDBDtoPesquisaId atorExato = ator("Kéanu Rééves", CONHECIDO_POR_ATUACAO, 1.0);
        AtorTMDBDtoPesquisaId atorComPrefixo = ator("Keanu Reeves Jr.", CONHECIDO_POR_ATUACAO, 100.0);
        AtorTMDBDtoPesquisaId atorComNomeContido = ator("The Keanu Reeves Story", CONHECIDO_POR_ATUACAO, 100.0);

        AtorTMDBDtoPesquisaId resultado = service.getAtor(
                of(atorComNomeContido, atorComPrefixo, atorExato),
                NOME_BUSCADO
        );

        assertThat(resultado).isSameAs(atorExato);
    }

    @Test
    @DisplayName("Deve priorizar ator cujo nome começa com o termo buscado")
    void devePriorizarAtorCujoNomeComecaComTermoBuscado() {
        AtorTMDBDtoPesquisaId atorComPrefixo = ator("Keanu Reeves Jr.", CONHECIDO_POR_ATUACAO, 1.0);
        AtorTMDBDtoPesquisaId atorComNomeContido = ator("The Keanu Reeves Story", CONHECIDO_POR_ATUACAO, 100.0);

        AtorTMDBDtoPesquisaId resultado = service.getAtor(of(atorComNomeContido, atorComPrefixo), NOME_BUSCADO);

        assertThat(resultado).isSameAs(atorComPrefixo);
    }

    @Test
    @DisplayName("Deve priorizar ator cujo nome contém o termo buscado")
    void devePriorizarAtorCujoNomeContemOTermoBuscado() {
        AtorTMDBDtoPesquisaId atorComNomeContido = ator("The Keanu Reeves Story", CONHECIDO_POR_ATUACAO, 1.0);
        AtorTMDBDtoPesquisaId atorSemCorrespondencia = ator("John Wick", CONHECIDO_POR_ATUACAO, 100.0);

        AtorTMDBDtoPesquisaId resultado = service.getAtor(
                of(atorSemCorrespondencia, atorComNomeContido),
                NOME_BUSCADO
        );

        assertThat(resultado).isSameAs(atorComNomeContido);
    }

    @Test
    @DisplayName("Deve priorizar ator mais popular quando os nomes tiverem a mesma correspondência")
    void devePriorizarAtorMaisPopularQuandoNomesTiveremMesmaCorrespondencia() {
        AtorTMDBDtoPesquisaId atorMenosPopular = ator("Keanu Reeves", CONHECIDO_POR_ATUACAO, 1.0);
        AtorTMDBDtoPesquisaId atorMaisPopular = ator("Keanu Reeves", CONHECIDO_POR_ATUACAO, 100.0);

        AtorTMDBDtoPesquisaId resultado = service.getAtor(
                of(atorMenosPopular, atorMaisPopular),
                NOME_BUSCADO
        );

        assertThat(resultado).isSameAs(atorMaisPopular);
    }

    @Test
    @DisplayName("Deve normalizar nomes nulos ao selecionar ator")
    void deveNormalizarNomesNulosAoSelecionarAtor() {
        AtorTMDBDtoPesquisaId atorSemNome = ator(null, CONHECIDO_POR_ATUACAO, 1.0);
        AtorTMDBDtoPesquisaId atorComNome = ator(NOME_BUSCADO, CONHECIDO_POR_ATUACAO, 100.0);

        AtorTMDBDtoPesquisaId resultado = service.getAtor(of(atorComNome, atorSemNome), null);

        assertThat(resultado).isSameAs(atorSemNome);
    }

    @Test
    @DisplayName("Deve ignorar profissionais que não são atores e retornar erro quando não houver atores")
    void deveIgnorarProfissionaisQueNaoSaoAtoresERetornarErroQuandoNaoHouverAtores() {
        AtorTMDBDtoPesquisaId diretor = ator(NOME_BUSCADO, CONHECIDO_POR_DIRECAO, 100.0);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.getAtor(of(diretor), NOME_BUSCADO)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(ATOR_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve montar informações do ator com imagem TMDB e suas produções")
    void deveMontarInformacoesDoAtorComImagemTmdbESuasProducoes() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        ProducaoComPersonagemResponse matrix = getMatrixProducaoComPersonagemResponse();
        ProducaoComPersonagemResponse breakingBad = getBreakingBadProducaoComPersonagemResponse();
        Pageable pageable = of(0, TAMANHO_LISTA);

        when(filmeAtorRepository.findProducoesResponsePorAtor(ID_ATOR, pageable))
                .thenReturn(new PageImpl<>(of(matrix)));
        when(serieAtorRepository.findProducoesResponsePorAtor(ID_ATOR, pageable))
                .thenReturn(new PageImpl<>(of(breakingBad)));

        AtorEProducoesResponse resultado = service.getAtorInfo(keanu);

        assertThat(resultado.getId()).isEqualTo(keanu.getId());
        assertThat(resultado.getNome()).isEqualTo(keanu.getNome());
        assertThat(resultado.getUrlFoto()).isEqualTo(URL_FOTO_KEANU);
        assertThat(resultado.getFavoritado()).isNull();
        assertThat(resultado.getProducoes().getFilmes()).containsExactly(matrix);
        assertThat(resultado.getProducoes().getSeries()).containsExactly(breakingBad);
    }

    private AtorTMDBDtoPesquisaId ator(String nome, String conhecidoPor, Double popularidade) {
        return AtorTMDBDtoPesquisaId.builder()
                .nome(nome)
                .conhecidoPor(conhecidoPor)
                .popularidade(popularidade)
                .build();
    }
}
