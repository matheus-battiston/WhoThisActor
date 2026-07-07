package com.MovieParticipations.MovieParticipations.controller.response;


import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProducaoInfoResponse {
    String nome;
    String imagem;
    String backdropPath;
    LocalDate dataLancamento;
    Integer anoPrimeiraTemporada;
    Integer anoUltimaTemporada;
    Integer quantidadeTemporadas;
    String genero;
    String overview;
    TipoMidia tipoMidia;
    List<ProviderDto> providers;
    Long id;
}
