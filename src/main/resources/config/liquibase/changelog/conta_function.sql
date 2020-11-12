CREATE OR REPLACE FUNCTION ${schema}.processa_conta("pINT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pINT_CODIGO ALIAS for $1;
vRETORNO NUMERIC;
vCONTAID NUMERIC;
vCNPJ TEXT;
vPARCEIROID NUMERIC;
BEGIN
vRETORNO := 0;	
SELECT CNPJ,parceiro_id INTO vCNPJ, vPARCEIROID FROM ${schema}.INTELIGENT where id = pINT_CODIGO;	
SELECT ID INTO vCONTAID FROM ${schema}.CONTA WHERE (CON_CNPJ = vCNPJ  or substring(CON_CNPJ,1,8) = substring(vCNPJ,1,8)) AND PARCEIRO_ID = vPARCEIROID and substring(con_classificacao,1,2) = '2.';
IF(vCONTAID is NOT null) then
  update ${schema}.INTELIGENT SET ASSOCIADO = TRUE, CONTA_ID = vCONTAID WHERE ID = pINT_CODIGO;
  vRETORNO := 1;
END IF;
return vRETORNO;
END;
$function$
;
