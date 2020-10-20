-- se existe
select c.com_datapagamento, c.com_datavencimento, c.com_juros,c.com_desconto,c.multa, c.com_valorpagamento, c.com_valordocumento, nf.not_numero, nf.not_parcela, nf.not_empresa 
from inteligent i inner join
comprovante c on i.comprovante_id = c.id
inner join notafiscal nf on c.com_cnpj = nf.not_cnpj
where i.associado = false 
and c.com_valordocumento = nf.not_valorparcela
and c.com_datavencimento = nf.not_dataparcela
and c.com_juros = 0
and c.com_desconto = 0
and c.multa = 0
and c.tipo_comprovante = 'TITULO'
and nf.id = ?;
-- vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA; 
-- update inteligent set notafiscal_id
-- update notafiscal set processado = true

--senão
select c.com_datapagamento, c.com_datavencimento, c.com_juros,c.com_desconto,c.multa, c.com_valorpagamento, c.com_valordocumento, nf.not_numero, nf.not_parcela, nf.not_empresa 
from inteligent i inner join
comprovante c on i.comprovante_id = c.id
inner join notafiscal nf on c.com_cnpj = nf.not_cnpj
where i.associado = false 
and c.com_valordocumento = nf.not_valorparcela
and c.com_datavencimento = nf.not_dataparcela
and (c.com_juros > 0 or c.com_desconto > 0 or c.multa > 0)
and c.tipo_comprovante = 'TITULO'
and nf.id = ?;
-- vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA; 
-- update inteligent set notafiscal_id
--se juros
-- vHISTORICOFINAL   := 'Pagto. de Juros sobre '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA; 
-- insert inteligent set notafiscal_id
-- se multa 
-- vHISTORICOFINAL   := 'Pagto. de Multa sobre '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA; 
-- insert inteligent set notafiscal_id
-- desconto ?
-- vHISTORICOFINAL   := 'Desconto sobre '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA; 
-- insert inteligent set notafiscal_id
-- update notafiscal set processado = true

