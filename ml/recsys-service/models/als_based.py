from scipy.sparse import coo_matrix
import implicit
from config.config import Config

class ALSRecommender():
    def __init__(self, DBModel, logger, model_name):
        self.model_name = model_name
        self.db = DBModel
        self.logger = logger
        self.config = Config()
        self.coefficient = self.config.BASE_COEFFICIENT
        self.als_model = implicit.als.AlternatingLeastSquares(
            factors=self.config.ALS_FACTORS,
            regularization=self.config.ALS_REGULARIZATION,
            iterations=self.config.ALS_ITERATIONS
        )
        self.user_items = None
        self.logger.info(f"Initialized {self.model_name} recommender with DB model.")
    
    def calculate_scores(self, user_id, project_ids=None):
        """Calculate scores for project recommendations for a given user.
        """
        self.logger.info(f"Fetching recommendations for user {user_id}.")

        if project_ids is None:
            self.logger.warning("No project IDs provided for filtering recommendations.")
            return []

        if self.user_items is None:
            self.logger.error("User items data is not available. Please call save_data_for_calculation first.")
            return [0.0] * len(project_ids)

        if user_id >= self.user_items.shape[0]:
            self.logger.warning(f"User {user_id} not in ALS matrix (size: {self.user_items.shape[0]}). Returning zeros.")
            return [0.0] * len(project_ids)

        raw_recommendations = self.als_model.recommend(
            user_id, 
            self.user_items[user_id], 
            N=self.user_items.shape[1],
            filter_already_liked_items=False
        )

        item_ids, scores = raw_recommendations
        
        score_dict = {item_id: score for item_id, score in zip(item_ids, scores)}
        
        recommendations = []
        for project_id in project_ids:
            score = float(score_dict.get(project_id, 0.0))
            recommendations.append(score)

        self.logger.info(f"Recommendations for user {user_id}: {recommendations}")
        return recommendations

    def save_data_for_calculation(self, project_ids=None, user_ids=None):
        """Save data needed for score calculation."""
        # TODO: Mapping to reduce memory usage
        self.logger.info(f"{user_ids}")
        self.logger.info(f"Saving data for calculation in {self.model_name} recommender.")
        if not project_ids:
            self.logger.warning("No projects available for saving data.")
            return

        if not user_ids:
            self.logger.warning("No users available for saving data.")
            return

        confidence = {}

        for user_id in user_ids:
            # Favorite projects
            fav_project_ids = self.db.fetch_favorites(user_id)
            for project_id in fav_project_ids:
                if f"{user_id}_{project_id}" not in confidence:
                    confidence[f"{user_id}_{project_id}"] = 0
                confidence[f"{user_id}_{project_id}"] += self.config.FAVORITE_SCORE
            # Clicked projects
            clicked_project_ids = self.db.fetch_clicks(user_id)
            for project_id in clicked_project_ids:
                if f"{user_id}_{project_id}" not in confidence:
                    confidence[f"{user_id}_{project_id}"] = 0
                confidence[f"{user_id}_{project_id}"] += self.config.CLICK_SCORE
            # Applied projects
            applied_project_ids = self.db.fetch_applies(user_id)
            for project_id in applied_project_ids:
                if f"{user_id}_{project_id}" not in confidence:
                    confidence[f"{user_id}_{project_id}"] = 0
                confidence[f"{user_id}_{project_id}"] += self.config.APPLY_SCORE

        als_user_ids = []
        als_item_ids = []
        als_confidence = []

        for key, value in confidence.items():
            user_id, project_id = map(int, key.split("_"))
            als_user_ids.append(user_id)
            als_item_ids.append(project_id)
            als_confidence.append(value)

        num_users = max(als_user_ids) + 1
        num_items = max(als_item_ids) + 1
        data = coo_matrix((als_confidence, (als_user_ids, als_item_ids)), shape=(num_users, num_items))

        self.user_items = data.tocsr()
        self.als_model.fit(data)
        self.logger.info(f"Data saved for {self.model_name} recommender with {len(als_user_ids)} users and {len(als_item_ids)} projects.")
