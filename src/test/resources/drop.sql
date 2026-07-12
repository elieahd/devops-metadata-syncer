DROP TABLE IF EXISTS project_report;
DROP SEQUENCE IF EXISTS project_report_id_seq;

DROP TABLE IF EXISTS project_contact;
DROP SEQUENCE IF EXISTS project_contact_id_seq;

DROP TABLE IF EXISTS pull_request_review;
DROP SEQUENCE IF EXISTS pull_request_review_id_seq;

DROP TABLE IF EXISTS pull_request;
DROP SEQUENCE IF EXISTS pull_request_id_seq;

DROP TABLE IF EXISTS pipeline_run;
DROP SEQUENCE IF EXISTS pipeline_run_id_seq;

DROP TABLE IF EXISTS pipeline;
DROP SEQUENCE IF EXISTS pipeline_id_seq;

DROP TABLE IF EXISTS vulnerability;
DROP SEQUENCE IF EXISTS vulnerability_id_seq;

DROP TABLE IF EXISTS release;
DROP SEQUENCE IF EXISTS release_id_seq;

DROP TABLE IF EXISTS repository;
DROP SEQUENCE IF EXISTS repository_id_seq;

DROP TABLE IF EXISTS project;
DROP SEQUENCE IF EXISTS project_id_seq;

DROP TABLE IF EXISTS organization;
DROP SEQUENCE IF EXISTS organization_id_seq;

DROP TABLE IF EXISTS databasechangelog;
DROP TABLE IF EXISTS databasechangeloglock;