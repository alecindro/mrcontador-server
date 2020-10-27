--liquibase formatted sql  


--changeset alecindro:25

ALTER TABLE ${schema}.agenciabancaria ALTER COLUMN ban_codigobancario DROP NOT NULL;
ALTER TABLE ${schema}.agenciabancaria ALTER COLUMN conta_id DROP NOT NULL;
ALTER TABLE ${schema}.agenciabancaria ADD COLUMN tipo_agencia varchar(255) NULL;
ALTER TABLE ${schema}.agenciabancaria ADD COLUMN possueAplicacao boolean;
