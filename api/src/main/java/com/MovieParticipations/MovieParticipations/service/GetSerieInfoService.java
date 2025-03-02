package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.SerieInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.mapper.SerieMapper;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GetSerieInfoService {
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao foi encontrada";
    @Autowired
    SerieRepository serieRepository;
    @Autowired
    ExisteSerieNoDBValidator existeSerieNoDBValidator;

    public SerieInfoResponse getSerieInfo(String nomeDaSerie, TipoMidia tipo){
        if (!existeSerieNoDBValidator.ExisteSerieComNome(nomeDaSerie, tipo))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERIE_NAO_ENCONTRADA);

        Serie serie = serieRepository.findByTituloIgnoreCaseAndTipo(nomeDaSerie, tipo);

        return SerieMapper.toResponse(serie);
    }
}
