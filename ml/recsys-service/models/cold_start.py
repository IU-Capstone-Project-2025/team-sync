class ColdStartModel:
    def __init__(self, ALSRecommender, logger):
        self.als_recommender = ALSRecommender
        self.logger = logger
    
    def calculate_scores(self, project_ids=None):
        # data = coo_matrix((als_confidence, (als_user_ids, als_item_ids)), shape=(num_users, num_items))
        # self.user_items = data.tocsr()
        user_items = self.als_recommender.user_items
        if user_items is None:
            self.logger.error("User items data is not available. Please call save_data_for_calculation first.")
            return [{"project_id": pid, "score": 0.0} for pid in project_ids] if project_ids else []
        scores = []
        for project_id in project_ids:
            score = user_items[:, project_id].sum() if project_id < user_items.shape[1] else 0.0
            scores.append({"project_id": project_id, "score": float(score)})
        return scores