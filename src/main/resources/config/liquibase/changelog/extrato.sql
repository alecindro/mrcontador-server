--liquibase formatted sql  

--changeset clientes:11

CREATE TABLE ${schema}.extrato (
	id bigserial,
	ext_datalancamento timestamp NULL,
	ext_historico varchar(90) NULL,
	ext_numerodocumento varchar(30) NULL,
	ext_numerocontrole varchar(30) NULL,
	ext_debito numeric(21,2) NULL,
	ext_credito numeric(21,2) NULL,
	ext_descricao varchar(30) NULL,
	parceiro_id int8 NOT NULL,
	agenciabancaria_id int8 NOT NULL,
	CONSTRAINT extrato_pkey PRIMARY KEY (id)
);

ALTER TABLE ${schema}.extrato ADD CONSTRAINT fk_extrato_agenciabancaria_id FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id);
ALTER TABLE ${schema}.extrato ADD CONSTRAINT fk_extrato_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);