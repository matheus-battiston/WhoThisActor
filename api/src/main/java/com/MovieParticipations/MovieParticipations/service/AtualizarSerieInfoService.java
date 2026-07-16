package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.SerieMapper;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AtualizarSerieInfoService {

    private final BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;
    private final SerieRepository serieRepository;

    @Transactional
    public void atualizar(Serie serie) {
        SerieDetalhesTMDBDto detalhes = buscarProducaoPorNomeTMBDService.buscarDetalhesSeriePorId(serie.getIdTmdb());
        SerieMapper.atualizarComDetalhes(serie, detalhes);
        serieRepository.save(serie);
    }
}
