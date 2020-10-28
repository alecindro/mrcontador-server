--liquibase formatted sql  


--changeset alecindro:27

ALTER TABLE ${schema}.extrato ADD COLUMN agencia_origem varchar(30);

