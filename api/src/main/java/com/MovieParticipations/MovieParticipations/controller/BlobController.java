package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.SasResponse;
import com.MovieParticipations.MovieParticipations.service.BlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gerarSas")
public class BlobController {

    @Autowired
    BlobService blobService;

    @GetMapping
    public SasResponse gerarSasBlob(){
        return blobService.gerarSas();
    }

}
