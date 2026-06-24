package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalvarAtoresClassificadosService")
class SalvarAtoresClassificadosServiceTest {

    private static final Long ID_TMDB_KEANU = 6384L;
    private static final Long ID_TMDB_BRYAN = 17419L;
    private static final Long ID_KEANU = 1L;
    private static final Long ID_BRYAN = 2L;

    @Mock
    private AtorRepository atorRepository;

    @Captor
    private ArgumentCaptor<List<Ator>> atoresCaptor;

    @InjectMocks
    private SalvarAtoresClassificadosService service;

    @Test
    @DisplayName("Deve preencher IDs de atores já existentes sem criar novos")
    void devePreencherIdsDeAtoresExistentesSemCriarNovos() {
        OpcoesAtoresParecidosResponse ator = ator(ID_TMDB_KEANU, "Keanu Reeves");
        Ator existente = atorEntity(ID_KEANU, ID_TMDB_KEANU, "Keanu Reeves");

        when(atorRepository.findByIdTmdbIn(List.of(ID_TMDB_KEANU))).thenReturn(List.of(existente));

        service.salvarNovosEPreencherIds(List.of(ator));

        assertThat(ator.getId()).isEqualTo(ID_KEANU);
        verify(atorRepository).findByIdTmdbIn(List.of(ID_TMDB_KEANU));
        verify(atorRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("Deve salvar somente atores ausentes e preencher IDs de todos")
    void deveSalvarSomenteAtoresAusentesEPreencherIdsDeTodos() {
        OpcoesAtoresParecidosResponse keanu = ator(ID_TMDB_KEANU, "Keanu Reeves");
        OpcoesAtoresParecidosResponse bryan = ator(ID_TMDB_BRYAN, "Bryan Cranston");
        Ator keanuExistente = atorEntity(ID_KEANU, ID_TMDB_KEANU, "Keanu Reeves");
        Ator bryanSalvo = atorEntity(ID_BRYAN, ID_TMDB_BRYAN, "Bryan Cranston");

        when(atorRepository.findByIdTmdbIn(List.of(ID_TMDB_KEANU, ID_TMDB_BRYAN)))
                .thenReturn(List.of(keanuExistente));
        when(atorRepository.saveAll(anyList())).thenReturn(List.of(bryanSalvo));

        service.salvarNovosEPreencherIds(List.of(keanu, bryan));

        verify(atorRepository).saveAll(atoresCaptor.capture());
        assertThat(atoresCaptor.getValue()).extracting(Ator::getIdTmdb).containsExactly(ID_TMDB_BRYAN);
        assertThat(keanu.getId()).isEqualTo(ID_KEANU);
        assertThat(bryan.getId()).isEqualTo(ID_BRYAN);
    }

    @Test
    @DisplayName("Não deve consultar repositório quando não houver IDs TMDB")
    void naoDeveConsultarRepositorioQuandoNaoHouverIdsTmdb() {
        service.salvarNovosEPreencherIds(List.of(ator(null, "Sem identificador")));

        verifyNoInteractions(atorRepository);
    }

    @Test
    @DisplayName("Não deve preencher ID quando ator não for retornado após salvar")
    void naoDevePreencherIdQuandoAtorNaoForRetornadoAposSalvar() {
        OpcoesAtoresParecidosResponse ator = ator(ID_TMDB_KEANU, "Keanu Reeves");

        when(atorRepository.findByIdTmdbIn(List.of(ID_TMDB_KEANU))).thenReturn(List.of());
        when(atorRepository.saveAll(anyList())).thenReturn(List.of());

        service.salvarNovosEPreencherIds(List.of(ator));

        assertThat(ator.getId()).isNull();
        verify(atorRepository).saveAll(atoresCaptor.capture());
        assertThat(atoresCaptor.getValue()).extracting(Ator::getIdTmdb).containsExactly(ID_TMDB_KEANU);
    }

    private OpcoesAtoresParecidosResponse ator(Long idTmdb, String nome) {
        return OpcoesAtoresParecidosResponse.builder()
                .idTmdb(idTmdb)
                .identidade(nome)
                .imagem("/" + nome + ".jpg")
                .popularidade(10.0)
                .build();
    }

    private Ator atorEntity(Long id, Long idTmdb, String nome) {
        return Ator.builder().id(id).idTmdb(idTmdb).nome(nome).build();
    }
}
