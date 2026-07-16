package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaAtorRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.AtorNaoFavoritadoValidator;
import com.MovieParticipations.MovieParticipations.validator.ExisteAtorNoDbValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FavoritaAtorFactory.getFavoritaAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioFactory.getUsuarioEntityComId;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoritarAtorService")
class FavoritarAtorServiceTest {

    private static final Long ID_USUARIO = 1L;
    private static final Long ID_ATOR = 99L;
    private static final Long ID_FAVORITO = 1L;
    private static final String ATOR_NAO_EXISTE = "O ator com essa ID nao existe";
    private static final String URL_IMAGEM_KEANU_REEVES = "https://image.tmdb.org/t/p/w400/keanu.jpg";
    private static final int PRIMEIRO_ATOR = 0;
    private static final int QUANTIDADE_UM_ATOR = 1;

    @Mock
    private AtorNaoFavoritadoValidator atorNaoFavoritadoValidator;

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private FavoritaAtorRepository favoritaAtorRepository;

    @Mock
    private ExisteAtorNoDbValidator existeAtorNoDbValidator;

    @Mock
    private ObterUsuarioService obterUsuarioService;

    @Captor
    private ArgumentCaptor<FavoritaAtor> favoritaAtorCaptor;

    @InjectMocks
    private FavoritarAtorService service;

    @Test
    @DisplayName("Deve favoritar ator que ainda não está favoritado")
    void deveFavoritarAtorQueAindaNaoEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Ator ator = getKeanuReevesAtorEntityComId();

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(atorNaoFavoritadoValidator.atorEstaFavoritado(ID_USUARIO, ID_ATOR)).thenReturn(false);
        when(atorRepository.findById(ID_ATOR)).thenReturn(Optional.of(ator));

        service.favoritarAtor(usuarioAutenticado, ID_ATOR);

        verify(favoritaAtorRepository).save(favoritaAtorCaptor.capture());
        FavoritaAtor favoritaAtor = favoritaAtorCaptor.getValue();

        assertEquals(usuario, favoritaAtor.getUsuario());
        assertEquals(ator, favoritaAtor.getAtor());
    }

    @Test
    @DisplayName("Não deve favoritar ator que já está favoritado")
    void naoDeveFavoritarAtorQueJaEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(atorNaoFavoritadoValidator.atorEstaFavoritado(ID_USUARIO, ID_ATOR)).thenReturn(true);

        service.favoritarAtor(usuarioAutenticado, ID_ATOR);

        verifyNoInteractions(atorRepository, favoritaAtorRepository);
    }

    @Test
    @DisplayName("Deve retornar erro ao favoritar ator inexistente")
    void deveRetornarErroAoFavoritarAtorInexistente() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(atorNaoFavoritadoValidator.atorEstaFavoritado(ID_USUARIO, ID_ATOR)).thenReturn(false);
        when(atorRepository.findById(ID_ATOR)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.favoritarAtor(usuarioAutenticado, ID_ATOR)
        );

        assertEquals(BAD_REQUEST, erro.getStatusCode());
        assertEquals(ATOR_NAO_EXISTE, erro.getReason());
        verifyNoInteractions(favoritaAtorRepository);
    }

    @Test
    @DisplayName("Deve informar quando ator está favoritado")
    void deveInformarQuandoAtorEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaAtorRepository.existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR)).thenReturn(true);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_ATOR);

        assertTrue(resultado);
        verify(existeAtorNoDbValidator).porId(ID_ATOR);
        verify(favoritaAtorRepository).existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR);
    }

    @Test
    @DisplayName("Deve informar quando ator não está favoritado")
    void deveInformarQuandoAtorNaoEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaAtorRepository.existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR)).thenReturn(false);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_ATOR);

        assertFalse(resultado);
        verify(existeAtorNoDbValidator).porId(ID_ATOR);
        verify(favoritaAtorRepository).existsByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR);
    }

    @Test
    @DisplayName("Deve listar atores favoritos do usuário autenticado")
    void deveListarAtoresFavoritosDoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Ator ator = getKeanuReevesAtorEntityComId();

        when(favoritaAtorRepository.findAtoresByAuthUserId(usuarioAutenticado.getId())).thenReturn(of(ator));

        List<InfoAtorResponse> resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        InfoAtorResponse atorFavorito = resultado.get(PRIMEIRO_ATOR);

        assertEquals(ator.getNome(), atorFavorito.getNome());
        assertEquals(ID_ATOR, atorFavorito.getId());
        assertEquals(URL_IMAGEM_KEANU_REEVES, atorFavorito.getUrlImagem());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tiver atores favoritos")
    void deveRetornarListaVaziaQuandoUsuarioNaoTiverAtoresFavoritos() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaAtorRepository.findAtoresByAuthUserId(usuarioAutenticado.getId())).thenReturn(of());

        List<InfoAtorResponse> resultado = service.listaDeFavoritos(usuarioAutenticado);

        assertEquals(of(), resultado);
        verify(favoritaAtorRepository).findAtoresByAuthUserId(usuarioAutenticado.getId());
    }

    @Test
    @DisplayName("Deve desfavoritar ator quando vínculo existir")
    void deveDesfavoritarAtorQuandoVinculoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Ator ator = getKeanuReevesAtorEntityComId();
        FavoritaAtor favoritaAtor = getFavoritaAtorEntityComId(usuario, ator, ID_FAVORITO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaAtorRepository.findByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR))
                .thenReturn(Optional.of(favoritaAtor));

        service.desfavoritarAtor(usuarioAutenticado, ID_ATOR);

        verify(favoritaAtorRepository).delete(favoritaAtor);
    }

    @Test
    @DisplayName("Não deve desfavoritar quando vínculo não existir")
    void naoDeveDesfavoritarQuandoVinculoNaoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaAtorRepository.findByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR))
                .thenReturn(Optional.empty());

        service.desfavoritarAtor(usuarioAutenticado, ID_ATOR);

        verify(favoritaAtorRepository).findByUsuarioIdAndAtorId(ID_USUARIO, ID_ATOR);
        verifyNoMoreInteractions(favoritaAtorRepository);
    }
}
