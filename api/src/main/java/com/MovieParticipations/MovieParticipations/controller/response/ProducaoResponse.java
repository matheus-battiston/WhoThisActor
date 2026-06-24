package com.MovieParticipations.MovieParticipations.controller.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProducaoResponse {
    String nome;
    String imagem;
    Long id;
}
