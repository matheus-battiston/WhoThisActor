package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProducaoAtorResponse {
    List<ProducaoComPersonagemResponse> filmes;
    List<ProducaoComPersonagemResponse> series;
}
