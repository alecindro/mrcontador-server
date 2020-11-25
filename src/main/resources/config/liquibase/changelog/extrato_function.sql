CREATE OR REPLACE FUNCTION ${schema}.processa_extrato("pEXT_CODIGO" bigint, "pAG_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pEXT_CODIGO ALIAS FOR $1;  
   pAG_CODIGO ALIAS FOR $2;  
  vRETORNO NUMERIC;
  vCODIGEXTRATO NUMERIC;
  vDATAEXTRATO DATE;  
  vHISTORICO VARCHAR(150);
  vNUMERODOCUMENTO VARCHAR(35);
  vNUMEROCONTROLE VARCHAR(35);
  vDESCRICAO VARCHAR(30);
  vDEBITO NUMERIC;
  vCREDITO NUMERIC;
  vASSOCIADO BOOLEAN;
  vAGENCIABANCARIAID NUMERIC;
  vPARCEIROID NUMERIC;
  vTIPOINTELIGENTE VARCHAR(1);
   
  cEXTRATOBANCARIO CURSOR FOR
  SELECT  ID, EXT_DATALANCAMENTO, EXT_HISTORICO, EXT_NUMERODOCUMENTO, EXT_NUMEROCONTROLE, EXT_DESCRICAO, EXT_DEBITO, EXT_CREDITO, 
  AGENCIABANCARIA_ID
  FROM ${schema}.EXTRATO
  WHERE EXTRATO.ID= pEXT_CODIGO;  
 
 vCONTAJUROS numeric;
vPERIODO text;

RECEXTRATO RECORD;
vHISTORICOFINAL text;

vQUANTIDADENOTA numeric;
vAPLICACAO_AUT numeric;


 vBANCOID numeric;
vAGEAGENCIA text;
  vAGENUMERO text;
  vAGENCIAAPLICACAOID numeric;
  vCONTAAPLICACAOID numeric;


BEGIN 
  vRETORNO:= 0;
 vCONTAJUROS:=-1;
 -- pesquisa a agencia bancaria
 SELECT   a.PARCEIRO_ID, a.BANCO_ID, a.AGE_AGENCIA, a.AGE_NUMERO INTO vPARCEIROID, vBANCOID, vAGEAGENCIA, vAGENUMERO from ${schema}.AGENCIABANCARIA a where a.ID = pAG_CODIGO AND a.POSSUEAPLICACAO = TRUE;
-- pesquisa a aplicacao automatica
SELECT  a.ID, A.CONTA_ID INTO vAGENCIAAPLICACAOID, vCONTAAPLICACAOID from ${schema}.AGENCIABANCARIA a where a.PARCEIRO_ID = vPARCEIROID AND a.TIPO_AGENCIA = 'APLICACAO' AND a.AGE_AGENCIA = vAGEAGENCIA AND a.AGE_NUMERO = vAGENUMERO;
 
   OPEN cEXTRATOBANCARIO;
  LOOP
  FETCH cEXTRATOBANCARIO INTO vCODIGEXTRATO, vDATAEXTRATO, vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vDESCRICAO, vDEBITO, vCREDITO,
  vAGENCIABANCARIAID;
  EXIT WHEN NOT FOUND;
   vASSOCIADO      := FALSE; 
   vTIPOINTELIGENTE := 'x';
   SELECT despesa_juros INTO vCONTAJUROS FROM  ${schema}.PARCEIRO WHERE ID = vPARCEIROID;
   vPERIODO = concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO));
  -- associa extrato ao inteligent
     INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                    historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, 
                                     periodo, associado, tipo_inteligent
                                  )
                           VALUES ( vCODIGEXTRATO, vPARCEIROID, vAGENCIABANCARIAID, vDATAEXTRATO,
                                    vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vDEBITO, vCREDITO, 
                                    vPERIODO, vASSOCIADO, vTIPOINTELIGENTE
                                  );
   UPDATE ${schema}.EXTRATO SET processado = true where id = pEXT_CODIGO;
  
  -- Aplicacao automatica
  vAPLICACAO_AUT := 0;
  IF (vCREDITO IS NOT null and vNUMERODOCUMENTO = '2' AND vAGENCIABANCARIAID = pAG_CODIGO and vAGENCIAAPLICACAOID is not null) THEN
	vHISTORICOFINAL:='Crédito ref. resgate de Aplicação Automática';
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAAPLICACAOID WHERE extrato_id = vCODIGEXTRATO;
	INSERT INTO ${schema}.INTELIGENT (ASSOCIADO,DATALANCAMENTO,HISTORICOFINAL,PERIODO,DEBITO,TIPO_INTELIGENT, PARCEIRO_ID,AGENCIABANCARIA_ID,EXTRATO_ID, CONTA_ID) 
	VALUES (TRUE,vDATAEXTRATO,vHISTORICOFINAL,vPERIODO,-1*vCREDITO,'x',vPARCEIROID,vAGENCIAAPLICACAOID,vCODIGEXTRATO, vCONTAAPLICACAOID);
	vAPLICACAO_AUT := 1;
