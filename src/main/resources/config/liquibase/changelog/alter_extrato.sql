--liquibase formatted sql  

--changeset clientes:18

ALTER TABLE ${schema}.regra ADD tipo_regra varchar(20) NULL;

