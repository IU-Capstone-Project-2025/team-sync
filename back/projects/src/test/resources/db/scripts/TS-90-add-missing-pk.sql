ALTER TABLE student_favourite_project
ADD COLUMN id SERIAL;

ALTER TABLE student_favourite_project
ADD CONSTRAINT student_favourite_project_pkey PRIMARY KEY (id);