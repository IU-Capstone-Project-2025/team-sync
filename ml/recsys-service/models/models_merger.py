class ModelsMerger:
    def __init__(self, logger, db):
        self.models = []
        self.db = db
        self.logger = logger
        self.projects_ids = None
        self.get_all_projects()
        self.logger.info("Initialized ModelsMerger.")

    def add_model(self, model):
        """Add a model to the merger."""
        self.models.append(model)
        self.logger.info(f"Added model: {model.model_name} to the merger.")

    def get_all_projects(self):
        """Fetch all projects from the database."""
        self.logger.info("Fetching all projects from the database.")
        projects = self.db.fetch_all("project")
        if not projects:
            self.logger.warning("No projects found in the database.")
        self.projects = projects

    def merge_scores_for_user(self, user_id):
        """Merge scores from all models for a given user."""
        self.logger.info(f"Merging scores for user {user_id}.")
        if not self.models:
            self.logger.warning("No models available for merging scores.")
            return {}
        # Value - [{"project_id": 123, "score": 0.95}, {"project_id": 456, "score": 0.88}]
        result = []
        for model in self.models:
            model_scores = model.calculate_scores(user_id)
            if not model_scores:
                self.logger.warning(f"No scores returned from model: {model.model_name} for user {user_id}.")
                continue
    
    def iterate_all_users(self):
        """Iterate through all users and merge scores for each."""
        self.logger.info("Iterating through all users to merge scores.")
        user_ids = self.db.get_student_ids()
        if not user_ids:
            self.logger.warning("No user IDs found in the database.")
            return
        
        for user_id in user_ids:
            self.merge_scores_for_user(user_id)
            self.logger.info(f"Merged scores for user {user_id}.")