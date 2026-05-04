class FaceRecognitionError(Exception):
    """Base error for face recognition failures."""


class FaceNotDetectedError(FaceRecognitionError):
    """Raised when no face can be detected in the provided image."""


class RecognitionIndexNotFoundError(FaceRecognitionError):
    """Raised when FAISS index artifacts are missing or unavailable."""
