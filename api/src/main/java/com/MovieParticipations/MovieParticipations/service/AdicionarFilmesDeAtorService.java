package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdicionarFilmesDeAtorService {

    private final FilmeResolverService filmeResolverService;
    private final FilmeAtorVinculoService filmeAtorVinculoService;

    @Transactional
    public void adicionar(Ator ator, List<ProducaoTMDBDto> producaoTMDBDtoStream) {

        List<Filme> producoes = filmeResolverService.resolverFilme(producaoTMDBDtoStream);
        filmeAtorVinculoService.vincularAtorAFilme(ator, producoes, producaoTMDBDtoStream);
    }
}
