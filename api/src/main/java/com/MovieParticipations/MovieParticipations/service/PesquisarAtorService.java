package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class PesquisarAtorService {

    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";

    private final AtorRepository atorRepository;
    private final PesquisarProducoesAtorService pesquisarProducoesAtorService;
    private final ChecarAtorFavoritadoService checarAtorFavoritadoService;
    private final AdicionarAtorService adicionarAtorService;

    public AtorEProducoesResponse pesquisarPorId(Long idAtor, UsuarioAutenticado usuarioAutenticado) {
        Ator ator = atorRepository.findAtorById(idAtor)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, ATOR_NAO_ENCONTRADO));

        if (!ator.getInicializado()) adicionarAtorService.processarAtor(ator);

        AtorEProducoesResponse response = pesquisarProducoesAtorService.getAtorInfo(ator);

        if (usuarioAutenticado == null) return response;
        response.setFavoritado(checarAtorFavoritadoService
                .estaFavoritadoPorAuthId(ator.getId(), usuarioAutenticado.getId()));

        return response;
    }

}

