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
        face_objs = DeepFace.extract_faces(
            img_path=np.array(image),
            detector_backend="retinaface",
            align=alignment_modes[0],
        )

        if face_objs:
            first_face = face_objs[0]
            face_array = first_face["face"]
            bbox = first_face["facial_area"]
            confidence = first_face["confidence"] 

            print(f"Confiança da detecção: {confidence:.2f}")

            face_bgr = cv2.cvtColor((face_array * 255).astype(np.uint8), cv2.COLOR_RGB2BGR)
            return face_bgr
        else:
            print("Nenhuma face detectada")
            return None

    except Exception as e:
        print(f"Erro na detecção ou alinhamento da face: {e}")
        return None