END IF;	
IF (vDEBITO IS NOT null and vNUMERODOCUMENTO = '2' AND vAGENCIABANCARIAID = pAG_CODIGO and vAGENCIAAPLICACAOID is not null) THEN
	vHISTORICOFINAL:='Débito ref. operação de aplicação Automática';
	UPDATE ${schema}.INTELIGENT SET ASSOCIADO = TRUE, HISTORICOFINAL = vHISTORICOFINAL, TIPO_INTELIGENT = 'x', CONTA_ID = vCONTAAPLICACAOID WHERE extrato_id = vCODIGEXTRATO;
	INSERT INTO ${schema}.INTELIGENT (ASSOCIADO,DATALANCAMENTO,HISTORICOFINAL,PERIODO,CREDITO,TIPO_INTELIGENT, PARCEIRO_ID,AGENCIABANCARIA_ID,EXTRATO_ID, CONTA_ID) 
	VALUES (TRUE,vDATAEXTRATO,vHISTORICOFINAL,vPERIODO,-1*vDEBITO,'x',vPARCEIROID,vAGENCIAAPLICACAOID,vCODIGEXTRATO, vCONTAAPLICACAOID);
	vAPLICACAO_AUT := 1;
END IF;
 -- comprovante
if (vAPLICACAO_AUT = 0) then
   for RECEXTRATO in select C.ID, C.COM_DOCUMENTO, C.COM_CNPJ, C.COM_BENEFICIARIO, C.COM_DATAVENCIMENTO, C.COM_VALORDOCUMENTO, C.agenciabancaria_id,
	C.COM_VALORPAGAMENTO, C.COM_DATAPAGAMENTO, C.AGENCIABANCARIA_ID, C.COM_MULTA,
     C.COM_JUROS, C.COM_DESCONTO 
    FROM  ${schema}.COMPROVANTE C
	WHERE C.PARCEIRO_ID= vPARCEIROID
      AND C.AGENCIABANCARIA_ID= vAGENCIABANCARIAID
      AND C.processado= FALSE
      and C.COM_DOCUMENTO = vNUMERODOCUMENTO
      and C.COM_DATAPAGAMENTO = vDATAEXTRATO
      and C.COM_VALORPAGAMENTO = vDEBITO*-1
    ORDER BY C.ID 
    LIMIT 1	
    loop
   IF ((RECEXTRATO.COM_MULTA > 0) OR (RECEXTRATO.COM_JUROS >0)) THEN
    vTIPOINTELIGENTE := 'C';
   END IF; 
   IF (RECEXTRATO.COM_DESCONTO >0) THEN
    vTIPOINTELIGENTE := 'D';
   END IF;
   vHISTORICOFINAL   := 'Pagto. de '|| RECEXTRATO.COM_BENEFICIARIO;
   -- associa comprovante ao inteligent
   UPDATE  ${schema}.INTELIGENT SET COMPROVANTE_ID= RECEXTRATO.ID,CNPJ= RECEXTRATO.COM_CNPJ,  BENEFICIARIO= RECEXTRATO.COM_BENEFICIARIO, TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE extrato_id = vCODIGEXTRATO;
       IF ((RECEXTRATO.COM_MULTA > 0) OR (RECEXTRATO.COM_JUROS >0)) THEN
     	vHISTORICOFINAL   := 'Pagto. de Juros de '|| RECEXTRATO.COM_BENEFICIARIO;
     -- atualiza o valor do inteligente para o valor do documento do extrato  
     UPDATE  ${schema}.INTELIGENT SET debito = RECEXTRATO.COM_VALORDOCUMENTO*-1 WHERE extrato_id = vCODIGEXTRATO;
       IF( vCONTAJUROS > -1) then
       -- insere juros e a conta juros
     	INSERT INTO  ${schema}.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal, conta_id) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN RECEXTRATO.COM_JUROS > 0 THEN RECEXTRATO.COM_JUROS*-1 ELSE RECEXTRATO.COM_MULTA*-1 END,true,
     		RECEXTRATO.COM_CNPJ,RECEXTRATO.COM_BENEFICIARIO, vTIPOINTELIGENTE,RECEXTRATO.ID,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL, vCONTAJUROS);
     	else
     	-- insere juros
		INSERT INTO  ${schema}.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN vCOMJUROS > 0 THEN vCOMJUROS*-1 ELSE vCOMMULTA*-1 END,false,
     		RECEXTRATO.COM_CNPJ,RECEXTRATO.COM_BENEFICIARIO, vTIPOINTELIGENTE,RECEXTRATO.ID,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
		END IF;
     END IF;
	 IF (RECEXTRATO.COM_DESCONTO > 0) THEN
     vHISTORICOFINAL   := 'Receb. de Desconto de '|| RECEXTRATO.COM_BENEFICIARIO;
     -- insere desconto
      UPDATE  ${schema}.INTELIGENT SET debito = RECEXTRATO.COM_VALORDOCUMENTO*-1 WHERE extrato_id = vCODIGEXTRATO;
     	INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,credito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES 
			('Receb. de desconto','DESCONTO',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO, RECEXTRATO.COM_DESCONTO,false,
     		RECEXTRATO.COM_CNPJ,RECEXTRATO.COM_BENEFICIARIO, vTIPOINTELIGENTE,RECEXTRATO.ID,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
     END IF;
    -- atualiza comprovante para processsado
  UPDATE ${schema}.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = RECEXTRATO.ID;
 end loop;
END IF;

  vRETORNO:= vRETORNO + 1;
  END LOOP;
  CLOSE cEXTRATOBANCARIO;  
          
  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
