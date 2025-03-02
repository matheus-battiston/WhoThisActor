package com.MovieParticipations.MovieParticipations.controller.response;


import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OpcoesAtoresParecidosResponse {
    private String identity;
    private double averageDistance;
    private int id;
    private String imagem;
}
