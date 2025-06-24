from fastapi import APIRouter, Request

router = APIRouter(prefix="/api/v1/test", tags=["test"])

@router.get("/health")
async def health_check():
    return {"status": "Test service is running"}

@router.get("/user/{user_id}")
async def get_user(user_id: int):
    return {"user_id": user_id, "message": "User data retrieved successfully"}

@router.get("/project/{project_id}")
async def get_project(project_id: int):
    return {"project_id": project_id, "message": "Project data retrieved successfully"}

# Checking DB logic
@router.get("/db-check")
async def db_check(request: Request):
    try:
        db = request.app.state.db
        logger = request.app.state.logger
        logger.info(f"get user skills {db.get_user_skills(1)}")
        logger.info(f"get project skills: {db.get_project_skills(1)}")
        logger.info(f"get all skills: {db.get_all_skills()}")
        logger.info(f"get all students: {db.get_all_students()}")
        logger.info(f"get all projects: {db.get_all_projects()}")
        logger.info(f"get project ids: {db.get_project_ids()}")
        logger.info(f"get student ids: {db.get_student_ids()}")
        db_status = "Database connection is healthy"
        return {"status": db_status}
    except Exception as e:
        return {"status": "Database connection failed", "error": str(e)}
    
@router.get("/redis/check")
async def redis_check(request: Request):
    try:
        redis = request.app.state.redis
        if redis:
            await redis.set("1", [{"project_id": 123, "score": 0.95}, {"project_id": 456, "score": 0.88}])
            result = await redis.get("1")
            return {"status": "Redis connection is healthy", "fetched_data": result}
        else:
            return {"status": "Redis connection is not established"}
    except Exception as e:
        return {"status": "Redis connection failed", "error": str(e)}
    
@router.get("/test")
async def test_endpoint(request: Request):
    redis = request.app.state.redis
    redis.check_connection()
    return {"status": "Test endpoint is working", "redis_status": "Checked"}

@router.get("/redis/get/{key}")
async def get_redis_value(key: str, request: Request):
    redis = request.app.state.redis
    value = await redis.get(key)
    if value is None:
        return {"status": "Key not found in Redis", "key": key}
    return {"status": "Value retrieved from Redis", "key": key, "value": value}