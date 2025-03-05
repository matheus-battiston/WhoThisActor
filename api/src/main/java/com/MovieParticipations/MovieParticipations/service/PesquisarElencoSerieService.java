package com.MovieParticipations.MovieParticipations.service;


import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencResponseo;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
import com.azure.core.annotation.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PesquisarElencoSerieService {

    @Autowired
    SerieRepository serieRepository;
    @Autowired
    BuscarElencoService buscarElencoService;
    @Autowired
    AtorRepository atorRepository;
    @Autowired
    SerieAtorRepository serieAtorRepository;
    @Autowired
    AdicionarSerieService adicionarSerieService;
    @Autowired
    ExisteSerieNoDBValidator existeSerieNoDBValidator;
    @Autowired
    GetSerieIdService getSerieIdService;
    @Autowired
    BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;


    public List<OpcaoPesquisaElencResponseo>  pesquisarElenco(TipoMidia tipo, String nomeSerie, String filtroNome) {

        String nomeOficial = nomeSerie;
        Pageable pageable = PageRequest.of(0, 20);
        if (filtroNome == null || filtroNome.isEmpty()) {
            filtroNome = "";
        }

        if (!existeSerieNoDBValidator.ExisteSerieComNome(nomeSerie, tipo)) {
            nomeOficial = adicionarSerieService.adicionarSerieComNome(nomeSerie, tipo);
        }
        Long serieId = getSerieIdService.porNome(nomeOficial, tipo);
        return serieAtorRepository.findElencoPorSerieIdComPersonagem(serieId, filtroNome, pageable).getContent();
    }
}
