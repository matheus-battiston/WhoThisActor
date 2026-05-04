package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

record ElencoProcessamento(
        Map<Long, Ator> atorPorIdTmdb,
        Set<Long> atoresJaRelacionados
) {

    static ElencoProcessamento criar(List<Ator> atores, List<Long> idsAtoresJaRelacionados) {
        Map<Long, Ator> atorPorIdTmdb = atores
                .stream()
                .collect(Collectors.toMap(Ator::getIdTmdb, Function.identity()));

        return new ElencoProcessamento(
                atorPorIdTmdb,
                new HashSet<>(idsAtoresJaRelacionados)
        );
    }

    boolean atorJaRelacionado(Long idTmdb) {
        return atoresJaRelacionados.contains(idTmdb);
    }

    Ator buscarAtor(Long idTmdb) {
        return atorPorIdTmdb.get(idTmdb);
    }
}
