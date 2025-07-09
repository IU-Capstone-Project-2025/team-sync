ALTER TABLE application RENAME COLUMN student_id TO person_id;

ALTER TABLE application
ADD CONSTRAINT fk_application_person
FOREIGN KEY (person_id) REFERENCES person(id);

ALTER TABLE application
DROP CONSTRAINT application_student_id_fkey;