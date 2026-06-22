package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ObterOuCriarSeriesService {

    private final SerieRepository serieRepository;
    private final AdicionarSerieService adicionarSerieService;

    public List<Serie> obterOuCriar(List<ProducaoTMDBDto> creditos) {
        Map<Long, ProducaoTMDBDto> creditosPorIdTmdb = agruparCreditosPorIdTmdb(creditos);
        if (creditosPorIdTmdb.isEmpty()) return List.of();

        List<Long> idsTmdb = creditosPorIdTmdb.keySet().stream().toList();
        Map<Long, Serie> seriesExistentesPorIdTmdb = serieRepository.findByIdTmdbIn(idsTmdb).stream()
                .collect(Collectors.toMap(
                        Serie::getIdTmdb,
                        Function.identity(),
                        (serieExistente, serieDuplicada) -> serieExistente
                ));

        List<ProducaoTMDBDto> creditosNovos = creditosPorIdTmdb.values().stream()
                .filter(credito -> !seriesExistentesPorIdTmdb.containsKey(credito.getId()))
                .filter(this::temNome)
                .toList();

        List<Serie> seriesCriadas = creditosNovos.isEmpty()
                ? List.of()
                : adicionarSerieService.adicionarSeries(creditosNovos);

        Map<Long, Serie> seriesPorIdTmdb = new HashMap<>(seriesExistentesPorIdTmdb);
        seriesCriadas.forEach(serie -> seriesPorIdTmdb.put(serie.getIdTmdb(), serie));

        return idsTmdb.stream()
                .map(seriesPorIdTmdb::get)
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private Map<Long, ProducaoTMDBDto> agruparCreditosPorIdTmdb(List<ProducaoTMDBDto> creditos) {
        return creditos.stream()
                .filter(credito -> credito.getId() != null)
                .collect(Collectors.toMap(
                        ProducaoTMDBDto::getId,
                        Function.identity(),
                        this::manterCreditoComNome,
                        LinkedHashMap::new
                ));
    }

    private ProducaoTMDBDto manterCreditoComNome(
            ProducaoTMDBDto creditoExistente,
            ProducaoTMDBDto creditoDuplicado
    ) {
        return temNome(creditoExistente) ? creditoExistente : creditoDuplicado;
    }

    private boolean temNome(ProducaoTMDBDto credito) {
        return credito.getNome() != null && !credito.getNome().isBlank();
    }
}
