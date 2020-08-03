--liquibase formatted sql  

--changeset clientes:16

CREATE UNIQUE INDEX notafiscal_not_numero_idx ON ds_demo.notafiscal (not_numero,not_cnpj,not_dataparcela,parceiro_id);

--changeset clientes:17

ALTER TABLE ds_demo.notafiscal ALTER COLUMN not_descricao TYPE varchar(255) USING not_descricao::varchar;