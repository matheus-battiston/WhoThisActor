package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.ClassificacaoResponseDTOFactory.getKeanuReeves;
import static com.MovieParticipations.MovieParticipations.mapper.OpcoesAtoresMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OpcoesAtoresMapperTest {

    @Test
    @DisplayName("Deve transformar classificacao em response")
    void transformarEmResponse() {
        ClassificacaoResponseDTO classificacao = getKeanuReeves();

        OpcoesAtoresParecidosResponse response = toResponse(classificacao);

        assertEquals(classificacao.getIdentidade(), response.getIdentidade());
        assertEquals(classificacao.getDistanciaMedia(), response.getDistanciaMedia());
    }
}
