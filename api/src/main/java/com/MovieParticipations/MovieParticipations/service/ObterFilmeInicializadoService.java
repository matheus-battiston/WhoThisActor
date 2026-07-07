package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ObterFilmeInicializadoService {
    private static final String FILME_NAO_ENCONTRADO = "Filem com esse id nao foi encontrada";

    private final FilmeRepository filmeRepository;
    private final AdicionarFilmeService adicionarFilmeService;
    private final DeveAtualizarFilmeService deveAtualizarFilmeService;
    private final AtualizarFilmeInfoService atualizarFilmeInfoService;

    public Filme obter(Long idFilme) {
        Filme filme = filmeRepository.findById(idFilme).
                orElseThrow(() -> new ResponseStatusException(NOT_FOUND, FILME_NAO_ENCONTRADO));

        if (deveAtualizarFilmeService.deveAtualizar(filme)) atualizarFilmeInfoService.atualizar(filme);
        if (!Boolean.TRUE.equals(filme.getElencoInicializado())) adicionarFilmeService.adicionarElenco(filme);

        return filme;
    }
}
