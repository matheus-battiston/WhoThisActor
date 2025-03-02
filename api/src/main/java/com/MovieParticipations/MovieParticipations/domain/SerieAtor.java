package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;


import static jakarta.persistence.GenerationType.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "serie_ator")
public class SerieAtor {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "serie", nullable = false)
    private Serie serie;

    @ManyToOne() // Permite salvar Serie automaticamente
    @JoinColumn(name = "ator", nullable = false)
    private Ator ator;

    @Column(nullable = false)
    private String personagem;

}