package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getProducaoComTipoMidiaDesconhecido;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarProducoesAtorService")
class AdicionarProducoesAtorServiceTest {

    @Mock
    private AdicionarFilmesDeAtorService adicionarFilmesDeAtorService;

    @Mock
    private AdicionarSeriesDeAtorService adicionarSeriesDeAtorService;

    @Mock
    private CreditosAtorService creditosAtorService;

    @Captor
    private ArgumentCaptor<List<ProducaoTMDBDto>> seriesCaptor;

    @Captor
    private ArgumentCaptor<List<ProducaoTMDBDto>> filmesCaptor;

    @InjectMocks
    private AdicionarProducoesAtorService adicionarProducoesAtorService;

    @Test
    @DisplayName("Deve buscar creditos validos e separar series de filmes")
    void deveBuscarCreditosValidosESepararSeriesDeFilmes() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto serie = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto filme = getMatrixProducaoTMDBDto();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of(serie, filme));

        adicionarProducoesAtorService.adicionar(ator);

        verify(creditosAtorService).buscarCreditosValidos(ator);
        verify(adicionarSeriesDeAtorService).adicionar(ator, seriesCaptor.capture());
        verify(adicionarFilmesDeAtorService).adicionar(ator, filmesCaptor.capture());

        assertThat(seriesCaptor.getValue()).containsExactly(serie);
        assertThat(filmesCaptor.getValue()).containsExactly(filme);
    }

    @Test
    @DisplayName("Deve adicionar series antes de adicionar filmes")
    void deveAdicionarSeriesAntesDeAdicionarFilmes() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto serie = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto filme = getMatrixProducaoTMDBDto();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of(filme, serie));

        adicionarProducoesAtorService.adicionar(ator);

        InOrder inOrder = inOrder(creditosAtorService, adicionarSeriesDeAtorService, adicionarFilmesDeAtorService);
        inOrder.verify(creditosAtorService).buscarCreditosValidos(ator);
        inOrder.verify(adicionarSeriesDeAtorService).adicionar(ator, List.of(serie));
        inOrder.verify(adicionarFilmesDeAtorService).adicionar(ator, List.of(filme));
    }

    @Test
    @DisplayName("Deve enviar lista vazia para series quando houver apenas filmes")
    void deveEnviarListaVaziaParaSeriesQuandoHouverApenasFilmes() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto filme = getMatrixProducaoTMDBDto();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of(filme));

        adicionarProducoesAtorService.adicionar(ator);

        verify(adicionarSeriesDeAtorService).adicionar(ator, List.of());
        verify(adicionarFilmesDeAtorService).adicionar(ator, List.of(filme));
    }

    @Test
    @DisplayName("Deve enviar lista vazia para filmes quando houver apenas series")
    void deveEnviarListaVaziaParaFilmesQuandoHouverApenasSeries() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto serie = getBreakingBadProducaoTMDBDto();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of(serie));

        adicionarProducoesAtorService.adicionar(ator);

        verify(adicionarSeriesDeAtorService).adicionar(ator, List.of(serie));
        verify(adicionarFilmesDeAtorService).adicionar(ator, List.of());
    }

    @Test
    @DisplayName("Deve ignorar creditos com tipo de midia desconhecido")
    void deveIgnorarCreditosComTipoDeMidiaDesconhecido() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto producaoDesconhecida = getProducaoComTipoMidiaDesconhecido();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of(producaoDesconhecida));

        adicionarProducoesAtorService.adicionar(ator);

        verify(adicionarSeriesDeAtorService).adicionar(ator, List.of());
        verify(adicionarFilmesDeAtorService).adicionar(ator, List.of());
    }

    @Test
    @DisplayName("Deve chamar servicos com listas vazias quando ator nao tiver creditos")
    void deveChamarServicosComListasVaziasQuandoAtorNaoTiverCreditos() {
        Ator ator = getKeanuReevesAtorEntityComId();

        when(creditosAtorService.buscarCreditosValidos(ator)).thenReturn(List.of());

        adicionarProducoesAtorService.adicionar(ator);

        verify(adicionarSeriesDeAtorService).adicionar(ator, List.of());
        verify(adicionarFilmesDeAtorService).adicionar(ator, List.of());
    }
}
