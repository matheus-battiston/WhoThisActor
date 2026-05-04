package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.ClassificarImagemResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import com.MovieParticipations.MovieParticipations.mapper.OpcoesAtoresMapper;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassificarImagemService {

    private final RequisicaoApiClassificacaoService requisicaoApiClassificacaoService;
    private final FavoritosClassificacaoService favoritosClassificacaoService;
    private final EnriquecerAtoresClassificadosService enriquecerAtoresClassificadosService;

    public ClassificarImagemResponse classificarImagem(
            MultipartFile image,
            boolean filtrarPorFavoritos,
            UsuarioAutenticado usuarioAutenticado
    ) throws IOException {
        List<ClassificacaoResponseDTO> respostaClassificacao =
                requisitarClassificacao(image, filtrarPorFavoritos, usuarioAutenticado);

        List<OpcoesAtoresParecidosResponse> atores = mapearResposta(respostaClassificacao);

        return new ClassificarImagemResponse(enriquecerAtoresClassificadosService.enriquecerComDadosTmdb(atores));

    }

    private List<ClassificacaoResponseDTO> requisitarClassificacao(
            MultipartFile image,
            boolean filtrarPorFavoritos,
            UsuarioAutenticado usuarioAutenticado
    ) throws IOException {
        if (!filtrarPorFavoritos || usuarioAutenticado == null)
            return requisicaoApiClassificacaoService.classificarImagem(image);

        return requisicaoApiClassificacaoService.classificarImagem(
                image,
                favoritosClassificacaoService.buscarIdsSeriesFavoritas(usuarioAutenticado),
                favoritosClassificacaoService.buscarIdsFilmesFavoritos(usuarioAutenticado)
        );
    }

    private List<OpcoesAtoresParecidosResponse> mapearResposta(List<ClassificacaoResponseDTO> respostaClassificacao) {
        return respostaClassificacao.stream()
                .map(OpcoesAtoresMapper::toResponse)
                .toList();
    }
}
