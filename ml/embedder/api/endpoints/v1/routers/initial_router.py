from fastapi import APIRouter, Request
from api.endpoints.v1.base_models.embedding import EmbeddingRequest
from api.endpoints.v1.base_models.qdrant import QdrantEmbeddingRequest

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

@router.post("/points")
async def create_points(req: Request, request: QdrantEmbeddingRequest):
    text = req.app.state.postgres.fetch_description(request.table, request.id)
    embeddings = req.app.state.onnx.get_embeddings(text)
    embeddings = [emb.tolist() if hasattr(emb, 'tolist') else emb for emb in embeddings]
    req.app.state.qdrant.update_embeddings(request.table, request.id, embeddings)
    return {"status": "points created"}

@router.get("/points/{table}/{id}")
async def get_points(req: Request, table: str, id: int):
    if table not in ["project", "student"]:
        return {"error": "Invalid table name. Use 'project' or 'student'."}
    points = req.app.state.qdrant.get_embeddings(table, id)
    return {"points": points}

@router.get("/similarities/{table}/{init_id}")
async def get_similarities(req: Request, table: str, init_id: int):
    if table not in ["project", "student"]:
        return {"error": "Invalid table name. Use 'project' or 'student'."}
    similarities = req.app.state.qdrant.get_similarities(table, init_id)
    return {"similarities": similarities}

@router.get("/similarities/{table}/{init_id}/among_projects")
async def get_similarities_among_projects(req: Request, table: str, init_id: int):
    if table not in ["project", "student"]:
        return {"error": "Invalid table name. Use 'project' or 'student'."}
    similarities = req.app.state.qdrant.get_similarities(table, init_id, among_projects=True)
    return {"similarities": similarities}