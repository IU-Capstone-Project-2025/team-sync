CREATE TABLE internal_external_user_id(
    internal_user_id INTEGER,
    external_user_id VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (internal_user_id, external_user_id)
)