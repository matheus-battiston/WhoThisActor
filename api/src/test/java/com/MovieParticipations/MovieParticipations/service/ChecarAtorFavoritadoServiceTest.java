package com.MovieParticipations.MovieParticipations.service;

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
@DisplayName("ChecarAtorFavoritadoService")
class ChecarAtorFavoritadoServiceTest {
    private static final Long ID_ATOR = 2L;
    private static final Long AUTH_USER_ID = 1L;

    @Mock
    private FavoritaAtorRepository favoritaAtorRepository;

    @InjectMocks
    private ChecarAtorFavoritadoService checarAtorFavoritadoService;

    @Test
    @DisplayName("Deve retornar verdadeiro e consultar repositorio com auth user id e id do ator")
    void deveRetornarVerdadeiroEConsultarRepositorioComAuthUserIdEIdDoAtor() {
        when(favoritaAtorRepository.existsByAuthUserIdAndAtorId(AUTH_USER_ID, ID_ATOR)).thenReturn(true);

        Boolean resultado = checarAtorFavoritadoService.estaFavoritadoPorAuthId(ID_ATOR, AUTH_USER_ID);

        assertThat(resultado).isTrue();
        verify(favoritaAtorRepository).existsByAuthUserIdAndAtorId(AUTH_USER_ID, ID_ATOR);
    }

    @Test
    @DisplayName("Deve retornar falso quando ator nao estiver favoritado")
    void deveRetornarFalsoQuandoAtorNaoEstiverFavoritado() {
        when(favoritaAtorRepository.existsByAuthUserIdAndAtorId(AUTH_USER_ID, ID_ATOR)).thenReturn(false);

        Boolean resultado = checarAtorFavoritadoService.estaFavoritadoPorAuthId(ID_ATOR, AUTH_USER_ID);

        assertThat(resultado).isFalse();
        verify(favoritaAtorRepository).existsByAuthUserIdAndAtorId(AUTH_USER_ID, ID_ATOR);
    }
}
