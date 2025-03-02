from retinaface import RetinaFace
import cv2
import numpy as np
import gc

AREA_FACIAL = "facial_area"

def detect_face_retina(image):
    try:
        # Redimensionar a imagem para reduzir o uso de memória
        height, width = image.shape[:2]
        max_dimension = 800  # Defina um valor adequado para o tamanho máximo
        if height > max_dimension or width > max_dimension:
            scale = max_dimension / float(max(height, width))
            image = cv2.resize(image, None, fx=scale, fy=scale)

        detections = RetinaFace.detect_faces(image)
        if not detections:
            return None

        face_key = list(detections.keys())[0]
        facial_area = detections[face_key]['facial_area']
        landmarks = detections[face_key]['landmarks']

        left_eye = np.array(landmarks['left_eye'])
        right_eye = np.array(landmarks['right_eye'])
        
        dY = right_eye[1] - left_eye[1]
        dX = right_eye[0] - left_eye[0]
        angle = np.degrees(np.arctan2(dY, dX))

        if angle > 90:
            angle -= 180
        elif angle < -90:
            angle += 180

        eyes_center = ((left_eye[0] + right_eye[0]) // 2, (left_eye[1] + right_eye[1]) // 2)
        rotation_matrix = cv2.getRotationMatrix2D(eyes_center, angle, 1)

    
        (h, w) = image.shape[:2]
        aligned_image = cv2.warpAffine(image, rotation_matrix, (w, h), flags=cv2.INTER_LINEAR, borderMode=cv2.BORDER_REFLECT_101)

        x1, y1, x2, y2 = facial_area

        margin = 20
        y1 = max(0, y1 - margin)
        y2 = min(h, y2 + margin)

    
        lateral_margin = 0 
        x1 = max(0, x1 - lateral_margin)
        x2 = min(w, x2 + lateral_margin)

        aligned_face = aligned_image[y1:y2, x1:x2]

        # Forçar a coleta de lixo para liberar memória
        del aligned_image
        gc.collect()

        return aligned_face
    except Exception as e:
        print(f"Erro na detecção ou alinhamento da face: {e}")
        return None