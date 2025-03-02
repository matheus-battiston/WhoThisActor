from calculo_embedding import make_square
from deepface import DeepFace
from detect_face import detect_face_retina
from blob_service import EmbeddingService
from urllib.parse import urlparse
import numpy as np
import cv2
import os
import faiss
from PIL import Image
from io import BytesIO
import gc
from fastapi import HTTPException
import pickle
import time

# Definições de diretórios e constantes
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ROSTO_NAO_DETECTADO = "Nenhum rosto detectado"
ATRIBUTO_IDENTIDADE = "identity"
ATRIBUTO_DISTANCIA_MEDIA = "average_distance"
CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=whothisactorstorage;AccountKey=sEWPmJWNNw5iJIZM9OROq8URi+lcw5XLTfBd5KsvHVMDLSGwDEIi0iocJjLw7WY0oCOUd2wIUp8d+AStYs2d5g==;EndpointSuffix=core.windows.net"
CONTAINER_NAME = "containerblob"
BLOB_NAME = "embeddings_combinados.pkl"
URL_NAO_FORNECIDA = "URL da imagem não fornecida"
EMBEDDINGS_FILE_PATH = "embeddings_combinados.pkl"

def load_embeddings_from_file():
    try:
        with open(EMBEDDINGS_FILE_PATH, "rb") as f:
            embeddings, labels = pickle.load(f)
        return embeddings, labels
    except Exception as e:
        print(f"Erro ao carregar os embeddings do arquivo: {e}")
        return {}, {}

def get_blob_name_from_url(blob_url):
    parsed_url = urlparse(blob_url)
    return parsed_url.path.split('/')[-1]

def build_faiss_index():

    # Carregar os embeddings do arquivo
    embeddings, labels = load_embeddings_from_file()    
    faiss_indices = {}
    
    
    for label, emb_list in embeddings.items():
        if not emb_list:
            continue

        
        emb_array = np.array(emb_list, dtype=np.float32)        
        faiss.normalize_L2(emb_array)

        n_neighbors = 16  
        index = faiss.IndexHNSWFlat(emb_array.shape[1], n_neighbors)
    
        index.add(emb_array)
        faiss_indices[label] = index

    del embeddings
    del labels
    gc.collect()

    return faiss_indices

faiss_indices = build_faiss_index()

def recognize_face_with_faiss(image, faiss_indices, top_n=5):
    try:
        image = make_square(image)
        
        embedding = DeepFace.represent(image, model_name="Facenet512", enforce_detection=False)[0]["embedding"]
        img_embedding = np.array([embedding], dtype=np.float32)

        faiss.normalize_L2(img_embedding)

        closest_images = []

        for label, index in faiss_indices.items():
            distances, indices = index.search(img_embedding, top_n)

            for i in range(len(indices[0])):
                closest_images.append((label, indices[0][i], distances[0][i]))

        closest_images.sort(key=lambda x: x[2])

        resposta = []
        labels_added = set()

        for item in closest_images:
            label, index, distance = item
            if label not in labels_added:
                resposta.append(item)
                labels_added.add(label)

            if len(resposta) >= top_n:
                break        
        return resposta
    except Exception as e:
        print(f"Erro ao reconhecer rosto com Faiss: {e}")
        return []

async def classify_image_service(req: str):
    try:
        image_url = req
    except Exception:
        raise HTTPException(status_code=400, detail=URL_NAO_FORNECIDA)

    try:
        imageDownload = EmbeddingService(CONNECTION_STRING, CONTAINER_NAME, get_blob_name_from_url(image_url))
        imagemBaixada = imageDownload.load_image_from_blob()
        imageDownload.delete_blob()
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Erro ao processar a imagem: {str(e)}")

    image = Image.open(BytesIO(imagemBaixada)).convert("RGB")
    image = cv2.cvtColor(np.array(image), cv2.COLOR_RGB2BGR)

    face_image = detect_face_retina(image)
    if face_image is None:
        raise HTTPException(status_code=404, detail=ROSTO_NAO_DETECTADO)

    top_n = 5
    top_results = recognize_face_with_faiss(face_image, faiss_indices, top_n)

    result = [
        {ATRIBUTO_IDENTIDADE: identity, ATRIBUTO_DISTANCIA_MEDIA: float(distance)} 
        for identity, _, distance in top_results
    ]
    return result