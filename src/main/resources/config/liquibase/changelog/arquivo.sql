--liquibase formatted sql  

--changeset clientes:9

CREATE TABLE ${schema}.arquivo (
	id bigserial NOT NULL,
	nome varchar(255) NOT NULL,
	nome_original varchar(255) NULL,
	data_cadastro timestamp NOT NULL,
	tipo_arquivo varchar(100) NULL,
	tipo_doc varchar(100) NOT NULL,
	s3_url varchar(255) NULL,
	s3_dir varchar(255) NULL,
	tamanho bigint NULL,
	etag varchar(255) NULL,
	usuario varchar(255) NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT arquivo_pk PRIMARY KEY (id),
	CONSTRAINT arquivo_parceiro_fk FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id)
);
