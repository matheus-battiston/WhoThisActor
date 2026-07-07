package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.*;

@Service
public class DeveAtualizarFilmeService {
    public boolean deveAtualizar(Filme filme) {
        return filme != null && !TRUE.equals(filme.getInfoAtualizado());
    }
}
