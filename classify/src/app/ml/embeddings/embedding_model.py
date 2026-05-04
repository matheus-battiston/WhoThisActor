import cv2
import faiss
import numpy as np
import onnxruntime as ort

from app.core.config import settings
from app.ml.embeddings.pca_projection import transform_query


def create_session_options():
    session_options = ort.SessionOptions()
    session_options.intra_op_num_threads = 1
    session_options.inter_op_num_threads = 1
    session_options.enable_mem_pattern = False
    session_options.enable_cpu_mem_arena = False
    return session_options


onnx_session = ort.InferenceSession(
    str(settings.model_path),
    sess_options=create_session_options(),
)


def preprocess_image(image):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = image.astype(np.float32)
    image = (image - 127.5) / 128.0
    return np.expand_dims(image, axis=0)


def get_embedding_onnx(image, session=onnx_session):
    preprocessed_img = preprocess_image(image)
    input_name = session.get_inputs()[0].name
    output_name = session.get_outputs()[0].name
    embedding = session.run([output_name], {input_name: preprocessed_img})[0]
    return embedding[0]


def gerar_embedding(image):
    embedding = get_embedding_onnx(image)
    img_embedding = np.array([embedding], dtype=np.float32)
    faiss.normalize_L2(img_embedding)
    return img_embedding


def gerar_embeddings_consulta(image, image_flip, projection=None):
    emb_original = gerar_embedding(image).astype(np.float32)
    emb_flip = gerar_embedding(image_flip).astype(np.float32)
    if projection is not None:
        emb_original = transform_query(emb_original, projection=projection)
        emb_flip = transform_query(emb_flip, projection=projection)
    return emb_original, emb_flip
