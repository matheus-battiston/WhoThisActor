package com.MovieParticipations.MovieParticipations.security.controller.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioHelloResponse {
    private Long id;
    private String nome;
    private String email;
    private boolean primeiroLogin;
}
