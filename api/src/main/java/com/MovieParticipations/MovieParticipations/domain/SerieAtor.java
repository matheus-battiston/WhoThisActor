package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "serie_ator")
public class SerieAtor {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "serie_ator_seq_gen")
    @SequenceGenerator(name = "serie_ator_seq_gen", sequenceName = "serie_ator_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "serie", nullable = false)
    private Serie serie;

    @ManyToOne()
    @JoinColumn(name = "ator", nullable = false)
    private Ator ator;

    @Column(nullable = false)
    private String personagem;

    private int numeroEpisodios;

}
