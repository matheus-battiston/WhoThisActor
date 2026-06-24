package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaAtorResponse;
import com.MovieParticipations.MovieParticipations.service.PesquisarAtorPorNomeService;
import com.MovieParticipations.MovieParticipations.service.PesquisarAtorService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/atores")
public class PesquisaAtorController {

    private final PesquisarAtorService pesquisarAtorService;
    private final PesquisarAtorPorNomeService pesquisarAtorPorNomeService;

    @Operation(summary = "Buscar opçoes de ator por nome", operationId = "pesquisarAtorPorNome")
    @GetMapping
    public PesquisaAtorResponse pesquisarPorNome(@RequestParam String nome) {
        return pesquisarAtorPorNomeService.pesquisarInfosPorNome(nome);
    }

    @Operation(summary = "Buscar ator pelo ID", operationId = "pesquisarAtorPorId")
    @GetMapping("/{id}")
    public AtorEProducoesResponse pesquisarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {
        return pesquisarAtorService.pesquisarPorId(id, usuarioAutenticado);
    }
}
