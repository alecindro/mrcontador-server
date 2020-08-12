--liquibase formatted sql  

--changeset clientes:18
ALTER TABLE ds_demo.comprovante ALTER COLUMN arquivo_id DROP NOT NULL;