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

    private final ObterOuCriarFilmesService obterOuCriarFilmesService;
    private final CriarVinculosFilmeAtorService criarVinculosFilmeAtorService;

    @Transactional
    public void adicionar(Ator ator, List<ProducaoTMDBDto> producaoTMDBDtoStream) {

        List<Filme> producoes = obterOuCriarFilmesService.obterOuCriar(producaoTMDBDtoStream);
        criarVinculosFilmeAtorService.criarVinculos(ator, producoes, producaoTMDBDtoStream);
    }
}
