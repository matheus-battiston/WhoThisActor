package com.MovieParticipations.MovieParticipations.service.internal;

import com.MovieParticipations.MovieParticipations.domain.Ator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ContextoElenco(
        Map<Long, Ator> atorPorIdTmdb,
        Set<Long> atoresJaRelacionados
) {

    public static ContextoElenco criar(List<Ator> atores, List<Long> idsAtoresJaRelacionados) {
        Map<Long, Ator> atorPorIdTmdb = atores
                .stream()
                .collect(Collectors.toMap(Ator::getIdTmdb, Function.identity()));

        return new ContextoElenco(
                atorPorIdTmdb,
                new HashSet<>(idsAtoresJaRelacionados)
        );
    }

    public boolean atorJaRelacionado(Long idTmdb) {
        return atoresJaRelacionados.contains(idTmdb);
    }

    public Ator buscarAtor(Long idTmdb) {
        return atorPorIdTmdb.get(idTmdb);
    }
}
