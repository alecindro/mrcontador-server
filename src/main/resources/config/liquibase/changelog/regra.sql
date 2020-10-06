--liquibase formatted sql  

--changeset clientes:12

CREATE TABLE ${schema}.regra (
	id bigserial,
	reg_descricao varchar(60) NULL,
	reg_historico varchar(60) NULL,
	reg_todos varchar(1) NULL,
	data_cadastro timestamp NULL,
	parceiro_id int8 NOT NULL,
	conta_id int8 NOT NULL,
	aplicacao bool not null,
	tipo_regra varchar(20) NULL,
	CONSTRAINT regra_pkey PRIMARY KEY (id)
);

ALTER TABLE ${schema}.regra ADD CONSTRAINT fk_regra_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.regra ADD CONSTRAINT fk_regra_conta_id FOREIGN KEY (conta_id) REFERENCES ${schema}.conta(id);