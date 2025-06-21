from fastapi import FastAPI
from api.endpoints.v1.initial_router import router
from database.base_model import DBModel
from config.logging import setup_logging
import asyncio

async def lifespan(app: FastAPI):    
    app.state.logger = setup_logging()
    logger = app.state.logger
    logger.info("Application startup")
    app.state.db = DBModel()
    await app.state.db.connect()
    logger.info("Database connection established successfully.")
    yield
    logger.info("Application shutdown")
    if app.state.db and app.state.db.connection:
        logger.info("Closing database connection...")
        app.state.db.disconnect()

app = FastAPI(lifespan=lifespan)

app.include_router(router=router)
