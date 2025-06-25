from fastapi import FastAPI
from api.endpoints.v1.airflow_router import router
from database.postgres_model import DBModel
from config.logging import setup_logging
from api.endpoints.v1.test_router import router as test_router
from database.redis_model import RedisModel
from models.models_merger import ModelsMerger
from models.description_based import DescriptionBasedRecommender

async def lifespan(app: FastAPI):    
    app.state.logger = setup_logging()
    logger = app.state.logger
    logger.info("Application startup")
    app.state.db = DBModel(logger=logger)
    app.state.redis = RedisModel(logger=logger)
    await app.state.db.connect()
    logger.info("Database connection established successfully.")

    # recommendation models initialization
    logger.info("Initializing recommendation models...")
    app.state.merger = ModelsMerger(logger, app.state.db, app.state.redis)
    app.state.merger.add_model(
        TagBasedRecommender(
            DBModel=app.state.db,
            RedisModel=app.state.redis,
            logger=logger,
            model_name="tag_based"
        )
    )
    app.state.merger.add_model(
        DescriptionBasedRecommender(
            DBModel=app.state.db,
            RedisModel=app.state.redis,
            logger=logger,
            model_name="description_based"
        )
    )
    logger.info("Recommendation models initialized successfully.")

    yield
    logger.info("Application shutdown")
    if app.state.db and app.state.db.connection:
        logger.info("Closing database connection...")
        await app.state.db.disconnect()

app = FastAPI(lifespan=lifespan)

app.include_router(router=router)
app.include_router(router=test_router)
