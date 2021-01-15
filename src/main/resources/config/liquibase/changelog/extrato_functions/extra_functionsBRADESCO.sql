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
  where e.id = pEXTRATOID and e.ext_debito is not null and  p.despesas_bancarias is not null and e.ext_numerodocumento in('11119' ,'21219','20120','30220','20320','10420', '40520','10620','10720','200720','30820','19820','10920','11020','31120','11220');
IF (vPARCEIROID IS NOT NULL) THEN
	vHISTORICOFINAL = 'Débito ref. Tarifas  Bancárias - Bradesco';
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAID WHERE extrato_id = pEXTRATOID;
	vRETORNO:= 1;
 END IF;  	          
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
