--liquibase formatted sql  
--changeset alecindro:10

---ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN id_agencia bigint NULL;
---ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN valido boolean;
ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN content_type varchar(20);