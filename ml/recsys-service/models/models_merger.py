class ModelsMerger:
    def __init__(self, logger, db, redis):
        self.models = []
        self.db = db
        self.redis = redis
        self.logger = logger
        self.logger.info("Initialized ModelsMerger.")

    def add_model(self, model):
        """Add a model to the merger."""
        self.models.append(model)
        self.logger.info(f"Added model: {model.model_name} to the merger.")

    def merge_scores_for_user(self, user_id, project_ids=None):
        """Merge scores from all models for a given user."""
        self.logger.info(f"Merging scores for user {user_id}.")
        if not self.models:
            self.logger.warning("No models available for merging scores.")
            return {}
        
        if project_ids is None:
            project_ids = []
        
        # Value - [{"project_id": 123, "score": 0.95}, {"project_id": 456, "score": 0.88}]
        result = []
        for id in project_ids:
                dictionary = {}
                dictionary["project_id"] = id
                dictionary["score"] = 0
                result.append(dictionary)

        for model in self.models:
            model_scores = model.calculate_scores(user_id, project_ids=project_ids)
            for i in range(len(project_ids)):
                result[i]["score"] += model_scores[i] * model.coefficient
            if not model_scores:
                self.logger.warning(f"No scores returned from model: {model.model_name} for user {user_id}.")
                continue
        return result
    
    async def iterate_all_users(self):
        """Iterate through all users and merge scores for each."""
        self.logger.info("Starting to iterate through all users to merge scores.")
        project_ids = self.db.get_project_ids()
        self.logger.info("Iterating through all users to merge scores.")
        user_ids = self.db.get_student_ids()
        if not user_ids:
            self.logger.warning("No user IDs found in the database.")
            return

        for model in self.models:
            model.save_data_for_calculation(project_ids=project_ids)
            self.logger.info(f"Saved data for calculation for model: {model.model_name}.")

        for user_id in user_ids:
            value = self.merge_scores_for_user(user_id, project_ids)
            # self.logger.info(f"Merging scores for user {user_id}: {value}")
            await self.redis.set(user_id, value)
            self.logger.info(f"Merged scores for user {user_id}.")
