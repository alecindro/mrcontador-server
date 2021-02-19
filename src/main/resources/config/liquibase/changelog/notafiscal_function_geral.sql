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
vQTDCONTA NUMERIC;
pPeriodo DATE;
 
 begin
	  vRETORNO:= 0; 
for REC in 
 SELECT  id FROM ${schema}.NOTAFISCAL  WHERE  TNO_CODIGO=0
    AND PARCEIRO_ID = CAST(pPAR_CODIGO AS int8) 
    AND PROCESSADO = false 
    and NOT_DATAPARCELA >= cast((pDATA_INICIAL -120)as DATE)
    order by NOT_DATAPARCELA desc
 LOOP
 SELECT * from ${schema}.processa_notafiscal(cast(REC.id as INT8)) INTO vQUANTIDADE;
   vRETORNO:= vRETORNO + vQUANTIDADE; 
END LOOP; 
select * from  ${schema}.processa_conta(pPAR_CODIGO,CONCAT(extract (month from pDATA_INICIAL) , extract (YEAR from pDATA_INICIAL ))) into vQTDCONTA;
pPeriodo := cast((pDATA_INICIAL -30)as DATE);
if (CONCAT(extract (month from pDATA_INICIAL) , extract (YEAR from pDATA_INICIAL )) <> CONCAT(extract (month from pPeriodo) , extract (YEAR from pPeriodo))) then
select * from  ${schema}.processa_conta(pPAR_CODIGO,CONCAT(extract (month from pPeriodo) , extract (YEAR from pPeriodo))) into vQTDCONTA;
end if;
RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
