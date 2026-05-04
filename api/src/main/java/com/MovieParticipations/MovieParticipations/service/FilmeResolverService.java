package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmeResolverService {
    private final FilmeRepository filmeRepository;
    private final AdicionarFilmeService adicionarFilmeService;

    public List<Filme>resolverFilme(List<ProducaoTMDBDto> creditos) {
        Map<Long, ProducaoTMDBDto> creditosUnicos = agruparPorIdTmdb(creditos);
        List<Long> idsTmdb = creditosUnicos.keySet().stream().toList();
        List<Filme> existentes = filmeRepository.findByIdTmdbIn(idsTmdb);
        List<Long> idsExistentes = existentes.stream()
                .map(Filme::getIdTmdb)
                .toList();

        List<ProducaoTMDBDto> novas = creditosUnicos.entrySet().stream()
                .filter(entry -> !idsExistentes.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(producao -> producao.getTitulo() != null && !producao.getTitulo().isEmpty())
                .toList();

        List<Filme> criadas = adicionarFilmeService.adicionarFilmes(novas);

        List<Filme> resultado = new ArrayList<>(existentes);
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
