import numpy as np


def get_IOU_recommendations(user_data, projects_with_data):
    recommendations = []
    for project_data in projects_with_data:
        recommendations.append(len(np.intersect1d(user_data, project_data)) / len(np.union1d(user_data, project_data)))
    return recommendations