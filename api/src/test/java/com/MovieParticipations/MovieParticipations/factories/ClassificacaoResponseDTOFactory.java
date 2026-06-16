package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;

public class ClassificacaoResponseDTOFactory {

    public static ClassificacaoResponseDTO getKeanuReeves() {
        return new ClassificacaoResponseDTO("Keanu Reeves", 0.15);
    }
}
