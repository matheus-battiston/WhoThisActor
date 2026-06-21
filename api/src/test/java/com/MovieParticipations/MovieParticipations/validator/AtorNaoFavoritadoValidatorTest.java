package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FavoritaAtorRepository;
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
@DisplayName("AtorNaoFavoritadoValidator")
class AtorNaoFavoritadoValidatorTest {
    private static final Long ID_USUARIO = 1L;
    private static final Long ID_ATOR = 2L;

    @Mock
    private FavoritaAtorRepository favoritaAtorRepository;

    @InjectMocks
    private AtorNaoFavoritadoValidator atorNaoFavoritadoValidator;

    @Test
    @DisplayName("Deve retornar verdadeiro quando ator estiver favoritado")
    void deveRetornarVerdadeiroQuandoAtorEstiverFavoritado() {
        when(favoritaAtorRepository.existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR)).thenReturn(true);

        boolean resultado = atorNaoFavoritadoValidator.atorEstaFavoritado(ID_USUARIO, ID_ATOR);

        assertThat(resultado).isTrue();
        verify(favoritaAtorRepository).existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR);
    }

    @Test
    @DisplayName("Deve retornar falso quando ator nao estiver favoritado")
    void deveRetornarFalsoQuandoAtorNaoEstiverFavoritado() {
        when(favoritaAtorRepository.existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR)).thenReturn(false);

        boolean resultado = atorNaoFavoritadoValidator.atorEstaFavoritado(ID_USUARIO, ID_ATOR);

        assertThat(resultado).isFalse();
        verify(favoritaAtorRepository).existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR);
    }
}
