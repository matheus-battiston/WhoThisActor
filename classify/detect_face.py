from deepface import DeepFace
import cv2
import numpy as np

backends = [
  'opencv', 'ssd', 'dlib', 'mtcnn', 'fastmtcnn',
  'retinaface', 'mediapipe', 'yolov8', 'yolov11s',
  'yolov11n', 'yolov11m', 'yunet', 'centerface',
]

alignment_modes = [True, False]

def detect_face_retina(image):
    try:
        # Face detection and alignment
        face_objs = DeepFace.extract_faces(
            img_path=np.array(image),
            detector_backend="mtcnn",  # RetinaFace
            align=alignment_modes[0],
        )

        if face_objs:  # Se detectou pelo menos um rosto
            first_face = face_objs[0]
            face_array = first_face["face"]  # O recorte da face
            bbox = first_face["facial_area"]  # Coordenadas do rosto
            confidence = first_face["confidence"]  # Confiança

            print(f"Confiança da detecção: {confidence:.2f}")

            # Converter de float64 para uint8
            face_bgr = cv2.cvtColor((face_array * 255).astype(np.uint8), cv2.COLOR_RGB2BGR)
            return face_bgr
        else:
            print("Nenhuma face detectada")
            return None

    except Exception as e:
        print(f"Erro na detecção ou alinhamento da face: {e}")
        return None