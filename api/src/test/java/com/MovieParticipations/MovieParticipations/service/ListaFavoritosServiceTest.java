package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListaFavoritosService")
class ListaFavoritosServiceTest {

    private static final Long ID_SERIE_FAVORITA = 2L;
    private static final Long ID_FILME_FAVORITO = 1L;

    @Mock
    private FavoritaSerieRepository favoritaSerieRepository;

    @Mock
    private FavoritaFilmeRepository favoritaFilmeRepository;

    @InjectMocks
    private ListaFavoritosService service;

    @Test
    @DisplayName("Deve buscar IDs das séries favoritas do usuário autenticado")
    void deveBuscarIdsDasSeriesFavoritasDoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<Long> idsSeriesFavoritas = of(ID_SERIE_FAVORITA);

        when(favoritaSerieRepository.findSerieIdByAuthUserId(usuarioAutenticado.getId()))
                .thenReturn(idsSeriesFavoritas);

        List<Long> resultado = service.buscarIdsSeriesFavoritas(usuarioAutenticado);

        assertEquals(idsSeriesFavoritas, resultado);
        verify(favoritaSerieRepository).findSerieIdByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve buscar IDs dos filmes favoritos do usuário autenticado")
    void deveBuscarIdsDosFilmesFavoritosDoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<Long> idsFilmesFavoritos = of(ID_FILME_FAVORITO);

        when(favoritaFilmeRepository.findFilmesIdByAuthUserId(usuarioAutenticado.getId()))
                .thenReturn(idsFilmesFavoritos);

        List<Long> resultado = service.buscarIdsFilmesFavoritos(usuarioAutenticado);

        assertEquals(idsFilmesFavoritos, resultado);
        verify(favoritaFilmeRepository).findFilmesIdByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia de séries favoritas")
    void deveRetornarListaVaziaDeSeriesFavoritas() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<Long> idsSeriesFavoritas = of();

        when(favoritaSerieRepository.findSerieIdByAuthUserId(usuarioAutenticado.getId()))
                .thenReturn(idsSeriesFavoritas);

        List<Long> resultado = service.buscarIdsSeriesFavoritas(usuarioAutenticado);

        assertEquals(idsSeriesFavoritas, resultado);
        verify(favoritaSerieRepository).findSerieIdByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia de filmes favoritos")
    void deveRetornarListaVaziaDeFilmesFavoritos() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<Long> idsFilmesFavoritos = of();

        when(favoritaFilmeRepository.findFilmesIdByAuthUserId(usuarioAutenticado.getId()))
                .thenReturn(idsFilmesFavoritos);

        List<Long> resultado = service.buscarIdsFilmesFavoritos(usuarioAutenticado);

        assertEquals(idsFilmesFavoritos, resultado);
        verify(favoritaFilmeRepository).findFilmesIdByAuthUserId(usuarioAutenticado.getId());
    }
}
