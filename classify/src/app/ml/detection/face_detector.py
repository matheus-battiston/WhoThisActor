import cv2
import numpy as np
from mtcnn_ort import MTCNN
from app.core.config import settings

CONFIDENCE = 'confidence'
BOX = 'box'
KEYPOINTS = 'keypoints'

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

def preprocess_image(image):
    return cv2.cvtColor(image, cv2.COLOR_BGR2RGB)


def align_face(image, bounding_box, keypoints):
    try:
        left_eye = keypoints['left_eye']
        right_eye = keypoints['right_eye']

        dx = right_eye[0] - left_eye[0]
        dy = right_eye[1] - left_eye[1]
        angle = np.degrees(np.arctan2(dy, dx))

        center = (
            (left_eye[0] + right_eye[0]) / 2,
            (left_eye[1] + right_eye[1]) / 2
        )

        M = cv2.getRotationMatrix2D(center, angle, 1.0)
        aligned_image = cv2.warpAffine(image, M, (image.shape[1], image.shape[0]))

        x, y, w, h = bounding_box

        box_pts = np.array([
            [x, y],
            [x + w, y],
            [x + w, y + h],
            [x, y + h]
        ], dtype=np.float32)

        ones = np.ones((4, 1), dtype=np.float32)
        box_pts_h = np.hstack([box_pts, ones])

        transformed_pts = (M @ box_pts_h.T).T

        x_coords = transformed_pts[:, 0]
        y_coords = transformed_pts[:, 1]

        x1 = int(max(0, np.min(x_coords)))
        y1 = int(max(0, np.min(y_coords)))
        x2 = int(min(aligned_image.shape[1], np.max(x_coords)))
        y2 = int(min(aligned_image.shape[0], np.max(y_coords)))

        margin = settings.face_crop_margin_ratio
        mx = int((x2 - x1) * margin)
        my = int((y2 - y1) * margin)

        x1 = max(0, x1 - mx)
        y1 = max(0, y1 - my)
        x2 = min(aligned_image.shape[1], x2 + mx)
        y2 = min(aligned_image.shape[0], y2 + my)

        aligned_face = aligned_image[y1:y2, x1:x2]

        return aligned_face

    except Exception as e:
        print(f"Erro no alinhamento da face: {e}")
        return None


def detect_face_mtcnn(
    image,
    max_size=settings.max_face_detection_size,
    output_size=settings.face_output_size,
):
    detector = MTCNN()

    try:
        h, w = image.shape[:2]
        scale = min(max_size / w, max_size / h, 1.0)
        small = cv2.resize(image, (int(w * scale), int(h * scale)), interpolation=cv2.INTER_AREA) if scale < 1.0 else image

        faces = detector.detect_faces(preprocess_image(small))

        if not faces or faces[0][CONFIDENCE] <= settings.min_face_confidence:
            return None, None

        face = faces[0]

        face_crop = align_face(small, face[BOX], face[KEYPOINTS])
        if face_crop is None:
            return None, None

        face_crop = cv2.resize(make_square(face_crop), (output_size, output_size), interpolation=cv2.INTER_AREA)

        small_flipped = cv2.flip(small, 1)
        flipped_faces = detector.detect_faces(preprocess_image(small_flipped))

        if flipped_faces and flipped_faces[0][CONFIDENCE] > settings.min_face_confidence:
            flipped_crop = align_face(small_flipped, flipped_faces[0][BOX], flipped_faces[0][KEYPOINTS])
            if flipped_crop is not None:
                flipped_crop = cv2.resize(make_square(flipped_crop), (output_size, output_size),interpolation=cv2.INTER_AREA)
            else:
                flipped_crop = face_crop
        else:
            flipped_crop = face_crop

        return face_crop, flipped_crop

    except Exception as e:
        print(f"Erro na detecção: {e}")
        return None, None
