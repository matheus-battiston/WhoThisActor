package com.MovieParticipations.MovieParticipations.controller.request;


import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassificarImgemRequest {
    String url;
    String nomeSerie;
    TipoMidia tipoMidia;
}
