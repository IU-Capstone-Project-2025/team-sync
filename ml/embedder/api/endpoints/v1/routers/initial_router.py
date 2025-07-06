from fastapi import APIRouter, Request
from api.endpoints.v1.base_models.embedding import EmbeddingRequest

router = APIRouter(prefix="/api/v1", tags=["embedder"])

@router.get("/health")
async def health_check():
    return {"status": "working embedder"}

@router.post("/embedding")
async def create_embedding(req: Request, request: EmbeddingRequest):
    texts = request.texts if isinstance(request.texts, list) else [request.texts]
    embeddings = req.app.state.onnx.get_embeddings(texts)
    embeddings = [emb.tolist() if hasattr(emb, 'tolist') else emb for emb in embeddings]
    return {"embeddings": embeddings}