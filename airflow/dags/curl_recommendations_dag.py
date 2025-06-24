from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime

default_args = {
    "start_date": datetime(2024, 6, 24),  # Обновите дату
}

with DAG(
    dag_id="curl_recommendations",
    default_args=default_args,
    schedule="* * * * *",
    catchup=False,
    is_paused_upon_creation=False,  # DAG будет активен сразу
) as dag:
    curl_task = BashOperator(
        task_id="post_recommendations",
        bash_command=(
            "curl -X 'POST' 'http://ml-recsys:8000/api/v1/airflow/recommendations' "
            "-H 'accept: application/json' -d ''"
        ),
    )