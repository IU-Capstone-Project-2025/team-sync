CREATE TABLE student_project_click (
    student_id INT NOT NULL REFERENCES student(id),
    project_id INT NOT NULL REFERENCES project(id),
    click_count INT NOT NULL DEFAULT 1,
    PRIMARY KEY (student_id, project_id)
);