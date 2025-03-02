package com.MovieParticipations.MovieParticipations.dto;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class TMDBDto {
    Long id;
    TipoMidia tipoProducao;
    String image;
    String name;
}
