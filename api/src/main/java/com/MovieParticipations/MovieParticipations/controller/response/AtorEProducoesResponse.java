package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtorEProducoesResponse {
    String nome;
    String urlFoto;
    List<ProducaoResponse> producoes;
}
