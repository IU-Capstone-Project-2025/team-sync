from abc import ABC, abstractmethod
from config.config import Config

class Recommender(ABC):
    def __init__(self, DBModel, logger, model_name):
        self.model_name = model_name
        self.db = DBModel
        self.logger = logger
        self.config = Config()
        self.coefficient = self.config.BASE_COEFFICIENT # for weighting scores among all models 
        self.logger.info(f"Initialized {self.model_name} recommender with DB model.")
    
    @abstractmethod
    def calculate_scores(self, user_id, project_ids=None):
        """Calculate scores for project recommendations for a given user.
        
        Args:
            user_id: The ID of the user for whom recommendations are being calculated.
            project_ids: Optional list of project IDs to filter recommendations.
        """
        pass

    @abstractmethod
    def save_data_for_calculation(self, project_ids=None):
        """Save data needed for score calculation."""
        pass