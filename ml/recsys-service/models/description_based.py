from models.base_recommender import Recommender
from config.config import Config

class DescriptionBasedRecommender(Recommender):
    def __init__(self, DBModel, RedisModel, logger, model_name, project_ids):
        super().__init__(DBModel, RedisModel, logger, model_name)
        self.set_projects_ids(project_ids)
        self.coefficient = Config.DESCRIPTION_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} description-based recommender.")

    def calculate_scores(self, user_id):
        """Get project recommendations based on project descriptions."""
        self.logger.info(f"Fetching recommendations for user {user_id}.")
        if not self.projects:
            self.get_all_projects()
        
        if not self.projects:
            self.logger.warning("No projects available for recommendations.")
            return []
        recommendations = []
        for project in self.project_ids:
            project_id = project[0]
            score = 1 * self.coefficient
            # TODO: Implement actual description-based scoring logic
            recommendations.append(score)
        self.logger.info(f"Calculated description-based scores for user {user_id}")
        return recommendations
