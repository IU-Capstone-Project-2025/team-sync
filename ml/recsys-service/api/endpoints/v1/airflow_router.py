from fastapi import APIRouter, Request

router = APIRouter(prefix="/api/v1/airflow", tags=["airflow"])

@router.get("/health")
async def health_check():
    return {"status": "Airflow service is running"}

@router.post("/recommendations")
async def trigger_recommendation(request: Request):
    logger = request.app.state.logger
    logger.info("Triggering recommendation job")
    await request.app.state.merger.iterate_all_users()
    logger.info("Recommendation job triggered successfully")
    return {"status": "Recommendation job triggered successfully"}