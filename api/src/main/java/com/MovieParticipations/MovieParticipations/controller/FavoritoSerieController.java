package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.service.FavoritarSerieService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class FavoritoSerieController {

    private final FavoritarSerieService favoritarSerieService;

    @Operation(summary = "Favoritar uma série", operationId = "favoritarSerie")
    @PostMapping("/series/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void favoritarSerie(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarSerieService.favoritarSerie(usuarioAutenticado, id);
    }

    @Operation(summary = "Remover favorito de uma série", operationId = "desfavoritarSerie")
    @DeleteMapping("/series/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void desfavoritarSerie(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarSerieService.desfavoritarSerie(usuarioAutenticado, id);
    }

    @Operation(summary = "Checar se série está favoritada", operationId = "serieEstaFavoritada")
    @GetMapping("/series/{id}/favorito")
    @ResponseStatus(OK)
    public boolean serieEstaFavoritada(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        return favoritarSerieService.estaFavoritado(usuarioAutenticado, id);
    }
}
