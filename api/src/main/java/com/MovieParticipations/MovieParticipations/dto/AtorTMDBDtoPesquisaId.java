package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AtorTMDBDtoPesquisaId {
    Long id;
    String name;
    String known_for_department;
    Double popularity;
    String profile_path;
}
