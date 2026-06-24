package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
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
public class ObterOuCriarFilmesService {

    private final FilmeRepository filmeRepository;
    private final AdicionarFilmeService adicionarFilmeService;

    public List<Filme> obterOuCriar(List<ProducaoTMDBDto> creditos) {
        Map<Long, ProducaoTMDBDto> creditosPorIdTmdb = agruparCreditosPorIdTmdb(creditos);
        if (creditosPorIdTmdb.isEmpty()) return List.of();

        List<Long> idsTmdb = creditosPorIdTmdb.keySet().stream().toList();
        Map<Long, Filme> filmesExistentesPorIdTmdb = filmeRepository.findByIdTmdbIn(idsTmdb).stream()
                .collect(Collectors.toMap(
                        Filme::getIdTmdb,
                        Function.identity(),
                        (filmeExistente, filmeDuplicado) -> filmeExistente
                ));

        List<ProducaoTMDBDto> creditosNovos = creditosPorIdTmdb.values().stream()
                .filter(credito -> !filmesExistentesPorIdTmdb.containsKey(credito.getId()))
                .filter(this::temTitulo)
                .toList();

        List<Filme> filmesCriados = creditosNovos.isEmpty()
                ? List.of()
                : adicionarFilmeService.adicionarFilmes(creditosNovos);

        Map<Long, Filme> filmesPorIdTmdb = new HashMap<>(filmesExistentesPorIdTmdb);
        filmesCriados.forEach(filme -> filmesPorIdTmdb.put(filme.getIdTmdb(), filme));

        return idsTmdb.stream()
                .map(filmesPorIdTmdb::get)
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private Map<Long, ProducaoTMDBDto> agruparCreditosPorIdTmdb(List<ProducaoTMDBDto> creditos) {
        return creditos.stream()
                .filter(credito -> credito.getId() != null)
                .collect(Collectors.toMap(
                        ProducaoTMDBDto::getId,
                        Function.identity(),
                        this::manterCreditoComTitulo,
                        LinkedHashMap::new
                ));
    }

    private ProducaoTMDBDto manterCreditoComTitulo(
            ProducaoTMDBDto creditoExistente,
            ProducaoTMDBDto creditoDuplicado
    ) {
        return temTitulo(creditoExistente) ? creditoExistente : creditoDuplicado;
    }

    private boolean temTitulo(ProducaoTMDBDto credito) {
        return credito.getTitulo() != null && !credito.getTitulo().isBlank();
    }
}
