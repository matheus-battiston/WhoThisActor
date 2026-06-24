package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.mapper.FavoritaFilmeMapper;
import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.ExisteFilmeNoDBValidator;
import com.MovieParticipations.MovieParticipations.validator.ProducaoNaoFavoritadoValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class FavoritarFilmeService {
    private static final String FILME_NAO_EXISTE = "O filme com essa ID nao existe";

    private final ProducaoNaoFavoritadoValidator producaoNaoFavoritadoValidator;
    private final FilmeRepository filmeRepository;
    private final FavoritaFilmeRepository favoritaFilmeRepository;
    private final ExisteFilmeNoDBValidator existeFilmeNoDBValidator;
    private final ObterUsuarioService obterUsuarioService;

    @Transactional
    public void favoritarFilme(UsuarioAutenticado usuarioAutenticado, Long idFilme) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        if (producaoNaoFavoritadoValidator.filmeEstaFavoritado(usuario.getId(), idFilme))
            return;

        Filme filme = filmeRepository.findById(idFilme)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, FILME_NAO_EXISTE));
        FavoritaFilme entity = FavoritaFilmeMapper.toEntity(filme, usuario);
        favoritaFilmeRepository.save(entity);
    }

    public boolean estaFavoritado(UsuarioAutenticado usuarioAutenticado, Long idFilme) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        existeFilmeNoDBValidator.porId(idFilme);
        return favoritaFilmeRepository.existsByUsuarioIdAndFilmeId(usuario.getId(), idFilme);
    }

    @Transactional
    public void desfavoritarFilme(UsuarioAutenticado usuarioAutenticado, Long idFilme) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        favoritaFilmeRepository
                .findByUsuarioIdAndFilmeId(usuario.getId(), idFilme)
                .ifPresent(favoritaFilmeRepository::delete);
    }

    public boolean estaFavoritadoComAuthId(UsuarioAutenticado usuarioAutenticado, Long id) {
        return favoritaFilmeRepository.existsByAuthUserIdAndFilmeId(usuarioAutenticado.getId(), id);
    }
}
