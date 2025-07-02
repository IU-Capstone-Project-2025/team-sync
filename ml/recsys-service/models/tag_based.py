import faiss
import numpy as np
from models.base_recommender import Recommender
from config.config import Config


class TagBasedRecommender(Recommender):
    def __init__(self, DBModel, logger, model_name):
        super().__init__(DBModel, logger, model_name)
        self.coefficient = Config.TAG_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} tag-based recommender.")
        self.all_skills = {}
        self.projects_with_skills = []

    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project skills."""
        self.logger.info(f"Fetching recommendations for user {user_id}.")
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []

        num_skills = len(self.all_skills)
        num_projects = len(project_ids)
        
        user_skills_v = np.zeros(shape=(1, num_skills), dtype=np.float32)
        for skill in self.db.get_user_skills(user_id):
            user_skills_v[0][self.all_skills[skill]] = 1

        index = faiss.IndexFlatL2(num_skills)
        index.add(self.projects_with_skills)
        distances, indices = index.search(user_skills_v, num_projects)
        distances, indices = distances[0], indices[0]

        min_dist, max_dist = np.min(distances), np.max(distances)
        normalized_scores = 1 - (distances - min_dist) / (max_dist - min_dist)

        recommendations = []
        for i, score in zip(indices, normalized_scores):
            recommendations.append((project_ids[i], score))
        recommendations = [i[1] for i in sorted(recommendations, key=lambda x: x[0])]

        self.logger.info(f"Calculated tag-based scores for user {user_id}")
        return recommendations  # [0.14, 0.1, 0.2, 0.15]
    
    def save_data_for_calculation(self, project_ids=None):
        """Save data needed for score calculation."""
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return
        self.logger.info(f"Saving projects with skills for {len(project_ids)} projects.")
        self.all_skills = {}
        for skill in self.db.get_all_skills():
            self.all_skills[skill[0]] = len(self.all_skills)
        num_skills = len(self.all_skills)
        num_projects = len(project_ids)

        self.projects_with_skills = np.zeros(shape=(num_projects, num_skills), dtype=np.float32)
        for i in range(num_projects):
            for skill in self.db.get_project_skills(project_ids[i]):
                self.projects_with_skills[i][self.all_skills[skill]] = 1
        
