import json
from json import JSONDecodeError
from typing import Optional

from fastapi import APIRouter, File, Form, HTTPException, UploadFile

from app.api.schemas import ClassificationResponse, WakeupResponse
from app.cache.serie_ator_cache import carregar_cache_atores_por_producao
from app.services.reconhecimento_facial_service import classify_image_service


router = APIRouter()


@router.post("/classify", response_model=ClassificationResponse)
async def classify_image(
    image: UploadFile = File(...),
    lista_series: Optional[str] = Form(None),
    lista_filmes: Optional[str] = Form(None),
):
    try:
        series_ids = json.loads(lista_series) if lista_series else None
        filmes_ids = json.loads(lista_filmes) if lista_filmes else None
    except JSONDecodeError as exc:
        raise HTTPException(
            status_code=400,
            detail="lista_series e lista_filmes devem ser JSONs validos.",
        ) from exc

    return await classify_image_service(image, series_ids, filmes_ids)


@router.get("/health", response_model=WakeupResponse)
async def wake_up():
    return WakeupResponse(status="Service is up and running!")


@router.post("/cache/atores-por-producao/recarregar")
async def recarregar_cache_atores_por_producao():
    carregar_cache_atores_por_producao()
    return {"message": "Cache de atores por produção recarregado com sucesso"}
