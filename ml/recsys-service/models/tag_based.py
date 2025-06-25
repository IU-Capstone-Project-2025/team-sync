import faiss
import numpy as np
from models.base_recommender import Recommender
from config.config import Config


class TagBasedRecommender(Recommender):
    def __init__(self, DBModel, RedisModel, logger, model_name):
        super().__init__(DBModel, RedisModel, logger, model_name)
        self.coefficient = Config.TAG_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} tag-based recommender.")

    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project skills."""
        self.logger.info(f"Fetching recommendations for user {user_id}.")
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []
        all_skills = {}
        for skill in self.db.get_all_skills():
            all_skills[skill[0]] = len(all_skills)
        num_skills = len(all_skills)

        user_skills_v = np.zeros(shape=(1, num_skills))
        for skill in self.db.get_user_skills(user_id):
            user_skills_v[0][all_skills[skill]] = 1
        num_projects = len(project_ids)

        projects_with_skills = np.zeros(shape=(num_projects, num_skills))
        for i in range(num_projects):
            for skill in self.db.get_project_skills(project_ids[i]):
                projects_with_skills[i][all_skills[skill]] = 1

        index = faiss.IndexFlatL2(num_skills)
        index.add(projects_with_skills)
        distances, indices = index.search(user_skills_v, num_projects)
        distances, indices = distances[0], indices[0]

        min_dist, max_dist = np.min(distances), np.max(distances)
        normalized_scores = 1 - (distances - min_dist) / (max_dist - min_dist)

        recommendations = []
        for i, score in zip(indices, normalized_scores):
            recommendations.append((project_ids[i], score))
        recommendations.sort(key=lambda x: x[0])  # if you need to sort by project ids

        self.logger.info(f"Calculated tag-based scores for user {user_id}")
        return recommendations  # [(0, 0.14), (1, 0.2)]
