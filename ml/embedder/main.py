from fastapi import FastAPI
from api.endpoints.v1.routers.initial_router import router
from models.onnx_model import ONNXInference

async def lifespan(app: FastAPI):
    app.state.onnx = ONNXInference()
    yield

app = FastAPI(lifespan=lifespan)

app.include_router(router=router)
