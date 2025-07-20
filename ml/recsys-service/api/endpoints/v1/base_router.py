from fastapi import APIRouter, FastAPI, Request, HTTPException, Header

router = APIRouter(prefix="", tags=["base"])

@router.get("/health")
async def health_check():
    return {"status": "Base service is running"}

@router.post("/recommendations/{secret_key}")
async def trigger_recommendation(request: Request, secret_key: str):
    if secret_key != request.app.state.config.SECRET_KEY:
        raise HTTPException(
            status_code=401,
            detail="Invalid secret key"
        )
    
    try:
        result = await trigger_recommendation_job(request.app)
        return result
    except Exception as e:
        if hasattr(request.app.state, 'logger'):
            request.app.state.logger.error(f"Error in trigger_recommendation endpoint: {e}")
        
        raise HTTPException(
            status_code=500,
            detail="Internal server error occurred while processing recommendation job"
        )

async def trigger_recommendation_job(app: FastAPI):
    logger = app.state.logger
    logger.info("Starting scheduled recommendation job")
    try:
        await app.state.merger.iterate_all_users()
        logger.info("Scheduled recommendation job completed successfully")
        return {"status": "Scheduled recommendation job executed successfully"}
    except Exception as e:
        logger.error(f"Error during scheduled recommendation job: {e}")
        raise HTTPException(
            status_code=500,
            detail=f"Recommendation job failed: {str(e)}"
        )