--liquibase formatted sql  

--changeset clientes:25

ALTER TABLE ${schema}.extrato ADD info_adicional varchar(254);

--changeset clientes:26
CREATE UNIQUE INDEX extrato_unique_idx ON ${schema}.extrato (ext_datalancamento,ext_numerodocumento,parceiro_id,agenciabancaria_id);

