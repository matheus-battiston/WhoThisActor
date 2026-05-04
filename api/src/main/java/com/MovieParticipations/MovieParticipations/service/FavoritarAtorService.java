package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.mapper.FavoritaAtorMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaAtorRepository;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.service.ObterUsuarioService;
import com.MovieParticipations.MovieParticipations.validator.AtorNaoFavoritadoValidator;
import com.MovieParticipations.MovieParticipations.validator.ExisteAtorNoDbValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class FavoritarAtorService {
    private static final String ATOR_NAO_EXISTE = "O ator com essa ID nao existe";

    private final AtorNaoFavoritadoValidator atorNaoFavoritadoValidator;
    private final AtorRepository atorRepository;
    private final FavoritaAtorRepository favoritaAtorRepository;
    private final ExisteAtorNoDbValidator existeAtorNoDbValidator;
    private final ObterUsuarioService obterUsuarioService;

    @Transactional
    public void favoritarAtor(UsuarioAutenticado usuarioAutenticado, Long idAtor) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());

        if (atorNaoFavoritadoValidator.atorEstaFavoritado(usuario.getId(), idAtor))
            return;

        Ator ator = atorRepository.findById(idAtor)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, ATOR_NAO_EXISTE));

        FavoritaAtor entity = FavoritaAtorMapper.toEntity(ator, usuario);
        favoritaAtorRepository.save(entity);
    }

    public boolean estaFavoritado(UsuarioAutenticado usuarioAutenticado, Long idAtor) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        existeAtorNoDbValidator.porId(idAtor);
        return favoritaAtorRepository.existsByUsuarioIdAndAtorId(usuario.getId(), idAtor);
    }

    public List<InfoAtorResponse> listaDeFavoritos(UsuarioAutenticado usuarioAutenticado) {
        List<Ator> atoresFavoritos = favoritaAtorRepository.findAtoresByAuthUserId(usuarioAutenticado.getId());
        return atoresFavoritos.stream().map(FavoritaAtorMapper :: toResponse).toList();

    }

    @Transactional
    public void desfavoritarAtor(UsuarioAutenticado usuarioAutenticado, Long idAtor) {
        Usuario usuario = obterUsuarioService.obterComAuthId(usuarioAutenticado.getId());
        favoritaAtorRepository
                .findByUsuarioIdAndAtorId(usuario.getId(), idAtor)
                .ifPresent(favoritaAtorRepository::delete);
    }

}
