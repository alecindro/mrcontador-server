--liquibase formatted sql  


--changeset alecindro:53
DROP INDEX IF EXISTS ${schema}.conta_con_conta_idx;
ALTER TABLE ${schema}.conta ADD CONSTRAINT conta_unique UNIQUE (con_conta, parceiro_id)
