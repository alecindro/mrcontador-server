CREATE OR REPLACE FUNCTION ${schema}.processa_comprovantegeral("pPAR_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pPAR_CODIGO ALIAS FOR $1;  
  
  vCODIGOCOMPROVANTE NUMERIC;
  vRETORNO NUMERIC;
  vQUANTIDADE NUMERIC;
  vQUANTIDADENOTA NUMERIC;
  
  cCOMPROVANTEGERAL CURSOR FOR
  SELECT ID
  FROM ${schema}.COMPROVANTE  
  WHERE  PARCEIRO_ID= pPAR_CODIGO
    AND PROCESSADO= FALSE;  

BEGIN
 
  vRETORNO:= 0;
  vQUANTIDADE:=0;
  vQUANTIDADENOTA :=0;
  
  OPEN cCOMPROVANTEGERAL;
  LOOP
  FETCH cCOMPROVANTEGERAL INTO vCODIGOCOMPROVANTE  ;
  EXIT WHEN NOT FOUND;
   SELECT ${schema}.PROCESSA_COMPROVANTE(CAST(vCODIGOCOMPROVANTE AS int8)) INTO vQUANTIDADE;
   vRETORNO:= vRETORNO + vQUANTIDADE; 
  END LOOP;
  CLOSE cCOMPROVANTEGERAL;
  IF (vRETORNO > 0) THEN
  SELECT ${schema}.processa_notafiscalgeral(CAST(pPAR_CODIGO AS int8)) INTO vQUANTIDADENOTA;
  END IF;
  
  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
