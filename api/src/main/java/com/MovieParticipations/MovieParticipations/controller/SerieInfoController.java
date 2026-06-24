package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.service.PesquisaSerieService;
import com.MovieParticipations.MovieParticipations.service.PesquisarElencoSerieService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/series")
public class SerieInfoController {

    private final PesquisarElencoSerieService pesquisarElencoSerieService;
    private final PesquisaSerieService pesquisaSerieService;

    @Operation(summary = "Pesquisar elenco de uma produção por nome, tipo de mídia e filtro opcional por personagem", operationId = "pesquisarDetalhesSerieComElenco")
    @GetMapping("/{id}/detalhes")
    public DetalhesProducaoComElenco pesquisarDetalhesSerieComElenco(
            @PathVariable Long id,
            @RequestParam(required = false) String personagem,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
            ) {
        return  pesquisarElencoSerieService.pesquisar(id, personagem, usuarioAutenticado);
    }

    @Operation(summary = "Pesquisa series por nome, retornando todas que tenham nome exato", operationId = "pesquisarSeriePorNome")
    @GetMapping("/pesquisa")
    public List<PesquisaProducaoInfoResponse> pesquisarSeriePorNome(@RequestParam String nome) {
        return pesquisaSerieService.pesquisaPorNome(nome);
    }
}
