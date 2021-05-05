--liquibase formatted sql  

--changeset alecindro:61
ALTER TABLE ${schema}.conta ALTER COLUMN con_descricao TYPE varchar(255);
