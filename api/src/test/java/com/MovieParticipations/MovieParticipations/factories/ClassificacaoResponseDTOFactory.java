package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;

public class ClassificacaoResponseDTOFactory {
    private static final String IDENTIDADE_KEANU_REEVES = "Keanu Reeves";
    private static final Double DISTANCIA_MEDIA_KEANU_REEVES = 0.15;

    public static ClassificacaoResponseDTO getKeanuReevesClassificacaoResponseDTO() {
        return new ClassificacaoResponseDTO(IDENTIDADE_KEANU_REEVES, DISTANCIA_MEDIA_KEANU_REEVES);
    }
}
