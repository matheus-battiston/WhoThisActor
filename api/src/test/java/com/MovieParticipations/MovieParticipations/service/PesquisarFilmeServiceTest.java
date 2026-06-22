package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.PesquisaFilmeCache;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.PesquisaFilmeCacheRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static java.time.LocalDate.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarFilmeService")
class PesquisarFilmeServiceTest {

    private static final String NOME_ORIGINAL = "  Mátrix  ";
    private static final String TERMO_NORMALIZADO = "matrix";
    private static final Long ID_FILME = 1L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String IMAGEM_MATRIX = "/matrix.jpg";
    private static final Double POPULARIDADE_MATRIX = 10.0;

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private AdicionarFilmeService adicionarFilmeService;

    @Mock
    private PesquisaFilmeCacheRepository pesquisaFilmeCacheRepository;

    @Captor
    private ArgumentCaptor<PesquisaFilmeCache> cacheCaptor;

    @InjectMocks
    private PesquisarFilmeService service;

    @Test
    @DisplayName("Deve buscar TMDB e salvar cache quando termo ainda não estiver sincronizado")
    void deveBuscarTmdbESalvarCacheQuandoTermoAindaNaoEstiverSincronizado() {
        Filme matrix = getMatrixFilmeEntityComId();

        when(pesquisaFilmeCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(false);
        when(filmeRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of(matrix));

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).hasSize(1);
        PesquisaProducaoInfoResponse filme = resultado.get(0);
        assertThat(filme.getId()).isEqualTo(ID_FILME);
        assertThat(filme.getNome()).isEqualTo(TITULO_MATRIX);
        assertThat(filme.getImagem()).isEqualTo(IMAGEM_MATRIX);
        assertThat(filme.getPopularidade()).isEqualTo(POPULARIDADE_MATRIX);
        assertThat(filme.getTipoMidia()).isEqualTo(MOVIE);
        verify(adicionarFilmeService).adicionarFilmeComNome(NOME_ORIGINAL);
        verify(pesquisaFilmeCacheRepository).save(cacheCaptor.capture());
        assertThat(cacheCaptor.getValue().getTermoNormalizado()).isEqualTo(TERMO_NORMALIZADO);
        assertThat(cacheCaptor.getValue().getUltimaSincronizacao()).isEqualTo(now());
        verify(filmeRepository).findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO);
    }

    @Test
    @DisplayName("Não deve buscar TMDB nem salvar cache quando termo já estiver sincronizado")
    void naoDeveBuscarTmdbNemSalvarCacheQuandoTermoJaEstiverSincronizado() {
        Filme matrix = getMatrixFilmeEntityComId();

        when(pesquisaFilmeCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(true);
        when(filmeRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of(matrix));

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).extracting(PesquisaProducaoInfoResponse::getNome)
                .containsExactly(TITULO_MATRIX);
        verifyNoInteractions(adicionarFilmeService);
        verify(pesquisaFilmeCacheRepository).existsByTermoNormalizado(TERMO_NORMALIZADO);
        verify(filmeRepository).findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum filme corresponder ao termo")
    void deveRetornarListaVaziaQuandoNenhumFilmeCorresponderAoTermo() {
        when(pesquisaFilmeCacheRepository.existsByTermoNormalizado(TERMO_NORMALIZADO)).thenReturn(true);
        when(filmeRepository.findByTituloNormalizadoOrderByPopularidadeDesc(TERMO_NORMALIZADO))
                .thenReturn(of());

        List<PesquisaProducaoInfoResponse> resultado = service.pesquisaPorNome(NOME_ORIGINAL);

        assertThat(resultado).isEmpty();
        verifyNoInteractions(adicionarFilmeService);
    }
}
