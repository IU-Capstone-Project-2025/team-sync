ALTER TABLE student_favourite_project
DROP CONSTRAINT IF EXISTS student_favourite_project_student_id_fkey;

ALTER TABLE student_favourite_project
ADD CONSTRAINT student_favourite_project_person_id_fkey
FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE;