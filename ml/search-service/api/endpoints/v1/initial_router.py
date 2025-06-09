from fastapi import APIRouter

router = APIRouter(prefix="/api", tags=["search-service"])

@router.get("/search")
async def health_check():
    return {"status": "working search"}
