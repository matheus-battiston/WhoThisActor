package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AtorTMDBSerieDto {
    String name;
    String profile_path;
    String known_for_department;
    List<RolesDto> roles;
}
