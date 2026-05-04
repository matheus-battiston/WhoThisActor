package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.service.FavoritarFilmeService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class FavoritoFilmeController {

    private final FavoritarFilmeService favoritarFilmeService;

    @Operation(summary = "Favoritar um filme", operationId = "favoritarFilme")
    @PostMapping("/filmes/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void favoritarFilme(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarFilmeService.favoritarFilme(usuarioAutenticado, id);
    }

    @Operation(summary = "Remover favorito de um filme", operationId = "desfavoritarFilme")
    @DeleteMapping("/filmes/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void desfavoritarFilme(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarFilmeService.desfavoritarFilme(usuarioAutenticado, id);
    }

    @Operation(summary = "Checar se filme está favoritado", operationId = "filmeEstaFavoritado")
    @GetMapping("/filmes/{id}/favorito")
    @ResponseStatus(OK)
    public boolean filmeEstaFavoritado(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        return favoritarFilmeService.estaFavoritado(usuarioAutenticado, id);
    }
}
