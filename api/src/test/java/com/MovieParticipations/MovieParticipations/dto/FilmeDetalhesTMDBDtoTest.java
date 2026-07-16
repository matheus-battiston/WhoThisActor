package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FilmeDetalhesTMDBDto")
class FilmeDetalhesTMDBDtoTest {
    private static final int ANO_LANCAMENTO_MATRIX = 2011;
    private static final int MES_LANCAMENTO_MATRIX = 2;
    private static final int DIA_LANCAMENTO_MATRIX = 10;
    private static final LocalDate DATA_LANCAMENTO_MATRIX = LocalDate.of(
            ANO_LANCAMENTO_MATRIX,
            MES_LANCAMENTO_MATRIX,
            DIA_LANCAMENTO_MATRIX
    );
    private static final String JSON_FILME_DETALHES_MATRIX = """
            {
              "id": 123,
              "title": "Matrix",
              "release_date": "2011-02-10"
            }
            """;

    @Test
    @DisplayName("Deve desserializar release_date como data de lancamento")
    void deveDesserializarReleaseDateComoDataLancamento() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        FilmeDetalhesTMDBDto dto = objectMapper.readValue(JSON_FILME_DETALHES_MATRIX, FilmeDetalhesTMDBDto.class);

        assertThat(dto.getDataLancamento()).isEqualTo(DATA_LANCAMENTO_MATRIX);
    }
}
