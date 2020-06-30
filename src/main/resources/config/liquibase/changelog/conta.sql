--liquibase formatted sql  

--changeset clientes:7


CREATE TABLE ${schema}.conta (
	id bigserial,
	con_conta int4 NULL,
	con_classificacao varchar(20) NULL,
	con_tipo varchar(1) NULL,
	con_descricao varchar(60) NULL,
	con_cnpj varchar(18) NULL,
	con_grau int4 NULL,
	parceiro_id int8 NULL,
	CONSTRAINT conta_pkey PRIMARY KEY (id)
);

ALTER TABLE ${schema}.conta ADD CONSTRAINT fk_conta_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);