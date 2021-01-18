CREATE OR REPLACE FUNCTION ds_demo.processa_comprovante("pCOM_CODIGO" bigint)
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
  REC2   RECORD;
  
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
  vTIPO_COMPROVANTE TEXT;
  vHISTORICOFINAL TEXT;
  vCONTAJUROS NUMERIC;
  
  vQUANTIDADENOTA NUMERIC;
 
  
BEGIN
 
 vCONTAJUROS := -1;
  vRETORNO:= 0;
  SELECT COMPROVANTE.ID, COM_DOCUMENTO, COM_CNPJ, COM_BENEFICIARIO, COM_DATAVENCIMENTO, COM_VALORDOCUMENTO, COM_VALORPAGAMENTO, COM_DATAPAGAMENTO, PARCEIRO_ID, AGENCIABANCARIA_ID, COM_MULTA,
  COM_JUROS, COM_DESCONTO, TIPO_COMPROVANTE 
  INTO vCOMPROVANTE, vDETALHECOMP, vCNPJ, vBENEFICIARIO, vDATAVENCIMENTO, vVALORDOCUMENTO, vVALORPAGAMENTO, vDATAPAGAMENTO, vPARCEIROID, vAGENCIABANCARIAID, vCOMMULTA, vCOMJUROS, VCOMDESCONTO, vTIPO_COMPROVANTE
  FROM  ds_demo.COMPROVANTE
  WHERE ID= pCOM_CODIGO; 
  
  SELECT DESPESA_JUROS INTO vCONTAJUROS FROM  ds_demo.PARCEIRO WHERE ID = vPARCEIROID;
  
   FOR REC IN
    SELECT  DISTINCT e.ID as extrato_id, e.EXT_DATALANCAMENTO, e.EXT_HISTORICO, e.EXT_NUMERODOCUMENTO, e.EXT_NUMEROCONTROLE, e.EXT_DESCRICAO, e.EXT_DEBITO, e.EXT_CREDITO, e.INFO_ADICIONAL, i.ID as inteligent_id,
    i.periodo
    FROM  ds_demo.EXTRATO e
     INNER JOIN  ds_demo.INTELIGENT i ON i.EXTRATO_ID=e.ID
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
     UPDATE  ds_demo.INTELIGENT SET COMPROVANTE_ID= vCOMPROVANTE,CNPJ= vCNPJ,  BENEFICIARIO= vBENEFICIARIO, TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE ID = vCODIGOINTELIGENTE;
     IF ((vCOMMULTA > 0) OR (vCOMJUROS >0)) THEN
     	vHISTORICOFINAL   := 'Pagto. de Juros de '|| vBENEFICIARIO;
        UPDATE  ds_demo.INTELIGENT SET debito = vVALORDOCUMENTO*-1 WHERE ID = vCODIGOINTELIGENTE;
		IF( vCONTAJUROS > -1) THEN
     	INSERT INTO  ds_demo.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal, conta_id) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN vCOMJUROS > 0 THEN vCOMJUROS*-1 ELSE vCOMMULTA*-1 END,true,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL, vCONTAJUROS);
		ELSE
		INSERT INTO  ds_demo.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Pagto. de Juros','JUROS',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO,  CASE WHEN vCOMJUROS > 0 THEN vCOMJUROS*-1 ELSE vCOMMULTA*-1 END,false,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
		END IF;
     END IF;
     IF (VCOMDESCONTO > 0) THEN
     vHISTORICOFINAL   := 'Receb. de Desconto de '|| vBENEFICIARIO;
      UPDATE  ds_demo.INTELIGENT SET debito = vVALORDOCUMENTO*-1 WHERE ID = vCODIGOINTELIGENTE;
     	INSERT INTO  ds_demo.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,credito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Receb. de desconto','DESCONTO',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO, VCOMDESCONTO,false,
     		vCNPJ,vBENEFICIARIO, vTIPOINTELIGENTE,vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO, vHISTORICOFINAL);
     END IF;
     
     	UPDATE ds_demo.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = vCOMPROVANTE; 
       vRETORNO:= vRETORNO + 1;

    
   END LOOP; 
   IF(vRETORNO = 0 and vTIPO_COMPROVANTE = 'TRANSFERENCIA') THEN
   
    FOR REC IN
    SELECT  DISTINCT e.ID as extrato_id, e.EXT_DATALANCAMENTO, e.EXT_HISTORICO, e.EXT_NUMERODOCUMENTO, e.EXT_NUMEROCONTROLE, e.EXT_DESCRICAO, e.EXT_DEBITO, e.EXT_CREDITO, e.INFO_ADICIONAL, i.ID as inteligent_id,
    i.periodo
    FROM  ds_demo.EXTRATO e
     INNER JOIN  ds_demo.INTELIGENT i ON i.EXTRATO_ID=e.ID
    WHERE e.PARCEIRO_ID= vPARCEIROID
      AND e.AGENCIABANCARIA_ID= vAGENCIABANCARIAID
      AND i.ASSOCIADO= FALSE
      AND i.COMPROVANTE_ID is null
      and EXT_DEBITO = vVALORPAGAMENTO*-1
      and EXT_DATALANCAMENTO > vDATAPAGAMENTO
    ORDER BY  e.EXT_DATALANCAMENTO DESC
    LIMIT 1
    
   LOOP

   vDESCRICAO        := REC.EXT_DESCRICAO; 
   vDEBITO           := REC.EXT_DEBITO;   
   vCODIGOINTELIGENTE:= REC.inteligent_id;
   vPERIODO          := REC.periodo;
   vTIPOINTELIGENTE := 'x';
   vHISTORICOFINAL   := 'Pagto. de '|| vBENEFICIARIO;
     UPDATE  ds_demo.INTELIGENT SET COMPROVANTE_ID= vCOMPROVANTE,CNPJ= vCNPJ,  BENEFICIARIO= vBENEFICIARIO, TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE ID = vCODIGOINTELIGENTE;
     UPDATE ds_demo.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = vCOMPROVANTE; 
   vRETORNO:= vRETORNO + 1;
    
   END LOOP; 
   
   END IF;

  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
