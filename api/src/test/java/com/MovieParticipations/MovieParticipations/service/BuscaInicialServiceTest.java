package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialPessoaResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getBryanCranstonAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getSerieEntityComId;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscaInicialService")
class BuscaInicialServiceTest {
    private static final int QUANTIDADE_ITENS_ESPERADA = 2;
    private static final Long ID_SERIE_TESTE = 3L;
    private static final Long ID_FILME_TESTE = 4L;
    private static final String METODO_BUSCAR = "buscar";
    private static final String CACHE_BUSCA_INICIAL = "buscaInicial";
    private static final String CHAVE_CACHE_BUSCA_INICIAL = "'dados'";
    private static final String TITULO_FILME_ATUALIZADO = "Matrix Atualizado";
    private static final String TITULO_SERIE_ATUALIZADO = "Breaking Bad Atualizado";
    private static final String OVERVIEW_FILME_ATUALIZADO = "Overview atualizado do filme.";
    private static final String OVERVIEW_SERIE_ATUALIZADO = "Overview atualizada da serie.";

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private AtualizarFilmeInfoService atualizarFilmeInfoService;

    @Mock
    private AtualizarSerieInfoService atualizarSerieInfoService;

    @InjectMocks
    private BuscaInicialService service;

    @Test
    @DisplayName("Deve buscar dois itens aleatórios do top 100 de pessoas, séries e filmes")
    void deveBuscarDoisItensAleatoriosDoTop100DeCadaTipo() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        Ator bryan = getBryanCranstonAtorEntityComId();
        Serie breakingBad = getBreakingBadSerieEntityComId();
        Serie serie = getSerieEntityComId(ID_SERIE_TESTE);
        Filme matrix = getMatrixFilmeEntityComId();
        Filme filme = getFilmeEntityComId(ID_FILME_TESTE);

        when(atorRepository.buscarDoisAtoresAleatoriosDoTop100()).thenReturn(of(keanu, bryan));
        when(serieRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(breakingBad, serie));
        when(filmeRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(matrix, filme));

        BuscaInicialResponse resultado = service.buscar();

