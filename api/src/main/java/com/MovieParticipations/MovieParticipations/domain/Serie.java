package com.MovieParticipations.MovieParticipations.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "serie")
@NoArgsConstructor
public class Serie {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "serie_seq_gen")
    @SequenceGenerator(name = "serie_seq_gen", sequenceName = "serie_seq")
    private Long id;
    private String imagem;
    private String titulo;
    private String tituloNormalizado;
    private Long idTmdb;
    private LocalDate ultimaAtualizacao;
    private Double popularidade;
    private Boolean elencoInicializado;
    private Boolean infoAtualizado;
    private String backdropPath;
    private Integer anoPrimeiraTemporada;
    private Integer anoUltimaTemporada;
    private Integer quantidadeTemporadas;
    private String genero;
    @Column(length = 4000)
    private String overview;
}
