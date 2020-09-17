--liquibase formatted sql  


--changeset clientes:1
CREATE TABLE ${schema}.jhi_persistent_audit_event (
	event_id bigserial NOT NULL,
	principal varchar(50) NOT NULL,
	event_date timestamp NULL,
	event_type varchar(255) NULL,
	CONSTRAINT jhi_persistent_audit_event_pkey PRIMARY KEY (event_id)
);
CREATE INDEX idx_persistent_audit_event ON ${schema}.jhi_persistent_audit_event USING btree (principal, event_date);

--changeset clientes:2
CREATE TABLE ${schema}.jhi_persistent_audit_evt_data (
	event_id bigint NOT NULL,
	"name" varchar(150) NOT NULL,
	value varchar(255) NULL,
	CONSTRAINT jhi_persistent_audit_evt_data_pkey PRIMARY KEY (event_id, name)
);
CREATE INDEX idx_persistent_audit_evt_data ON ${schema}.jhi_persistent_audit_evt_data USING btree (event_id);

ALTER TABLE ${schema}.jhi_persistent_audit_evt_data ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id) REFERENCES 
${schema}.jhi_persistent_audit_event(event_id);
