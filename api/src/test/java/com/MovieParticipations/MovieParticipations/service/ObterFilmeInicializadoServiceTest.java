package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterFilmeInicializadoService")
class ObterFilmeInicializadoServiceTest {

    private static final Long ID_FILME = 1L;
    private static final String FILME_NAO_ENCONTRADO = "Filem com esse id nao foi encontrada";

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private AdicionarFilmeService adicionarFilmeService;

    @Mock
    private DeveAtualizarFilmeService deveAtualizarFilmeService;

    @Mock
    private AtualizarFilmeInfoService atualizarFilmeInfoService;

    @InjectMocks
    private ObterFilmeInicializadoService service;

    @Test
    @DisplayName("Deve retornar filme inicializado sem buscar elenco")
    void deveRetornarFilmeInicializadoSemBuscarElenco() {
        Filme filme = getMatrixFilmeEntityComId();

        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.of(filme));

        Filme resultado = service.obter(ID_FILME);

        assertThat(resultado).isSameAs(filme);
        verify(filmeRepository).findById(ID_FILME);
        verify(deveAtualizarFilmeService).deveAtualizar(filme);
        verifyNoInteractions(adicionarFilmeService, atualizarFilmeInfoService);
    }

    @Test
    @DisplayName("Deve atualizar info antes de retornar filme com info desatualizada")
    void deveAtualizarInfoAntesDeRetornarFilmeComInfoDesatualizada() {
        Filme filme = getMatrixFilmeEntityComId();

        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.of(filme));
        when(deveAtualizarFilmeService.deveAtualizar(filme)).thenReturn(true);

        Filme resultado = service.obter(ID_FILME);

        assertThat(resultado).isSameAs(filme);
        InOrder inOrder = inOrder(deveAtualizarFilmeService, atualizarFilmeInfoService, adicionarFilmeService);
        inOrder.verify(deveAtualizarFilmeService).deveAtualizar(filme);
        inOrder.verify(atualizarFilmeInfoService).atualizar(filme);
        verifyNoInteractions(adicionarFilmeService);
    }

    @Test
    @DisplayName("Deve inicializar elenco antes de retornar filme não inicializado")
    void deveInicializarElencoAntesDeRetornarFilmeNaoInicializado() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setElencoInicializado(false);

        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.of(filme));

        Filme resultado = service.obter(ID_FILME);

        assertThat(resultado).isSameAs(filme);
        verify(filmeRepository).findById(ID_FILME);
        verify(deveAtualizarFilmeService).deveAtualizar(filme);
        verify(adicionarFilmeService).adicionarElenco(filme);
        verifyNoInteractions(atualizarFilmeInfoService);
    }

    @Test
    @DisplayName("Deve retornar erro quando filme não for encontrado")
    void deveRetornarErroQuandoFilmeNaoForEncontrado() {
        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.obter(ID_FILME)
        );

        assertThat(erro.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
        verify(filmeRepository).findById(ID_FILME);
        verifyNoInteractions(adicionarFilmeService, deveAtualizarFilmeService, atualizarFilmeInfoService);
    }
}
