package com.MovieParticipations.MovieParticipations.security.controller;


import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioResponse;
import com.MovieParticipations.MovieParticipations.security.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioAutenticadoService service;

    @PostMapping
    public UsuarioResponse login() {
        return service.getResponse();
    }
}
