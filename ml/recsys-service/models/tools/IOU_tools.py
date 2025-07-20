import numpy as np


def get_IOU_recommendations(user_data, projects_with_data):
    recommendations = []
    for project_data in projects_with_data:
        temp = len(np.union1d(user_data, project_data))
        if temp != 0:
            recommendations.append(len(np.intersect1d(user_data, project_data)) / temp)
        else:
            recommendations.append(0)
    return recommendations