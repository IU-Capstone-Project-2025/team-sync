from fastapi import FastAPI
from api.endpoints.v1.base_router import router, trigger_recommendation_job
from database.postgres_model import DBModel
from config.logging import setup_logging
from api.endpoints.v1.test_router import router as test_router
from database.redis_model import RedisModel
from models.models_merger import ModelsMerger
from models.tag_based import TagBasedRecommender
from models.description_based import DescriptionBasedRecommender
from config.config import Config
from metrics.metrics_model import Metrics
from apscheduler.schedulers.asyncio import AsyncIOScheduler

async def lifespan(app: FastAPI):    
    app.state.logger = setup_logging()
    logger = app.state.logger
    logger.info("Application startup")
    app.state.db = DBModel(logger=logger)
    app.state.redis = RedisModel(logger=logger)
    await app.state.redis.connect()
    await app.state.db.connect()
    logger.info("Database connection established successfully.")

    # recommendation models initialization
    logger.info("Initializing recommendation models...")
    app.state.merger = ModelsMerger(logger, app.state.db, app.state.redis)
    app.state.merger.add_model(
        TagBasedRecommender(
            DBModel=app.state.db,
            logger=logger,
            model_name="tag_based"
        )
    )
    app.state.merger.add_model(
        DescriptionBasedRecommender(
            DBModel=app.state.db,
            logger=logger,
            model_name="description_based"
        )
    )
    logger.info("Recommendation models initialized successfully.")

    # scheduler initialization
    logger.info("Initializing scheduler...")
    app.state.scheduler = AsyncIOScheduler()
    app.state.scheduler.add_job(
        trigger_recommendation_job,
        'interval',
        minutes=5,
        args=[app]
    )
    app.state.scheduler.start()
    logger.info("Scheduler initialized and started successfully.")

    # metrics model initialization
    app.state.metrics = Metrics(
        relevance_matrix=[[1, 1, 1, 0, 0, 1, 1, 1, 0, 0],
                          [0, 1, 1, 1, 1, 0, 1, 0, 1, 0],
                          [1, 0, 1, 1, 0, 1, 1, 1, 0, 0]], # calculated for 3 random users :o
        logger=logger
    )
    yield
    logger.info("Application shutdown")
    if app.state.db and app.state.db.connection:
        logger.info("Closing database connection...")
        await app.state.db.disconnect()
    app.state.scheduler.shutdown()

app = FastAPI(lifespan=lifespan)

app.include_router(router=router)
app.include_router(router=test_router)
