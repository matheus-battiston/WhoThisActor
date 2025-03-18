from fastapi import FastAPI
from pydantic import BaseModel
from reconhecimento_facial_service import classify_image_service

app = FastAPI()

class Imagem(BaseModel):
    image_url: str
    fast: bool = False  # Valor padrão para `fast` é False

@app.post("/classify")
async def create_item(item: Imagem):
    result = await classify_image_service(item.image_url, item.fast)
    return result

@app.get("/wakeup")
async def wake_up():
    return {"status": "Service is up and running! v2"}