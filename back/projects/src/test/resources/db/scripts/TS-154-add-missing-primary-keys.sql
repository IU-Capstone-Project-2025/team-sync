ALTER TABLE application
ADD COLUMN id SERIAL;

ALTER TABLE application
ADD CONSTRAINT application_pkey PRIMARY KEY (id);