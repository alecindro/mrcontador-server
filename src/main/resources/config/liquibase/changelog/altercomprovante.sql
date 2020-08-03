--liquibase formatted sql  

--changeset clientes:15

ALTER TABLE ${schema}.comprovante ADD parceiro_id int8 NOT NULL;
ALTER TABLE ${schema}.comprovante ADD arquivo_id int8 NOT NULL;
ALTER TABLE ${schema}.comprovante ADD agenciabancaria_id int8 NOT NULL;
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_arquivo_id FOREIGN KEY (arquivo_id) REFERENCES ${schema}.arquivo(id);
ALTER TABLE ${schema}.comprovante ADD CONSTRAINT fk_comprovante_agenciabancaria_id FOREIGN KEY (agenciabancaria_id) REFERENCES ${schema}.agenciabancaria(id);