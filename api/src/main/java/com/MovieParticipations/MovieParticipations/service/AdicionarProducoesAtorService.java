package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdicionarProducoesAtorService {

    private static final String TV = "tv";
    private static final String FILME = "movie";

    private final AdicionarFilmesDeAtorService adicionarFilmesDeAtorService;
    private final AdicionarSeriesDeAtorService adicionarSeriesDeAtorService;
    private final CreditosAtorService creditosAtorService;

    public void adicionar(Ator ator) {
        List<ProducaoTMDBDto> creditos = creditosAtorService.buscarCreditosValidos(ator);

        Map<String, List<ProducaoTMDBDto>> creditosPorTipo = creditos.stream()
                .collect(Collectors.groupingBy(ProducaoTMDBDto::getTipoMidia));

        List<ProducaoTMDBDto> series = creditosPorTipo.getOrDefault(TV, List.of());
        List<ProducaoTMDBDto> filmes = creditosPorTipo.getOrDefault(FILME, List.of());

        adicionarSeriesDeAtorService.adicionar(ator, series);
        adicionarFilmesDeAtorService.adicionar(ator, filmes);
    }
}
