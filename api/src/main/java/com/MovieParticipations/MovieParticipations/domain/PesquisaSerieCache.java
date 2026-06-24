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
@Table(name = "pesquisa_serie_cache")
public class PesquisaSerieCache {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "pesquisa_serie_cache_seq_gen")
    @SequenceGenerator(name = "pesquisa_serie_cache_seq_gen", sequenceName = "pesquisa_serie_cache_seq")
    private Long id;

    @Column(name = "termo_normalizado", nullable = false, unique = true)
    private String termoNormalizado;

    @Column(name = "ultima_sincronizacao", nullable = false)
    private LocalDate ultimaSincronizacao;
}
