package com.MovieParticipations.MovieParticipations.controller.response;


import com.MovieParticipations.MovieParticipations.domain.Provedores;
import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProvedoresResponse {
    Provedores provedor;
}
