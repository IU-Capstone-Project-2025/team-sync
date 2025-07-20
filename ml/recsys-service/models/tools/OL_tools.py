import numpy as np


def get_OL_recommendations(user_data, projects_with_data):
    recommendations = []
    for project_data in projects_with_data:
        recommendations.append(len(np.intersect1d(user_data, project_data)) / min(len(user_data), len(project_data)))
    return recommendations