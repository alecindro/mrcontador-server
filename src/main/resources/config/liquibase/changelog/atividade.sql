--liquibase formatted sql  

--changeset clientes:13

CREATE TABLE ${schema}.atividade (
	id bigserial,
	descricao varchar(255) NULL,
	code varchar(60) NULL,
	tipo varchar(20) NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT atividade_pkey PRIMARY KEY (id)
);
ALTER TABLE ${schema}.atividade ADD CONSTRAINT fk_atividade_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);