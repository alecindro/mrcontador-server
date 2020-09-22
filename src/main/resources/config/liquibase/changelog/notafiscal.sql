--liquibase formatted sql  

--changeset clientes:15

CREATE TABLE ${schema}.notafiscal (
	id bigserial,
	not_numero varchar(255) NULL,
	not_descricao varchar(255) NULL,
	not_cnpj varchar(18) NULL,
	not_empresa varchar(60) NULL,
	not_datasaida DATE NULL,
	not_valornota numeric(21,2) NULL,
	not_dataparcela DATE NULL,
	not_valorparcela numeric(21,2) NULL,
	tno_codigo int4 NULL,
	not_parcela varchar(10) NULL,
	parceiro_id int8 NOT NULL,
	arquivo_id int8 NULL,
	arquivopdf_id int8 NULL,
	CONSTRAINT notafiscal_pkey PRIMARY KEY (id)
);

CREATE INDEX fki_notaparceiro ON ${schema}.notafiscal USING btree (parceiro_id);
CREATE UNIQUE INDEX notafiscal_not_numero_idx ON ${schema}.notafiscal (not_numero,not_cnpj,not_dataparcela,parceiro_id);

ALTER TABLE ${schema}.notafiscal ADD CONSTRAINT fk_notafiscal_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.notafiscal ADD CONSTRAINT fk_notafiscal_arquivo_id FOREIGN KEY (arquivo_id) REFERENCES ${schema}.arquivo(id);
ALTER TABLE ${schema}.notafiscal ADD CONSTRAINT fk_notafiscal_arquivopdf_id FOREIGN KEY (arquivopdf_id) REFERENCES ${schema}.arquivo(id);