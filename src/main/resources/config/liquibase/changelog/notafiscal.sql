--liquibase formatted sql  

--changeset clientes:9

CREATE TABLE ${schema}.notafiscal (
	id bigserial,
	not_numero int4 NULL,
	not_descricao varchar(50) NULL,
	not_cnpj varchar(18) NULL,
	not_empresa varchar(60) NULL,
	not_datasaida timestamp NULL,
	not_valornota numeric(21,2) NULL,
	not_dataparcela timestamp NULL,
	not_valorparcela numeric(21,2) NULL,
	tno_codigo int4 NULL,
	not_parcela varchar(10) NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT notafiscal_pkey PRIMARY KEY (id)
);

CREATE INDEX fki_notaparceiro ON ${schema}.notafiscal USING btree (parceiro_id);

ALTER TABLE ${schema}.notafiscal ADD CONSTRAINT fk_notafiscal_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);