import gc
import logging
import time
from typing import List, Optional

from fastapi import HTTPException, UploadFile

from app.cache.serie_ator_cache import buscar_atores_por_ids_producoes_em_memoria
from app.core.config import settings
from app.image.image_loader import upload_bytes_to_bgr_image
from app.ml.detection.face_detector import detect_face_mtcnn
from app.ml.recognition.rank_fusion import (
    FUSION_TOP_N,
    montar_resultados_fusionados,
)
from app.ml.recognition.recognizer import (
    recognize_face_with_faiss,
    recognize_face_with_faiss_filtrado,
)

ATRIBUTO_LABEL = "label"
ATRIBUTO_PONTUACAO = "pontuacao"
ATRIBUTO_RESULTADO = "resultado"

logger = logging.getLogger(__name__)


async def classify_image_service(
    image: UploadFile,
    ids_series: Optional[List[int]] = None,
    ids_filmes: Optional[List[int]] = None,
):
    tempo_inicio_total = time.perf_counter()

    try:
        t0 = time.perf_counter()
        imagem_bytes = await image.read()
        tempo_leitura_upload = time.perf_counter() - t0
        logger.info(
            "[classify] leitura do upload concluída em %.4fs | bytes=%s",
            tempo_leitura_upload,
            len(imagem_bytes),
        )

        t0 = time.perf_counter()
        image_cv = upload_bytes_to_bgr_image(imagem_bytes)
        tempo_decode_imagem = time.perf_counter() - t0
        logger.info(
            "[classify] conversão bytes -> imagem concluída em %.4fs",
            tempo_decode_imagem,
        )

    except Exception as e:
        tempo_total_erro = time.perf_counter() - tempo_inicio_total
        logger.exception(
            "[classify] erro ao processar imagem após %.4fs",
            tempo_total_erro,
        )
        raise HTTPException(status_code=400, detail=f"Erro ao processar a imagem: {str(e)}")

    t0 = time.perf_counter()
    face_image, flipped_face_image = detect_face_mtcnn(image_cv)
    tempo_detect_face = time.perf_counter() - t0
    logger.info(
        "[classify] detecção de face concluída em %.4fs | face_detectada=%s",
        tempo_detect_face,
        face_image is not None,
    )

    if face_image is None:
        tempo_total_sem_face = time.perf_counter() - tempo_inicio_total
        logger.warning(
            "[classify] nenhuma face detectada | tempo total até falha: %.4fs",
            tempo_total_sem_face,
        )
        raise HTTPException(status_code=404, detail=settings.face_not_detected_message)

    if ids_series or ids_filmes:
        t0 = time.perf_counter()
        atores = buscar_atores_por_ids_producoes_em_memoria(ids_series, ids_filmes)
        tempo_busca_atores = time.perf_counter() - t0
        logger.info(
            "[classify] busca de atores por produções concluída em %.4fs | ids_series=%s | ids_filmes=%s | qtd_atores=%s",
            tempo_busca_atores,
            len(ids_series or []),
            len(ids_filmes or []),
            len(atores) if atores is not None else 0,
        )

        t0 = time.perf_counter()
        top_results_pca = recognize_face_with_faiss_filtrado(
            face_image,
            flipped_face_image,
            atores,
            top_n=max(settings.top_n_search, FUSION_TOP_N),
            mode="pca",
        )
        top_results_raw = recognize_face_with_faiss_filtrado(
            face_image,
            flipped_face_image,
            atores,
            top_n=max(settings.top_n_search, FUSION_TOP_N),
            mode="raw",
        )
        top_results = montar_resultados_fusionados(
            top_results_pca,
            top_results_raw,
            settings.output_limit,
        )
        tempo_reconhecimento = time.perf_counter() - t0
        logger.info(
            "[classify] reconhecimento filtrado concluído em %.4fs | top_n=%s | pca=%s | raw=%s",
            tempo_reconhecimento,
            settings.top_n_search,
            len(top_results_pca),
            len(top_results_raw),
        )
    else:
        t0 = time.perf_counter()
        top_results_pca = recognize_face_with_faiss(
            face_image,
            flipped_face_image,
            top_n=max(settings.top_n_search, FUSION_TOP_N),
            mode="pca",
        )
        top_results_raw = recognize_face_with_faiss(
            face_image,
            flipped_face_image,
            top_n=max(settings.top_n_search, FUSION_TOP_N),
            mode="raw",
        )
        top_results = montar_resultados_fusionados(
            top_results_pca,
            top_results_raw,
            settings.output_limit,
        )
        tempo_reconhecimento = time.perf_counter() - t0
        logger.info(
            "[classify] reconhecimento geral concluído em %.4fs | top_n=%s | pca=%s | raw=%s",
            tempo_reconhecimento,
            settings.top_n_search,
            len(top_results_pca),
            len(top_results_raw),
        )

    t0 = time.perf_counter()
    top_results = top_results[:settings.output_limit]
    response = [
        {ATRIBUTO_LABEL: label, ATRIBUTO_PONTUACAO: float(pontuacao)}
        for label, pontuacao in top_results
    ]
    tempo_montagem_resposta = time.perf_counter() - t0
    logger.info(
        "[classify] montagem da resposta concluída em %.4fs | output_limit=%s | retornados=%s",
        tempo_montagem_resposta,
        settings.output_limit,
        len(response),
    )

    tempo_total = time.perf_counter() - tempo_inicio_total
    logger.info("[classify] processamento total concluído em %.4fs", tempo_total)

    return {ATRIBUTO_RESULTADO: response}
