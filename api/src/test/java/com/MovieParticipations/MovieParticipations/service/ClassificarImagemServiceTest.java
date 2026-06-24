package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.ClassificarImagemResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.response.ClassificacaoResponseDTOFactory.getKeanuReevesClassificacaoResponseDTO;
import static com.MovieParticipations.MovieParticipations.factories.response.OpcoesAtoresParecidosResponseFactory.getKeanuReevesOpcoesAtoresParecidosResponse;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClassificarImagemService")
class ClassificarImagemServiceTest {

    private static final Long ID_SERIE_FAVORITA = 1L;
    private static final Long ID_FILME_FAVORITO = 2L;
    private static final String IDENTIDADE_KEANU_REEVES = "Keanu Reeves";
    private static final Double DISTANCIA_MEDIA_KEANU_REEVES = 0.15;

    @Mock
    private RequisicaoApiClassificacaoService requisicaoApiClassificacaoService;

    @Mock
    private ListaFavoritosService listaFavoritosService;

    @Mock
    private EnriquecerAtoresClassificadosService enriquecerAtoresClassificadosService;

    @Mock
    private MultipartFile imagem;

    @Captor
    private ArgumentCaptor<List<OpcoesAtoresParecidosResponse>> atoresCaptor;

    @InjectMocks
    private ClassificarImagemService service;

    @Test
    @DisplayName("Deve classificar imagem sem filtro e mapear resposta antes de enriquecer atores")
    void deveClassificarImagemSemFiltroEMapearRespostaAntesDeEnriquecerAtores() throws IOException {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<ClassificacaoResponseDTO> classificacoes = of(getKeanuReevesClassificacaoResponseDTO());
        List<OpcoesAtoresParecidosResponse> atoresEnriquecidos = of(getKeanuReevesOpcoesAtoresParecidosResponse());

        when(requisicaoApiClassificacaoService.classificarImagem(imagem)).thenReturn(classificacoes);
        when(enriquecerAtoresClassificadosService.enriquecerComDadosTmdb(anyList()))
                .thenReturn(atoresEnriquecidos);

        ClassificarImagemResponse resultado = service.classificarImagem(imagem, false, usuarioAutenticado);

        assertEquals(atoresEnriquecidos, resultado.getResultado());
        verify(requisicaoApiClassificacaoService).classificarImagem(imagem);
        verify(requisicaoApiClassificacaoService, never()).classificarImagem(eq(imagem), anyList(), anyList());
        verifyNoInteractions(listaFavoritosService);
        verify(enriquecerAtoresClassificadosService).enriquecerComDadosTmdb(atoresCaptor.capture());
        assertEquals(IDENTIDADE_KEANU_REEVES, atoresCaptor.getValue().get(0).getIdentidade());
        assertEquals(DISTANCIA_MEDIA_KEANU_REEVES, atoresCaptor.getValue().get(0).getDistanciaMedia());
    }

    @Test
    @DisplayName("Deve classificar imagem sem filtro de favoritos quando usuário não estiver autenticado")
    void deveClassificarImagemSemFiltroDeFavoritosQuandoUsuarioNaoEstiverAutenticado() throws IOException {
        List<ClassificacaoResponseDTO> classificacoes = of(getKeanuReevesClassificacaoResponseDTO());
        List<OpcoesAtoresParecidosResponse> atoresEnriquecidos = of(getKeanuReevesOpcoesAtoresParecidosResponse());

        when(requisicaoApiClassificacaoService.classificarImagem(imagem)).thenReturn(classificacoes);
        when(enriquecerAtoresClassificadosService.enriquecerComDadosTmdb(anyList()))
                .thenReturn(atoresEnriquecidos);

        ClassificarImagemResponse resultado = service.classificarImagem(imagem, true, null);

        assertEquals(atoresEnriquecidos, resultado.getResultado());
        verify(requisicaoApiClassificacaoService).classificarImagem(imagem);
        verify(requisicaoApiClassificacaoService, never()).classificarImagem(eq(imagem), anyList(), anyList());
        verifyNoInteractions(listaFavoritosService);
    }

    @Test
    @DisplayName("Deve classificar imagem usando filmes e séries favoritos do usuário")
    void deveClassificarImagemUsandoFilmesESeriesFavoritosDoUsuario() throws IOException {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        List<Long> idsSeriesFavoritas = of(ID_SERIE_FAVORITA);
        List<Long> idsFilmesFavoritos = of(ID_FILME_FAVORITO);
        List<ClassificacaoResponseDTO> classificacoes = of(getKeanuReevesClassificacaoResponseDTO());
        List<OpcoesAtoresParecidosResponse> atoresEnriquecidos = of(getKeanuReevesOpcoesAtoresParecidosResponse());

        when(listaFavoritosService.buscarIdsSeriesFavoritas(usuarioAutenticado)).thenReturn(idsSeriesFavoritas);
        when(listaFavoritosService.buscarIdsFilmesFavoritos(usuarioAutenticado)).thenReturn(idsFilmesFavoritos);
        when(requisicaoApiClassificacaoService.classificarImagem(imagem, idsSeriesFavoritas, idsFilmesFavoritos))
                .thenReturn(classificacoes);
        when(enriquecerAtoresClassificadosService.enriquecerComDadosTmdb(anyList()))
                .thenReturn(atoresEnriquecidos);

        ClassificarImagemResponse resultado = service.classificarImagem(imagem, true, usuarioAutenticado);

        assertEquals(atoresEnriquecidos, resultado.getResultado());
        verify(listaFavoritosService).buscarIdsSeriesFavoritas(usuarioAutenticado);
        verify(listaFavoritosService).buscarIdsFilmesFavoritos(usuarioAutenticado);
        verify(requisicaoApiClassificacaoService)
                .classificarImagem(imagem, idsSeriesFavoritas, idsFilmesFavoritos);
        verify(requisicaoApiClassificacaoService, never()).classificarImagem(imagem);
    }
}
