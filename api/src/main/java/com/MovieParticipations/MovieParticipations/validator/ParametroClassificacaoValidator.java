package com.MovieParticipations.MovieParticipations.validator;


import com.MovieParticipations.MovieParticipations.controller.request.ClassificarImgemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ParametroClassificacaoValidator {
    private static final String ERRO_TIPO_MIDIA = "Tipo Midia deve ser informado ao filtrar por série";
    public void validar(ClassificarImgemRequest request) {
        if (!(request.getNomeSerie() == null) && (request.getTipoMidia() == null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERRO_TIPO_MIDIA);
    }
}
