--liquibase formatted sql  


--changeset clientes:6

CREATE TABLE ${schema}.comprovante (
	id bigserial,
	par_codigo int4 NULL,
	age_codigo int4 NULL,
	com_cnpj varchar(18) NULL,
	com_beneficiario varchar(60) NULL,
	com_documento varchar(25) NULL,
	com_datavencimento timestamp NULL,
	com_datapagamento timestamp NULL,
	com_valordocumento numeric(21,2) NULL,
	com_valorpagamento numeric(21,2) NULL,
	com_observacao varchar(90) NULL,
	CONSTRAINT comprovante_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_comprovante_com_cnpj ON ${schema}.comprovante USING btree (com_cnpj);
