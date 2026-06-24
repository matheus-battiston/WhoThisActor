package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
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
@DisplayName("ExisteSerieNoDBValidator")
class ExisteSerieNoDBValidatorTest {
    private static final Long ID_SERIE = 1L;
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao encontrada";

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private ExisteSerieNoDBValidator existeSerieNoDBValidator;

    @Test
    @DisplayName("Deve aceitar serie existente")
    void deveAceitarSerieExistente() {
        when(serieRepository.existsSerieById(ID_SERIE)).thenReturn(true);

        assertDoesNotThrow(() -> existeSerieNoDBValidator.porId(ID_SERIE));

        verify(serieRepository).existsSerieById(ID_SERIE);
    }

    @Test
    @DisplayName("Deve retornar erro quando serie nao existir")
    void deveRetornarErroQuandoSerieNaoExistir() {
        when(serieRepository.existsSerieById(ID_SERIE)).thenReturn(false);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> existeSerieNoDBValidator.porId(ID_SERIE)
        );

        assertThat(erro.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
        verify(serieRepository).existsSerieById(ID_SERIE);
    }
}
