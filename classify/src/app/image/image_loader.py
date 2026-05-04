from io import BytesIO

import cv2
import numpy as np
from PIL import Image


def upload_bytes_to_bgr_image(image_bytes: bytes):
    img = Image.open(BytesIO(image_bytes)).convert("RGB")
    return cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)
