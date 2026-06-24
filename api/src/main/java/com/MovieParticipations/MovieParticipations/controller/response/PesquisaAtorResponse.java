package com.MovieParticipations.MovieParticipations.controller.response;

import com.MovieParticipations.MovieParticipations.dto.ContratoRespostaDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PesquisaAtorResponse {
    ContratoRespostaDTO contratoResposta;
    List<InfoAtorResponse> atores;
}
