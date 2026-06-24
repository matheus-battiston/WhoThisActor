package com.MovieParticipations.MovieParticipations.controller.response;


import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalhesProducaoComElenco {
    List<OpcaoPesquisaElencoResponse> elenco;
    String nome;
    String imagem;
    TipoMidia tipoMidia;
    List<ProviderDto> providers;
    Long id;
    Boolean estaFavoritado;
}
