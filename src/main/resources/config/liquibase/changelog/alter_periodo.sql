--liquibase formatted sql  


--changeset alecindro:29

ALTER TABLE ${schema}.extrato ADD COLUMN periodo varchar(6) null;
ALTER TABLE ${schema}.comprovante ADD COLUMN periodo varchar(6) null;
ALTER TABLE ${schema}.notafiscal ADD COLUMN periodo varchar(6) null;