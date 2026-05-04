package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreditosAtorService {

    private final RequisicaoApiService requisicaoApiService;

    public List<ProducaoTMDBDto> buscarCreditosValidos(Ator ator) {

        ListaProducoesTMDBDto response = requisicaoApiService.pesquisarFilmesDoAtorPorId(ator.getIdTmdb());

        return response.getElenco()
                .stream()
                .filter(this::temTitulo)
                .toList();
    }

    private boolean temTitulo(ProducaoTMDBDto dto) {
        return dto.getTitulo() != null || dto.getNome() != null;
    }
}