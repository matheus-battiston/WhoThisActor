package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class ExisteFilmeNoDBValidator {
    private final static String FILME_NAO_EXISTE = "Filme nao encontrado";
    private final FilmeRepository filmeRepository;

    public void porId(Long idFilme) {
        if (!filmeRepository.existsById(idFilme))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, FILME_NAO_EXISTE);
    }
}
