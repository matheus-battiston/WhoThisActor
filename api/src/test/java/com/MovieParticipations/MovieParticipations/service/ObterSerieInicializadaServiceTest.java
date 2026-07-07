package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterSerieInicializadaService")
class ObterSerieInicializadaServiceTest {

    private static final Long ID_SERIE = 2L;
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao encontrada";

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private AdicionarSerieService adicionarSerieService;

    @Mock
    private DeveAtualizarSerieService deveAtualizarSerieService;

    @Mock
    private AtualizarSerieInfoService atualizarSerieInfoService;

    @InjectMocks
    private ObterSerieInicializadaService service;

    @Test
    @DisplayName("Deve retornar série inicializada sem buscar elenco")
    void deveRetornarSerieInicializadaSemBuscarElenco() {
        Serie serie = getBreakingBadSerieEntityComId();

        when(serieRepository.findById(ID_SERIE)).thenReturn(of(serie));

        Serie resultado = service.obter(ID_SERIE);

        assertThat(resultado).isSameAs(serie);
        verify(serieRepository).findById(ID_SERIE);
        verify(deveAtualizarSerieService).deveAtualizar(serie);
        verifyNoInteractions(adicionarSerieService, atualizarSerieInfoService);
    }

    @Test
    @DisplayName("Deve atualizar info antes de retornar série com info desatualizada")
    void deveAtualizarInfoAntesDeRetornarSerieComInfoDesatualizada() {
        Serie serie = getBreakingBadSerieEntityComId();

        when(serieRepository.findById(ID_SERIE)).thenReturn(of(serie));
        when(deveAtualizarSerieService.deveAtualizar(serie)).thenReturn(true);

        Serie resultado = service.obter(ID_SERIE);

        assertThat(resultado).isSameAs(serie);
        InOrder inOrder = inOrder(deveAtualizarSerieService, atualizarSerieInfoService, adicionarSerieService);
        inOrder.verify(deveAtualizarSerieService).deveAtualizar(serie);
        inOrder.verify(atualizarSerieInfoService).atualizar(serie);
        verifyNoInteractions(adicionarSerieService);
    }

    @Test
    @DisplayName("Deve inicializar elenco antes de retornar série não inicializada")
    void deveInicializarElencoAntesDeRetornarSerieNaoInicializada() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setElencoInicializado(false);

        when(serieRepository.findById(ID_SERIE)).thenReturn(of(serie));

        Serie resultado = service.obter(ID_SERIE);

        assertThat(resultado).isSameAs(serie);
        verify(serieRepository).findById(ID_SERIE);
        verify(deveAtualizarSerieService).deveAtualizar(serie);
        verify(adicionarSerieService).adicionarElenco(serie);
        verifyNoInteractions(atualizarSerieInfoService);
    }

    @Test
    @DisplayName("Deve retornar erro quando série não for encontrada")
    void deveRetornarErroQuandoSerieNaoForEncontrada() {
        when(serieRepository.findById(ID_SERIE)).thenReturn(empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.obter(ID_SERIE)
        );

        assertThat(erro.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
        verify(serieRepository).findById(ID_SERIE);
        verifyNoInteractions(adicionarSerieService, deveAtualizarSerieService, atualizarSerieInfoService);
    }
}
