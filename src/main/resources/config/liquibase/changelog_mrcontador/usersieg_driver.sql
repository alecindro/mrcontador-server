--liquibase formatted sql  
--changeset mrcontador:12
INSERT INTO ds_mrcontador.jhi_user (id,login,password_hash,first_name,last_name,email,image_url,activated,lang_key,activation_key,reset_key,funcao,datasource,created_by,created_date,reset_date,last_modified_by,last_modified_date) VALUES 
(8,'integradorDriver','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','IntegradorDriver','Driver','integradorDriver@localhost.com',NULL,true,'pt-br',NULL,NULL,NULL,'ds_mrcontador','system',NULL,NULL,'system',NULL)
;
INSERT INTO ds_mrcontador.jhi_user (id,login,password_hash,first_name,last_name,email,image_url,activated,lang_key,activation_key,reset_key,funcao,datasource,created_by,created_date,reset_date,last_modified_by,last_modified_date) VALUES 
(9,'integradorSeg','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','IntegradorSeg','Seg','integradorSeg@localhost.com',NULL,true,'pt-br',NULL,NULL,NULL,'ds_mrcontador','system',NULL,NULL,'system',NULL)
;
INSERT INTO ds_mrcontador.jhi_user_authority (user_id,authority_name) VALUES (8,'ds_mrcontador');
INSERT INTO ds_mrcontador.jhi_user_authority (user_id,authority_name) VALUES (8,'ROLE_USER');
INSERT INTO ds_mrcontador.jhi_user_authority (user_id,authority_name) VALUES (9,'ds_mrcontador');
INSERT INTO ds_mrcontador.jhi_user_authority (user_id,authority_name) VALUES (9,'ROLE_USER');
