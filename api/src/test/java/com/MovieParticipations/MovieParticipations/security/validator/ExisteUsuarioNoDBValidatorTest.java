package com.MovieParticipations.MovieParticipations.security.validator;

import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExisteUsuarioNoDBValidator")
class ExisteUsuarioNoDBValidatorTest {
    private static final String EMAIL = "usuario@teste.com";

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ExisteUsuarioNoDBValidator existeUsuarioNoDBValidator;

    @Test
    @DisplayName("Deve retornar verdadeiro quando usuario existir")
    void deveRetornarVerdadeiroQuandoUsuarioExistir() {
        when(usuarioRepository.existsByEmail(EMAIL)).thenReturn(true);

        boolean resultado = existeUsuarioNoDBValidator.ExisteUsuarioComEmail(EMAIL);

        assertThat(resultado).isTrue();
        verify(usuarioRepository).existsByEmail(EMAIL);
    }

    @Test
    @DisplayName("Deve retornar falso quando usuario nao existir")
    void deveRetornarFalsoQuandoUsuarioNaoExistir() {
        when(usuarioRepository.existsByEmail(EMAIL)).thenReturn(false);

        boolean resultado = existeUsuarioNoDBValidator.ExisteUsuarioComEmail(EMAIL);

        assertThat(resultado).isFalse();
        verify(usuarioRepository).existsByEmail(EMAIL);
    }
}
