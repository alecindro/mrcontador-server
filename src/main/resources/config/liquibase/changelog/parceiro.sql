--liquibase formatted sql  

--changeset clientes:3

CREATE TABLE ${schema}.parceiro (
	id bigserial,
	par_descricao varchar(50) NULL,
	par_razaosocial varchar(70) NULL,
	par_tipopessoa char(1) NULL,
	par_cnpjcpf varchar(20) NULL,
	par_rgie varchar(20) NULL,
	par_obs varchar(200) NULL,
	par_datacadastro timestamp NULL,
	spa_codigo int4 NULL,
	logradouro varchar(255) NULL,
	cep varchar(8) null,
	cidade varchar(255) NULL,
	estado varchar(255) NULL,
	area_atuacao varchar(255) NULL,
	comercio boolean NULL,
	nfc_e boolean NULL,
    danfe boolean NULL,
    servico boolean NULL,
    nfs_e boolean NULL,
    transportadora boolean NULL,
    conhec_transporte boolean NULL,
    industria boolean NULL,
    ct boolean NULL,
    outras varchar(255),	
	CONSTRAINT parceiro_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_parceiro_razao ON ${schema}.parceiro USING btree (par_razaosocial);
CREATE INDEX fki_parceiro_cnpj ON ${schema}.parceiro USING btree (par_cnpjcpf);