package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtorEProducoesResponse {
    String nome;
    String urlFoto;
    Long id;
    ProducaoAtorResponse producoes;
    Boolean favoritado;
}
