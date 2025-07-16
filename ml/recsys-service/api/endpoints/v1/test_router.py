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

@router.get("/postgres/get_projects")
async def get_projects(request: Request):
    db = request.app.state.db
    projects = db.get_all_projects()
    if not projects:
        return {"status": "No projects found in the database"}
    return {"status": "Projects retrieved successfully", "projects": projects}

@router.get("/postgres/get_students")
async def get_students(request: Request):
    db = request.app.state.db
    students = db.get_all_students()
    if not students:
        return {"status": "No students found in the database"}
    return {"status": "Students retrieved successfully", "students": students}

@router.get("/postgres/get_student_description/{user_id}")
async def get_student_description(user_id: int, request: Request):
    db = request.app.state.db
    description = db.get_user_description(user_id)
    if not description:
        return {"status": "No description found for the user", "user_id": user_id}
    return {"status": "User description retrieved successfully", "user_id": user_id, "description": description}

@router.get("/postgres/get_project_description/{project_id}")
async def get_project_description(project_id: int, request: Request):
    db = request.app.state.db
    description = db.get_project_description(project_id)
    if not description:
        return {"status": "No description found for the project", "project_id": project_id}
    return {"status": "Project description retrieved successfully", "project_id": project_id, "description": description}

@router.get("/redis/get_range/{user_id}/{start}/{end}")
async def get_redis_range(user_id: int, start: int, end: int, request: Request):
    redis = request.app.state.redis
    values = await redis.get_range(user_id, start, end)
    if not values:
        return {"status": "No values found in Redis", "user_id": user_id, "start": start, "end": end}
    return {"status": "Values retrieved from Redis", "user_id": user_id, "start": start, "end": end, "values": values}

@router.get("/metrics/map")
async def get_metrics_map(request: Request):
    metrics_model = request.app.state.metrics
    metrics = metrics_model.get_metrics()
    return {"status": "Metrics retrieved successfully", "metrics": metrics}

@router.get("/postgres/clicks/{user_id}")
async def get_user_clicks(user_id: int, request: Request):
    db = request.app.state.db
    clicks = db.fetch_clicks(user_id)
    if not clicks:
        return {"status": "No clicks found for the user", "user_id": user_id}
    return {"status": "User clicks retrieved successfully", "user_id": user_id, "clicks": clicks}

@router.get("/postgres/applications/{user_id}")
async def get_user_applications(user_id: int, request: Request):
    db = request.app.state.db
    applications = db.fetch_applies(user_id)
    if not applications:
        return {"status": "No applications found for the user", "user_id": user_id}
    return {"status": "User applications retrieved successfully", "user_id": user_id, "applications": applications}

@router.get("/postgres/favorites/{user_id}")
async def get_user_favorites(user_id: int, request: Request):
    db = request.app.state.db
    favorites = db.fetch_favorites(user_id)
    if not favorites:
        return {"status": "No favorites found for the user", "user_id": user_id}
    return {"status": "User favorites retrieved successfully", "user_id": user_id, "favorites": favorites}

@router.get("/qdrant/embedding/{collection_name}/{id}")
async def get_qdrant_embedding(collection_name: str, id: int, request: Request):
    qdrant = request.app.state.qdrant
    embedding = qdrant.get_embedding(collection_name, id)
    if not embedding:
        return {"status": "No embedding found for the ID", "collection_name": collection_name, "id": id}
    return {"status": "Embedding retrieved successfully", "collection_name": collection_name, "id": id, "embedding": embedding}