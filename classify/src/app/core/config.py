from dataclasses import dataclass
import os
from pathlib import Path


SRC_DIR = Path(__file__).resolve().parents[2]


@dataclass(frozen=True)
class Settings:
    base_dir: Path = SRC_DIR

    model_path: Path = base_dir / "app" / "artifacts" / "models" / "saved_facenet_model.onnx"
    pca_projection_path: Path = base_dir / "pca_projection.pkl"
    faiss_index_pca_path: Path = base_dir / "faiss_index_pca.bin"
    faiss_labels_pca_path: Path = base_dir / "faiss_labels_pca.pkl"
    faiss_index_raw_path: Path = base_dir / "faiss_index_raw.bin"
    faiss_labels_raw_path: Path = base_dir / "faiss_labels_raw.pkl"

    top_n_search: int = 20
    output_limit: int = 5
    pca_components: int = int(os.getenv("PCA_COMPONENTS", "384"))
    pca_whiten: bool = os.getenv("PCA_WHITEN", "true").lower() == "true"

    max_face_detection_size: int = 640
    face_output_size: int = 160
    min_face_confidence: float = 0.9
    face_crop_margin_ratio: float = 0.05

    face_not_detected_message: str = "Nenhum rosto detectado"

    postgres_db: str = os.getenv("POSTGRES_DB", "postgres")
    postgres_user: str = os.getenv("POSTGRES_USER", "postgres")
    postgres_password: str = os.getenv("POSTGRES_PASSWORD", "")
    postgres_host: str = os.getenv("POSTGRES_HOST", "localhost")
    postgres_port: str = os.getenv("POSTGRES_PORT", "5432")
    postgres_sslmode: str = os.getenv("POSTGRES_SSLMODE", "require")


settings = Settings()
