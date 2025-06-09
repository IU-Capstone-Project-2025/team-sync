from fastapi import FastAPI
from api.endpoints.v1.initial_router import router

app = FastAPI()

app.include_router(router=router)
