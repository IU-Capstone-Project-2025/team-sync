CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

ALTER TABLE project
ADD COLUMN course_id INTEGER;

INSERT INTO course (name)
SELECT DISTINCT course_name
FROM project
WHERE course_name IS NOT NULL;

UPDATE project p
SET course_id = c.id
FROM course c
WHERE p.course_name = c.name;

ALTER TABLE project
DROP COLUMN course_name;

ALTER TABLE project
ADD CONSTRAINT fk_project_course
FOREIGN KEY (course_id)
REFERENCES course(id)
ON DELETE SET NULL;