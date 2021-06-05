--liquibase formatted sql  

--changeset clientes:62

CREATE TABLE ${schema}.exportacao (
	id bigserial NOT NULL,
	data_cadastro timestamp NOT NULL,
	periodo varchar(6) not null,
	usuario varchar(255) NOT NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT exportacao_pk PRIMARY KEY (id),
	CONSTRAINT exportacao_parceiro_fk FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id)
);