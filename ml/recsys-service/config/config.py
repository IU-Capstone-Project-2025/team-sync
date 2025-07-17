import os

class Config:
    @property
    def POSTGRES_HOST(self):
        host = os.getenv("POSTGRES_HOST")
        if not host:
            raise RuntimeError("POSTGRES_HOST environment variable is not set!")
        return host

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
    def POSTGRES_DB(self):
        db = os.getenv("DB_NAME")
        if not db:
            raise RuntimeError("DB_NAME environment variable is not set!")
        return db

    @property
    def POSTGRES_PORT(self):
        port = os.getenv("POSTGRES_PORT")
        if not port:
            raise RuntimeError("POSTGRES_PORT environment variable is not set!")
        return int(port)

    @property
    def DB_URL(self):
        return f"postgresql://{self.POSTGRES_USER}:{self.POSTGRES_PASSWORD}@{self.POSTGRES_HOST}:{self.POSTGRES_PORT}/{self.POSTGRES_DB}"

    @property
    def REDIS_HOST(self):
        host = os.getenv("REDIS_HOST")
        if not host:
            raise RuntimeError("REDIS_HOST environment variable is not set!")
        return host

    @property
    def REDIS_PORT(self):
        port = os.getenv("REDIS_PORT")
        if not port:
            raise RuntimeError("REDIS_PORT environment variable is not set!")
        return int(port)

    @property
    def REDIS_DB(self):
        db = os.getenv("REDIS_DB")
        if not db:
            raise RuntimeError("REDIS_DB environment variable is not set!")
        return int(db)

    @property
    def REDIS_PASSWORD(self):
        password = os.getenv("KEY_DB_PASSWORD")
        if not password:
            raise RuntimeError("KEY_DB_PASSWORD environment variable is not set!")
        return password

    @property
    def QDRANT_API_KEY(self):
        api_key = os.getenv("QDRANT_API_KEY")
        if not api_key:
            raise RuntimeError("QDRANT_API_KEY environment variable is not set!")
        return api_key

    @property
    def EMBEDDER_URL(self):
        embedder_url = os.getenv("EMBEDDER_URL")
        if not embedder_url:
            raise RuntimeError("EMBEDDER_URL environment variable is not set!")
        return embedder_url

    @property
    def QDRANT_HOST(self):
        host = os.getenv("QDRANT_HOST")
        if not host:
            raise RuntimeError("QDRANT_HOST environment variable is not set!")
        return host

    @property
    def QDRANT_PORT(self):
        port = os.getenv("QDRANT_PORT")
        if not port:
            raise RuntimeError("QDRANT_PORT environment variable is not set!")
        return int(port)

    RECOMMENDATION_JOB_INTERVAL = 5

    BASE_COEFFICIENT = 0.5

    TAG_COEFFICIENT = 0.3

    DESCRIPTION_COEFFICIENT = 0.3

    ROLE_COEFFICIENT = 0.3
    
    ALS_COEFFICIENT = 0.3

    DESCRIPTION_MODEL_NAME = "all-MiniLM-L6-v2"

    ALS_FACTORS = 64
    ALS_REGULARIZATION = 0.01
    ALS_ITERATIONS = 20
    FAVORITE_SCORE = 5
    CLICK_SCORE = 1
    APPLY_SCORE = 10