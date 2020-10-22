DO $function$
DECLARE
REC   RECORD; 
vRETORNO NUMERIC;
BEGIN
  vRETORNO:= 0;
FOR REC IN 
SELECT C.ID AS COMPROVANTE_ID, C.COM_DATAPAGAMENTO, C.COM_DATAVENCIMENTO, C.COM_JUROS,C.COM_DESCONTO,C.COM_MULTA, C.COM_VALORPAGAMENTO, C.COM_VALORDOCUMENTO, NF.NOT_NUMERO, NF.NOT_PARCELA, I.ID AS INTELIGENT_ID,
i.historicofinal 
    FROM ds_demo.INTELIGENT I INNER JOIN
    ds_demo.COMPROVANTE C ON I.COMPROVANTE_ID = C.ID
    INNER JOIN ds_demo.NOTAFISCAL NF ON C.COM_CNPJ = NF.NOT_CNPJ
    WHERE I.ASSOCIADO = FALSE 
    AND C.COM_VALORDOCUMENTO = NF.NOT_VALORPARCELA
    AND C.COM_DATAVENCIMENTO = NF.NOT_DATAPARCELA
    AND C.TIPO_COMPROVANTE = 'TITULO'
    AND NF.ID = 2

 LOOP
	vRETORNO:= vRETORNO + 1;
	raise notice 'TESTE %, %', REC.INTELIGENT_ID, REC.COMPROVANTE_ID; 
 END LOOP; 

END;
$function$;
 

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

