package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExisteAtorNoDBPorNome {
    @Autowired
    AtorRepository atorRepository;

    public boolean existeAtorComNome(String nome){
        return atorRepository.existsAtorByNomeIgnoreCase(nome);
    }
}
