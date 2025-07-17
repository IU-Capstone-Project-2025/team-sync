import os

class Config:
    @property
    def POSTGRES_HOST(self):
        return os.getenv("POSTGRES_HOST", "postgres")

    @property
    def POSTGRES_USER(self):
        return os.getenv("DB_USER", "dev-user")

    @property
    def POSTGRES_PASSWORD(self):
        return os.getenv("DB_PASSWORD", "dev-psw")

    @property
    def POSTGRES_DB(self):
        return os.getenv("DB_NAME", "dev-db")

    @property
    def POSTGRES_PORT(self):
        return int(os.getenv("POSTGRES_PORT", 5432))

    @property
    def DB_URL(self):
        return f"postgresql://{self.POSTGRES_USER}:{self.POSTGRES_PASSWORD}@{self.POSTGRES_HOST}:{self.POSTGRES_PORT}/{self.POSTGRES_DB}"

    @property
    def REDIS_HOST(self):
        return os.getenv("REDIS_HOST", "keydb")

    @property
    def REDIS_PORT(self):
        return int(os.getenv("REDIS_PORT", 6379))

    @property
    def REDIS_DB(self):
        return int(os.getenv("REDIS_DB", 0))

    @property
    def REDIS_PASSWORD(self):
        return os.getenv("KEY_DB_PASSWORD", "dev-keydb-psw")

    @property
    def QDRANT_API_KEY(self):
        return os.getenv("QDRANT_API_KEY", "dev-qdrant-api-key")

    QDRANT_HOST = "qdrant"
    QDRANT_PORT = 6333

    RECOMMENDATION_JOB_INTERVAL = 5

    BASE_COEFFICIENT = 0.5

    TAG_COEFFICIENT = 0.3

    DESCRIPTION_COEFFICIENT = 0.3

    ROLE_COEFFICIENT = 0.3
    
    ALS_COEFFICIENT = 0.3

    DESCRIPTION_MODEL_NAME = "all-MiniLM-L6-v2"

    EMBEDDER_URL = "http://ml-embedder:8000"

    TAG_L2_COEFFICIENT = 0.2
    TAG_IP_COEFFICIENT = 0.1
    TAG_IOU_COEFFICIENT = 0.7
    
    ROLE_L2_COEFFICIENT = 0.2
    ROLE_IOU_COEFFICIENT = 0.4
    ROLE_OL_COEFFICIENT = 0.4

    ALS_FACTORS = 64
    ALS_REGULARIZATION = 0.01
    ALS_ITERATIONS = 20
    FAVORITE_SCORE = 5
    CLICK_SCORE = 1
    APPLY_SCORE = 10
