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
REC_PERIODO RECORD;
vQTDCONTA NUMERIC;
pPeriodo DATE;
 
 begin
	  vRETORNO:= 0; 
for REC in 
 SELECT  id FROM ${schema}.NOTAFISCAL  WHERE  TNO_CODIGO=0
    AND PARCEIRO_ID = CAST(pPAR_CODIGO AS int8) 
    AND PROCESSADO = false 
    and NOT_DATAPARCELA >= cast((pDATA_INICIAL)as DATE)
    order by NOT_DATAPARCELA DESC
 LOOP
 SELECT * from ${schema}.processa_notafiscal(cast(REC.id as INT8)) INTO vQUANTIDADE;
   vRETORNO:= vRETORNO + vQUANTIDADE; 
END LOOP; 
for REC_PERIODO in 
select periodo from (
 SELECT  DISTINCT ON (periodo) * FROM ${schema}.NOTAFISCAL  WHERE  TNO_CODIGO=0
    AND PARCEIRO_ID = pPAR_CODIGO 
    and processado = true
    and NOT_DATAPARCELA >= cast((pDATA_INICIAL)as DATE)
    ) t
    order by NOT_DATAPARCELA asc
   LOOP
select * from  ${schema}.processa_conta(pPAR_CODIGO,REC_PERIODO.PERIODO) into vQTDCONTA;
end loop;
RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
