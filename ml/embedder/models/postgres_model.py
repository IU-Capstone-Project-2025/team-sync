import psycopg2
from psycopg2 import sql
from config.config import Config

class PostgresModel:
    def __init__(self):
        self.config = Config()
        self.connection = psycopg2.connect(
            host=self.config.POSTGRES_HOST,
            port=self.config.POSTGRES_PORT,
            user=self.config.POSTGRES_USER,
            password=self.config.POSTGRES_PASSWORD,
            dbname=self.config.POSTGRES_DBNAME
        )
        self.cursor = self.connection.cursor()

    def fetch_description(self, table, id):
        query = sql.SQL("SELECT description FROM {table} WHERE id = %s").format(
            table=sql.Identifier(table)
        )
        self.cursor.execute(query, (id,))
        result = self.cursor.fetchone()
        return result[0] if result else None
