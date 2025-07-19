ALTER TABLE security.security_user RENAME to security.user;

ALTER TABLE security.user ADD COLUMN profile_id INTEGER;

UPDATE security.user 
SET profile_id = student.id
FROM person, student
WHERE security.user.person_id = person.id
    and student.person_id = person.id;