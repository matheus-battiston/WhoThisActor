package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencResponseo;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.service.PesquisarElencoSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/elenco")
public class PesquisarElencoSerieController {
    @Autowired
    PesquisarElencoSerieService pesquisarElencoSerieService;

    @GetMapping()
    public List<OpcaoPesquisaElencResponseo> pesquisarElenco(@RequestParam TipoMidia tipoMidia, @RequestParam String nomeDaSerie, @RequestParam String nomeDoPersonagem){
        return pesquisarElencoSerieService.pesquisarElenco(tipoMidia, nomeDaSerie, nomeDoPersonagem);
    }
}
