package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdicionarSeriesDeAtorService {

    private final SerieResolverService serieResolverService;
    private final SerieAtorVinculoService serieAtorVinculoService;

    @Transactional
    public void adicionar(Ator ator, List<ProducaoTMDBDto> producaoTMDBDtoStream) {

        List<Serie> producoes = serieResolverService.resolverSerie(producaoTMDBDtoStream);
        serieAtorVinculoService.vincularAtorASerie(ator, producoes, producaoTMDBDtoStream);
    }
}