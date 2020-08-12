--liquibase formatted sql  

--changeset clientes:18
ALTER TABLE ds_demo.comprovante ALTER COLUMN arquivo_id DROP NOT NULL;

--changeset clientes:19
ALTER TABLE ds_demo.comprovante ALTER COLUMN com_beneficiario TYPE varchar(254) USING com_beneficiario::varchar;
ALTER TABLE ds_demo.comprovante ALTER COLUMN com_documento TYPE varchar(254) USING com_documento::varchar;
ALTER TABLE ds_demo.comprovante ALTER COLUMN com_observacao TYPE varchar(254) USING com_observacao::varchar;