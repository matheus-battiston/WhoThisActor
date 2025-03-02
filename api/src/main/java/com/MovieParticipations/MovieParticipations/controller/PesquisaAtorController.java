package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;
import com.MovieParticipations.MovieParticipations.service.PesquisarAtorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pesquisa")
public class PesquisaAtorController {
    @Autowired
    private PesquisarAtorService pesquisarAtorService;

    @GetMapping("/getId")
    public PesquisaPorNomeResponse pesquisaIdPorNome(@RequestParam String nome){
        return pesquisarAtorService.pesquisarIdPorNome(nome);
    }

    @CrossOrigin
    @GetMapping("/nome")
    public AtorEProducoesResponse pesquisaProducoes(@RequestParam String nome) {
        return pesquisarAtorService.pesquisarProducoesPorNome(nome);
    }

}