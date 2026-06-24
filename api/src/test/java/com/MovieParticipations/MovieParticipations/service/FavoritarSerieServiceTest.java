package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
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

import static com.MovieParticipations.MovieParticipations.factories.domain.FavoritaSerieFactory.getFavoritaSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
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
@DisplayName("FavoritarSerieService")
class FavoritarSerieServiceTest {

    private static final Long ID_USUARIO = 1L;
    private static final Long ID_SERIE = 2L;
    private static final Long ID_FAVORITO = 1L;
    private static final String SERIE_NAO_EXISTE = "A serie com essa ID nao existe";

    @Mock
    private ProducaoNaoFavoritadoValidator producaoNaoFavoritadoValidator;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private FavoritaSerieRepository favoritaSerieRepository;

    @Mock
    private ExisteSerieNoDBValidator existeSerieNoDBValidator;

    @Mock
    private ObterUsuarioService obterUsuarioService;

    @Captor
    private ArgumentCaptor<FavoritaSerie> favoritaSerieCaptor;

    @InjectMocks
    private FavoritarSerieService service;

    @Test
    @DisplayName("Deve favoritar série que ainda não está favoritada")
    void deveFavoritarSerieQueAindaNaoEstaFavoritada() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Serie serie = getBreakingBadSerieEntityComId();

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.serieEstaFavoritada(ID_USUARIO, ID_SERIE)).thenReturn(false);
        when(serieRepository.findById(ID_SERIE)).thenReturn(Optional.of(serie));

        service.favoritarSerie(usuarioAutenticado, ID_SERIE);

        verify(favoritaSerieRepository).save(favoritaSerieCaptor.capture());
        FavoritaSerie favoritaSerie = favoritaSerieCaptor.getValue();

        assertEquals(usuario, favoritaSerie.getUsuario());
        assertEquals(serie, favoritaSerie.getSerie());
    }

    @Test
    @DisplayName("Não deve favoritar série que já está favoritada")
    void naoDeveFavoritarSerieQueJaEstaFavoritada() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.serieEstaFavoritada(ID_USUARIO, ID_SERIE)).thenReturn(true);

        service.favoritarSerie(usuarioAutenticado, ID_SERIE);

        verifyNoInteractions(serieRepository, favoritaSerieRepository);
    }

    @Test
    @DisplayName("Deve retornar erro ao favoritar série inexistente")
    void deveRetornarErroAoFavoritarSerieInexistente() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(producaoNaoFavoritadoValidator.serieEstaFavoritada(ID_USUARIO, ID_SERIE)).thenReturn(false);
        when(serieRepository.findById(ID_SERIE)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.favoritarSerie(usuarioAutenticado, ID_SERIE)
        );

        assertEquals(BAD_REQUEST, erro.getStatusCode());
        assertEquals(SERIE_NAO_EXISTE, erro.getReason());
        verifyNoInteractions(favoritaSerieRepository);
    }

    @Test
    @DisplayName("Deve informar quando série está favoritada")
    void deveInformarQuandoSerieEstaFavoritada() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaSerieRepository.existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE)).thenReturn(true);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_SERIE);

        assertTrue(resultado);
        verify(existeSerieNoDBValidator).porId(ID_SERIE);
        verify(favoritaSerieRepository).existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE);
    }

    @Test
    @DisplayName("Deve informar quando série não está favoritada")
    void deveInformarQuandoSerieNaoEstaFavoritada() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaSerieRepository.existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE)).thenReturn(false);

        boolean resultado = service.estaFavoritado(usuarioAutenticado, ID_SERIE);

        assertFalse(resultado);
        verify(existeSerieNoDBValidator).porId(ID_SERIE);
        verify(favoritaSerieRepository).existsByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE);
    }

    @Test
    @DisplayName("Deve desfavoritar série quando vínculo existir")
    void deveDesfavoritarSerieQuandoVinculoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);
        Serie serie = getBreakingBadSerieEntityComId();
        FavoritaSerie favoritaSerie = getFavoritaSerieEntityComId(usuario, serie, ID_FAVORITO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaSerieRepository.findByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE))
                .thenReturn(Optional.of(favoritaSerie));

        service.desfavoritarSerie(usuarioAutenticado, ID_SERIE);

        verify(favoritaSerieRepository).delete(favoritaSerie);
    }

    @Test
    @DisplayName("Não deve desfavoritar série quando vínculo não existir")
    void naoDeveDesfavoritarSerieQuandoVinculoNaoExistir() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        when(obterUsuarioService.obterComAuthId(usuarioAutenticado.getId())).thenReturn(usuario);
        when(favoritaSerieRepository.findByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE))
                .thenReturn(Optional.empty());

        service.desfavoritarSerie(usuarioAutenticado, ID_SERIE);

        verify(favoritaSerieRepository).findByUsuarioIdAndSerieId(ID_USUARIO, ID_SERIE);
        verifyNoMoreInteractions(favoritaSerieRepository);
    }

    @Test
    @DisplayName("Deve informar quando série está favoritada pelo authUserId")
    void deveInformarQuandoSerieEstaFavoritadaPeloAuthUserId() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaSerieRepository.existsByAuthUserIdAndProducaoId(usuarioAutenticado.getId(), ID_SERIE))
                .thenReturn(true);

        boolean resultado = service.estaFavoritadoComAuthId(usuarioAutenticado, ID_SERIE);

        assertTrue(resultado);
        verify(favoritaSerieRepository).existsByAuthUserIdAndProducaoId(usuarioAutenticado.getId(), ID_SERIE);
    }

    @Test
    @DisplayName("Deve informar quando série não está favoritada pelo authUserId")
    void deveInformarQuandoSerieNaoEstaFavoritadaPeloAuthUserId() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();

        when(favoritaSerieRepository.existsByAuthUserIdAndProducaoId(usuarioAutenticado.getId(), ID_SERIE))
                .thenReturn(false);

        boolean resultado = service.estaFavoritadoComAuthId(usuarioAutenticado, ID_SERIE);

        assertFalse(resultado);
        verify(favoritaSerieRepository).existsByAuthUserIdAndProducaoId(usuarioAutenticado.getId(), ID_SERIE);
    }
}
