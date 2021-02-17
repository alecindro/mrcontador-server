CREATE OR REPLACE FUNCTION ${schema}.extrato_functionCEF("parceiroId" bigint, "agenciaID" bigint, "pPeriodo" character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pParceiroId ALIAS FOR $1;  
  pAgenciaId ALIAS FOR $2; 
  pPeriodo ALIAS FOR $3; 
  vRETORNO NUMERIC;
  vRETORNO_COMPROVANTE NUMERIC;
  vBANCOID NUMERIC;
  vAGEAGENCIA TEXT;
  vAGENUMERO TEXT;
  vAGENCIAAPLICACAOID NUMERIC;
  vCONTAAPLICACAOID NUMERIC;
  vAPLICACAO_AUT NUMERIC;
  REC_EXTRATO RECORD;
  vHISTORICOFINAL TEXT;
   
BEGIN  
    vRETORNO:= 0;

 -- pesquisa a agencia bancaria
 SELECT   a.BANCO_ID, a.AGE_AGENCIA, a.AGE_NUMERO INTO vBANCOID, vAGEAGENCIA, vAGENUMERO from ${schema}.AGENCIABANCARIA a 
 where a.ID = pAgenciaId AND a.TIPO_AGENCIA = 'CONTA';
-- pesquisa a aplicacao automatica
SELECT  a.ID, A.CONTA_ID INTO vAGENCIAAPLICACAOID, vCONTAAPLICACAOID from ${schema}.AGENCIABANCARIA a 
where a.PARCEIRO_ID = pParceiroId AND a.TIPO_AGENCIA = 'APLICACAO' AND a.AGE_AGENCIA = vAGEAGENCIA
 AND a.AGE_NUMERO = vAGENUMERO;
 
 FOR REC_EXTRATO IN
    SELECT  *
    FROM  ${schema}.EXTRATO e
    WHERE e.parceiro_id = pParceiroId
    and e.agenciabancaria_id = pAgenciaId
    and e.periodo = pPeriodo
    and e.processado = false
 LOOP
    INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, 
                                     periodo, associado, tipo_inteligent
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, pAgenciaId, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.EXT_HISTORICO, REC_EXTRATO.EXT_NUMERODOCUMENTO, REC_EXTRATO.EXT_NUMEROCONTROLE, 
                                    REC_EXTRATO.EXT_DEBITO, REC_EXTRATO.EXT_CREDITO, 
                                    pPERIODO, false, 'x'
                                  );
	UPDATE ${schema}.EXTRATO SET processado = true where id = REC_EXTRATO.ID;
	vRETORNO = vRETORNO +1;
 END LOOP;
   IF (vRETORNO > 0) THEN 
        SELECT * INTO vRETORNO_COMPROVANTE FROM ${schema}.comprovante_cef(pParceiroId,pAgenciaId,pPeriodo); 
        END IF;
  RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
