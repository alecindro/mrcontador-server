--liquibase formatted sql  

--changeset mrcontador:13
insert into ${schema}.jhi_authority values('ROLE_MANAGER');