CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    surname VARCHAR(32) NOT NULL,
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
    person_id INTEGER NOT NULL REFERENCES person(id),
    tg_alias VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE professor (
    person_id INTEGER NOT NULL REFERENCES person(id),
    id SERIAL PRIMARY KEY,
    tg_alias VARCHAR(64) UNIQUE
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

CREATE TABLE skill (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE student_skill (
    student_id INTEGER REFERENCES student(id),
    skill_id INTEGER REFERENCES skill(id)
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) Unique NOT NULL,
    description TEXT
);

CREATE TABLE project_role (
    project_id INTEGER REFERENCES project(id),
    role_id INTEGER REFERENCES role(id)
);

CREATE TABLE student_role (
    student_id INTEGER REFERENCES student(id),
    role_id INTEGER REFERENCES skill(id)
);

CREATE TABLE project_skill (
    project_id INTEGER REFERENCES project(id),
    skill_id INTEGER REFERENCES project(id)
);