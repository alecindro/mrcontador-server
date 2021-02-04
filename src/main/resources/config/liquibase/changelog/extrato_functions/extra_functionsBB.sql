CREATE OR REPLACE FUNCTION ${schema}.extrabb_function("pEXTRATOID" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pEXTRATOID ALIAS FOR $1;  
  vRETORNO numeric;
  vPARCEIROID numeric;
  vCONTAID numeric;
  vCONTADESPBANC numeric;
  vHISTORICOFINAL TEXT;

BEGIN 
  vRETORNO:= 0; 
   -- despesas juros
	  select i.parceiro_id, p.despesa_juros INTO vPARCEIROID, vCONTAID  from ${schema}.inteligent i inner join ${schema}.parceiro p 
	  on i.parceiro_id = p.id inner join ${schema}.extrato e on i.extrato_id = e.id
	  where e.id = pEXTRATOID and e.ext_debito is not null and  p.despesa_juros is not null and (e.ext_numerocontrole = '130120'  or e.ext_numerodocumento = '511057727');
IF (vCONTAID IS NOT NULL) then
	vHISTORICOFINAL = 'Débito ref. Juros Bancários - Banco do Brasil';
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAID WHERE extrato_id = pEXTRATOID;
	vRETORNO:= 1;
 END IF;
 if (vRETORNO = 0) then
 -- despesas_bancarias
	 select i.parceiro_id, p.despesas_bancarias INTO vPARCEIROID, vCONTADESPBANC  from ${schema}.inteligent i inner join ${schema}.parceiro p 
	  on i.parceiro_id = p.id inner join ${schema}.extrato e on i.extrato_id = e.id
	  where e.id = pEXTRATOID and e.ext_debito is not null and  p.despesas_bancarias is not null and e.ext_numerocontrole = '13113';
	  IF (vCONTADESPBANC IS NOT NULL) THEN
		vHISTORICOFINAL = 'Débito ref. Tarifas Bancárias - Banco do Brasil';
		UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTADESPBANC WHERE extrato_id = pEXTRATOID;
		vRETORNO:= -1;
	 END IF;
 end if;
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
