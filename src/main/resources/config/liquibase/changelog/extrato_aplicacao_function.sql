CREATE OR REPLACE FUNCTION ${schema}.extrato_aplicacao_function("pAG_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pAG_CODIGO ALIAS FOR $1;  
  vRETORNO numeric;
  vPARCEIROID numeric;
  vBANCOID numeric;
  vAGEAGENCIA text;
  vAGENUMERO text;
  vAGENCIAAPLICACAOID numeric;
  vCONTAAPLICACAOID numeric;
  REC   RECORD;
  vHISTORICOFINAL TEXT;

BEGIN 
  vRETORNO:= 0; 
SELECT  a.PARCEIRO_ID, a.BANCO_ID, a.AGE_AGENCIA, a.AGE_NUMERO INTO vPARCEIROID, vBANCOID, vAGEAGENCIA, vAGENUMERO from ${schema}.AGENCIABANCARIA a where a.ID = pAG_CODIGO AND a.POSSUEAPLICACAO = TRUE;
SELECT  a.ID, A.CONTA_ID INTO vAGENCIAAPLICACAOID, vCONTAAPLICACAOID from ${schema}.AGENCIABANCARIA a where a.PARCEIRO_ID = vPARCEIROID AND a.TIPO_AGENCIA = 'APLICACAO' AND a.AGE_AGENCIA = vAGEAGENCIA AND a.AGE_NUMERO = vAGENUMERO;


FOR REC IN  
SELECT I.ID as INTELIGENT_ID, I.debito as DEBITO, I.credito as CREDITO, I.DATALANCAMENTO AS DATAL, I.PERIODO as PERIODO, I.EXTRATO_ID as EXTRATOID  FROM ${schema}.INTELIGENT I INNER JOIN ${schema}.EXTRATO E ON I.EXTRATO_ID = E.ID AND I.PARCEIRO_ID = vPARCEIROID 
AND I.ASSOCIADO = FALSE AND E.EXT_NUMERODOCUMENTO = '2' AND I.AGENCIABANCARIA_ID = pAG_CODIGO

LOOP
IF (REC.CREDITO IS NOT NULL) THEN
	vHISTORICOFINAL:='Valor referente resgate de Aplicação Automática conforme extrato na data '|| REC.DATAL;
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAAPLICACAOID WHERE ID = REC.INTELIGENT_ID;
	INSERT INTO ${schema}.INTELIGENT (ASSOCIADO,DATALANCAMENTO,HISTORICOFINAL,PERIODO,DEBITO,TIPO_INTELIGENT, PARCEIRO_ID,AGENCIABANCARIA_ID,EXTRATO_ID, CONTA_ID) 
	VALUES (TRUE,REC.DATAL,vHISTORICOFINAL,REC.PERIODO,-1*REC.CREDITO,'x',vPARCEIROID,vAGENCIAAPLICACAOID,REC.EXTRATOID, vCONTAAPLICACAOID);
END IF;	
IF (REC.DEBITO IS NOT NULL) THEN
	vHISTORICOFINAL:='Valor referente Aplicação Automática conforme extrato na data '|| REC.DATAL;
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAAPLICACAOID WHERE ID = REC.INTELIGENT_ID;
	INSERT INTO ${schema}.INTELIGENT (ASSOCIADO,DATALANCAMENTO,HISTORICOFINAL,PERIODO,CREDITO,TIPO_INTELIGENT, PARCEIRO_ID,AGENCIABANCARIA_ID,EXTRATO_ID, CONTA_ID) 
	VALUES (TRUE,REC.DATAL,vHISTORICOFINAL,REC.PERIODO,-1*REC.DEBITO,'x',vPARCEIROID,vAGENCIAAPLICACAOID,REC.EXTRATOID, vCONTAAPLICACAOID);
END IF;
vRETORNO:= vRETORNO + 1;
END LOOP; 	          
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;