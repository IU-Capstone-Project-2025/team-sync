from fastapi import FastAPI
from api.endpoints.v1.routers.initial_router import router
from models.onnx_model import ONNXInference
from models.qdrant_model import QdrantModel
from models.postgres_model import PostgresModel
from config.logging import setup_logging

async def lifespan(app: FastAPI):
    app.state.logger = setup_logging()
    app.state.onnx = ONNXInference()
    app.state.qdrant = QdrantModel(logger=app.state.logger)
    app.state.postgres = PostgresModel()
    yield

app = FastAPI(lifespan=lifespan, root_path="/embedder/api/v1")

app.include_router(router=router)
