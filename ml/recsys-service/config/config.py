class Config:
    POSTGRES_HOST = "postgres"
    POSTGRES_USER = "dev-user"
    POSTGRES_PASSWORD = "dev-psw"
    POSTGRES_DB = "dev-db"
    POSTGRES_PORT = 5432
    DB_URL = f"postgresql://{POSTGRES_USER}:{POSTGRES_PASSWORD}@{POSTGRES_HOST}:{POSTGRES_PORT}/{POSTGRES_DB}"

