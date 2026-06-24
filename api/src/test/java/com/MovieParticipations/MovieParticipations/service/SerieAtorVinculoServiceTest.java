package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieAtorFactory.getSerieAtorEntity;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SerieAtorVinculoService")
class SerieAtorVinculoServiceTest {

    @Mock
    private SerieAtorRepository serieAtorRepository;

    @Captor
    private ArgumentCaptor<List<SerieAtor>> vinculosCaptor;

    @InjectMocks
    private SerieAtorVinculoService service;

    @Test
    @DisplayName("Deve criar vínculo para crédito correspondente à série")
    void deveCriarVinculoParaCreditoCorrespondenteASerie() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Serie serie = getBreakingBadSerieEntityComId();
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();

        when(serieAtorRepository.findByAtorId(ator.getId())).thenReturn(List.of());

        service.vincularAtorASerie(ator, List.of(serie), List.of(credito));

        verify(serieAtorRepository).saveAll(vinculosCaptor.capture());
        SerieAtor vinculo = vinculosCaptor.getValue().get(0);
        assertThat(vinculo.getAtor()).isSameAs(ator);
        assertThat(vinculo.getSerie()).isSameAs(serie);
        assertThat(vinculo.getPersonagem()).isEqualTo("Walter White");
        assertThat(vinculo.getNumeroEpisodios()).isEqualTo(62);
    }

    @Test
    @DisplayName("Não deve duplicar vínculo existente com personagem normalizado")
    void naoDeveDuplicarVinculoExistenteComPersonagemNormalizado() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Serie serie = getBreakingBadSerieEntityComId();
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();
        SerieAtor existente = getSerieAtorEntity(serie, ator, "  walter white  ");

        when(serieAtorRepository.findByAtorId(ator.getId())).thenReturn(List.of(existente));

        service.vincularAtorASerie(ator, List.of(serie), List.of(credito));

        verify(serieAtorRepository).findByAtorId(ator.getId());
        verify(serieAtorRepository).saveAll(List.of());
        verifyNoMoreInteractions(serieAtorRepository);
    }

    @Test
    @DisplayName("Deve salvar somente um vínculo para créditos duplicados")
    void deveSalvarSomenteUmVinculoParaCreditosDuplicados() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Serie serie = getBreakingBadSerieEntityComId();
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto creditoDuplicado = getBreakingBadProducaoTMDBDto();
        creditoDuplicado.setPersonagem(" walter white ");

        when(serieAtorRepository.findByAtorId(ator.getId())).thenReturn(List.of());

        service.vincularAtorASerie(ator, List.of(serie), List.of(credito, creditoDuplicado));

        verify(serieAtorRepository).saveAll(vinculosCaptor.capture());
        assertThat(vinculosCaptor.getValue()).hasSize(1);
    }

    @Test
    @DisplayName("Deve ignorar crédito sem série correspondente")
    void deveIgnorarCreditoSemSerieCorrespondente() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();

        when(serieAtorRepository.findByAtorId(ator.getId())).thenReturn(List.of());

        service.vincularAtorASerie(ator, List.of(), List.of(credito));

        verify(serieAtorRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("Não deve duplicar vínculo existente sem personagem")
    void naoDeveDuplicarVinculoExistenteSemPersonagem() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Serie serie = getBreakingBadSerieEntityComId();
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();
        credito.setPersonagem(null);
        SerieAtor existente = getSerieAtorEntity(serie, ator, null);

        when(serieAtorRepository.findByAtorId(ator.getId())).thenReturn(List.of(existente));

        service.vincularAtorASerie(ator, List.of(serie), List.of(credito));

        verify(serieAtorRepository).saveAll(List.of());
    }
}
