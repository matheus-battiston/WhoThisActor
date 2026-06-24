from pydantic import BaseModel


class ClassificationResult(BaseModel):
    label: str
    pontuacao: float


class ClassificationResponse(BaseModel):
    resultado: list[ClassificationResult]


class WakeupResponse(BaseModel):
    status: str
