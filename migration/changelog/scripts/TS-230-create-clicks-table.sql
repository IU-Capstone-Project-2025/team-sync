CREATE TABLE user_project_click (
    user_id INT NOT NULL REFERENCES student(id),
    project_id INT NOT NULL REFERENCES project(id),
    click_count INT NOT NULL DEFAULT 1,
    PRIMARY KEY (user_id, project_id)
);