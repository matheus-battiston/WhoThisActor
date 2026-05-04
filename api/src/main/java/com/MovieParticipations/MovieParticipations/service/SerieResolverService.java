package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

@RequiredArgsConstructor
@Service
public class SerieResolverService {

    private final SerieRepository serieRepository;
    private final AdicionarSerieService adicionarSerieService;

    public List<Serie> resolverSerie(List<ProducaoTMDBDto> creditos) {
        Map<Long, ProducaoTMDBDto> creditosUnicos = agruparPorIdTmdb(creditos);
        List<Long> idsTmdb = creditosUnicos.keySet().stream().toList();
        List<Serie> existentes = serieRepository.findByIdTmdbIn(idsTmdb);
        List<Long> idsExistentes = existentes.stream()
                .map(Serie::getIdTmdb)
                .toList();

        List<ProducaoTMDBDto> novas = creditosUnicos.entrySet().stream()
                .filter(entry -> !idsExistentes.contains(entry.getKey()))
                .map(Entry::getValue)
                .filter(serie -> serie.getNome() != null && !serie.getNome().isEmpty())
                .toList();

        List<Serie> criadas = adicionarSerieService.adicionarSeries(novas);

        List<Serie> resultado = new ArrayList<>(existentes);
        resultado.addAll(criadas);

        return resultado;
    }

    private Map<Long, ProducaoTMDBDto> agruparPorIdTmdb(List<ProducaoTMDBDto> creditos) {
        return creditos.stream()
                .collect(Collectors.toMap(
                        ProducaoTMDBDto::getId,
                        Function.identity(),
                        (primeiro, repetido) -> primeiro
                ));
    }
}