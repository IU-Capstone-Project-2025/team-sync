import os


class Config:
    MODEL_PATH = "models/allminilm_l6_v2_onnx/model.onnx"
    TOKENIZER_PATH = "models/allminilm_l6_v2_onnx/tokenizer.json"
    @property
    def POSTGRES_HOST(self):
        host = os.getenv("POSTGRES_HOST")
        if not host:
            raise RuntimeError("POSTGRES_HOST environment variable is not set!")
        return host
    @property
    def POSTGRES_PORT(self):
        port = os.getenv("POSTGRES_PORT")
        if not port:
            raise RuntimeError("POSTGRES_PORT environment variable is not set!")
        return port

    @property
    def POSTGRES_USER(self):
        user = os.getenv("DB_USER")
        if not user:
            raise RuntimeError("DB_USER environment variable is not set!")
        return user

    @property
    def POSTGRES_PASSWORD(self):
        password = os.getenv("DB_PASSWORD")
        if not password:
            raise RuntimeError("DB_PASSWORD environment variable is not set!")
        return password
    @property
    def POSTGRES_DBNAME(self):
        dbname = os.getenv("DB_NAME")
        if not dbname:
            raise RuntimeError("DB_NAME environment variable is not set!")
        return dbname

    @property
    def QDRANT_URL(self):
        url = os.getenv("QDRANT_URL")
        if not url:
            raise RuntimeError("QDRANT_URL environment variable is not set!")
        return url

    @property
    def QDRANT_API_KEY(self):
        api_key = os.getenv("QDRANT_API_KEY")
        if not api_key:
            raise RuntimeError("QDRANT_API_KEY environment variable is not set!")
        return api_key
