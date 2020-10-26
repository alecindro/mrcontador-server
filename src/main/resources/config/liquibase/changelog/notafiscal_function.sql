CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscal("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
vRETORNO NUMERIC;

BEGIN
  vRETORNO:= 0;
SELECT ${schema}.PROCESSA_NOTAFISCAL1(CAST(pNOT_CODIGO AS int8)) INTO vRETORNO;
 	if (vRETORNO = 0) THEN
		SELECT ${schema}.PROCESSA_NOTAFISCAL2(CAST(pNOT_CODIGO AS int8)) INTO vRETORNO;
	end if;
	if (vRETORNO = 0) THEN
		SELECT ${schema}.PROCESSA_NOTAFISCAL3(CAST(pNOT_CODIGO AS int8)) INTO vRETORNO;
	end if;
 	if (vRETORNO > 0) THEN
	update ${schema}.notafiscal set processado = true where ID = pNOT_CODIGO;
	end if;	
RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
