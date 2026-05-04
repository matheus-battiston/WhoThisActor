package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProducoesFavoritasResponse {
    List<ProducaoResponse> series;
    List<ProducaoResponse> filmes;

}
