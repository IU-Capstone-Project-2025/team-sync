from fastapi import APIRouter, FastAPI, Request

router = APIRouter(prefix="", tags=["base"])

@router.get("/health")
async def health_check():
    return {"status": "Base service is running"}

@router.post("/recommendations")
async def trigger_recommendation(request: Request):
    result = await trigger_recommendation_job(request.app)
    return result

async def trigger_recommendation_job(app: FastAPI):
    logger = app.state.logger
    logger.info("Starting scheduled recommendation job")
    try:
        await app.state.merger.iterate_all_users()
        logger.info("Scheduled recommendation job completed successfully")
    except Exception as e:
        logger.error(f"Error during scheduled recommendation job: {e}")
    return {"status": "Scheduled recommendation job executed"}