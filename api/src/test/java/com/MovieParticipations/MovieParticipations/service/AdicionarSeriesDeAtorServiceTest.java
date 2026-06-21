package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarSeriesDeAtorService")
class AdicionarSeriesDeAtorServiceTest {

    @Mock
    private SerieResolverService serieResolverService;

    @Mock
    private SerieAtorVinculoService serieAtorVinculoService;

    @InjectMocks
    private AdicionarSeriesDeAtorService adicionarSeriesDeAtorService;

    @Test
    @DisplayName("Deve resolver series e vincular ao ator")
    void deveResolverSeriesEVincularAoAtor() {
        Ator ator = getKeanuReevesAtorEntityComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getBreakingBadProducaoTMDBDto());
        List<Serie> series = List.of(getBreakingBadSerieEntityComId());

        when(serieResolverService.resolverSerie(producoesDto)).thenReturn(series);

        adicionarSeriesDeAtorService.adicionar(ator, producoesDto);

        verify(serieResolverService).resolverSerie(producoesDto);
        verify(serieAtorVinculoService).vincularAtorASerie(ator, series, producoesDto);
    }

    @Test
    @DisplayName("Deve resolver series antes de vincular ao ator")
    void deveResolverSeriesAntesDeVincularAoAtor() {
        Ator ator = getKeanuReevesAtorEntityComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getBreakingBadProducaoTMDBDto());
        List<Serie> series = List.of(getBreakingBadSerieEntityComId());

        when(serieResolverService.resolverSerie(producoesDto)).thenReturn(series);

        adicionarSeriesDeAtorService.adicionar(ator, producoesDto);

        InOrder inOrder = inOrder(serieResolverService, serieAtorVinculoService);
        inOrder.verify(serieResolverService).resolverSerie(producoesDto);
        inOrder.verify(serieAtorVinculoService).vincularAtorASerie(ator, series, producoesDto);
    }

    @Test
    @DisplayName("Deve resolver lista vazia e vincular lista vazia ao ator")
    void deveResolverListaVaziaEVincularListaVaziaAoAtor() {
        Ator ator = getKeanuReevesAtorEntityComId();
        List<ProducaoTMDBDto> producoesDto = List.of();
        List<Serie> series = List.of();

        when(serieResolverService.resolverSerie(producoesDto)).thenReturn(series);

        adicionarSeriesDeAtorService.adicionar(ator, producoesDto);

        verify(serieResolverService).resolverSerie(producoesDto);
        verify(serieAtorVinculoService).vincularAtorASerie(ator, series, producoesDto);
    }
}
