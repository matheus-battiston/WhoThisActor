package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "serie")
public class Serie {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    private String imagem;
    private String titulo;
    private Long idTmdb;
    private LocalDate ultimaAtualizacao;

    @Enumerated(value = STRING)
    private TipoMidia tipo; // Adicionado o campo tipo

    @ManyToMany()
    @JoinTable(
            name = "serie_ator",
            joinColumns = @JoinColumn(name = "serie"),
            inverseJoinColumns = @JoinColumn(name = "ator")
    )
    private List<Ator> atores = new ArrayList<>();

    public Serie() {
    }
}