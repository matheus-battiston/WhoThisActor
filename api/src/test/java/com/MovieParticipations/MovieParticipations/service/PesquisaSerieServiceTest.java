package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.PesquisaSerieCache;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.PesquisaSerieCacheRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisaSerieService")
class PesquisaSerieServiceTest {

    private static final int QUANTIDADE_RESULTADOS_ESPERADA = 1;
    private static final int INDICE_PRIMEIRO_RESULTADO = 0;
    private static final String NOME_ORIGINAL = "  Bréaking Bad  ";
    private static final String TERMO_NORMALIZADO = "breaking bad";
    private static final Long ID_SERIE = 2L;
    private static final String TITULO_BREAKING_BAD = "Breaking Bad";
    private static final String IMAGEM_BREAKING_BAD = "/breaking-bad.jpg";
    private static final String OVERVIEW_BREAKING_BAD = "Um professor de quimica diagnosticado com cancer passa a produzir metanfetamina.";
    private static final String GENERO_BREAKING_BAD = "Drama";
    private static final Integer ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD = 2008;
    private static final Integer ANO_ULTIMA_TEMPORADA_BREAKING_BAD = 2013;
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private AdicionarSerieService adicionarSerieService;

    @Mock
    private PesquisaSerieCacheRepository pesquisaSerieCacheRepository;

    @Mock
    private DeveAtualizarSerieService deveAtualizarSerieService;

    @Mock
    private AtualizarSerieInfoService atualizarSerieInfoService;

    @Captor
    private ArgumentCaptor<PesquisaSerieCache> cacheCaptor;

    @InjectMocks
    private PesquisaSerieService service;

    @Test
    @DisplayName("Deve buscar TMDB e salvar cache quando termo ainda não estiver sincronizado")
    void deveBuscarTmdbESalvarCacheQuandoTermoAindaNaoEstiverSincronizado() {
        Serie breakingBad = getBreakingBadSerieEntityComId();

        when(pesquisaSerieCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(false);
        when(serieRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of(breakingBad));

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).hasSize(QUANTIDADE_RESULTADOS_ESPERADA);
        PesquisaProducaoInfoResponse serie = resultado.get(INDICE_PRIMEIRO_RESULTADO);
        assertThat(serie.getId()).isEqualTo(ID_SERIE);
        assertThat(serie.getNome()).isEqualTo(TITULO_BREAKING_BAD);
        assertThat(serie.getImagem()).isEqualTo(IMAGEM_BREAKING_BAD);
        assertThat(serie.getOverview()).isEqualTo(OVERVIEW_BREAKING_BAD);
        assertThat(serie.getGenero()).isEqualTo(GENERO_BREAKING_BAD);
        assertThat(serie.getAnoPrimeiraTemporada()).isEqualTo(ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD);
        assertThat(serie.getAnoUltimaTemporada()).isEqualTo(ANO_ULTIMA_TEMPORADA_BREAKING_BAD);
        assertThat(serie.getPopularidade()).isEqualTo(POPULARIDADE_BREAKING_BAD);
        assertThat(serie.getTipoMidia()).isEqualTo(TV);
        verify(adicionarSerieService).adicionarSerieComNome(NOME_ORIGINAL);
        verify(pesquisaSerieCacheRepository).save(cacheCaptor.capture());
        assertThat(cacheCaptor.getValue().getTermoNormalizado()).isEqualTo(TERMO_NORMALIZADO);
        assertThat(cacheCaptor.getValue().getUltimaSincronizacao()).isEqualTo(LocalDate.now());
        verify(serieRepository).findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO);
    }

    @Test
    @DisplayName("Deve inicializar infos da série na pesquisa sem inicializar elenco")
    void deveInicializarInfosDaSerieNaPesquisaSemInicializarElenco() {
        Serie breakingBad = getBreakingBadSerieEntityComId();
        breakingBad.setOverview(null);
        breakingBad.setGenero(null);
        breakingBad.setAnoPrimeiraTemporada(null);
        breakingBad.setAnoUltimaTemporada(null);
        breakingBad.setInfoAtualizado(false);
        breakingBad.setElencoInicializado(false);

        when(pesquisaSerieCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(true);
        when(serieRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of(breakingBad));
        when(deveAtualizarSerieService.deveAtualizar(breakingBad)).thenReturn(true);
        doAnswer(invocation -> {
            Serie serie = invocation.getArgument(0);
            serie.setOverview(OVERVIEW_BREAKING_BAD);
            serie.setGenero(GENERO_BREAKING_BAD);
            serie.setAnoPrimeiraTemporada(ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD);
            serie.setAnoUltimaTemporada(ANO_ULTIMA_TEMPORADA_BREAKING_BAD);
            serie.setInfoAtualizado(true);
            return null;
        }).when(atualizarSerieInfoService).atualizar(breakingBad);

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).hasSize(QUANTIDADE_RESULTADOS_ESPERADA);
        PesquisaProducaoInfoResponse serie = resultado.get(INDICE_PRIMEIRO_RESULTADO);
        assertThat(serie.getOverview()).isEqualTo(OVERVIEW_BREAKING_BAD);
        assertThat(serie.getGenero()).isEqualTo(GENERO_BREAKING_BAD);
        assertThat(serie.getAnoPrimeiraTemporada()).isEqualTo(ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD);
        assertThat(serie.getAnoUltimaTemporada()).isEqualTo(ANO_ULTIMA_TEMPORADA_BREAKING_BAD);
        verify(atualizarSerieInfoService).atualizar(breakingBad);
        verify(adicionarSerieService, never()).adicionarElenco(breakingBad);
    }

    @Test
    @DisplayName("Não deve buscar TMDB nem salvar cache quando termo já estiver sincronizado")
    void naoDeveBuscarTmdbNemSalvarCacheQuandoTermoJaEstiverSincronizado() {
        Serie breakingBad = getBreakingBadSerieEntityComId();

        when(pesquisaSerieCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(true);
        when(serieRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of(breakingBad));

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).extracting(PesquisaProducaoInfoResponse::getNome)
                .containsExactly(TITULO_BREAKING_BAD);
        verifyNoInteractions(adicionarSerieService);
        verify(pesquisaSerieCacheRepository).existsByTermoNormalizado(TERMO_NORMALIZADO);
        verify(serieRepository).findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhuma série corresponder ao termo")
    void deveRetornarListaVaziaQuandoNenhumaSerieCorresponderAoTermo() {
        when(pesquisaSerieCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(true);
        when(serieRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of());

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).isEmpty();
        verifyNoInteractions(adicionarSerieService);
    }
}
