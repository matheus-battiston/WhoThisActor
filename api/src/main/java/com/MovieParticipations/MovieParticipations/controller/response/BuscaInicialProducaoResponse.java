package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BuscaInicialProducaoResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Long id;
    String nomeProducao;
    String urlImagem;
    String overview;
}
