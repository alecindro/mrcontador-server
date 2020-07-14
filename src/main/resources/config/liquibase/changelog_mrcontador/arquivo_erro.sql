--liquibase formatted sql  

--changeset mrcontador:7
CREATE TABLE ds_mrcontador.arquivo_erro (
	id bigserial NOT NULL,
	nome varchar(255) NOT NULL,
	nome_original varchar(255) NULL,
	data_cadastro timestamp NOT NULL,
	tipo_arquivo varchar(100) NULL,
	s3_url varchar(255) NULL,
	s3_dir varchar(255) NULL,
	tamanho bigint NULL,
	usuario varchar(255) NULL,
	contador varchar(255) not NULL,	
	CONSTRAINT arquivo_pk PRIMARY KEY (id)
);
