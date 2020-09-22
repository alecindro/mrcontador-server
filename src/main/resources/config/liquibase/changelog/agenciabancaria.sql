--liquibase formatted sql  


--changeset clientes:10

CREATE TABLE ${schema}.agenciabancaria (
	id bigserial,
	age_numero varchar(20) NULL,
	age_digito varchar(20) NULL,
	age_agencia varchar(6) NULL,
	age_descricao varchar(30) NULL,
	age_situacao bool NULL,
	ban_codigobancario varchar(5) NOT NULL,
	con_conta int4 NULL,
	banco_id int8 NULL,
	parceiro_id int8 NOT NULL,
	CONSTRAINT agenciabancaria_pkey PRIMARY KEY (id)
);
ALTER TABLE ${schema}.agenciabancaria ADD CONSTRAINT fk_agenciabancaria_parceiro_id FOREIGN KEY (parceiro_id) REFERENCES ${schema}.parceiro(id);
ALTER TABLE ${schema}.agenciabancaria ADD CONSTRAINT fk_agenciabancaria_banco_id FOREIGN KEY (banco_id) REFERENCES ${schema}.banco(id);
CREATE INDEX fki_agenciabancaria_codigobancario ON ${schema}.agenciabancaria USING btree (ban_codigobancario);