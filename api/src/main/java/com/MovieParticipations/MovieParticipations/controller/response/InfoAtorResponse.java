package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InfoAtorResponse {
    String nome;
    String urlImagem;
    Long id;
    Double popularity;
}
