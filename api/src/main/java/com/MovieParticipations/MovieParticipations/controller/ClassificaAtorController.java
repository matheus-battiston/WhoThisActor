package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.request.ClassificarImgemRequest;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.service.ClassificarImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classificar")
public class ClassificaAtorController {

    @Autowired
    ClassificarImagemService classificarImagemService;

    @PostMapping()
    public List<OpcoesAtoresParecidosResponse> classificarImagem(@RequestBody ClassificarImgemRequest request){
        return classificarImagemService.classificarImagem(request);
    }

}
