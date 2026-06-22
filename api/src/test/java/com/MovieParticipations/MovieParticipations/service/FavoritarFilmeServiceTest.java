package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.ExisteFilmeNoDBValidator;
import com.MovieParticipations.MovieParticipations.validator.ProducaoNaoFavoritadoValidator;
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

import java.util.Optional;

import static com.MovieParticipations.MovieParticipations.factories.domain.FavoritaFilmeFactory.getFavoritaFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioFactory.getUsuarioEntityComId;
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
@DisplayName("FavoritarFilmeService")
class FavoritarFilmeServiceTest {

    private static final Long ID_USUARIO = 1L;
    private static final Long ID_FILME = 1L;
    private static final Long ID_FAVORITO = 1L;
    private static final String FILME_NAO_EXISTE = "O filme com essa ID nao existe";

    @Mock
    private ProducaoNaoFavoritadoValidator producaoNaoFavoritadoValidator;

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private FavoritaFilmeRepository favoritaFilmeRepository;

    @Mock
    private ExisteFilmeNoDBValidator existeFilmeNoDBValidator;

    @Mock
    private ObterUsuarioService obterUsuarioService;

    @Captor
    private ArgumentCaptor<FavoritaFilme> favoritaFilmeCaptor;

    @InjectMocks
    private FavoritarFilmeService service;

    @Test
    @DisplayName("Deve favoritar filme que ainda não está favoritado")
    void deveFavoritarFilmeQueAindaNaoEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Filme filme = getMatrixFilmeEntityComId();

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.filmeEstaFavoritado(ID_USUARIO, ID_FILME)).thenReturn(false);
        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.of(filme));

        service.favoritarFilme(usuarioAutenticado, ID_FILME);

        verify(favoritaFilmeRepository).save(favoritaFilmeCaptor.capture());
        FavoritaFilme favoritaFilme = favoritaFilmeCaptor.getValue();

        assertEquals(usuario, favoritaFilme.getUsuario());
        assertEquals(filme, favoritaFilme.getFilme());
    }

    @Test
    @DisplayName("Não deve favoritar filme que já está favoritado")
    void naoDeveFavoritarFilmeQueJaEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.filmeEstaFavoritado(ID_USUARIO, ID_FILME)).thenReturn(true);

        service.favoritarFilme(usuarioAutenticado, ID_FILME);

        verifyNoInteractions(filmeRepository, favoritaFilmeRepository);
    }

    @Test
    @DisplayName("Deve retornar erro ao favoritar filme inexistente")
    void deveRetornarErroAoFavoritarFilmeInexistente() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.filmeEstaFavoritado(ID_USUARIO, ID_FILME)).thenReturn(false);
        when(filmeRepository.findById(ID_FILME)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.favoritarFilme(usuarioAutenticado, ID_FILME)
        );

        assertEquals(BAD_REQUEST, erro.getStatusCode());
        assertEquals(FILME_NAO_EXISTE, erro.getReason());
        verifyNoInteractions(favoritaFilmeRepository);
    }

    @Test
    @DisplayName("Deve informar quando filme está favoritado")
    void deveInformarQuandoFilmeEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaFilmeRepository.existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME)).thenReturn(true);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_FILME);

        assertTrue(resultado);
        verify(existeFilmeNoDBValidator).porId(ID_FILME);
        verify(favoritaFilmeRepository).existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME);
    }

    @Test
    @DisplayName("Deve informar quando filme não está favoritado")
    void deveInformarQuandoFilmeNaoEstaFavoritado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaFilmeRepository.existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME)).thenReturn(false);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_FILME);

        assertFalse(resultado);
        verify(existeFilmeNoDBValidator).porId(ID_FILME);
        verify(favoritaFilmeRepository).existsByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME);
    }

    @Test
    @DisplayName("Deve desfavoritar filme quando vínculo existir")
    void deveDesfavoritarFilmeQuandoVinculoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Filme filme = getMatrixFilmeEntityComId();
        FavoritaFilme favoritaFilme = getFavoritaFilmeEntityComId(usuario, filme, ID_FAVORITO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaFilmeRepository.findByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME))
                .thenReturn(Optional.of(favoritaFilme));

        service.desfavoritarFilme(usuarioAutenticado, ID_FILME);

        verify(favoritaFilmeRepository).delete(favoritaFilme);
    }

    @Test
    @DisplayName("Não deve desfavoritar filme quando vínculo não existir")
    void naoDeveDesfavoritarFilmeQuandoVinculoNaoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaFilmeRepository.findByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME))
                .thenReturn(Optional.empty());

        service.desfavoritarFilme(usuarioAutenticado, ID_FILME);

        verify(favoritaFilmeRepository).findByUsuarioIdAndFilmeId(ID_USUARIO, ID_FILME);
        verifyNoMoreInteractions(favoritaFilmeRepository);
    }

    @Test
    @DisplayName("Deve informar quando filme está favoritado pelo authUserId")
    void deveInformarQuandoFilmeEstaFavoritadoPeloAuthUserId() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaFilmeRepository.existsByAuthUserIdAndFilmeId(usuarioAutenticado.getId(), ID_FILME))
                .thenReturn(true);

        boolean resultado = service.estaFavoritadoComAuthId(usuarioAutenticado, ID_FILME);

        assertTrue(resultado);
        verify(favoritaFilmeRepository).existsByAuthUserIdAndFilmeId(usuarioAutenticado.getId(), ID_FILME);
    }

    @Test
    @DisplayName("Deve informar quando filme não está favoritado pelo authUserId")
    void deveInformarQuandoFilmeNaoEstaFavoritadoPeloAuthUserId() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaFilmeRepository.existsByAuthUserIdAndFilmeId(usuarioAutenticado.getId(), ID_FILME))
                .thenReturn(false);

        boolean resultado = service.estaFavoritadoComAuthId(usuarioAutenticado, ID_FILME);

        assertFalse(resultado);
        verify(favoritaFilmeRepository).existsByAuthUserIdAndFilmeId(usuarioAutenticado.getId(), ID_FILME);
    }
}
