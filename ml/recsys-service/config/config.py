class Config:
    POSTGRES_HOST = "postgres"
    POSTGRES_USER = "dev-user"
    POSTGRES_PASSWORD = "dev-psw"
    POSTGRES_DB = "dev-db"
    POSTGRES_PORT = 5432
    DB_URL = f"postgresql://{POSTGRES_USER}:{POSTGRES_PASSWORD}@{POSTGRES_HOST}:{POSTGRES_PORT}/{POSTGRES_DB}"
    REDIS_HOST = "keydb"
    REDIS_PORT = 6379
    REDIS_DB = 0
    REDIS_PASSWORD = "dev-keydb-psw"
    BASE_COEFFICIENT = 0.5
    DESCRIPTION_COEFFICIENT = 0.3