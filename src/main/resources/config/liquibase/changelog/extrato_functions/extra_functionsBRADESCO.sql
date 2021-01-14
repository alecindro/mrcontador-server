CREATE OR REPLACE FUNCTION ${schema}.extraBradesco_function("pEXTRATOID" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pEXTRATOID ALIAS FOR $1;  
  vRETORNO numeric;
  vPARCEIROID numeric;
  vCONTAID numeric;
  vHISTORICOFINAL TEXT;

BEGIN 
  vRETORNO:= 0;
  -- despesas bancarias
  select i.parceiro_id, p.despesas_bancarias INTO vPARCEIROID, vCONTAID  from ${schema}.inteligent i inner join ${schema}.parceiro p 
  on i.parceiro_id = p.id inner join ${schema}.extrato e on i.extrato_id = e.id
  where e.id = pEXTRATOID and e.ext_debito is not null and  p.despesas_bancarias is not null and (e.ext_numerodocumento = '11119'  or e.ext_numerodocumento = '21219' or e.ext_numerodocumento = '20120' 
  or e.ext_numerodocumento = '30220' or e.ext_numerodocumento = '20320' or e.ext_numerodocumento = '10420');
IF (vPARCEIROID IS NOT NULL) THEN
	vHISTORICOFINAL = 'Débito ref. Tarifas  Bancárias - Bradesco';
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAID WHERE extrato_id = pEXTRATOID;
	vRETORNO:= 1;
 END IF;  	          
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
