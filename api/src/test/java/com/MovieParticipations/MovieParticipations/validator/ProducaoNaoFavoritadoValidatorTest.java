package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
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
@DisplayName("ProducaoNaoFavoritadoValidator")
class ProducaoNaoFavoritadoValidatorTest {
    private static final Long ID_USUARIO = 1L;
    private static final Long ID_SERIE = 2L;
    private static final Long ID_FILME = 3L;

    @Mock
    private FavoritaSerieRepository favoritaSerieRepository;

    @Mock
    private FavoritaFilmeRepository favoritaFilmeRepository;

    @InjectMocks
    private ProducaoNaoFavoritadoValidator producaoNaoFavoritadoValidator;

    @Test
    @DisplayName("Deve retornar favorito da serie consultado no repositorio")
    void deveRetornarFavoritoDaSerieConsultadoNoRepositorio() {
        when(favoritaSerieRepository.existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE)).thenReturn(true);

        boolean resultado = producaoNaoFavoritadoValidator.serieEstaFavoritada(ID_USUARIO, ID_SERIE);

        assertThat(resultado).isTrue();
        verify(favoritaSerieRepository).existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE);
    }

    @Test
    @DisplayName("Deve retornar nao favorito do filme consultado no repositorio")
    void deveRetornarNaoFavoritoDoFilmeConsultadoNoRepositorio() {
        when(favoritaFilmeRepository.existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME)).thenReturn(false);

        boolean resultado = producaoNaoFavoritadoValidator.filmeEstaFavoritado(ID_USUARIO, ID_FILME);

        assertThat(resultado).isFalse();
        verify(favoritaFilmeRepository).existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME);
    }
}
