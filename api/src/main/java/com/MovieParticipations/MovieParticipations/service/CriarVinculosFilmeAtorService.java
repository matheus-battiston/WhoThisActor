package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CriarVinculosFilmeAtorService {

    private final FilmeAtorRepository filmeAtorRepository;

    private record VinculoFilmeAtor(Long filmeId, String personagem) {
        static VinculoFilmeAtor de(FilmeAtor relacao) {
            return new VinculoFilmeAtor(
                    relacao.getFilme().getId(),
                    normalizarPersonagem(relacao.getPersonagem())
            );
        }
    }

    public void criarVinculos(Ator ator, List<Filme> filmes, List<ProducaoTMDBDto> creditos) {
        Map<Long, Filme> filmePorTmdb = filmes.stream()
                .collect(Collectors.toMap(
                        Filme::getIdTmdb,
                        Function.identity(),
                        (filmeExistente, filmeDuplicado) -> filmeExistente
                ));

        Set<VinculoFilmeAtor> vinculosExistentes = filmeAtorRepository.findByAtorId(ator.getId())
                .stream()
                .map(VinculoFilmeAtor::de)
                .collect(Collectors.toSet());

        List<FilmeAtor> novosVinculos = new ArrayList<>();

        for (ProducaoTMDBDto credito : creditos) {
            Filme filme = filmePorTmdb.get(credito.getId());
            String personagem = credito.getPersonagem();

            if (filme == null || personagem == null || personagem.isBlank()) continue;

            FilmeAtor vinculo = FilmeAtor.builder()
                    .ator(ator)
                    .filme(filme)
                    .personagem(personagem)
                    .creditOrder(credito.getOrdem())
                    .build();

            if (vinculosExistentes.add(VinculoFilmeAtor.de(vinculo))) {
                novosVinculos.add(vinculo);
            }
        }

        if (!novosVinculos.isEmpty()) filmeAtorRepository.saveAll(novosVinculos);
    }

    private static String normalizarPersonagem(String personagem) {
        return personagem.trim().toLowerCase(Locale.ROOT);
    }
}
