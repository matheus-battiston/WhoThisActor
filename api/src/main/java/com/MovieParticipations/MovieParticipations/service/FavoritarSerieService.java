package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.mapper.FavoritaSerieMapper;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
import com.MovieParticipations.MovieParticipations.validator.ProducaoNaoFavoritadoValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class FavoritarSerieService {
    private static final String SERIE_NAO_EXISTE = "A serie com essa ID nao existe";

    private final ProducaoNaoFavoritadoValidator producaoNaoFavoritadoValidator;
    private final SerieRepository serieRepository;
    private final FavoritaSerieRepository favoritaSerieRepository;
    private final ExisteSerieNoDBValidator existeSerieNoDBValidator;
    private final ObterUsuarioService obterUsuarioService;

    @Transactional
    public void favoritarSerie(UsuarioAutenticado usuarioAutenticado, Long idSerie) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        if (producaoNaoFavoritadoValidator.serieEstaFavoritada(usuario.getId(), idSerie))
            return;

        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, SERIE_NAO_EXISTE));
        FavoritaSerie entity = FavoritaSerieMapper.toEntity(serie, usuario);
        favoritaSerieRepository.save(entity);
    }

    public boolean estaFavoritado(UsuarioAutenticado usuarioAutenticado, Long idSerie) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        existeSerieNoDBValidator.porId(idSerie);
        return favoritaSerieRepository.existsByUsuarioIdAndSerieId(usuario.getId(), idSerie);
    }

    @Transactional
    public void desfavoritarSerie(UsuarioAutenticado usuarioAutenticado, Long idProducao) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        favoritaSerieRepository
                .findByUsuarioIdAndSerieId(usuario.getId(), idProducao)
                .ifPresent(favoritaSerieRepository::delete);
    }

    public boolean estaFavoritadoComAuthId(UsuarioAutenticado usuarioAutenticado, Long id) {
        return favoritaSerieRepository.existsByAuthUserIdAndProducaoId(usuarioAutenticado.getId(), id);
    }
}
