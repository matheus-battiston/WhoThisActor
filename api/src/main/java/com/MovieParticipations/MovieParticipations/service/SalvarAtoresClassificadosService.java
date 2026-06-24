package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.mapper.AtorMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SalvarAtoresClassificadosService {

    private final AtorRepository atorRepository;

    @Transactional
    public void salvarNovosEPreencherIds(List<OpcoesAtoresParecidosResponse> atoresNovos) {
        List<Long> idsTmdb = atoresNovos.stream()
                .map(OpcoesAtoresParecidosResponse::getIdTmdb)
                .filter(Objects::nonNull)
                .toList();

        if (idsTmdb.isEmpty()) return;

        Map<Long, Ator> atoresPorTmdb = atorRepository.findByIdTmdbIn(idsTmdb).stream()
                .collect(Collectors.toMap(Ator::getIdTmdb, Function.identity()));

        List<Ator> novos = atoresNovos.stream()
                .filter(ator -> !atoresPorTmdb.containsKey(ator.getIdTmdb()))
                .map(AtorMapper::toEntityFromOpcoesParecidos)
                .toList();

        atorRepository.saveAll(novos)
                .forEach(ator -> atoresPorTmdb.put(ator.getIdTmdb(), ator));

        atoresNovos.forEach(atorResponse -> preencherId(atorResponse, atoresPorTmdb));
    }

    private void preencherId(OpcoesAtoresParecidosResponse atorResponse, Map<Long, Ator> atoresPorTmdb) {
        Ator ator = atoresPorTmdb.get(atorResponse.getIdTmdb());
        if (ator != null) atorResponse.setId(ator.getId());
    }
}
