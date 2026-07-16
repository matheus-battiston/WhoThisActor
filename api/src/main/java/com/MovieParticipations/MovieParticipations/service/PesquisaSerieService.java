package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.PesquisaSerieCache;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.mapper.PesquisaSerieMapper;
import com.MovieParticipations.MovieParticipations.repository.PesquisaSerieCacheRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;

@RequiredArgsConstructor
@Service
public class PesquisaSerieService {
    private final SerieRepository serieRepository;
    private final AdicionarSerieService adicionarSerieService;
    private final PesquisaSerieCacheRepository pesquisaSerieCacheRepository;
    private final DeveAtualizarSerieService deveAtualizarSerieService;
    private final AtualizarSerieInfoService atualizarSerieInfoService;

    @Transactional
    public List<PesquisaProducaoInfoResponse> pesquisaPorNome(String nome) {
        String termoNormalizado = normalizar(nome);

        if (!pesquisaSerieCacheRepository.existsByTermoNormalizado(termoNormalizado)) {
            adicionarSerieService.adicionarSerieComNome(nome);
            salvarCache(termoNormalizado);
        }

        List<Serie> series = serieRepository.findByTituloNormalizadoOrderByPopularidadeDesc(termoNormalizado);
        inicializarInfos(series);

        return series.stream()
                .map(PesquisaSerieMapper::toResponse)
                .toList();
    }

    private void inicializarInfos(List<Serie> series) {
        series.stream()
                .filter(deveAtualizarSerieService::deveAtualizar)
                .forEach(atualizarSerieInfoService::atualizar);
    }

    private void salvarCache(String termoNormalizado) {
        pesquisaSerieCacheRepository.save(PesquisaSerieCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(LocalDate.now())
                .build());
    }
}
