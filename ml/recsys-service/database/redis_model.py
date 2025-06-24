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

    async def set(self, key, value):
        """Set a key-value pair in Redis."""
        # Key - user_id
        # Value - [{"project_id": 123, "score": 0.95}, {"project_id": 456, "score": 0.88}]
        self.logger.info(f"Setting key: {key}")
        json_value = json.dumps(value)
        await self.client.delete(key)
        await self.client.set(key, json_value)
        self.logger.info(f"Value set for key: {key}")

    async def get(self, key):
        """Get a value by key from Redis."""
        self.logger.info(f"Getting value for key: {key}")
        value = await self.client.get(key)
        if value is None:
            self.logger.warning(f"No value found for key: {key}")
            return None
        try:
            return json.loads(value)
        except json.JSONDecodeError as e:
            self.logger.error(f"Error decoding JSON for key {key}: {e}")
            return None
        
    async def check_connection(self):
        """Check if the Redis connection is alive."""
        try:
            await self.client.ping()
            self.logger.info("Redis connection is healthy.")
            return True
        except redis.ConnectionError as e:
            self.logger.error(f"Redis connection error: {e}")
            return False