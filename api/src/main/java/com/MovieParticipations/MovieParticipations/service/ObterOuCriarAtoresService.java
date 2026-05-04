package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.AtorMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@RequiredArgsConstructor
@Service
public class ObterOuCriarAtoresService {
    private final AtorRepository atorRepository;

    <T extends AtorTMDBDto> List<Ator> obter(List<T> atoresDto) {
        List<Long> idsTmdb = atoresDto.stream()
                .map(AtorTMDBDto::getId)
                .toList();

        List<Ator> atoresExistentes = atorRepository.findByIdTmdbIn(idsTmdb);

        Map<Long, Ator> existentesPorIdTmdb = atoresExistentes
                .stream()
                .collect(Collectors.toMap(Ator::getIdTmdb, identity()));

        List<Ator> novosAtores = atoresDto.stream()
                .filter(dto -> !existentesPorIdTmdb.containsKey(dto.getId()))
                .map(AtorMapper::toEntity)
                .toList();

        if (novosAtores.isEmpty()) return atoresExistentes;

        try {
            List<Ator> salvos = atorRepository.saveAll(novosAtores);

            List<Ator> resultado = new ArrayList<>(atoresExistentes);
            resultado.addAll(salvos);
            return resultado;

        } catch (DataIntegrityViolationException e) {
            return atorRepository.findByIdTmdbIn(idsTmdb);
        }
    }
}
