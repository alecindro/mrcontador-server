--liquibase formatted sql  

--changeset clientes:63

CREATE TABLE ${schema}.permissao_parceiro (
	id bigserial NOT NULL,
	data_cadastro timestamp NOT NULL,
	usuario varchar(255) NOT NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT permissao_parceiro_pk PRIMARY KEY (id),
	CONSTRAINT permissao_parceiro_fk FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id)
);