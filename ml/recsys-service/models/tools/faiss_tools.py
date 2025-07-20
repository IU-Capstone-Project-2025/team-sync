import faiss
import numpy as np


def get_faiss_recommendations(index_type, num_data, project_ids, projects_with_data_v, user_data_v):
    if index_type == "euclidean distance":
        index = faiss.IndexFlatL2(num_data)
    else:
        index = faiss.IndexFlatIP(num_data)
    index.add(projects_with_data_v)
    distances, indices = index.search(user_data_v, len(project_ids))
    distances, indices = distances[0], indices[0]
    
    min_dist, max_dist = np.min(distances), np.max(distances)
    if max_dist - min_dist == 0:
        normalized_scores = 1 - (max_dist - min_dist)
    elif index_type == "euclidean distance":
        normalized_scores = 1 - (distances - min_dist) / (max_dist - min_dist)
    else:
        normalized_scores = (distances - min_dist) / (max_dist - min_dist)

    recommendations = []
    for i, score in zip(indices, normalized_scores):
        recommendations.append((project_ids[i], score))
    recommendations = [i[1] for i in sorted(recommendations, key=lambda x: x[0])]
    return recommendations