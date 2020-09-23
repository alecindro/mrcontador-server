--liquibase formatted sql  


--changeset clientes:17

CREATE SEQUENCE ${schema}.seq_aguardando
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 5125
  CACHE 1;
ALTER TABLE ${schema}.seq_aguardando
  OWNER TO postgres;

CREATE TABLE ${schema}.aguardando
(
  agu_sequencia integer NOT NULL,
  agu_codigo integer,
  agu_cnpj character varying(20),
  agu_beneficiario character varying(60),
  agu_datavencimento timestamp with time zone,
  agu_valordocumento double precision,
  agu_observacao character varying(80),
  par_codigo integer,
  age_codigo integer,
  CONSTRAINT pk_aguardando PRIMARY KEY (agu_sequencia)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ${schema}.aguardando
  OWNER TO postgres;
--------------------------------------------------------------------------------
CREATE SEQUENCE ${schema}.seq_fornecedor
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 5876
  CACHE 1;
ALTER TABLE ${schema}.seq_fornecedor
  OWNER TO postgres;

CREATE TABLE ${schema}.fornecedor
(
  for_sequencia integer NOT NULL,
  for_codigo integer NOT NULL,
  for_cnpj character varying(20),
  for_parceiro character varying(60),
  for_contacontabil integer,
  for_valorboleto double precision,
  par_codigo integer,
  age_codigo integer,
  CONSTRAINT pk_fornecedor PRIMARY KEY (for_sequencia, for_codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ${schema}.fornecedor
  OWNER TO postgres;
