--liquibase formatted sql  

--changeset clientes:18
ALTER TABLE ${schema}.comprovante ALTER COLUMN arquivo_id DROP NOT NULL;

--changeset clientes:19
ALTER TABLE ${schema}.comprovante ALTER COLUMN com_beneficiario TYPE varchar(254) USING com_beneficiario::varchar;
ALTER TABLE ${schema}.comprovante ALTER COLUMN com_documento TYPE varchar(254) USING com_documento::varchar;
ALTER TABLE ${schema}.comprovante ALTER COLUMN com_observacao TYPE varchar(254) USING com_observacao::varchar;

--changeset clientes:20
CREATE UNIQUE INDEX comprovante_unique_idx ON ${schema}.comprovante (com_beneficiario,com_documento,com_datapagamento,com_valorpagamento,parceiro_id,agenciabancaria_id);