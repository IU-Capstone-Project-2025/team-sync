from fastapi import APIRouter

router = APIRouter(prefix="/api", tags=["recsys-service"])

@router.get("/health")
async def health_check():
    return {"status": "healthy"}
