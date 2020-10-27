--liquibase formatted sql  


--changeset alecindro:22

ALTER TABLE ${schema}.agenciabancaria ALTER COLUMN ban_codigobancario DROP NOT NULL;
ALTER TABLE ${schema}.agenciabancaria ALTER COLUMN conta_id DROP NOT NULL;
ALTER TABLE ${schema}.agenciabancaria ALTER COLUMN conta_id DROP NOT NULL;
ALTER TABLE ${schema}.agenciabancaria ADD COLUMN tipo_agencia varchar(255) NULL;

