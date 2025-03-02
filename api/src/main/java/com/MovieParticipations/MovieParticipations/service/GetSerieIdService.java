package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSerieIdService {
    @Autowired
    SerieRepository serieRepository;

    public Long porNome(String nome, TipoMidia tipo){
        return serieRepository.findByTituloIgnoreCaseAndTipo(nome, tipo).getId();
    }
}
