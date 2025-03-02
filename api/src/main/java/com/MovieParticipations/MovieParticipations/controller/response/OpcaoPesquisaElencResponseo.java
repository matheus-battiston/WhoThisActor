package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpcaoPesquisaElencResponseo {
    String nome;
    String urlImagem;
    String nomePersonagem;
}
