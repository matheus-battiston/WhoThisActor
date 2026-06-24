package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
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
public class SerieAtorVinculoService {

    private final SerieAtorRepository serieAtorRepository;

    private record VinculoSerieAtor(Long serieId, String personagem) {
        static VinculoSerieAtor de(SerieAtor relacao) {
            return new VinculoSerieAtor(
                    relacao.getSerie().getId(),
                    normalizarPersonagem(relacao.getPersonagem())
            );
        }
    }

    public void vincularAtorASerie(
            Ator ator,
            List<Serie> series,
            List<ProducaoTMDBDto> creditos
    ) {
        Map<Long, Serie> seriePorIdTmdb = series.stream()
                .collect(Collectors.toMap(Serie::getIdTmdb, Function.identity()));

        Set<VinculoSerieAtor> vinculosExistentes = serieAtorRepository.findByAtorId(ator.getId())
                .stream()
                .map(VinculoSerieAtor::de)
                .collect(Collectors.toSet());

        List<SerieAtor> novasRelacoes = creditos.stream()
                .map(credito -> criarRelacao(ator, seriePorIdTmdb, credito))
                .filter(Objects::nonNull)
                .filter(relacao -> vinculosExistentes.add(VinculoSerieAtor.de(relacao)))
                .toList();

        serieAtorRepository.saveAll(novasRelacoes);
    }

    private SerieAtor criarRelacao(
            Ator ator,
            Map<Long, Serie> seriePorIdTmdb,
            ProducaoTMDBDto credito
    ) {
        Serie serie = seriePorIdTmdb.get(credito.getId());
        if (serie == null) return null;

        return SerieAtor.builder()
                .ator(ator)
                .serie(serie)
                .personagem(credito.getPersonagem())
                .numeroEpisodios(credito.getNumeroEpisodios())
                .build();
    }

    private static String normalizarPersonagem(String personagem) {
        return personagem == null ? null : personagem.trim().toLowerCase();
    }
}
