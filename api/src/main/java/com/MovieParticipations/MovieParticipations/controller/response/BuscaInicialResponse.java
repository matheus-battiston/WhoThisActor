package com.MovieParticipations.MovieParticipations.controller.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BuscaInicialResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    List<BuscaInicialPessoaResponse> pessoas;
    List<BuscaInicialProducaoResponse> series;
    List<BuscaInicialProducaoResponse> filmes;
}
