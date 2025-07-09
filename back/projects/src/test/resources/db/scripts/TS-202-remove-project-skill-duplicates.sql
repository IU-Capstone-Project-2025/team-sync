-- project_skill
WITH ranked_duplicates AS (
  SELECT 
    ctid, 
    ROW_NUMBER() OVER (PARTITION BY project_id, skill_id ORDER BY ctid) AS rn
  FROM project_skill
)
DELETE FROM project_skill
WHERE ctid IN (
  SELECT ctid FROM ranked_duplicates WHERE rn > 1
);

ALTER TABLE project_skill
ADD CONSTRAINT pk_project_skill PRIMARY KEY (project_id, skill_id);


-- student_role
WITH ranked_duplicates AS (
  SELECT 
    ctid, 
    ROW_NUMBER() OVER (PARTITION BY student_id, role_id ORDER BY ctid) AS rn
  FROM student_role
)
DELETE FROM student_role
WHERE ctid IN (
  SELECT ctid FROM ranked_duplicates WHERE rn > 1
);

ALTER TABLE student_role
ADD CONSTRAINT pk_student_role PRIMARY KEY (student_id, role_id);


-- student_skill
WITH ranked_duplicates AS (
  SELECT 
    ctid, 
    ROW_NUMBER() OVER (PARTITION BY student_id, skill_id ORDER BY ctid) AS rn
  FROM student_skill
)
DELETE FROM student_skill
WHERE ctid IN (
  SELECT ctid FROM ranked_duplicates WHERE rn > 1
);

ALTER TABLE student_skill
ADD CONSTRAINT pk_student_skill PRIMARY KEY (student_id, skill_id);


-- project_role
WITH ranked_duplicates AS (
  SELECT 
    ctid, 
    ROW_NUMBER() OVER (PARTITION BY project_id, role_id ORDER BY ctid) AS rn
  FROM project_role
)
DELETE FROM project_role
WHERE ctid IN (
  SELECT ctid FROM ranked_duplicates WHERE rn > 1
);

ALTER TABLE project_role
ADD CONSTRAINT pk_project_role PRIMARY KEY (project_id, role_id);


-- project_member
WITH ranked_duplicates AS (
  SELECT 
    ctid, 
    ROW_NUMBER() OVER (PARTITION BY project_id, member_id ORDER BY ctid) AS rn
  FROM project_member
)
DELETE FROM project_member
WHERE ctid IN (
  SELECT ctid FROM ranked_duplicates WHERE rn > 1
);

ALTER TABLE project_member
ADD CONSTRAINT pk_project_member PRIMARY KEY (project_id, member_id);