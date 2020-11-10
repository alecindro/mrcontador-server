--liquibase formatted sql  


--changeset alecindro:28

ALTER TABLE ${schema}.parceiro ADD COLUMN despesa_juros bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN despesa_iof bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN juros_ativos bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN descontos_ativos bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN caixa_conta bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN despesas_bancarias bigint;  
ALTER TABLE ${schema}.parceiro ADD COLUMN despesa_tarifa bigint;
ALTER TABLE ${schema}.parceiro ADD COLUMN cadastro_status int;  

ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_despesa_juros_conta_id FOREIGN KEY (despesa_juros) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_despesa_iof_conta_id FOREIGN KEY (despesa_iof) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_despesa_juros_ativos_id FOREIGN KEY (juros_ativos) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_descontos_ativos_conta_id FOREIGN KEY (descontos_ativos) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_caixa_conta_conta_id FOREIGN KEY (caixa_conta) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_despesas_bancarias_conta_id FOREIGN KEY (despesas_bancarias) REFERENCES ${schema}.conta(id);
ALTER TABLE ${schema}.parceiro ADD CONSTRAINT fk_parceiro_despesa_tarifa_conta_id FOREIGN KEY (despesa_tarifa) REFERENCES ${schema}.conta(id);
