package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpcaoPesquisaElencoResponse {
    String nome;
    String urlImagem;
    String nomePersonagem;
    Double popularity;
}
