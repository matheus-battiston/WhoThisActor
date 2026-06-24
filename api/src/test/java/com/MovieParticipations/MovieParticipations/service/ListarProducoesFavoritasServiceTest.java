package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarProducoesFavoritasService")
class ListarProducoesFavoritasServiceTest {

    private static final int PRIMEIRA_PRODUCAO = 0;
    private static final int QUANTIDADE_UMA_PRODUCAO = 1;

    @Mock
    private FavoritaSerieRepository favoritaSerieRepository;

    @Mock
    private FavoritaFilmeRepository favoritaFilmeRepository;

    @InjectMocks
    private ListarProducoesFavoritasService service;

    @Test
    @DisplayName("Deve listar séries e filmes favoritos do usuário autenticado")
    void deveListarSeriesEFilmesFavoritosDoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Serie serie = getBreakingBadSerieEntityComId();
        Filme filme = getMatrixFilmeEntityComId();

        when(favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of(serie));
        when(favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of(filme));

        ProducoesFavoritasResponse resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(QUANTIDADE_UMA_PRODUCAO, resultado.getSeries().size());
        assertEquals(serie.getId(), resultado.getSeries().get(PRIMEIRA_PRODUCAO).getId());
        assertEquals(serie.getTitulo(), resultado.getSeries().get(PRIMEIRA_PRODUCAO).getNome());
        assertEquals(QUANTIDADE_UMA_PRODUCAO, resultado.getFilmes().size());
        assertEquals(filme.getId(), resultado.getFilmes().get(PRIMEIRA_PRODUCAO).getId());
        assertEquals(filme.getTitulo(), resultado.getFilmes().get(PRIMEIRA_PRODUCAO).getNome());
        verify(favoritaSerieRepository).findSeriesByAuthUserId(usuarioAutenticado.getId());
        verify(favoritaFilmeRepository).findFilmesByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve retornar listas vazias quando usuário não tiver produções favoritas")
    void deveRetornarListasVaziasQuandoUsuarioNaoTiverProducoesFavoritas() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of());
        when(favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of());

        ProducoesFavoritasResponse resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(of(), resultado.getSeries());
        assertEquals(of(), resultado.getFilmes());
        verify(favoritaSerieRepository).findSeriesByAuthUserId(usuarioAutenticado.getId());
        verify(favoritaFilmeRepository).findFilmesByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve listar apenas filmes quando usuário não tiver séries favoritas")
    void deveListarApenasFilmesQuandoUsuarioNaoTiverSeriesFavoritas() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Filme filme = getMatrixFilmeEntityComId();

        when(favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of());
        when(favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of(filme));

        ProducoesFavoritasResponse resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(of(), resultado.getSeries());
        assertEquals(QUANTIDADE_UMA_PRODUCAO, resultado.getFilmes().size());
        assertEquals(filme.getId(), resultado.getFilmes().get(PRIMEIRA_PRODUCAO).getId());
    }

    @Test
    @DisplayName("Deve listar apenas séries quando usuário não tiver filmes favoritos")
    void deveListarApenasSeriesQuandoUsuarioNaoTiverFilmesFavoritos() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Serie serie = getBreakingBadSerieEntityComId();

        when(favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of(serie));
        when(favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId())).thenReturn(of());

        ProducoesFavoritasResponse resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(QUANTIDADE_UMA_PRODUCAO, resultado.getSeries().size());
        assertEquals(serie.getId(), resultado.getSeries().get(PRIMEIRA_PRODUCAO).getId());
        assertEquals(of(), resultado.getFilmes());
    }
}
