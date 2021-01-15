--liquibase formatted sql  


--changeset alecindro:51

ALTER TABLE ${schema}.regra ALTER COLUMN reg_descricao TYPE varchar(254);
ALTER TABLE ${schema}.regra ALTER COLUMN reg_historico TYPE varchar(254);