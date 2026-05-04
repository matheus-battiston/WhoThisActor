package com.MovieParticipations.MovieParticipations.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoImagemResponseDTO {

    private List<ClassificacaoResponseDTO> resultado;
}
