--liquibase formatted sql  
-- changeset alecindro: 30
INSERT INTO ${schema}.banco (ban_descricao,ban_sigla,ban_codigobancario,ban_ispb) VALUES
('Banco Safra S.A','Safra','422',58160789);