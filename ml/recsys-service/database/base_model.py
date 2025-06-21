import psycopg2
from config.config import Config
from config.logging import setup_logging
import asyncio

class DBModel:
    def __init__(self):
        self.db_url = Config.DB_URL
        self.logger = setup_logging()
        if not self.db_url:
            raise ValueError("Database URL is not set in the configuration.")
        self.connection = None

    async def connect(self):
        max_retries = 10
        while max_retries > 0 and not self.connection:
            try:
                self.connection = psycopg2.connect(self.db_url)
            except psycopg2.Error as e:
                self.logger.error(f"Error connecting to the database: {e}, retrying...")
                max_retries -= 1
                if max_retries == 0:
                    raise ConnectionError("Failed to connect to the database after multiple attempts.")
                await asyncio.sleep(2)

    def fetch_all(self, table_name):
        """Generic method to fetch all rows from a given table."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                query = psycopg2.sql.SQL("SELECT * FROM {}").format(psycopg2.sql.Identifier(table_name))
                cursor.execute(query)
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching data from table {table_name}: {e}")
            return []
        
    def get_all_skills(self):
        """Returns list of tuples with all skills in the database."""
        return self.fetch_all("skill")
    
    def get_all_students(self):
        """Returns list of tuples with all students in the database."""
        return self.fetch_all("student")
    
    def get_all_projects(self):
        """Returns list of tuples with all projects in the database."""
        return self.fetch_all("project")

    async def disconnect(self):
        if self.connection:
            self.connection.close()
    