package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PesquisaPorNomeResponse {
    String nome;
    String urlImagem;
    int id;
}
