--liquibase formatted sql  
--changeset alecindro:9

ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN tipo_documento varchar(255) NULL;
ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN parceiro_id bigint NULL;
ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN parceiro varchar(255);
ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN contador_id bigint NULL;
ALTER TABLE ds_mrcontador.arquivo_erro ADD COLUMN processado boolean;
ALTER TABLE ds_mrcontador.arquivo_erro ADD CONSTRAINT fk_arquivo_erro_contador FOREIGN KEY (contador_id) REFERENCES ds_mrcontador.contador(id);


