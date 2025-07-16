from models.base_recommender import Recommender
from config.config import Config
from models.embedder import Embedder
import numpy as np

class DescriptionBasedRecommender(Recommender):
    def __init__(self, DBModel, qdrant_model, logger, model_name):
        super().__init__(DBModel, logger, model_name)
        self.config = Config()
        self.coefficient = self.config.DESCRIPTION_COEFFICIENT
        self.sbert = Embedder(url=self.config.EMBEDDER_URL, logger=logger)
        self.qdrant = qdrant_model
        self.embeddings = []
        self.logger.info(f"Initialized {self.model_name} description-based recommender.")

    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project descriptions."""
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []
        recommendations = []
        user_description = self.db.get_user_description(user_id)
        embedding = self.sbert.encode(user_description)[0] # [[0.1, 0.2, 0.3, ...]]
        for index, project_id in enumerate(project_ids):
            score = 0
            if not user_description:
                recommendations.append(score)
                self.logger.warning(f"No description found for user {user_id}.")
                continue
            if self.embeddings:
                score = np.dot(self.embeddings[index], embedding) / (np.linalg.norm(self.embeddings[index]) * np.linalg.norm(embedding))
            else:
                self.logger.warning(f"No embeddings available for project {project_id}.")
            recommendations.append(score)
        return recommendations

    def save_data_for_calculation(self, project_ids=None, user_ids=None):
        """Save project descriptions for scoring."""
        if not project_ids:
            self.logger.warning("No projects available for saving descriptions.")
            return
        self.logger.info(f"Saving project descriptions for {len(project_ids)} projects.")
        self.embeddings = []
        for project in project_ids:
            emb = self.qdrant.get_embedding("project", project)
            if emb:
                self.embeddings.append(emb)
                continue
            project_description = self.db.get_project_description(project)
            if project_description:
                embedding = self.sbert.encode(project_description)[0]
                self.embeddings.append(embedding)