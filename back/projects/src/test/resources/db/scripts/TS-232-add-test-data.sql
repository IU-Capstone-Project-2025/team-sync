INSERT INTO application (person_id, project_id, status, created_at)
SELECT person_id, 10, 'OPEN', NOW()
FROM student
WHERE id = 1;

INSERT INTO student_project_click (student_id, project_id)
VALUES (1, 1);

INSERT INTO student_favourite_project (person_id, project_id, created_at)
SELECT person_id, 5, NOW()
FROM student
WHERE id = 1;