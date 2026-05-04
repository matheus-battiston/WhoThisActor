package com.MovieParticipations.MovieParticipations.controller.response;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PesquisaProducaoInfoResponse {
    String nome;
    String imagem;
    TipoMidia tipoMidia;
    Long id;
    Double popularidade;
}
