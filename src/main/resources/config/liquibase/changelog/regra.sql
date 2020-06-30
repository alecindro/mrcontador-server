--liquibase formatted sql  

--changeset clientes:12

CREATE TABLE ${schema}.regra (
	id bigserial,
	par_codigo int4 NULL,
	reg_descricao varchar(60) NULL,
	reg_conta int4 NULL,
	reg_historico varchar(60) NULL,
	reg_todos varchar(1) NULL,
	CONSTRAINT regra_pkey PRIMARY KEY (id)
);