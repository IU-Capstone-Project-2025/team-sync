from abc import ABC, abstractmethod
from config.config import Config

class Recommender(ABC):
    def __init__(self, DBModel, RedisModel, logger, model_name):
        self.model_name = model_name
        self.db = DBModel(logger)
        self.redis = RedisModel(logger)
        self.logger = logger
        self.project_ids = None
        self.coefficient = Config.BASE_COEFFICIENT # for weighting scores among all models 
        self.logger.info(f"Initialized {self.model_name} recommender with DB and Redis models.")
    
    @abstractmethod
    def calculate_scores(self, user_id):
        """Calculate scores for project recommendations for a given user."""
        pass