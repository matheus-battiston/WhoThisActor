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
@Table(name = "filme_ator")
public class FilmeAtor {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "filme_ator_seq_gen")
    @SequenceGenerator(name = "filme_ator_seq_gen", sequenceName = "filme_ator_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "filme", nullable = false)
    private Filme filme;

    @ManyToOne()
    @JoinColumn(name = "ator", nullable = false)
    private Ator ator;

    @Column(nullable = false)
    private String personagem;

    private Long creditOrder;

}
