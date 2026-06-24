package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ObterSerieInicializadaService {

    private static final String SERIE_NAO_ENCONTRADA = "Serie nao encontrada";

    private final SerieRepository serieRepository;
    private final AdicionarSerieService adicionarSerieService;

    public Serie obter(Long idSerie) {
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, SERIE_NAO_ENCONTRADA));

        if (!serie.getInicializado()) adicionarSerieService.adicionarElenco(serie);
        return serie;
    }
}
