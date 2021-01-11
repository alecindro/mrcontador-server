CREATE OR REPLACE FUNCTION ${schema}.comprovante_bb("pCOM_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pCOM_CODIGO ALIAS FOR $1;  
  
  vRETORNO NUMERIC;
  
  vCODIGEXTRATO NUMERIC;
  vDATAEXTRATO DATE;  
  vHISTORICO VARCHAR(150);
  vINFOADICIONAL VARCHAR(240);
  vNUMERODOCUMENTO VARCHAR(35);
  vNUMEROCONTROLE VARCHAR(35);
  vDESCRICAO VARCHAR(30);
  vDEBITO NUMERIC;
  vCREDITO NUMERIC;
  vCODIGOINTELIGENTE NUMERIC;
  vPERIODO TEXT;
  vTIPOINTELIGENTE TEXT;
  
  REC   RECORD; 
  
  vCOMPROVANTE NUMERIC;
  vDETALHECOMP TEXT; 
  vCNPJ TEXT;
  vBENEFICIARIO TEXT;
  vDATAVENCIMENTO DATE;
  vVALORDOCUMENTO NUMERIC;  
  vVALORPAGAMENTO NUMERIC;
  vDATAPAGAMENTO DATE;
  vPARCEIROID NUMERIC;
  vAGENCIABANCARIAID NUMERIC;
  vCOMMULTA NUMERIC;
  vCOMJUROS NUMERIC;
  VCOMDESCONTO NUMERIC;
  vHISTORICOFINAL TEXT;
  vCONTAJUROS NUMERIC;
  vQUANTIDADENOTA NUMERIC;
 
  
BEGIN
 
 vCONTAJUROS := -1;
  vRETORNO:= 0;
  SELECT COMPROVANTE.ID, COM_DOCUMENTO, COM_CNPJ, COM_BENEFICIARIO, COM_DATAVENCIMENTO, COM_VALORDOCUMENTO, COM_VALORPAGAMENTO, COM_DATAPAGAMENTO, PARCEIRO_ID, AGENCIABANCARIA_ID, COM_MULTA,
  COM_JUROS, COM_DESCONTO 
  INTO vCOMPROVANTE, vDETALHECOMP, vCNPJ, vBENEFICIARIO, vDATAVENCIMENTO, vVALORDOCUMENTO, vVALORPAGAMENTO, vDATAPAGAMENTO, vPARCEIROID, vAGENCIABANCARIAID, vCOMMULTA, vCOMJUROS, VCOMDESCONTO
  FROM  ${schema}.COMPROVANTE
  WHERE ID= pCOM_CODIGO; 
  
  SELECT DESPESA_JUROS INTO vCONTAJUROS FROM  ${schema}.PARCEIRO WHERE ID = vPARCEIROID;
  
        
   FOR REC IN
    SELECT  DISTINCT e.ID as extrato_id, e.EXT_DATALANCAMENTO, e.EXT_HISTORICO, e.EXT_NUMERODOCUMENTO, e.EXT_NUMEROCONTROLE, e.EXT_DESCRICAO, e.EXT_DEBITO, e.EXT_CREDITO, e.INFO_ADICIONAL, i.ID as inteligent_id,
    i.periodo
    FROM  ${schema}.EXTRATO e
     INNER JOIN  ${schema}.INTELIGENT i ON i.EXTRATO_ID=e.ID
    WHERE e.PARCEIRO_ID= vPARCEIROID
      AND e.AGENCIABANCARIA_ID= vAGENCIABANCARIAID
      AND i.ASSOCIADO= FALSE
      AND i.COMPROVANTE_ID is null
      and EXT_DATALANCAMENTO = vDATAPAGAMENTO
      and EXT_DEBITO = vVALORPAGAMENTO*-1
    ORDER BY e.ID 
    LIMIT 1
   LOOP

   vCODIGEXTRATO     := REC.extrato_id; 
   vDATAEXTRATO      := REC.EXT_DATALANCAMENTO; 
   vHISTORICO        := REC.EXT_HISTORICO;
   vNUMERODOCUMENTO  := REC.EXT_NUMERODOCUMENTO; 
   vNUMEROCONTROLE   := REC.EXT_NUMEROCONTROLE;
   vDESCRICAO        := REC.EXT_DESCRICAO; 
   vDEBITO           := REC.EXT_DEBITO; 
   vCREDITO          := REC.EXT_CREDITO; 
   vINFOADICIONAL    := REC.INFO_ADICIONAL; 
   vCODIGOINTELIGENTE:= REC.inteligent_id;
   vPERIODO          := REC.periodo;
   vTIPOINTELIGENTE := 'x';
   
   
   IF ((vCOMMULTA > 0) OR (vCOMJUROS >0)) THEN
    vTIPOINTELIGENTE := 'C';
   END IF; 
   
   IF (VCOMDESCONTO >0) THEN
    vTIPOINTELIGENTE := 'D';
   END IF;

 	 vHISTORICOFINAL   := 'Pagto. de '|| vBENEFICIARIO;
     UPDATE  ${schema}.INTELIGENT SET COMPROVANTE_ID= vCOMPROVANTE,CNPJ= vCNPJ,  BENEFICIARIO= vBENEFICIARIO, TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE ID = vCODIGOINTELIGENTE;
     IF ((vCOMMULTA > 0) OR (vCOMJUROS >0)) THEN
     	vHISTORICOFINAL   := 'Pagto. de Juros de '|| vBENEFICIARIO;
        UPDATE  ${schema}.INTELIGENT SET debito = vVALORDOCUMENTO*-1 WHERE ID = vCODIGOINTELIGENTE;
		IF( vCONTAJUROS > -1) THEN
     	INSERT INTO  ${schema}.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal, conta_id) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN vCOMJUROS > 0 THEN vCOMJUROS*-1 ELSE vCOMMULTA*-1 END,true,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL, vCONTAJUROS);
		ELSE
		INSERT INTO  ${schema}.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN vCOMJUROS > 0 THEN vCOMJUROS*-1 ELSE vCOMMULTA*-1 END,false,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
		END IF;
     END IF;
     IF (VCOMDESCONTO > 0) THEN
     vHISTORICOFINAL   := 'Receb. de Desconto de '|| vBENEFICIARIO;
      UPDATE  ${schema}.INTELIGENT SET debito = vVALORDOCUMENTO*-1 WHERE ID = vCODIGOINTELIGENTE;
     	INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,credito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Receb. de desconto','DESCONTO',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO, VCOMDESCONTO,false,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
     END IF;
     
     	UPDATE ${schema}.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = vCOMPROVANTE; 
       vRETORNO:= vRETORNO + 1;

    
   END LOOP; 

  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
