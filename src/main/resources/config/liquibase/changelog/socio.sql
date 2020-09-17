--liquibase formatted sql  

--changeset clientes:14

CREATE TABLE ${schema}.socio (
	id bigserial,
	descricao varchar(255) NULL,
	nome varchar(60) NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT socio_pkey PRIMARY KEY (id)
);
ALTER TABLE ${schema}.socio ADD CONSTRAINT fk_socio_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);