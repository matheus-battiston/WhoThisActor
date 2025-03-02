package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExisteSerieNoDBValidator {
    @Autowired
    SerieRepository serieRepository;

    public boolean ExisteSerieComNome(String nome, TipoMidia tipoProducao){
        return serieRepository.existsSerieByTituloIgnoreCaseAndTipo(nome, tipoProducao);
    }
}
