package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getBryanCranstonAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBMovieDtoFactory.getKeanuReevesAtorTMDBMovieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getBryanCranstonAtorTMDBSerieDto;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterOuCriarAtoresService")
class ObterOuCriarAtoresServiceTest {

    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final Long ID_TMDB_BRYAN_CRANSTON = 17419L;
    private static final String NOME_BRYAN_CRANSTON = "Bryan Cranston";
    private static final String NOME_NORMALIZADO_BRYAN_CRANSTON = "bryan cranston";
    private static final String IMAGEM_BRYAN_CRANSTON = "/bryan-cranston.jpg";
    private static final Double POPULARIDADE_BRYAN_CRANSTON = 12.0;

    @Mock
    private AtorRepository atorRepository;

    @Captor
    private ArgumentCaptor<List<Ator>> atoresCaptor;

    @InjectMocks
    private ObterOuCriarAtoresService service;

    @Test
    @DisplayName("Deve retornar atores existentes sem salvar novos")
    void deveRetornarAtoresExistentesSemSalvarNovos() {
        AtorTMDBMovieDto keanuDto = getKeanuReevesAtorTMDBMovieDto();
        Ator keanu = getKeanuReevesAtorEntityComId();

        when(atorRepository.findByIdTmdbIn(of(ID_TMDB_KEANU_REEVES))).thenReturn(of(keanu));

        List<Ator> resultado = service.obter(of(keanuDto));

        assertThat(resultado).containsExactly(keanu);
        verify(atorRepository).findByIdTmdbIn(of(ID_TMDB_KEANU_REEVES));
        verifyNoMoreInteractions(atorRepository);
    }

    @Test
    @DisplayName("Deve criar ator ausente a partir dos dados do TMDB")
    void deveCriarAtorAusenteAPartirDosDadosDoTmdb() {
        AtorTMDBSerieDto bryanDto = getBryanCranstonAtorTMDBSerieDto();
        Ator bryanSalvo = getBryanCranstonAtorEntityComId();

        when(atorRepository.findByIdTmdbIn(of(ID_TMDB_BRYAN_CRANSTON))).thenReturn(of());
        when(atorRepository.saveAll(anyList())).thenReturn(of(bryanSalvo));

        List<Ator> resultado = service.obter(of(bryanDto));

        assertThat(resultado).containsExactly(bryanSalvo);
        verify(atorRepository).saveAll(atoresCaptor.capture());
        Ator atorParaSalvar = atoresCaptor.getValue().get(0);
        assertThat(atorParaSalvar.getIdTmdb()).isEqualTo(ID_TMDB_BRYAN_CRANSTON);
        assertThat(atorParaSalvar.getNome()).isEqualTo(NOME_BRYAN_CRANSTON);
        assertThat(atorParaSalvar.getNomeNormalizado()).isEqualTo(NOME_NORMALIZADO_BRYAN_CRANSTON);
        assertThat(atorParaSalvar.getImagem()).isEqualTo(IMAGEM_BRYAN_CRANSTON);
        assertThat(atorParaSalvar.getPopularity()).isEqualTo(POPULARIDADE_BRYAN_CRANSTON);
        assertThat(atorParaSalvar.getInicializado()).isFalse();
        assertThat(atorParaSalvar.getUltimaAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve salvar somente atores ausentes e retornar existentes com os salvos")
    void deveSalvarSomenteAtoresAusentesERetornarExistentesComOsSalvos() {
        AtorTMDBMovieDto keanuDto = getKeanuReevesAtorTMDBMovieDto();
        AtorTMDBSerieDto bryanDto = getBryanCranstonAtorTMDBSerieDto();
        Ator keanu = getKeanuReevesAtorEntityComId();
        Ator bryan = getBryanCranstonAtorEntityComId();
        List<Long> idsTmdb = of(ID_TMDB_KEANU_REEVES, ID_TMDB_BRYAN_CRANSTON);

        when(atorRepository.findByIdTmdbIn(idsTmdb)).thenReturn(of(keanu));
        when(atorRepository.saveAll(anyList())).thenReturn(of(bryan));

        List<Ator> resultado = service.obter(of(keanuDto, bryanDto));

        assertThat(resultado).containsExactly(keanu, bryan);
        verify(atorRepository).saveAll(atoresCaptor.capture());
        assertThat(atoresCaptor.getValue()).extracting(Ator::getIdTmdb)
                .containsExactly(ID_TMDB_BRYAN_CRANSTON);
    }

    @Test
    @DisplayName("Deve consultar novamente quando salvar atores gerar conflito de integridade")
    void deveConsultarNovamenteQuandoSalvarAtoresGerarConflitoDeIntegridade() {
        AtorTMDBMovieDto keanuDto = getKeanuReevesAtorTMDBMovieDto();
        Ator keanu = getKeanuReevesAtorEntityComId();

        when(atorRepository.findByIdTmdbIn(of(ID_TMDB_KEANU_REEVES)))
                .thenReturn(of(), of(keanu));
        when(atorRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Conflito de integridade"));

        List<Ator> resultado = service.obter(of(keanuDto));

        assertThat(resultado).containsExactly(keanu);
        verify(atorRepository, times(2)).findByIdTmdbIn(of(ID_TMDB_KEANU_REEVES));
    }
}
