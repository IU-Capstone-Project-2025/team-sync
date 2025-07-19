
ALTER TABLE security.security_user ADD COLUMN profile_id INTEGER;

UPDATE security.security_user
SET profile_id = student.id
FROM person, student
WHERE security.security_user.internal_user_id = person.id
    and student.person_id = person.id;