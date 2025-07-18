<p align="center">
<img src="https://raw.githubusercontent.com/IU-Capstone-Project-2025/team-sync/refs/heads/main/docs/img/main.png" width="600"/>
</p>

# Overview

**TeamSync** aims to improve the quality and balance of student teams by offering a data-driven, user-friendly experience for discovering, evaluating, and matching with potential teammates.

<p align="center">
<img src="https://raw.githubusercontent.com/IU-Capstone-Project-2025/team-sync/refs/heads/main/docs/gif/registration.gif" width="600"/>
</p>

## This project helps people find teams by interests.

**TeamSync** is a collaborative platform where students can create projects, apply to join other teams, add interesting projects to their favorites, and edit their profiles. The recommendation system uses your profile and activity to suggest the most relevant projects and teammates. You can review applications, select who joins your team, and build effective teams based on interests and skills.

<p align="center">
<img src="https://raw.githubusercontent.com/IU-Capstone-Project-2025/team-sync/refs/heads/main/docs/gif/project_highlights.gif" width="600"/>
</p>

# Access 

The table below lists the default ports and example endpoints for each service in the platform. You can use these URLs to access the web interface, API documentation, or Swagger UI for each component in both production and development environments.

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
|  | KeyDB (uses redis client) | Fast session/matching-cache access |
|  | Qdrant | Embeddings storage for ml service calculations |
| **ML** | Python  | For its simplicity and extensive libraries for data processing and machine learning. |
| **Infra** | Docker + Docker Compose | Environment consistency, TA reproducibility |
|  | GitHub Actions (CI/CD) | Automated testing/deployment |


## Project Structure

---

```
team-sync/
├── back/         # Backend microservices (auth, projects, recommendation, resume)
├── front/        # Frontend (React app)
├── ml/           # Machine learning services (embedder, recsys, search)
├── migration/    # Database migration scripts (Liquibase)
├── docs/         # Docs (images, gifs, md)
├── docker-compose.yml  # Docker Compose configs
├── README.md     # Project documentation
└── ...           # Other configuration and documentation files
```

##  Team

---

| Team member | Telegram alias | Innopolis Email | Responcibilities |
| --- | --- | --- | --- |
| Diana Minnakhmetova (Lead) | @diana_minn | d.minnakhmetova@innopolis.university | Product management, design, Report writing |
| Danis Sharafiev | @HarneMer | d.sharafiev@innopolis.university | ML + MLOps |
| Daria Alexandrova | @ae_quor | d.alexandrova@innopolis.university | Frontend |
| Stepan Dementev | @dementevssstepan | s.dementev@innopolis.university | Backend + DevOps |
| Elizaveta Zagurskih | @wkwthigo | e.zagurskih@innopolis.university | Backend |
| Kamilya Shakirova | @hugecatwithacheburek | k.shakirova@innopolis.university | ML |

### Deployment
Production: https://team-sync.online  
Development: https://dev.team-sync.online

## Third-party Models
This project uses the `all-MiniLM-L6-v2` model from UKPLab:
- Model card: https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2
- License: Apache 2.0

