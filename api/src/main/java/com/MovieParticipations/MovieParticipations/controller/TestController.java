package com.MovieParticipations.MovieParticipations.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TestController {
    @GetMapping()
    public String teste (){
        return "FUNCIONOU ATUALIZADOBLOB22222";
    }
}
