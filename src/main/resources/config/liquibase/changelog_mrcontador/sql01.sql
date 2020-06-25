--liquibase formatted sql  

--changeset mrcontador:1
CREATE TABLE ${schema}.jhi_persistent_audit_event (
	event_id bigserial NOT NULL,
	principal varchar(50) NOT NULL,
	event_date timestamp NULL,
	event_type varchar(255) NULL,
	CONSTRAINT jhi_persistent_audit_event_pkey PRIMARY KEY (event_id)
);
CREATE INDEX idx_persistent_audit_event ON ${schema}.jhi_persistent_audit_event USING btree (principal, event_date);

--changeset mrcontador:2
CREATE TABLE ${schema}.jhi_persistent_audit_evt_data (
	event_id bigint NOT NULL,
	"name" varchar(150) NOT NULL,
	value varchar(255) NULL,
	CONSTRAINT jhi_persistent_audit_evt_data_pkey PRIMARY KEY (event_id, name)
);
CREATE INDEX idx_persistent_audit_evt_data ON ${schema}.jhi_persistent_audit_evt_data USING btree (event_id);

ALTER TABLE ${schema}.jhi_persistent_audit_evt_data ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id) REFERENCES ${schema}.jhi_persistent_audit_event(event_id);

--changeset mrcontador:3
CREATE TABLE ${schema}.jhi_authority (
	"name" varchar(50) NOT NULL,
	CONSTRAINT jhi_authority_pkey PRIMARY KEY (name)
);
--changeset mrcontador:4
CREATE TABLE ${schema}.jhi_user (
	id bigserial NOT NULL,
	login varchar(50) NOT NULL,
	password_hash varchar(60) NOT NULL,
	first_name varchar(50) NULL,
	last_name varchar(50) NULL,
	email varchar(191) NULL,
	image_url varchar(256) NULL,
	activated bool NOT NULL,
	lang_key varchar(10) NULL,
	activation_key varchar(20) NULL,
	reset_key varchar(20) NULL,
    datasource varchar(254) NULL,
    created_by varchar(50) NOT NULL,
	created_date timestamp NULL,
	reset_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	CONSTRAINT jhi_user_pkey PRIMARY KEY (id),
	CONSTRAINT ux_user_email UNIQUE (email),
	CONSTRAINT ux_user_login UNIQUE (login)
);

--changeset mrcontador:5
CREATE TABLE ${schema}.jhi_user_authority (
	user_id bigint NOT NULL,
	authority_name varchar(50) NOT NULL,
	CONSTRAINT jhi_user_authority_pkey PRIMARY KEY (user_id, authority_name)
);

ALTER TABLE ${schema}.jhi_user_authority ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES ${schema}.jhi_authority(name);
ALTER TABLE ${schema}.jhi_user_authority ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES ${schema}.jhi_user(id);

--changeset mrcontador:6
CREATE TABLE ds_mrcontador.contador (
	id bigserial NOT NULL,
	razao varchar(255) NOT NULL,
	fantasia varchar(255) NULL,
	telefones varchar(255) NULL,
	datasource varchar(255) NOT NULL,
	cnpj varchar(18) NOT NULL,
	cidade varchar(255) NULL,
	estado varchar(255) NULL,
	cep varchar(255) NULL,
	email varchar(255) NOT NULL,
	crc varchar(255) NOT NULL,
	sistema varchar(255) NOT NULL,
	CONSTRAINT contador_pkey PRIMARY KEY (id),
	CONSTRAINT ux_contador_crc UNIQUE (crc),
	CONSTRAINT ux_contador_email UNIQUE (email),
	CONSTRAINT ux_contador_cnpj UNIQUE (cnpj)
);

--changeset mrcontador:7
CREATE TABLE ${schema}.sistema (
	"name" varchar(254) NOT NULL,
	CONSTRAINT sistema_pkey PRIMARY KEY (name)
);

--changeset mrcontador:8
insert into ${schema}.jhi_authority values('ROLE_ADMIN'),('ROLE_USER');
insert into ${schema}.jhi_user(login,password_hash,first_name,last_name,email,image_url,activated,lang_key,created_by,last_modified_by)
values('system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost',null,true,'pt-br','system','system')
,('admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost',null,true,'pt-br','system','system');
insert into ${schema}.jhi_user_authority values(2,'ROLE_ADMIN'),(2,'ROLE_USER');
insert into ${schema}.sistema values('Dominio Sistemas');

