package com.MovieParticipations.MovieParticipations.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PesquisarElencoSerieRequest {
    @NotBlank
    String nomeDaSerie;

    String nomeDoPersonagem;
}
