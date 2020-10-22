--liquibase formatted sql  


--changeset clientes:15

CREATE TABLE ${schema}.inteligent (
	id bigserial,
	historico varchar(254) NULL,
	historicofinal varchar(254) NULL,
	datalancamento date NULL,
	associado bool NULL,
	numerodocumento varchar(254) NULL,
	numerocontrole varchar(254) NULL,
	periodo varchar(6) NULL,
	debito numeric(21,2) NULL,
	credito numeric(21,2) NULL,
	datainicio date NULL,
	datafim date NULL,
	cnpj varchar(20) NULL,
	beneficiario varchar(254) NULL,
	tipo_inteligent varchar(254) NULL,
	tipo_valor varchar(254) NULL,
	comprovante_id int8 NULL,
	notafiscal_id int8 NULL,
	notaservico_id int8 NULL,
	parceiro_id int8 NULL,
	agenciabancaria_id int8 NULL,
	extrato_id int8 NULL,
	conta_id int8 NULL,
	regra_id int8 NULL,
	CONSTRAINT inteligent_pkey PRIMARY KEY (id),
	CONSTRAINT inteligent_agenciabancaria FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id),
	CONSTRAINT inteligent_comprovante FOREIGN KEY (comprovante_id) REFERENCES ${schema}.comprovante(id),
	CONSTRAINT inteligent_extrato FOREIGN KEY (extrato_id) REFERENCES ${schema}.extrato(id),
	CONSTRAINT inteligent_notafiscal FOREIGN KEY (notafiscal_id) REFERENCES ${schema}.notafiscal(id),
	CONSTRAINT inteligent_notaservico FOREIGN KEY (notaservico_id) REFERENCES ${schema}.notaservico(id),
	CONSTRAINT inteligent_parceiro FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id),
	CONSTRAINT inteligent_conta FOREIGN KEY (conta_id) REFERENCES ${schema}.conta(id),
	CONSTRAINT inteligent_regra FOREIGN KEY (regra_id) REFERENCES ${schema}.regra(id)
);
CREATE INDEX fki_inteligent_associado ON ${schema}.inteligent USING btree (associado);