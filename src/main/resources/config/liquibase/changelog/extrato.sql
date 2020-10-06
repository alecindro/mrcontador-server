--liquibase formatted sql  

--changeset clientes:11

CREATE TABLE ${schema}.extrato (
	id bigserial,
	ext_datalancamento date NULL,
	ext_historico varchar(90) NULL,
	ext_numerodocumento varchar(30) NULL,
	ext_numerocontrole varchar(30) NULL,
	ext_debito numeric(21,2) NULL,
	ext_credito numeric(21,2) NULL,
	ext_descricao varchar(30) NULL,
	info_adicional varchar(254) NULL,
	arquivo_id int8 NOT NULL,
	parceiro_id int8 NOT NULL,
	agenciabancaria_id int8 NOT NULL,
	processado boolean null, 
	CONSTRAINT extrato_pkey PRIMARY KEY (id)
);

ALTER TABLE ${schema}.extrato ALTER COLUMN processado SET DEFAULT FALSE;
ALTER TABLE ${schema}.extrato ADD CONSTRAINT fk_extrato_agenciabancaria_id FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id);
ALTER TABLE ${schema}.extrato ADD CONSTRAINT fk_extrato_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.extrato ADD CONSTRAINT fk_extrato_arquivo_id FOREIGN KEY (arquivo_id) REFERENCES ${schema}.arquivo(id);
CREATE UNIQUE INDEX extrato_ext_debito_idx ON ds_demo.extrato (ext_datalancamento,ext_historico,ext_numerodocumento,ext_debito,parceiro_id,agenciabancaria_id);
CREATE UNIQUE INDEX extrato_ext_credito_idx ON ds_demo.extrato (ext_datalancamento,ext_historico,ext_numerodocumento,ext_credito,parceiro_id,agenciabancaria_id);


