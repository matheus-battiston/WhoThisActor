package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.ClassificarImagemResponse;
import com.MovieParticipations.MovieParticipations.service.ClassificarImagemService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classificar")
public class ClassificaAtorController {

    private final ClassificarImagemService classificarImagemService;

    @Operation(summary = "Classificar imagem de ator", operationId = "classificarImagem")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ClassificarImagemResponse classificarImagem(
            @RequestPart("image") MultipartFile image,
            @RequestParam(defaultValue = "false") boolean filtrarPorFavoritos,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado
            ) throws IOException {
        return classificarImagemService.classificarImagem(image, filtrarPorFavoritos, usuarioAutenticado);
    }


}
