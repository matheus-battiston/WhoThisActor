import cv2
import numpy as np
from mtcnn_ort import MTCNN

def preprocess_image(image):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)  # Converte BGR para RGB
    return image

def align_face(image, bounding_box, keypoints):
    try:
        left_eye = keypoints['left_eye']
        right_eye = keypoints['right_eye']
        nose = keypoints['nose']
        mouth_left = keypoints['mouth_left']
        mouth_right = keypoints['mouth_right']

        dx = right_eye[0] - left_eye[0]
        dy = right_eye[1] - left_eye[1]
        angle = np.arctan2(dy, dx) * 180 / np.pi

        center = ((left_eye[0] + right_eye[0]) / 2, (left_eye[1] + right_eye[1]) / 2)

        rotation_matrix = cv2.getRotationMatrix2D(center, angle, 1)

        aligned_face = cv2.warpAffine(image, rotation_matrix, (image.shape[1], image.shape[0]))

        x, y, w, h = bounding_box
        aligned_face = aligned_face[y:y+h, x:x+w]

        return aligned_face

    except Exception as e:
        print(f"Erro no alinhamento da face: {e}")
        return None

def detect_face_mtcnn(image):
    try:
        detector = MTCNN()

        image_rgb = preprocess_image(image)

        faces = detector.detect_faces(image_rgb)

        if faces:
            face = faces[0]
            bounding_box = face['box']
            keypoints = face['keypoints']
            confidence = face['confidence']

            if confidence > 0.9:
                aligned_face = align_face(image, bounding_box, keypoints)
                if aligned_face is not None:
                    print(f"Confiança da detecção: {confidence:.2f}")
                    return aligned_face
                else:
                    print("Erro ao alinhar a face")
                    return None
            else:
                print("Confiança abaixo do limiar")
                return None
        else:
            print("Nenhuma face detectada")
            return None
    except Exception as e:
        print(f"Erro na detecção ou alinhamento da face: {e}")
        return None