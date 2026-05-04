package com.MovieParticipations.MovieParticipations.domain;

import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "favorita_serie")
public class FavoritaSerie {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne()
    @JoinColumn(name = "serie", nullable = false)
    private Serie serie;
}