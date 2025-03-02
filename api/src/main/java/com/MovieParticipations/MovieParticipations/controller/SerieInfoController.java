package com.MovieParticipations.MovieParticipations.controller;


import com.MovieParticipations.MovieParticipations.controller.response.SerieInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.service.GetSerieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/serie")
public class SerieInfoController {
    @Autowired
    GetSerieInfoService getSerieInfoService;

    @GetMapping()
    public SerieInfoResponse getSerieInfo(@RequestParam String nome, @RequestParam TipoMidia tipoMidia){
        return getSerieInfoService.getSerieInfo(nome, tipoMidia);
    }

}
