--liquibase formatted sql  


--changeset alecindro:64
ALTER TABLE ${schema}.exportacao ADD COLUMN agenciabancaria_id bigint;
ALTER TABLE ${schema}.exportacao ADD CONSTRAINT fk_exportacao_agenciabancaria_id FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id);
