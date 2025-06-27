
## Overview


**TeamSync** aims to improve the quality and balance of student teams by offering a data-driven, user-friendly experience for discovering, evaluating, and matching with potential teammates.


### Quick start

```bash
# Clone repository
git clone https://github.com/IU-Capstone-Project-2025/team-sync
cd team-sync

# Build and start containers via docker-compose (try to turn on/off VPN, if you can't download some dependencies)
docker-compose up --build
```

# Access 
Default ports and sample endpoints
| **Service** | **Port** | **Sample endpoint** | **localhost curl** |
| --- | --- | --- | --- |
| frontent | 80 | `/` | `curl 'http://localhost'` or `curl 'http://localhost:80'` |                              
| backend-projects | 80 | `/projects/api/v1/swagger-ui/index.html` | `curl '/projects/api/v1/swagger-ui/index.html'` |
| backend-auth | 80 | `/auth/api/v1/swagger-ui/index.html` | `curl '/auth/api/v1/swagger-ui/index.html'` |
| backend-resume | - | - | - |
| backend-recommendation | 8083 | only gRPC | only gRPC |
| ml-search | 8001 | `/api/searh` | `curl 'http://localhost:8001/api/search'` |
| ml-recsys | 8000 | `/api/recsys` | `curl 'http://localhost:8000/api/recsys'` |

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
