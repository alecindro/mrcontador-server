--liquibase formatted sql  


--changeset clientes:6

CREATE TABLE ${schema}.comprovante (
	id bigserial,
	com_cnpj varchar(18) NULL,
	com_beneficiario varchar(254) NULL,
	com_documento varchar(254) NULL,
	com_datavencimento date NULL,
	com_datapagamento date NULL,
	com_valordocumento numeric(21,2) NULL,
	com_valorpagamento numeric(21,2) NULL,
	com_juros numeric(21,2) NULL,
	com_desconto numeric(21,2) NULL,
	com_observacao varchar(254) NULL,
	com_competencia varchar(254) NULL,
	com_codigorecolhimento varchar(254) NULL,
	agenciabancaria_id int8 NOT NULL,
	parceiro_id int8 NOT NULL,
	 arquivo_id int8 NULL,
	 
	CONSTRAINT comprovante_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_comprovante_com_cnpj ON ${schema}.comprovante USING btree (com_cnpj);
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_arquivo_id FOREIGN KEY (arquivo_id) REFERENCES ${schema}.arquivo(id);
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_agenciabancaria_id FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id);
CREATE UNIQUE INDEX comprovante_unique_idx ON ${schema}.comprovante (com_beneficiario,com_documento,com_datapagamento,com_valorpagamento,parceiro_id,agenciabancaria_id);
