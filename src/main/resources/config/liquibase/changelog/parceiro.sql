--liquibase formatted sql  

--changeset clientes:3

CREATE TABLE ${schema}.parceiro (
	id bigserial,
	par_descricao varchar(50) NULL,
	par_razaosocial varchar(70) NULL,
	par_tipopessoa char(1) NULL,
	par_cnpjcpf varchar(20) NOT NULL,
	par_rgie varchar(20) NULL,
	par_obs varchar(200) NULL,
	par_datacadastro timestamp NULL,
	spa_codigo int4 NULL,
	logradouro varchar(255) NULL,
	cep varchar(8) null,
	cidade varchar(255) NULL,
	estado varchar(255) NULL,
	area_atuacao varchar(255) NULL,
	numero varchar(30) NULL,
    bairro varchar(255) NULL,
    porte varchar(255) NULL,
    abertura varchar(255) NULL,
    natureza_juridica varchar(255) NULL,
    ultima_atualizacao varchar(25) NULL,
    status varchar(4) NULL,
    tipo varchar(30) NULL,
    complemento varchar(255) NULL,
    email varchar(255) NULL,
    telefone varchar(255) NULL,
    data_situacao varchar(25),
    efr varchar(255) NULL,
    motivo_situacao varchar(255) NULL,
	situacao_especial varchar(255) NULL,
	data_situacao_especial varchar(25) NULL,
    capital_social varchar(255) NULL,
    outras varchar(255) NULL,
    enabled bool NOT NULL DEFAULT true,	
	CONSTRAINT parceiro_pkey PRIMARY KEY (id)
);
CREATE INDEX fki_parceiro_razao ON ${schema}.parceiro USING btree (par_razaosocial);
CREATE INDEX fki_parceiro_cnpj ON ${schema}.parceiro USING btree (par_cnpjcpf);