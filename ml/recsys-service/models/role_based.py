import faiss
import numpy as np
from models.base_recommender import Recommender

def get_faiss_recommendations(num_roles, project_ids, projects_with_roles_v, user_roles_v):
    index = faiss.IndexFlatL2(num_roles)
    index.add(projects_with_roles_v)

    distances, indices = index.search(user_roles_v, len(project_ids))
    distances, indices = distances[0], indices[0]
    
    min_dist, max_dist = np.min(distances), np.max(distances)
    normalized_scores = 1 - (distances - min_dist) / (max_dist - min_dist)

    recommendations = []
    for i, score in zip(indices, normalized_scores):
        recommendations.append((project_ids[i], score))
    recommendations = [i[1] for i in sorted(recommendations, key=lambda x: x[0])]
    return recommendations


def get_IoU_recommendations(user_roles, projects_with_roles):
    recommendations = []
    for project_roles in projects_with_roles:
        recommendations.append(len(np.intersect1d(user_roles, project_roles)) / len(np.union1d(user_roles, project_roles)))
    return recommendations


def get_OL_recommendations(user_roles, projects_with_roles):
    recommendations = []
    for project_roles in projects_with_roles:
        recommendations.append(len(np.intersect1d(user_roles, project_roles)) / min(len(user_roles), len(project_roles)))
    return recommendations


class RoleBasedRecommender(Recommender):
    def __init__(self, DBModel, logger, model_name):
        super().__init__(DBModel, logger, model_name)
        self.coefficient = self.config.ROLE_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} role-based recommender.")
        self.all_roles = {}
        self.projects_with_roles = []
        self.projects_with_roles_v = []
        
    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project roles."""
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []

        num_roles = len(self.all_roles)
        num_projects = len(project_ids)

        user_roles = self.db.get_user_roles(user_id)
        user_roles_v = np.zeros(shape=(1, num_roles), dtype=np.float32)
        for role in user_roles:
            user_roles_v[0][self.all_roles[role]] = 1

        recommendations_L2 = get_faiss_recommendations(num_roles, project_ids, self.projects_with_roles_v, user_roles_v)
        recommendations_IoU = get_IoU_recommendations(, self.projects_with_roles)
        recommendations_OL = get_OL_recommendations(user_roles, self.projects_with_roles)

        recommendations = []
        for score_id in range(num_projects):
            recommendations.append(recommendations_L2[score_id] * self.config.ROLE_L2_COEFFICIENT +
                                   recommendations_IoU[score_id] * self.config.ROLE_IOU_COEFFICIENT +
                                   recommendations_OL[score_id] * self.config.ROLE_OL_COEFFICIENT)
        return recommendations  # [0.14, 0.1, 0.2, 0.15]
    
    def save_data_for_calculation(self, project_ids=None, user_ids=None):
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

        self.projects_with_roles_v = np.zeros(shape=(num_projects, num_roles), dtype=np.float32)
        for i in range(num_projects):
            self.projects_with_roles.append([])
            for role in self.db.get_project_roles(project_ids[i]):
                self.projects_with_roles[-1].append(role)
                self.projects_with_roles_v[i][self.all_roles[role]] = 1
        