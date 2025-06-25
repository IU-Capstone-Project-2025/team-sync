import psycopg2
from psycopg2 import sql
from config.config import Config
from config.logging import setup_logging
import asyncio

class DBModel:
    def __init__(self, logger):
        self.db_url = Config.DB_URL
        self.logger = logger
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
                query = sql.SQL("SELECT * FROM {}").format(sql.Identifier(table_name))
                cursor.execute(query)
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching data from table {table_name}: {e}")
            return []
        
    def fetch_skills(self, table_name, id):
        """Returns list of tuples with all skills for a given user."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                if table_name == "student_skill":
                    query = sql.SQL("SELECT skill_id FROM {} WHERE student_id = %s").format(
                        sql.Identifier(table_name)
                    )
                else:
                    query = sql.SQL("SELECT skill_id FROM {} WHERE project_id = %s").format(
                        sql.Identifier(table_name)
                    )
                cursor.execute(query, (id,))

                return [i[0] for i in cursor.fetchall()]
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching skills for user {id}: {e}")
            return []

    def fetch_ids(self, table_name):
        """Fetches all IDs from a given table."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                query = sql.SQL("SELECT id FROM {}").format(sql.Identifier(table_name))
                cursor.execute(query)
                return [row[0] for row in cursor.fetchall()]
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching IDs from table {table_name}: {e}")
            return []

    def fetch_description(self, table, id):
        """Fetches the description for a given project."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                query = sql.SQL("SELECT description FROM {} WHERE id = %s").format(
                    sql.Identifier(table)
                )
                cursor.execute(query, (id,))
                result = cursor.fetchone()
                return result[0] if result else None
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching description for {table} with ID {id}: {e}")
            return None

    def get_user_description(self, user_id):
        """Fetches the description for a given user."""
        return self.fetch_description("student", user_id)

    def get_project_description(self, project_id):
        """Fetches the description for a given project."""
        return self.fetch_description("project", project_id)

    def get_user_skills(self, user_id):  # [1, 2, 78]
        """Returns list with all skills for a given user."""
        return self.fetch_skills("student_skill", user_id)

    def get_project_skills(self, project_id):  # [4, 64, 65]
        """Returns list with all skills for a given project."""
        return self.fetch_skills("project_skill", project_id)

    def get_all_skills(self):
        """Returns list of tuples with all skills in the database."""
        return self.fetch_all("skill")
    
    def get_all_students(self):
        """Returns list of tuples with all students in the database."""
        return self.fetch_all("student")
    
    def get_all_projects(self):
        """Returns list of tuples with all projects in the database."""
        return self.fetch_all("project")

    def get_project_ids(self):
        """Fetches all project IDs from the database."""
        return self.fetch_ids("project")
    
    def get_student_ids(self):
        """Fetches all student IDs from the database."""
        return self.fetch_ids("student")

    async def disconnect(self):
        if self.connection:
            self.connection.close()
            self.connection = None
        self.logger.info("Database connection closed.")
    