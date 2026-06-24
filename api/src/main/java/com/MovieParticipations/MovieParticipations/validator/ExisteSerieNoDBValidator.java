package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class ExisteSerieNoDBValidator {
    private final static String SERIE_NAO_EXISTE = "Serie nao encontrada";
    private final SerieRepository serieRepository;

    public void porId(Long idSerie) {
        if (!serieRepository.existsSerieById(idSerie))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SERIE_NAO_EXISTE);
    }
}
