--liquibase formatted sql  
--changeset clientes:54
DROP INDEX ${schema}.notafiscal_not_numero_idx;
CREATE UNIQUE INDEX notafiscal_not_numero_idx ON ${schema}.notafiscal (not_numero,not_cnpj,not_dataparcela,parceiro_id, not_parcela);
