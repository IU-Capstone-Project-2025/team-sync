import psycopg2
from psycopg2 import sql
from config.config import Config

class PostgresModel:
    def __init__(self, host: str = Config().POSTGRES_HOST,
                 port: int = Config().POSTGRES_PORT,
                 user: str = Config().POSTGRES_USER, 
                 password: str = Config().POSTGRES_PASSWORD, 
                 dbname: str = Config().POSTGRES_DBNAME):
        self.connection = psycopg2.connect(
            host=host,
            port=port,
            user=user,
            password=password,
            dbname=dbname
        )
        self.cursor = self.connection.cursor()

    def fetch_description(self, table, id):
        query = sql.SQL("SELECT description FROM {table} WHERE id = %s").format(
            table=sql.Identifier(table)
        )
        self.cursor.execute(query, (id,))
        result = self.cursor.fetchone()
        return result[0] if result else None
