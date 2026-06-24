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
@Table(name = "filme")
@NoArgsConstructor
public class Filme {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "filme_seq_gen")
    @SequenceGenerator(name = "filme_seq_gen", sequenceName = "filme_seq")
    private Long id;
    private String imagem;
    private String titulo;
    private String tituloNormalizado;
    private Long idTmdb;
    private LocalDate ultimaAtualizacao;
    private Double popularidade;
    private Boolean inicializado;

}
