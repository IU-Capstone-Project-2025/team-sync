import numpy as np
from models.base_recommender import Recommender
from models.tools.faiss_tools import get_faiss_recommendations
from models.tools.IOU_tools import get_IOU_recommendations
from models.tools.OL_tools import get_OL_recommendations


class TagBasedRecommender(Recommender):
    def __init__(self, DBModel, logger, model_name):
        super().__init__(DBModel, logger, model_name)
        self.coefficient = self.config.TAG_COEFFICIENT
        self.logger.info(f"Initialized {self.model_name} tag-based recommender.")
        self.all_skills = {}
        self.projects_with_skills = []
        self.projects_with_skills_v = []
        
    def calculate_scores(self, user_id, project_ids=None):
        """Get project recommendations based on project skills."""
        if not project_ids:
            self.logger.warning("No projects available for recommendations.")
            return []

        num_skills = len(self.all_skills)
        num_projects = len(project_ids)
        
        user_skills = self.db.get_user_skills(user_id)
        user_skills_v = np.zeros(shape=(1, num_skills), dtype=np.float32)
        for skill in user_skills:
            user_skills_v[0][self.all_skills[skill]] = 1

        recommendations_L2 = get_faiss_recommendations("euclidean distance", num_skills, project_ids, self.projects_with_skills_v, user_skills_v)
        recommendations_OL = get_OL_recommendations(user_skills, self.projects_with_skills)
        recommendations_IOU = get_IOU_recommendations(user_skills, self.projects_with_skills)
        
        recommendations = []
        for score_id in range(num_projects):
            recommendations.append(recommendations_L2[score_id] * self.config.TAG_L2_COEFFICIENT +
                                   recommendations_OL[score_id] * self.config.TAG_OL_COEFFICIENT +
                                   recommendations_IOU[score_id] * self.config.TAG_IOU_COEFFICIENT)
        return recommendations  # [0.14, 0.1, 0.2, 0.15]
    
    def save_data_for_calculation(self, project_ids=None, user_ids=None):
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

        self.projects_with_skills_v = np.zeros(shape=(num_projects, num_skills), dtype=np.float32)
        for i in range(num_projects):
            self.projects_with_skills.append([])
            for skill in self.db.get_project_skills(project_ids[i]):
                self.projects_with_skills[-1].append(skill)
                self.projects_with_skills_v[i][self.all_skills[skill]] = 1
