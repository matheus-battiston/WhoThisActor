package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.FilmeMapper;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AtualizarFilmeInfoService {

    private final BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;
    private final FilmeRepository filmeRepository;

    @Transactional
    public void atualizar(Filme filme) {
        FilmeDetalhesTMDBDto detalhes = buscarProducaoPorNomeTMBDService.buscarDetalhesFilmePorId(filme.getIdTmdb());
        FilmeMapper.atualizarComDetalhes(filme, detalhes);
        filmeRepository.save(filme);
    }
}
