package com.MovieParticipations.MovieParticipations.controller.request;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassificarImgemRequest {
    String url;

    @Builder.Default
    boolean fast = false;
}
