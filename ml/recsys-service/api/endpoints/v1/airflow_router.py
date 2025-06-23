from fastapi import APIRouter, Request

router = APIRouter(prefix="/api/v1/airflow", tags=["airflow"])

@router.get("/health")
async def health_check():
    return {"status": "Airflow service is running"}

@router.post("/recommendations")
async def trigger_recommendation(request: Request):
    db = request.app.state.db
    logger = request.app.state.logger
    logger.info("Triggering recommendation job")
    
    return {"status": "Recommendation job triggered successfully"}