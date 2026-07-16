package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.service.BuscaInicialService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/busca")
public class BuscaInicialController {

    private final BuscaInicialService buscaInicialService;

    @Operation(summary = "Buscar informações iniciais da tela de busca", operationId = "buscarInformacoesIniciaisBusca")
    @GetMapping("/inicial")
    public BuscaInicialResponse buscar() {
        return buscaInicialService.buscar();
    }
}
