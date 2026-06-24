package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
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
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExisteAtorNoDbValidator")
class ExisteAtorNoDbValidatorTest {
    private static final Long ID_ATOR = 1L;
    private static final String ATOR_NAO_EXISTE = "Não existe um ator com esse ID";

    @Mock
    private AtorRepository atorRepository;

    @InjectMocks
    private ExisteAtorNoDbValidator existeAtorNoDbValidator;

    @Test
    @DisplayName("Deve aceitar ator existente")
    void deveAceitarAtorExistente() {
        when(atorRepository.existsAtorById(ID_ATOR)).thenReturn(true);

        assertDoesNotThrow(() -> existeAtorNoDbValidator.porId(ID_ATOR));

        verify(atorRepository).existsAtorById(ID_ATOR);
    }

    @Test
    @DisplayName("Deve retornar erro quando ator nao existir")
    void deveRetornarErroQuandoAtorNaoExistir() {
        when(atorRepository.existsAtorById(ID_ATOR)).thenReturn(false);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> existeAtorNoDbValidator.porId(ID_ATOR)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(ATOR_NAO_EXISTE);
        verify(atorRepository).existsAtorById(ID_ATOR);
    }
}
