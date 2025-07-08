ALTER TABLE project
ADD COLUMN required_members_count INTEGER NOT NULL DEFAULT 0;

UPDATE project
SET required_members_count = floor(random() * 8 + 2)::int;