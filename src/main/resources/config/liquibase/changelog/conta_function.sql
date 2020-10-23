CREATE OR REPLACE FUNCTION ds_demo.processa_conta("pINT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
vRETORNO NUMERIC;

vCONTAID NUMERIC;
vCNPJ TEXT;
vPARCEIROID NUMERIC;
BEGIN
vRETORNO := 0;	
SELECT CNPJ,parceiro_id INTO vCNPJ, vPARCEIROID FROM ds_demo.INTELIGENT i where id=pNOT_CODIGO;	
SELECT ID INTO vCONTAID FROM ds_demo.CONTA WHERE CON_CONTA = vCNPJ AND PARCEIRO_ID = vPARCEIROID;
IF(vCONTAID != null) THEN
update ds_demo.INTELIGENT SET ASSOCIADO = TRUE, CONTA_ID = vCONTAID WHERE ID = pNOT_CODIGO;
vRETORNO := 1;
END IF;
return vRETORNO;
END;
$function$
;