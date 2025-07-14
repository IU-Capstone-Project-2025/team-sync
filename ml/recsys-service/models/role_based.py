import faiss
import numpy as np
from models.base_recommender import Recommender
from config.config import Config


class RoleBasedRecommender(Recommender):
    def __init__(self, DBModel, logger, model_name):
        super().__init__(DBModel, logger, model_name)
        self.coefficient = self.config.ROLE_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} role-based recommender.")
        self.all_roles = {}
        self.projects_with_roles = []
        
    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project roles."""
        self.logger.info(f"Fetching recommendations for user {user_id}.")
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []

        num_roles = len(self.all_roles)
        num_projects = len(project_ids)
        
        user_roles_v = np.zeros(shape=(1, num_roles), dtype=np.float32)
        for role in self.db.get_user_roles(user_id):
            user_roles_v[0][self.all_roles[role]] = 1

        index = faiss.IndexFlatL2(num_roles)
        index.add(self.projects_with_roles)
        distances, indices = index.search(user_roles_v, num_projects)
        distances, indices = distances[0], indices[0]

        min_dist, max_dist = np.min(distances), np.max(distances)
        if max_dist == min_dist:
            normalized_scores = np.ones_like(distances)  # Assign uniform scores
        else:
            normalized_scores = 1 - (distances - min_dist) / (max_dist - min_dist)

        recommendations = []
        for i, score in zip(indices, normalized_scores):
            recommendations.append((project_ids[i], score))
        recommendations = [i[1] for i in sorted(recommendations, key=lambda x: x[0])]

        self.logger.info(f"Calculated role-based scores for user {user_id}")
        return recommendations  # [0.14, 0.1, 0.2, 0.15]
    
    def save_data_for_calculation(self, project_ids=None):
        """Save data needed for score calculation."""
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return
        self.logger.info(f"Saving projects with roles for {len(project_ids)} projects.")
        self.all_roles = {}
        for role in self.db.get_all_roles():
            self.all_roles[role[0]] = len(self.all_roles)
        num_roles = len(self.all_roles)
        num_projects = len(project_ids)

        self.projects_with_roles = np.zeros(shape=(num_projects, num_roles), dtype=np.float32)
        for i in range(num_projects):
            for role in self.db.get_project_roles(project_ids[i]):
                self.projects_with_roles[i][self.all_roles[role]] = 1
        