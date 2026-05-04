package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.MovieParticipations.MovieParticipations.mapper.AtorMapper.toEntity;

@RequiredArgsConstructor
@Service
public class AdicionarAtorService {
    private final AtorRepository atorRepository;
    private final AdicionarProducoesAtorService adicionarProducoesAtorService;

    @Transactional
    public Ator adicionarAtor(AtorTMDBDtoPesquisaId response) {
        Ator ator = toEntity(response);
        atorRepository.save(ator);
        adicionarProducoesAtorService.adicionar(ator);
        return ator;
    }

    @Transactional
    public void processarAtor(Ator ator) {
        adicionarProducoesAtorService.adicionar(ator);
        ator.setInicializado(true);
        atorRepository.save(ator);
    }

}
