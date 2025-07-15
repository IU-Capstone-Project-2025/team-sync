import os


class Config:
    MODEL_PATH = "models/allminilm_l6_v2_onnx/model.onnx"
    TOKENIZER_PATH = "models/allminilm_l6_v2_onnx/tokenizer.json"
    POSTGRES_HOST = "postgres"
    POSTGRES_PORT = 5432
    @property
    def POSTGRES_USER(self):
        return os.getenv("DB_USER", "dev-user")
    @property
    def POSTGRES_PASSWORD(self):
        return os.getenv("DB_PASSWORD", "password")
    @property
    def POSTGRES_DBNAME(self):
        return os.getenv("DB_NAME", "dbname")

    QDRANT_HOST = "qdrant"
    QDRANT_PORT = 6333
    @property
    def QDRANT_API_KEY(self):
        return os.getenv("QDRANT_API_KEY", "your-qdrant-api-key")
