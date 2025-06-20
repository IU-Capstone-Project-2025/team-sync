import psycopg2
from config.config import Config
from config.logging import setup_logging

class DBModel:
    def __init__(self):
        self.db_url = Config.DB_URL
        self.logger = setup_logging()
        if not self.db_url:
            raise ValueError("Database URL is not set in the configuration.")
        self.connection = None

    def connect(self):
        try:
            self.connection = psycopg2.connect(self.db_url)
        except psycopg2.Error as e:
            self.logger.error(f"Error connecting to the database: {e}")
            self.connection = None

    def get_all_skills(self):
        """Returns list of tuples with all skills in the database."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("SELECT * FROM skill")
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching skills: {e}")
            return []

    def get_all_students(self):
        """Returns list of tuples with all students in the database."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("SELECT * FROM student")
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching students: {e}")
            return []

    def get_all_projects(self):
        """Returns list of tuples with all projects in the database."""
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("SELECT * FROM project")
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching projects: {e}")
            return []
    
    def get_students_skills(self, student_id):
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("SELECT * FROM skill WHERE student_id = %s", (student_id,))
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching skills for student {student_id}: {e}")
            return []

    def get_projects_skills(self, project_id):
        if not self.connection:
            raise ConnectionError("Database connection is not established.")
        
        try:
            with self.connection.cursor() as cursor:
                cursor.execute("SELECT * FROM skill WHERE project_id = %s", (project_id,))
                return cursor.fetchall()
        except psycopg2.Error as e:
            self.logger.error(f"Error fetching skills for project {project_id}: {e}")
            return []

    def disconnect(self):
        if self.connection:
            self.connection.close()
    