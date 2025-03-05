package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PesquisaIdPorNomeDTO {
    List<AtorTMDBDtoPesquisaId> results;
}
