package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ator")
public class Ator {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "ator_seq_gen")
    @SequenceGenerator(name = "ator_seq_gen", sequenceName = "ator_seq")
    private Long id;

    private String nome;
    private String nomeNormalizado;

    private String imagem;
    private Double popularity;

    @Column(name = "id_tmdb", unique = true)
    private Long idTmdb;
    private LocalDate ultimaAtualizacao;
    private Boolean inicializado;
}
