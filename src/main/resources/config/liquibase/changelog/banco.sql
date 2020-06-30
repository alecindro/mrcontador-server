--liquibase formatted sql  


--changeset clientes:4

CREATE TABLE ${schema}.banco (
	id serial,
	ban_descricao varchar(200) NULL,
	ban_sigla varchar(100) NULL,
	ban_codigobancario int4 NULL,
	ban_ispb int4 NULL,	
	CONSTRAINT banco_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_banco_ban_ispb ON ${schema}.banco USING btree (ban_ispb);
CREATE INDEX fki_banco_ban_descricao ON ${schema}.banco USING btree (ban_descricao);
CREATE INDEX fki_banco_ban_codigobancario ON ${schema}.banco USING btree (ban_codigobancario);

