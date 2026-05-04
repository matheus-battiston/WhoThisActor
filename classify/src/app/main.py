from fastapi import FastAPI
from contextlib import asynccontextmanager

from app.api.routes import router
from app.cache.serie_ator_cache import carregar_cache_atores_por_producao
from app.core.logging import configure_logging

configure_logging()


@asynccontextmanager
async def lifespan(app: FastAPI):
    carregar_cache_atores_por_producao()
    yield
app = FastAPI(
    title="Who Is This Actor Classifier",
    lifespan=lifespan
)
app.include_router(router)
