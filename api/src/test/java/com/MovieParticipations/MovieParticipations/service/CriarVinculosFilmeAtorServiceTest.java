package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
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
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeAtorFactory.getFilmeAtorEntity;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CriarVinculosFilmeAtorService")
class CriarVinculosFilmeAtorServiceTest {

    private static final Long ID_ATOR = 99L;
    private static final Long ID_FILME_DUPLICADO = 2L;
    private static final Long ID_TMDB_FILME_NAO_RESOLVIDO = 456L;
    private static final String PERSONAGEM_NEO = "Neo";
    private static final String PERSONAGEM_NEO_COM_ESPACOS = " neo ";
    private static final String PERSONAGEM_THOMAS_ANDERSON = "Thomas Anderson";
    private static final String PERSONAGEM_VAZIO = "";
    private static final String PERSONAGEM_EM_BRANCO = "   ";
    private static final Long ORDEM_CREDITO = 1L;
    private static final int PRIMEIRO_VINCULO = 0;
    private static final int SEGUNDO_VINCULO = 1;
    private static final int QUANTIDADE_UM_VINCULO = 1;
    private static final int QUANTIDADE_DOIS_VINCULOS = 2;

    @Mock
    private FilmeAtorRepository filmeAtorRepository;

    @Captor
    private ArgumentCaptor<List<FilmeAtor>> vinculosCaptor;

    @InjectMocks
    private CriarVinculosFilmeAtorService service;

    @Test
    @DisplayName("Deve criar vínculo quando crédito corresponder a um filme")
    void deveCriarVinculoQuandoCreditoCorresponderAFilme() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme filme = getMatrixFilmeEntityComId();
        ProducaoTMDBDto credito = getMatrixProducaoTMDBDto();

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of());

        service.criarVinculos(ator, of(filme), of(credito));

        verify(filmeAtorRepository).saveAll(vinculosCaptor.capture());
        assertEquals(QUANTIDADE_UM_VINCULO, vinculosCaptor.getValue().size());
        FilmeAtor vinculo = vinculosCaptor.getValue().get(PRIMEIRO_VINCULO);

        assertEquals(ator, vinculo.getAtor());
        assertEquals(filme, vinculo.getFilme());
        assertEquals(PERSONAGEM_NEO, vinculo.getPersonagem());
        assertEquals(ORDEM_CREDITO, vinculo.getCreditOrder());
    }

    @Test
    @DisplayName("Deve criar vínculo com primeiro filme quando lista tiver IDs TMDB duplicados")
    void deveCriarVinculoComPrimeiroFilmeQuandoListaTiverIdsTmdbDuplicados() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme primeiroFilme = getMatrixFilmeEntityComId();
        Filme filmeDuplicado = getMatrixFilmeEntityComId();
        ProducaoTMDBDto credito = getMatrixProducaoTMDBDto();
        filmeDuplicado.setId(ID_FILME_DUPLICADO);

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of());

        service.criarVinculos(ator, of(primeiroFilme, filmeDuplicado), of(credito));

        verify(filmeAtorRepository).saveAll(vinculosCaptor.capture());
        assertEquals(QUANTIDADE_UM_VINCULO, vinculosCaptor.getValue().size());
        FilmeAtor vinculo = vinculosCaptor.getValue().get(PRIMEIRO_VINCULO);

        assertEquals(primeiroFilme, vinculo.getFilme());
    }

    @Test
    @DisplayName("Não deve criar vínculo já existente com personagem normalizado")
    void naoDeveCriarVinculoJaExistenteComPersonagemNormalizado() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme filme = getMatrixFilmeEntityComId();
        ProducaoTMDBDto credito = getMatrixProducaoTMDBDto();
        FilmeAtor vinculoExistente = getFilmeAtorEntity(filme, ator, PERSONAGEM_NEO_COM_ESPACOS);

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of(vinculoExistente));

        service.criarVinculos(ator, of(filme), of(credito));

        verify(filmeAtorRepository).findByAtorId(ID_ATOR);
        verifyNoMoreInteractions(filmeAtorRepository);
    }

    @Test
    @DisplayName("Deve criar apenas um vínculo para créditos duplicados")
    void deveCriarApenasUmVinculoParaCreditosDuplicados() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme filme = getMatrixFilmeEntityComId();
        ProducaoTMDBDto primeiroCredito = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoDuplicado = getMatrixProducaoTMDBDto();
        creditoDuplicado.setPersonagem(PERSONAGEM_NEO_COM_ESPACOS);

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of());

        service.criarVinculos(ator, of(filme), of(primeiroCredito, creditoDuplicado));

        verify(filmeAtorRepository).saveAll(vinculosCaptor.capture());
        assertEquals(QUANTIDADE_UM_VINCULO, vinculosCaptor.getValue().size());
    }

    @Test
    @DisplayName("Deve criar vínculos distintos para personagens diferentes no mesmo filme")
    void deveCriarVinculosDistintosParaPersonagensDiferentesNoMesmoFilme() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme filme = getMatrixFilmeEntityComId();
        ProducaoTMDBDto creditoNeo = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoThomasAnderson = getMatrixProducaoTMDBDto();
        creditoThomasAnderson.setPersonagem(PERSONAGEM_THOMAS_ANDERSON);

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of());

        service.criarVinculos(ator, of(filme), of(creditoNeo, creditoThomasAnderson));

        verify(filmeAtorRepository).saveAll(vinculosCaptor.capture());
        assertEquals(QUANTIDADE_DOIS_VINCULOS, vinculosCaptor.getValue().size());
        assertEquals(PERSONAGEM_NEO, vinculosCaptor.getValue().get(PRIMEIRO_VINCULO).getPersonagem());
        assertEquals(PERSONAGEM_THOMAS_ANDERSON, vinculosCaptor.getValue().get(SEGUNDO_VINCULO).getPersonagem());
    }

    @Test
    @DisplayName("Não deve criar vínculos quando crédito não corresponder a filme ou personagem válido")
    void naoDeveCriarVinculosQuandoCreditoNaoCorresponderAFilmeOuPersonagemValido() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Filme filme = getMatrixFilmeEntityComId();
        ProducaoTMDBDto creditoSemFilme = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoSemPersonagem = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoComPersonagemVazio = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoComPersonagemEmBranco = getMatrixProducaoTMDBDto();
        creditoSemFilme.setId(ID_TMDB_FILME_NAO_RESOLVIDO);
        creditoSemPersonagem.setPersonagem(null);
        creditoComPersonagemVazio.setPersonagem(PERSONAGEM_VAZIO);
        creditoComPersonagemEmBranco.setPersonagem(PERSONAGEM_EM_BRANCO);

        when(filmeAtorRepository.findByAtorId(ID_ATOR)).thenReturn(of());

        service.criarVinculos(
                ator,
                of(filme),
                of(creditoSemFilme, creditoSemPersonagem, creditoComPersonagemVazio, creditoComPersonagemEmBranco)
        );

        verify(filmeAtorRepository).findByAtorId(ID_ATOR);
        verifyNoMoreInteractions(filmeAtorRepository);
    }
}
