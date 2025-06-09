from fastapi import APIRouter

router = APIRouter(prefix="/api", tags=["recsys-service"])

@router.get("/recsys")
async def health_check():
    return {"status": "working recsys"}
