import numpy as np
import cv2

TAMANHO_IMAGEM = 160

def make_square(image):
    h, w = image.shape[:2]
    if h == w:
        return image
    elif h > w:
        padding = (h - w) // 2
        image_padded = cv2.copyMakeBorder(image, 0, 0, padding, padding, cv2.BORDER_CONSTANT, value=(0, 0, 0))
    else:
        padding = (w - h) // 2
        image_padded = cv2.copyMakeBorder(image, padding, padding, 0, 0, cv2.BORDER_CONSTANT, value=(0, 0, 0))
    
    image = cv2.resize(image, (TAMANHO_IMAGEM, TAMANHO_IMAGEM))

    return image_padded

def get_embedding(model, image):

    image = make_square(image)
    image = cv2.resize(image, (TAMANHO_IMAGEM, TAMANHO_IMAGEM))

    
    image = image.astype("float32")
    image = (image - 127.5) / 128.0  # Normalização
    image = np.expand_dims(image, axis=0)


    embedding = model.predict(image)
    return embedding[0]