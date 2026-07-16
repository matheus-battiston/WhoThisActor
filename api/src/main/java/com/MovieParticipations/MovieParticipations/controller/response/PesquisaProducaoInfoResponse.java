package com.MovieParticipations.MovieParticipations.controller.response;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PesquisaProducaoInfoResponse {
    String nome;
    String imagem;
    LocalDate dataLancamento;
    String overview;
    String genero;
    Integer anoPrimeiraTemporada;
    Integer anoUltimaTemporada;
    TipoMidia tipoMidia;
    Long id;
    Double popularidade;
}
