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
from fastapi import HTTPException

import pickle

# Definições de diretórios e constantes
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ROSTO_NAO_DETECTADO = "Nenhum rosto detectado"
ATRIBUTO_IDENTIDADE = "identity"
ATRIBUTO_DISTANCIA_MEDIA = "average_distance"
CONTAINER_NAME = "blobs"
URL_NAO_FORNECIDA = "URL da imagem não fornecida"
CONNECTION_STRING="DefaultEndpointsProtocol=https;AccountName=whothisactorblobstorage;AccountKey=OFlwXfhQgnLNt8rhf3tAQ2a/0j1D06LtL/VFm4UGGSdvLcDAA0v8DbeNwcWROuvDLEl9kYSIr+NX+ASts08AHw==;EndpointSuffix=core.windows.net"

def euclidean_distance(a, b):
    return np.linalg.norm(a - b)

def recognize_face_with_centroids(embedding, labels):
    try:
        with open('centroids.pkl', 'rb') as f:
            centroids = pickle.load(f)

        distances_to_centroids = {}
        centroids_filtered = {label: centroid for label, centroid in centroids.items() if label in labels}
        
        for label, centroid in centroids_filtered.items():
            distance = euclidean_distance(embedding, centroid)
            distances_to_centroids[label] = distance

        sorted_distances = sorted(distances_to_centroids.items(), key=lambda x: x[1])

        top_5_labels = sorted_distances[:5]

        return top_5_labels
        
    except Exception as e:
        print(f"Erro ao reconhecer rosto com Faiss: {e}")
        return []

def get_blob_name_from_url(blob_url):
    parsed_url = urlparse(blob_url)
    return parsed_url.path.split('/')[-1]


def recognize_face_with_faiss(image, top_n=5):
    try:
        image = make_square(image)

        embedding = DeepFace.represent(image, model_name="Facenet512", enforce_detection=False, detector_backend= "skip",)[0]["embedding"]
        img_embedding = np.array([embedding], dtype=np.float32)

        faiss.normalize_L2(img_embedding)

        closest_images = {}

        index_dir = "faiss_indexes"
        if not os.path.exists(index_dir):
            print(f"Pasta {index_dir} não encontrada.")
            return []

        for filename in os.listdir(index_dir):
            if filename.endswith("_index.idx"):
                label = filename.replace("_index.idx", "")
                index_path = os.path.join(index_dir, filename)

                index = faiss.read_index(index_path)
                distances, indices = index.search(img_embedding, 1)
                closest_images[label] = distances[0][0]

        menores = dict(sorted(closest_images.items(), key=lambda item: item[1])[:5])

        distancia_referencia = next(iter(menores.values()))  
        filtrados = {k: v for k, v in menores.items() if abs(v - distancia_referencia) <= 0.10}


        if (len(filtrados) == 1):
            return [next(iter(filtrados.items()))]
        else:
            return recognize_face_with_centroids(img_embedding, list(filtrados.keys()))

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
        # imageDownload.delete_blob()
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Erro ao processar a imagem: {str(e)}")

    image = Image.open(BytesIO(imagemBaixada)).convert("RGB")
    image = cv2.cvtColor(np.array(image), cv2.COLOR_RGB2BGR)

    face_image = detect_face_retina(image)
    if face_image is None:
        raise HTTPException(status_code=404, detail=ROSTO_NAO_DETECTADO)

    top_n = 10
    top_results = recognize_face_with_faiss(face_image, top_n)

    result = [
        {ATRIBUTO_IDENTIDADE: identity, ATRIBUTO_DISTANCIA_MEDIA: float(distance)} 
        for identity, distance in top_results
    ]
    return result