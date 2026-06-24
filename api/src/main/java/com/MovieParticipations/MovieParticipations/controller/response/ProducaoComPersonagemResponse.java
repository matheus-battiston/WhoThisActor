package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProducaoComPersonagemResponse {
    private Long id;
    private String nomeProducao;
    private String nomePersonagem;
    private String posterLink;
}
