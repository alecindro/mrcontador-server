CREATE OR REPLACE FUNCTION ${schema}.extrato_functionBB("parceiroId" bigint, "agenciaID" bigint, "pPeriodo" varchar)
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

  -- Aplicacao automatica
  vAPLICACAO_AUT := 0;
  IF (REC_EXTRATO.EXT_CREDITO IS NOT null and (REC_EXTRATO.EXT_NUMERODOCUMENTO = '2' OR REC_EXTRATO.EXT_NUMERODOCUMENTO = '5' OR REC_EXTRATO.EXT_NUMERODOCUMENTO = '42') AND REC_EXTRATO.AGENCIABANCARIA_ID = pAgenciaId and vAGENCIAAPLICACAOID is not null) THEN
		vHISTORICOFINAL:='Operação de aplicação Automática';
	INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, historicofinal, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, 
                                     periodo, associado, tipo_inteligent, CONTA_ID
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, pAgenciaId, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.ext_historico,vHISTORICOFINAL, REC_EXTRATO.ext_NUMERODOCUMENTO, REC_EXTRATO.ext_NUMEROCONTROLE, REC_EXTRATO.ext_DEBITO, REC_EXTRATO.ext_CREDITO, 
                                    pPERIODO, true, 'x', vCONTAAPLICACAOID
                                  );
                                  
	INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, historicofinal,  NUMERODOCUMENTO, NUMEROCONTROLE, CREDITO, 
                                     periodo, associado, tipo_inteligent, CONTA_ID
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, vAGENCIAAPLICACAOID, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.ext_historico,vHISTORICOFINAL, REC_EXTRATO.ext_NUMERODOCUMENTO, REC_EXTRATO.ext_NUMEROCONTROLE, 
                                    -1*REC_EXTRATO.ext_DEBITO,
                                    pPERIODO, true, 'x', vCONTAAPLICACAOID
                                  );
                                  
	vAPLICACAO_AUT := 1;
ELSEIF (REC_EXTRATO.EXT_DEBITO IS NOT null and (REC_EXTRATO.EXT_NUMERODOCUMENTO = '2' OR REC_EXTRATO.EXT_NUMERODOCUMENTO = '5' OR REC_EXTRATO.EXT_NUMERODOCUMENTO = '42') AND REC_EXTRATO.AGENCIABANCARIA_ID = pAgenciaId and vAGENCIAAPLICACAOID is not null) THEN
		vHISTORICOFINAL:='Resgate de Aplicação Automática';
		INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, historicofinal, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, 
                                     periodo, associado, tipo_inteligent, CONTA_ID
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, pAgenciaId, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.ext_historico,vHISTORICOFINAL, REC_EXTRATO.ext_NUMERODOCUMENTO, REC_EXTRATO.ext_NUMEROCONTROLE, REC_EXTRATO.ext_DEBITO, REC_EXTRATO.ext_CREDITO, 
                                    pPERIODO, true, 'x', vCONTAAPLICACAOID
                                  );
		INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico,historicofinal, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, 
                                     periodo, associado, tipo_inteligent, CONTA_ID
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, vAGENCIAAPLICACAOID, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.ext_historico,vHISTORICOFINAL, REC_EXTRATO.ext_NUMERODOCUMENTO, REC_EXTRATO.ext_NUMEROCONTROLE, 
                                    -1*REC_EXTRATO.ext_credito,
                                    pPERIODO, true, 'x', vCONTAAPLICACAOID
                                  );
                                  vAPLICACAO_AUT := 1;
 else
    INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, 
                                     periodo, associado, tipo_inteligent
                                  )
                           VALUES ( REC_EXTRATO.ID, pParceiroId, pAgenciaId, REC_EXTRATO.ext_datalancamento,
                                    REC_EXTRATO.EXT_HISTORICO, REC_EXTRATO.EXT_NUMERODOCUMENTO, REC_EXTRATO.EXT_NUMEROCONTROLE, 
                                    REC_EXTRATO.EXT_DEBITO, REC_EXTRATO.EXT_CREDITO, 
                                    pPERIODO, false, 'x'
                                  );
vAPLICACAO_AUT := 2;
END IF;
IF (vAPLICACAO_AUT > 0) THEN
 UPDATE ${schema}.EXTRATO SET processado = true where id = REC_EXTRATO.ID;
 vRETORNO = vRETORNO +1;
END IF; 	
 END LOOP;
   IF (vRETORNO > 0) THEN 
        SELECT * INTO vRETORNO_COMPROVANTE FROM ${schema}.comprovante_bb(pParceiroId,pAgenciaId,pPeriodo); 
        END IF;
  RETURN COALESCE(vRETORNO ,0);
END;
$function$