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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisaSerieService")
class PesquisaSerieServiceTest {

    private static final String NOME_ORIGINAL = "  Bréaking Bad  ";
    private static final String TERMO_NORMALIZADO = "breaking bad";
    private static final Long ID_SERIE = 2L;
    private static final String TITULO_BREAKING_BAD = "Breaking Bad";
    private static final String IMAGEM_BREAKING_BAD = "/breaking-bad.jpg";
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private AdicionarSerieService adicionarSerieService;

    @Mock
    private PesquisaSerieCacheRepository pesquisaSerieCacheRepository;

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

        assertThat(resultado).hasSize(1);
        PesquisaProducaoInfoResponse serie = resultado.get(0);
        assertThat(serie.getId()).isEqualTo(ID_SERIE);
        assertThat(serie.getNome()).isEqualTo(TITULO_BREAKING_BAD);
        assertThat(serie.getImagem()).isEqualTo(IMAGEM_BREAKING_BAD);
        assertThat(serie.getPopularidade()).isEqualTo(POPULARIDADE_BREAKING_BAD);
        assertThat(serie.getTipoMidia()).isEqualTo(TV);
        verify(adicionarSerieService).adicionarSerieComNome(NOME_ORIGINAL);
        verify(pesquisaSerieCacheRepository).save(cacheCaptor.capture());
        assertThat(cacheCaptor.getValue().getTermoNormalizado()).isEqualTo(TERMO_NORMALIZADO);
        assertThat(cacheCaptor.getValue().getUltimaSincronizacao()).isEqualTo(LocalDate.now());
        verify(serieRepository).findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO);
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
