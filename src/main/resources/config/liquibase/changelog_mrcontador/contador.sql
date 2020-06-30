--liquibase formatted sql  

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
	pessoa_fisica bool NOT NULL,
	logradouro varchar(255) NULL,
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