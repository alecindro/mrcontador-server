--liquibase formatted sql 
 
--changeset alecindro:30:insertSQLChangeType

INSERT INTO ${schema}.banco (ban_descricao,ban_sigla,ban_codigobancario,ban_ispb) VALUES
('Banco Safra S.A','Safra','422',58160789);
INSERT INTO ${schema}.banco (ban_descricao,ban_sigla,ban_codigobancario,ban_ispb) VALUES
('Banco Inter S.A.','Inter','077',416968);
