--liquibase formatted sql  

--changeset clientes:8

CREATE TABLE ${schema}.notaservico (
	id bigserial,
	nse_numero int4 NULL,
	nse_descricao varchar(50) NULL,
	nse_cnpj varchar(20) NULL,
	nse_empresa varchar(60) NULL,
	nse_datasaida DATE NULL,
	nse_valornota numeric(21,2) NULL,
	nse_dataparcela DATE NULL,
	nse_valorparcela numeric(21,2) NULL,
	tno_codigo int4 NULL,
	nse_parcela varchar(10) NULL,
	parceiro_id int8 NOT NULL,
	processado boolean null, 
	CONSTRAINT notaservico_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_notaservico_nse_cnpj ON ${schema}.notaservico USING btree (nse_cnpj);
ALTER TABLE ${schema}.notaservico ALTER COLUMN processado SET DEFAULT FALSE;
ALTER TABLE ${schema}.notaservico ADD CONSTRAINT fk_notaservico_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
