package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.service.PesquisarElencoFilmeService;
import com.MovieParticipations.MovieParticipations.service.PesquisarFilmeService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/filmes")
public class FilmeInfoController {

    private final PesquisarElencoFilmeService pesquisarElencoFilmeService;
    private final PesquisarFilmeService pesquisarFilmeService;

    @Operation(summary = "Pesquisar elenco de um filme por id e opcionalmente filtrando por personagem", operationId = "pesquisarDetalhesFilmeComElenco")
    @GetMapping("/{id}/detalhes")
    public DetalhesProducaoComElenco pesquisarDetalhesFilmeComElenco(
            @PathVariable Long id,
            @RequestParam(required = false) String personagem,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
    ) {
        return  pesquisarElencoFilmeService.pesquisar(id, personagem, usuarioAutenticado);
    }

    @Operation(summary = "Pesquisa filmes por nome retornando todos que tem nome exato", operationId = "pesquisarFilmePorNome")
    @GetMapping("/pesquisa")
    public List<PesquisaProducaoInfoResponse> pesquisarSeriePorNome(@RequestParam String nome) {
        return pesquisarFilmeService.pesquisaPorNome(nome);
    }
}
