package com.MovieParticipations.MovieParticipations.controller.response;


import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProducaoInfoResponse {
    String nome;
    String imagem;
    TipoMidia tipoMidia;
    List<ProviderDto> providers;
    Long id;
}
