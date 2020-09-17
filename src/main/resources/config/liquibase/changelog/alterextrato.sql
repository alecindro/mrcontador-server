--liquibase formatted sql  

--changeset clientes:25

ALTER TABLE ${schema}.extrato ADD info_adicional varchar(254);

