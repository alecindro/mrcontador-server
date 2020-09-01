--liquibase formatted sql  

--changeset clientes:22

ALTER TABLE ${schema}.agenciabancaria ADD con_conta int4 NULL;