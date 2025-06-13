CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    surname VARCHAR(32) NOT NULL,
    tg_alias VARCHAR(64) UNIQUE NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE study_group (
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    study_group_id INTEGER NOT NULL REFERENCES study_group(id),
    description TEXT,
    github_alias VARCHAR(32) NOT NULL,
    resume_path TEXT,
    person_id INTEGER NOT NULL REFERENCES person(id)
);

CREATE TABLE professor (
    person_id INTEGER NOT NULL REFERENCES person(id),
    id SERIAL PRIMARY KEY
);

CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    course_name VARCHAR(32) NOT NULL,
    team_lead_id INTEGER NOT NULL REFERENCES person(id),
    description TEXT,
    project_link TEXT,
    status VARCHAR(16) NOT NULL
);

CREATE TABLE project_member (
    project_id INTEGER REFERENCES project(id),
    member_id INTEGER REFERENCES person(id)
);

CREATE TABLE application (
    student_id INTEGER REFERENCES student(id),
    project_id INTEGER REFERENCES project(id),
    status VARCHAR(16) NOT NULL, 
    created_at TIMESTAMP
);

CREATE TABLE student_favourite_project (
    student_id INTEGER REFERENCES student(id),
    project_id INTEGER REFERENCES project(id),
    created_at TIMESTAMP
);

CREATE TABLE tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE student_tag (
    student_id INTEGER REFERENCES student(id),
    tag_id INTEGER REFERENCES tag(id)
);

CREATE TABLE project_tag (
    project_id INTEGER REFERENCES project(id),
    tag_id INTEGER REFERENCES project(id)
);
