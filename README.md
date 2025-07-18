<p align="center">
<img src="https://raw.githubusercontent.com/IU-Capstone-Project-2025/team-sync/refs/heads/main/docs/img/main.png" width="600"/>
</p>
## Overview

**TeamSync** aims to improve the quality and balance of student teams by offering a data-driven, user-friendly experience for discovering, evaluating, and matching with potential teammates.


### Deployment
Production: https://team-sync.online  
Development: https://dev.team-sync.online

# Access 
Default ports and sample endpoints
| **Service** | **Swagger or url** |
| --- | --- |
| frontend | https://team-sync.online or https://dev.team-sync.online |
| backend-projects | https://team-sync.online/projects/api/v1/swagger-ui/index.html or https://dev.team-sync.online/projects/api/v1/swagger-ui/index.html |
| backend-resume | https://team-sync.online/resume/api/v1/swagger-ui/index.html or https://dev.team-sync.online/resume/api/v1/swagger-ui/index.html |
| backend-auth | https://team-sync.online/auth/api/v1/swagger-ui/index.html or https://dev.team-sync.online/auth/api/v1/swagger-ui/index.html |
| ml-recsys |  https://team-sync.online/recsys/api/v1/docs or https://dev.team-sync.online/recsys/api/v1/docs |
| ml-embedder |  https://team-sync.online/embedder/api/v1/docs or https://dev.team-sync.online/embedder/api/v1/docs |

It's planned to use the _api_ and _api.dev_ subdomain for backend and ml services

## Tech Stack

---

|  | **Choice** | **Justification** |
| --- | --- | --- |
| **Frontend** | React | For building dynamic and high-performance user interfaces and server-side rendering capabilities. |
|  | TypeScript | A strongly typed programming language that builds on JavaScript, providing better tooling at any scale. |
|  | Tailwind CSS | A utility-first CSS framework for rapid UI development with consistent and responsive design. |
| **Backend** | Java/Spring Boot | Robust REST APIs, team expertise |
| **Database** | PostgreSQL | ACID compliance for user/project data |
|  | Redis | Fast session/matching-cache access |
| **ML** | Python  | For its simplicity and extensive libraries for data processing and machine learning. |
|  | Airflow | Pipeline orchestration |
| **Infra** | Docker + Docker Compose | Environment consistency, TA reproducibility |
|  | GitHub Actions (CI/CD) | Automated testing/deployment |


##  Team

---

| Team member | Telegram alias | Innopolis Email | Responcibilities |
| --- | --- | --- | --- |
| Diana MInnakhmetova (Lead) | @diana_minn | d.minnakhmetova@innopolis.university | Product management, design, Report writing |
| Danis Sharafiev | @HarneMer | d.sharafiev@innopolis.university | ML + MLOps |
| Daria Alexandrova | @ae_quor | d.alexandrova@innopolis.university | Frontend |
| Stepan Dementev | @dementevssstepan | s.dementev@innopolis.university | Backend + DevOps |
| Elizaveta Zagurskih | @wkwthigo | e.zagurskih@innopolis.university | Backend |
| Kamilya Shakirova | @hugecatwithacheburek | k.shakirova@innopolis.university | ML |

## Third-party Models
This project uses the `all-MiniLM-L6-v2` model from UKPLab:
- Model card: https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2
- License: Apache 2.0

