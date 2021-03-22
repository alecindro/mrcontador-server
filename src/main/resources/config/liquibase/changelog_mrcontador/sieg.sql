--liquibase formatted sql  
--changeset mrcontador:11
CREATE TABLE ds_mrcontador.seg (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	api_key varchar(255) NOT NULL,
	active bool NOT NULL,
	contador_id bigint,
	CONSTRAINT seg_pkey PRIMARY KEY (id),
	CONSTRAINT ux_seg_email UNIQUE (email)
);
ALTER TABLE ds_mrcontador.seg ADD CONSTRAINT fk_seg_contador FOREIGN KEY (contador_id) REFERENCES ds_mrcontador.contador(id);
