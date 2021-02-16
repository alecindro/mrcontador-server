CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscalgeral("pPAR_CODIGO" bigint, "pDATA_INICIAL" date)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
  pPAR_CODIGO ALIAS for $1;
 pDATA_INICIAL ALIAS for $2;
  vRETORNO NUMERIC;
  vQUANTIDADE NUMERIC;
 REC   RECORD; 
 
 begin
	  vRETORNO:= 0; 
for REC in 
 SELECT  distinct id AS vNOTAFISCAL_ID  FROM ${schema}.NOTAFISCAL  WHERE  TNO_CODIGO=0
    AND PARCEIRO_ID = CAST(pPAR_CODIGO AS int8) 
    AND PROCESSADO = false 
    and NOT_DATAPARCELA >= cast((pDATA_INICIAL -120)as DATE)
 LOOP
 SELECT * from ${schema}.processa_notafiscal(cast(REC.vNOTAFISCAL_ID as INT8)) INTO vQUANTIDADE;
   vRETORNO:= vRETORNO + vQUANTIDADE; 
END LOOP; 
  
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
