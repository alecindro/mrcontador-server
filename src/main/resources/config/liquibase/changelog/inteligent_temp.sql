--liquibase formatted sql  


--changeset clientes:17

CREATE SEQUENCE ${schema}.seq_aguardando
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 5125
	CACHE 1
	NO CYCLE;



CREATE TABLE ${schema}.aguardando (
	agu_sequencia int4 NOT NULL,
	agu_codigo int4 NULL,
	agu_cnpj varchar(20) NULL,
	agu_beneficiario varchar(60) NULL,
	agu_datavencimento timestamptz NULL,
	agu_valordocumento float8 NULL,
	agu_observacao varchar(80) NULL,
	par_codigo int4 NULL,
	age_codigo int4 NULL,
	CONSTRAINT pk_aguardando PRIMARY KEY (agu_sequencia)
);
--------------------------------------------------------------------------------
CREATE SEQUENCE ${schema}.seq_fornecedor
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 5876
	CACHE 1
	NO CYCLE;

CREATE TABLE ${schema}.fornecedor (
	for_sequencia int4 NOT NULL,
	for_codigo int4 NOT NULL,
	for_cnpj varchar(20) NULL,
	for_parceiro varchar(60) NULL,
	for_contacontabil int4 NULL,
	for_valorboleto float8 NULL,
	par_codigo int4 NULL,
	age_codigo int4 NULL,
	CONSTRAINT pk_fornecedor PRIMARY KEY (for_sequencia, for_codigo)
);
