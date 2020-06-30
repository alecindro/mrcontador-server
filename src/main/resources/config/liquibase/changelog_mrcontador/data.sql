--liquibase formatted sql  

--changeset mrcontador:8
insert into ${schema}.jhi_authority values('ROLE_ADMIN'),('ROLE_USER'),('ds_mrcontador'),('ds_demo');
insert into ${schema}.jhi_user(login,password_hash,first_name,last_name,email,image_url,activated,lang_key,created_by,last_modified_by, datasource)
values('system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost',null,true,'pt-br','system','system','ds_mrcontador'),
('admin@localhost.com','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost.com',null,true,'pt-br','system','system','ds_mrcontador'),
('demo@localhost.com','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Demo','Demo','demo@localhost.com',null,true,'pt-br','system','system','ds_demo');
insert into ${schema}.jhi_user_authority values(2,'ROLE_ADMIN'),(2,'ROLE_USER'),(2,'ds_mrcontador'),(3,'ROLE_ADMIN'),(3,'ds_demo'),(3,'ROLE_USER');
insert into ${schema}.sistema values('Dominio Sistemas');

