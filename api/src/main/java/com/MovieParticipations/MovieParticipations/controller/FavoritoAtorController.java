package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.service.FavoritarAtorService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class FavoritoAtorController {

    private final FavoritarAtorService favoritarAtorService;

    @Operation(summary = "Favoritar um ator", operationId = "favoritarAtor")
    @PostMapping("/atores/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void favoritarAtor(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarAtorService.favoritarAtor(usuarioAutenticado, id);
    }

    @Operation(summary = "Remover favorito de um ator", operationId = "desfavoritarAtor")
    @DeleteMapping("/atores/{id}/favorito")
    @ResponseStatus(NO_CONTENT)
    public void desfavoritarAtor(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        favoritarAtorService.desfavoritarAtor(usuarioAutenticado, id);
    }

    @Operation(summary = "Checar se ator está favoritado", operationId = "atorEstaFavoritado")
    @GetMapping("/atores/{id}/favorito")
    @ResponseStatus(OK)
    public boolean atorEstaFavoritado(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @PathVariable Long id
    ) {
        return favoritarAtorService.estaFavoritado(usuarioAutenticado, id);
    }

    @Operation(summary = "Listar atores favoritos do usuário", operationId = "listarAtoresFavoritos")
    @GetMapping("/favoritos/atores")
    @ResponseStatus(OK)
    public List<InfoAtorResponse> listarAtoresFavoritos(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
    ) {
        return favoritarAtorService.listaDeFavoritos(usuarioAutenticado);
    }
}
