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
    
    return image_padded