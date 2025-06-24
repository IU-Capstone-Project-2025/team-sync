from models.base_recommender import Recommender
from config.config import Config

class DescriptionBasedRecommender(Recommender):
    def __init__(self, DBModel, RedisModel, logger, model_name):
        super().__init__(DBModel, RedisModel, logger, model_name)
        self.coefficient = Config.DESCRIPTION_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} description-based recommender.")

    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project descriptions."""
        self.logger.info(f"Fetching recommendations for user {user_id}.")

        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []
        recommendations = []
        for project_id in project_ids:
            score = 1
            # TODO: Implement actual description-based scoring logic
            recommendations.append(score)
        self.logger.info(f"Calculated description-based scores for user {user_id}")
        return recommendations
