package com.MovieParticipations.MovieParticipations.controller.response;


import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OpcoesAtoresParecidosResponse {
    private String identidade;
    private double distanciaMedia;
    private Long id;
    private String imagem;
    private Long idTmdb;
    Double popularidade;

}
