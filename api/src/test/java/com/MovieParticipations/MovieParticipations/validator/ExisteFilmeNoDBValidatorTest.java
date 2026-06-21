package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExisteFilmeNoDBValidator")
class ExisteFilmeNoDBValidatorTest {
    private static final Long ID_FILME = 1L;
    private static final String FILME_NAO_ENCONTRADO = "Filme nao encontrado";

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private ExisteFilmeNoDBValidator existeFilmeNoDBValidator;

    @Test
    @DisplayName("Deve aceitar filme existente")
    void deveAceitarFilmeExistente() {
        when(filmeRepository.existsById(ID_FILME)).thenReturn(true);

        assertDoesNotThrow(() -> existeFilmeNoDBValidator.porId(ID_FILME));

        verify(filmeRepository).existsById(ID_FILME);
    }

    @Test
    @DisplayName("Deve retornar erro quando filme nao existir")
    void deveRetornarErroQuandoFilmeNaoExistir() {
        when(filmeRepository.existsById(ID_FILME)).thenReturn(false);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> existeFilmeNoDBValidator.porId(ID_FILME)
        );

        assertThat(erro.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
        verify(filmeRepository).existsById(ID_FILME);
    }
}
