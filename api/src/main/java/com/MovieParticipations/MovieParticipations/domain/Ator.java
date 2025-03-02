package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ator")
public class Ator {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String nome;

    @ManyToMany(mappedBy = "atores")
    private List<Serie> series = new ArrayList<>();
    private String imagem;

    // Getters, Setters, Construtores
}