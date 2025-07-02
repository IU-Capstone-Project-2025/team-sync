import math
class Metrics:
    def __init__(self, relevance_matrix, logger):
        self.relevance_matrix = relevance_matrix
        self.logger = logger

    def get_map(self):
        self.logger.info("Calculating Mean Average Precision (MAP)")
        total_ap = 0.0
        num_users = len(self.relevance_matrix)

        for user_relevance in self.relevance_matrix:
            ap = self._average_precision(user_relevance)
            total_ap += ap

        return total_ap / num_users if num_users > 0 else 0.0
    
    def get_ndcg(self):
        self.logger.info("Calculating Normalized Discounted Cumulative Gain (NDCG)")
        total_ndcg = 0.0
        num_users = len(self.relevance_matrix)

        for user_relevance in self.relevance_matrix:
            ndcg = self._ndcg(user_relevance)
            total_ndcg += ndcg

        return total_ndcg / num_users if num_users > 0 else 0.0

    def get_mrr(self):
        self.logger.info("Calculating Mean Reciprocal Rank (MRR)")
        total_mrr = 0.0
        num_users = len(self.relevance_matrix)

        for user_relevance in self.relevance_matrix:
            mrr = self._mean_reciprocal_rank(user_relevance)
            total_mrr += mrr

        return total_mrr / num_users if num_users > 0 else 0.0
    
    def _average_precision(self, relevance_scores):
        relevant_items = sum(relevance_scores)
        if relevant_items == 0:
            return 0.0
        
        precision_sum = 0.0
        relevant_count = 0
        
        for i, rel in enumerate(relevance_scores):
            if rel == 1:
                relevant_count += 1
                precision_at_k = relevant_count / (i + 1)
                precision_sum += precision_at_k
        
        return precision_sum / relevant_items
    
    def dcg(self, scores):
            return sum((2**rel - 1) / math.log2(i + 2) for i, rel in enumerate(scores))

    def _ndcg(self, relevance_scores):

        actual_dcg = self.dcg(relevance_scores)
        ideal_scores = sorted(relevance_scores, reverse=True)
        ideal_dcg = self.dcg(ideal_scores)

        return actual_dcg / ideal_dcg if ideal_dcg > 0 else 0.0
    
    def _mean_reciprocal_rank(self, relevance_scores):
        for i, rel in enumerate(relevance_scores):
            if rel == 1:
                return 1.0 / (i + 1)
        return 0.0
    
    def get_metrics(self):
        self.logger.info("Logging metrics:")
        map_value = self.get_map()
        self.logger.info(f"Mean Average Precision (MAP): {map_value}")
        ndcg_value = self.get_ndcg()
        self.logger.info(f"Normalized Discounted Cumulative Gain (NDCG): {ndcg_value}")
        mrr_value = self.get_mrr()
        self.logger.info(f"Mean Reciprocal Rank (MRR): {mrr_value}")
        return {
            "map": map_value,
            "ndcg": ndcg_value,
            "mrr": mrr_value
        }