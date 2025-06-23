import json
import redis
from redis.asyncio import Redis
from config.config import Config

class RedisModel:
    def __init__(self, logger):
        self.logger = logger
        self.client = Redis(
            host = Config.REDIS_HOST, 
            port=Config.REDIS_PORT, 
            db=Config.REDIS_DB,
            password=Config.REDIS_PASSWORD)
        max_retries = 10
        while max_retries > 0:
            try:
                self.client.ping()
                self.logger.info("Connected to Redis successfully.")
                break
            except redis.ConnectionError as e:
                self.logger.error(f"Error connecting to Redis: {e}, retrying...")
                max_retries -= 1
                if max_retries == 0:
                    raise ConnectionError("Failed to connect to Redis after multiple attempts.")

    def set(self, key, value):
        """Set a key-value pair in Redis."""
        # Key - user_id
        # Value - [{"project_id": 123, "score": 0.95}, {"project_id": 456, "score": 0.88}]
        self.logger.info(f"Setting key: {key}")
        json_value = json.dumps(value)
        self.client.delete(key)
        self.client.set(key, value)
        self.logger.info(f"Value set for key: {key}")