        assertThat(resultado.getPessoas())
                .hasSize(QUANTIDADE_ITENS_ESPERADA)
                .extracting(BuscaInicialPessoaResponse::getId)
                .containsExactlyInAnyOrder(keanu.getId(), bryan.getId());
        assertThat(resultado.getSeries())
                .hasSize(QUANTIDADE_ITENS_ESPERADA)
                .extracting(BuscaInicialProducaoResponse::getId)
                .containsExactlyInAnyOrder(breakingBad.getId(), serie.getId());
        assertThat(resultado.getFilmes())
                .hasSize(QUANTIDADE_ITENS_ESPERADA)
                .extracting(BuscaInicialProducaoResponse::getId)
                .containsExactlyInAnyOrder(matrix.getId(), filme.getId());
        assertThat(resultado.getPessoas()).extracting(BuscaInicialPessoaResponse::getNome)
                .containsExactlyInAnyOrder(keanu.getNome(), bryan.getNome());
        assertThat(resultado.getSeries()).extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactlyInAnyOrder(breakingBad.getTitulo(), serie.getTitulo());
        assertThat(resultado.getFilmes()).extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactlyInAnyOrder(matrix.getTitulo(), filme.getTitulo());
        assertThat(resultado.getSeries()).extracting(BuscaInicialProducaoResponse::getOverview)
                .containsExactlyInAnyOrder(breakingBad.getOverview(), serie.getOverview());
        assertThat(resultado.getFilmes()).extracting(BuscaInicialProducaoResponse::getOverview)
                .containsExactlyInAnyOrder(matrix.getOverview(), filme.getOverview());
        verify(atorRepository).buscarDoisAtoresAleatoriosDoTop100();
        verify(serieRepository).buscarDoisAleatoriosDoTop100();
        verify(filmeRepository).buscarDoisAleatoriosDoTop100();
        verifyNoInteractions(atualizarFilmeInfoService, atualizarSerieInfoService);
    }

    @Test
    @DisplayName("Deve atualizar filme e série sem info atualizada antes de montar response")
    void deveAtualizarFilmeESerieSemInfoAtualizadaAntesDeMontarResponse() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        Serie breakingBad = getBreakingBadSerieEntityComId();
        Filme matrix = getMatrixFilmeEntityComId();
        breakingBad.setInfoAtualizado(null);
        matrix.setInfoAtualizado(false);

        when(atorRepository.buscarDoisAtoresAleatoriosDoTop100()).thenReturn(of(keanu));
        when(serieRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(breakingBad));
        when(filmeRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(matrix));
        doAnswer(invocation -> {
            Filme filme = invocation.getArgument(0);
            filme.setTitulo(TITULO_FILME_ATUALIZADO);
            filme.setOverview(OVERVIEW_FILME_ATUALIZADO);
            filme.setInfoAtualizado(true);
            return null;
        }).when(atualizarFilmeInfoService).atualizar(matrix);
        doAnswer(invocation -> {
            Serie serie = invocation.getArgument(0);
            serie.setTitulo(TITULO_SERIE_ATUALIZADO);
            serie.setOverview(OVERVIEW_SERIE_ATUALIZADO);
            serie.setInfoAtualizado(true);
            return null;
        }).when(atualizarSerieInfoService).atualizar(breakingBad);

        BuscaInicialResponse resultado = service.buscar();

        assertThat(resultado.getSeries())
                .hasSize(1)
                .extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactly(TITULO_SERIE_ATUALIZADO);
        assertThat(resultado.getFilmes())
                .hasSize(1)
                .extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactly(TITULO_FILME_ATUALIZADO);
        assertThat(resultado.getSeries()).extracting(BuscaInicialProducaoResponse::getUrlImagem)
                .doesNotContainNull();
        assertThat(resultado.getFilmes()).extracting(BuscaInicialProducaoResponse::getUrlImagem)
                .doesNotContainNull();
        assertThat(resultado.getSeries()).extracting(BuscaInicialProducaoResponse::getOverview)
                .containsExactly(OVERVIEW_SERIE_ATUALIZADO);
        assertThat(resultado.getFilmes()).extracting(BuscaInicialProducaoResponse::getOverview)
                .containsExactly(OVERVIEW_FILME_ATUALIZADO);
        verify(atualizarSerieInfoService).atualizar(breakingBad);
        verify(atualizarFilmeInfoService).atualizar(matrix);
    }

    @Test
    @DisplayName("Não deve atualizar filme e série quando info já estiver atualizada")
    void naoDeveAtualizarFilmeESerieQuandoInfoJaEstiverAtualizada() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        Serie breakingBad = getBreakingBadSerieEntityComId();
        Filme matrix = getMatrixFilmeEntityComId();
        breakingBad.setInfoAtualizado(true);
        matrix.setInfoAtualizado(true);

        when(atorRepository.buscarDoisAtoresAleatoriosDoTop100()).thenReturn(of(keanu));
        when(serieRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(breakingBad));
        when(filmeRepository.buscarDoisAleatoriosDoTop100()).thenReturn(of(matrix));

        BuscaInicialResponse resultado = service.buscar();

        assertThat(resultado.getSeries()).extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactly(breakingBad.getTitulo());
        assertThat(resultado.getFilmes()).extracting(BuscaInicialProducaoResponse::getNomeProducao)
                .containsExactly(matrix.getTitulo());
        verify(atualizarSerieInfoService, never()).atualizar(breakingBad);
        verify(atualizarFilmeInfoService, never()).atualizar(matrix);
    }

    @Test
    @DisplayName("Deve retornar listas vazias quando não houver dados no banco")
    void deveRetornarListasVaziasQuandoNaoHouverDadosNoBanco() {
        when(atorRepository.buscarDoisAtoresAleatoriosDoTop100()).thenReturn(List.of());
        when(serieRepository.buscarDoisAleatoriosDoTop100()).thenReturn(List.of());
        when(filmeRepository.buscarDoisAleatoriosDoTop100()).thenReturn(List.of());

        BuscaInicialResponse resultado = service.buscar();

        assertThat(resultado.getPessoas()).isEmpty();
        assertThat(resultado.getSeries()).isEmpty();
        assertThat(resultado.getFilmes()).isEmpty();
    }

    @Test
    @DisplayName("Deve manter cache Redis no mesmo padrão da busca de providers")
    void deveManterCacheRedisNoMesmoPadraoDaBuscaDeProviders() throws NoSuchMethodException {
        Method method = BuscaInicialService.class.getMethod(METODO_BUSCAR);

        Cacheable cacheable = method.getAnnotation(Cacheable.class);

        assertThat(cacheable).isNotNull();
        assertThat(cacheable.cacheNames()).containsExactly(CACHE_BUSCA_INICIAL);
        assertThat(cacheable.key()).isEqualTo(CHAVE_CACHE_BUSCA_INICIAL);
    }

}
