from fastapi import APIRouter, Request
from api.endpoints.v1.base_models.embedding import EmbeddingRequest
import asyncio

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

@router.post("/points/student/{id}")
async def create_points(req: Request, id: int):
    try:
        text = await asyncio.get_event_loop().run_in_executor(None, req.app.state.postgres.fetch_description, "student", id)
        embeddings = await asyncio.get_event_loop().run_in_executor(None, req.app.state.onnx.get_embeddings, text)
        embeddings = [emb.tolist() if hasattr(emb, 'tolist') else emb for emb in embeddings]
        req.app.state.qdrant.update_embeddings("student", id, embeddings)
    except Exception as e:
        return {"error": str(e)}
    return {"status": "points created"}

@router.post("/points/project/{id}")
async def create_project_points(req: Request, id: int):
    try:
        text = await asyncio.get_event_loop().run_in_executor(None, req.app.state.postgres.fetch_description, "project", id)
        embeddings = await asyncio.get_event_loop().run_in_executor(None, req.app.state.onnx.get_embeddings, text)
        embeddings = [emb.tolist() if hasattr(emb, 'tolist') else emb for emb in embeddings]
        req.app.state.qdrant.update_embeddings("project", id, embeddings)
    except Exception as e:
        return {"error": str(e)}
    return {"status": "points created for project"}

@router.get("/points/{table}/{id}")
async def get_points(req: Request, table: str, id: int):
    if table not in ["project", "student"]:
        return {"error": "Invalid table name. Use 'project' or 'student'."}
    points = await asyncio.get_event_loop().run_in_executor(None, req.app.state.qdrant.get_embeddings, table, id)
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