package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Component
public class ExisteAtorNoDbValidator {
    private static final String NAO_EXISTE_ATOR_COM_ID = "Não existe um ator com esse ID";

    private final AtorRepository atorRepository;

    public void porId(Long idAtor){
        if (!atorRepository.existsAtorById(idAtor)) throw new ResponseStatusException(BAD_REQUEST, NAO_EXISTE_ATOR_COM_ID);
    }

}
