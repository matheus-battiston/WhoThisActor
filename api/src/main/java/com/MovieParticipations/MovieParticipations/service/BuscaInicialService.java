package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialPessoaResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.mapper.BuscaInicialMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.mapper.BuscaInicialMapper.toResponse;

@RequiredArgsConstructor
@Service
public class BuscaInicialService {


    private final AtorRepository atorRepository;
    private final SerieRepository serieRepository;
    private final FilmeRepository filmeRepository;
    private final AtualizarFilmeInfoService atualizarFilmeInfoService;
    private final AtualizarSerieInfoService atualizarSerieInfoService;

    @Cacheable(cacheNames = "buscaInicial", key = "'dados'")
    @Transactional
    public BuscaInicialResponse buscar() {

        List<BuscaInicialProducaoResponse> series =
                serieRepository.buscarDoisAleatoriosDoTop100()
                        .stream()
                        .map(this::prepararSerie)
                        .toList();

        List<BuscaInicialProducaoResponse> filmes =
                filmeRepository.buscarDoisAleatoriosDoTop100()
                        .stream()
                        .map(this::prepararFilme)
                        .toList();

        List<BuscaInicialPessoaResponse> pessoas =
                atorRepository.buscarDoisAtoresAleatoriosDoTop100()
                        .stream()
                        .map(BuscaInicialMapper::toPessoaResponse)
                        .toList();

        return toResponse(pessoas, series, filmes);

    }

    private BuscaInicialProducaoResponse prepararSerie(Serie serie) {
        if (!Boolean.TRUE.equals(serie.getInfoAtualizado())) atualizarSerieInfoService.atualizar(serie);
        return BuscaInicialMapper.toProducaoResponse(serie);
    }

    private BuscaInicialProducaoResponse prepararFilme(Filme filme) {
        if (!Boolean.TRUE.equals(filme.getInfoAtualizado())) atualizarFilmeInfoService.atualizar(filme);
        return BuscaInicialMapper.toProducaoResponse(filme);
    }
}
