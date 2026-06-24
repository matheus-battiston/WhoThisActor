package com.MovieParticipations.MovieParticipations.security.controller;

import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioHelloResponse;
import com.MovieParticipations.MovieParticipations.security.service.HelloService;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/hello")
public class HelloAppController {

    private final HelloService helloService;

    @PostMapping
    public UsuarioHelloResponse Hello(@AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado){
        return helloService.hello(usuarioAutenticado);
    }
}
