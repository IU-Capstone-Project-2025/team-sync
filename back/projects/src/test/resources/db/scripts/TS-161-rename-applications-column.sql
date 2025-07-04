ALTER TABLE application RENAME COLUMN student_id TO person_id;

ALTER TABLE stuapplicationdents
ADD CONSTRAINT fk_application_person
FOREIGN KEY (person_id) REFERENCES persons(id);
