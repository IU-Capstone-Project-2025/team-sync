from fastapi import FastAPI
from api.endpoints.v1.initial_router import router
from database.base_model import DBModel
from config.logging import setup_logging
import asyncio

async def lifespan(app: FastAPI):    
    app.state.logger = setup_logging()
    logger = app.state.logger
    logger.info("Application startup")
    app.state.db = None
    app.state.db = DBModel()
    if not app.state.db:
        raise ValueError("Database model is not initialized.")
    while not app.state.db.connection:
        logger.info("Connecting to the database...")
        try:
            app.state.db.connect()
        except Exception as e:
            logger.error(f"Failed to connect to the database: {e}")
            await asyncio.sleep(5)
    logger.info("Database connection established successfully.")
    yield
    logger.info("Application shutdown")

app = FastAPI(lifespan=lifespan)

app.include_router(router=router)
