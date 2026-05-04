package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmeAtorVinculoService {

    private final FilmeAtorRepository filmeAtorRepository;

    private record VinculoFilmeAtor(Long filmeId, String personagem) {
        static VinculoFilmeAtor de(FilmeAtor relacao) {
            return new VinculoFilmeAtor(
                    relacao.getFilme().getId(),
                    normalizarPersonagem(relacao.getPersonagem())
            );
        }
    }

    public void vincularAtorAFilme(Ator ator, List<Filme> filmes, List<ProducaoTMDBDto> creditos) {
        Map<Long, Filme> filmePorTmdb = filmes.stream()
                .collect(Collectors.toMap(Filme::getIdTmdb, Function.identity()));

        Set<VinculoFilmeAtor> vinculosExistentes = filmeAtorRepository.findByAtorId(ator.getId())
                .stream()
                .map(VinculoFilmeAtor::de)
                .collect(Collectors.toSet());

        List<FilmeAtor> novasRelacoes = creditos.stream()
                .map(credito -> criarRelacao(ator, filmePorTmdb, credito))
                .filter(Objects::nonNull)
                .filter(relacao -> vinculosExistentes.add(VinculoFilmeAtor.de(relacao)))
                .toList();

        filmeAtorRepository.saveAll(novasRelacoes);
    }

    private FilmeAtor criarRelacao(
            Ator ator,
            Map<Long, Filme> seriePorIdTmdb,
            ProducaoTMDBDto credito
    ) {
        Filme filme = seriePorIdTmdb.get(credito.getId());
        if (filme == null) return null;

        return FilmeAtor.builder()
                .ator(ator)
                .filme(filme)
                .personagem(credito.getPersonagem())
                .creditOrder(credito.getOrdem())
                .build();
    }

    private static String normalizarPersonagem(String personagem) {
        return personagem == null ? null : personagem.trim().toLowerCase();
    }
}
