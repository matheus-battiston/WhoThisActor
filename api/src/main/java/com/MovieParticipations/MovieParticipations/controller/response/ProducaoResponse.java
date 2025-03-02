package com.MovieParticipations.MovieParticipations.controller.response;

import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProducaoResponse {
    private int id;
    private String nomeProducao;
    private String nomePersonagem;
    private String posterLink;
    private Float popularidade;
    private TipoProducao tipoProducao;
}
