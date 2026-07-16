package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.*;

@Service
public class DeveAtualizarSerieService {
    public boolean deveAtualizar(Serie serie) {
        return serie != null && !TRUE.equals(serie.getInfoAtualizado());
    }
}
