package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.service.ListarProducoesFavoritasService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class FavoritosController {

    private final ListarProducoesFavoritasService listarProducoesFavoritasService;

    @Operation(summary = "Listar produções favoritas do usuário", operationId = "listarProducoesFavoritas")
    @GetMapping("/favoritos/producoes")
    @ResponseStatus(OK)
    public ProducoesFavoritasResponse listarProducoesFavoritas(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
    ) {
        return listarProducoesFavoritasService.listaDeFavoritos(usuarioAutenticado);
    }
}
