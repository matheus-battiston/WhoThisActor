package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.PesquisaFilmeCache;
import com.MovieParticipations.MovieParticipations.mapper.PesquisaFilmeMapper;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.PesquisaFilmeCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;

@RequiredArgsConstructor
@Service
public class PesquisarFilmeService {
    private final FilmeRepository filmeRepository;
    private final AdicionarFilmeService adicionarFilmeService;
    private final PesquisaFilmeCacheRepository pesquisaFilmeCacheRepository;

    @Transactional
    public List<PesquisaProducaoInfoResponse> pesquisaPorNome(String nome) {
        String termoNormalizado = normalizar(nome);

        if (!pesquisaFilmeCacheRepository.existsByTermoNormalizado(termoNormalizado)) {
            adicionarFilmeService.adicionarFilmeComNome(nome);
            salvarCache(termoNormalizado);
        }

        return filmeRepository.findByTituloNormalizadoOrderByPopularidadeDesc(termoNormalizado)
                .stream()
                .map(PesquisaFilmeMapper::toResponse)
                .toList();
    }

    private void salvarCache(String termoNormalizado) {
        pesquisaFilmeCacheRepository.save(PesquisaFilmeCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(LocalDate.now())
                .build());
    }
}
