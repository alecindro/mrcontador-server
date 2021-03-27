--liquibase formatted sql  
--changeset clientes:57
CREATE TABLE ${schema}.integracao (
	id bigserial,
	integrador varchar(255) NULL,
	tipo_integracao varchar(255) NULL,
	data_inicio date NULL,
	data_cadastro date NULL,
	habilitado boolean NOT NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT integracao_pkey PRIMARY KEY (id)
);
ALTER TABLE ${schema}.integracao ADD CONSTRAINT fk_integracao_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.integracao alter column habilitado set default false;