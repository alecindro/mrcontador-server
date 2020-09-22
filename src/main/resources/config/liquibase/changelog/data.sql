--liquibase formatted sql  

--changeset clientes:5

INSERT INTO ${schema}.banco (ban_descricao,ban_sigla,ban_codigobancario,ban_ispb) VALUES 
('Banco Do Brasil S.A','BB','001',0)
,('Bradesco S.A','Bradesco','0237',60746948)
,('Itaú Unibanco S.A','Itau','341',60701190)
,('Caixa Econômica Federal','CEF','0104',360305)
,('Banco Santander Brasil S.A','Santander','033',90400888)
,('Banco Cooperativo Do Brasil','Sicoob','756',2038232)
,('Unicred Cooperativa','Unicred','136',315557)
,('Sicredi S.A','Sicredi','748',1181521)
,('Unicred Central RS','Unicred','091',1634601)
,('Coop Central Ailos','CredCrea','085',5463212)
;


