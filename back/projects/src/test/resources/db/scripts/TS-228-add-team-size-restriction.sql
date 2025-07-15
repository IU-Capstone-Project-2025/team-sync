ALTER TABLE project
ADD CONSTRAINT chk_required_members_count_range
CHECK (required_members_count >= 0);
