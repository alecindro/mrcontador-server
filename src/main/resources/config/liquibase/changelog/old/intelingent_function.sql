CREATE OR REPLACE FUNCTION ${schema}.processa_inteligente("pPAR_CODIGO" int8, "pAGE_CODIGO" int8, "pDATAINICIO" date, "pDATAFIM" date)
  RETURNS numeric AS
$BODY$
DECLARE
  pPAR_CODIGO ALIAS FOR $1;  
  pAGE_CODIGO ALIAS FOR $2;  
  pDATAINICIO ALIAS FOR $3;
  pDATAFIM    ALIAS FOR $4;
  
  
  vRETORNO NUMERIC;
  vCODIGEXTRATO NUMERIC;
  vDATAEXTRATO DATE;  
  vHISTORICO VARCHAR(150);
  vNUMERODOCUMENTO VARCHAR(35);
  vNUMEROCONTROLE VARCHAR(35);
  vDESCRICAO VARCHAR(30);
  vDEBITO NUMERIC;
  vCREDITO NUMERIC;
  REC   RECORD;
  RNOTA RECORD;
  RREGRA RECORD;
  RCONTA RECORD;
  RFORNECEDOR RECORD;
  
  
  vCNPJ TEXT;
  vBENEFICIARIO TEXT;
  vCOMPROVANTE NUMERIC;
  vDATAVENCIMENTO DATE;  
  vVALORDOCUMENTO NUMERIC;
  vVALORPAGAMENTO NUMERIC;
  vVALORDIFERENTE NUMERIC;
  
  

  vCODIGOCONTABIL NUMERIC;
  vCODIGOEXTRA NUMERIC;

  vHISTORICOFINAL TEXT;
  vCODIGONOTA NUMERIC;
  vNIVEL NUMERIC;
  vASSOCIADO BOOLEAN;
  vCODIGOFORNECEDOR NUMERIC;
  vCODIGOAGUARDANDO NUMERIC;
  vCONTAFORNECEDOR NUMERIC;
  vCONTAAGUARDANDO NUMERIC;
  vGUARDACONTA NUMERIC;
  vCNPJAGUARDANDO TEXT;
  vACHOUNOTA NUMERIC;
  vACHOUFORNECEDOR NUMERIC;
  vQUEBRAPARCELA NUMERIC;  
  vDETALHECOMP TEXT;  
  vCONTROLAVALOR NUMERIC;  

  
  
  cEXTRATOBANCARIO CURSOR FOR
  SELECT ID, EXT_DATALANCAMENTO, EXT_HISTORICO, EXT_NUMERODOCUMENTO, EXT_NUMEROCONTROLE, EXT_DESCRICAO, EXT_DEBITO, EXT_CREDITO
  FROM ${schema}.EXTRATO
  WHERE PARCEIRO_ID= pPAR_CODIGO
    AND AGENCIABANCARIA_ID= pAGE_CODIGO
    AND EXT_DATALANCAMENTO >= pDATAINICIO
    AND EXT_DATALANCAMENTO <= pDATAFIM
  ORDER BY ID ;  

  
BEGIN
 
  vRETORNO:= 0;

  DELETE FROM ${schema}.INTELIGENT
  WHERE PARCEIRO_ID= pPAR_CODIGO
    AND AGENCIABANCARIA_ID= pAGE_CODIGO
    AND DATALANCAMENTO >= pDATAINICIO
    AND DATALANCAMENTO <= pDATAFIM;
 
  
   OPEN cEXTRATOBANCARIO;
  LOOP
  FETCH cEXTRATOBANCARIO INTO vCODIGEXTRATO, vDATAEXTRATO, vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vDESCRICAO, vDEBITO, vCREDITO;
  EXIT WHEN NOT FOUND;

  IF vDEBITO < 0 THEN
   vDEBITO:= vDEBITO * (-1);
  END IF;

   SELECT trim(leading '0' from (replace(vNUMERODOCUMENTO, '.', ''))) 
    INTO vNUMERODOCUMENTO;


   vCOMPROVANTE    := 0;
   vCNPJ           := '';
   vBENEFICIARIO   := '';   
   vCODIGOCONTABIL := 0;

   vHISTORICOFINAL := '';
   vCODIGONOTA     := 0;   
   vNIVEL          := 0;  
   vASSOCIADO      := FALSE;  
   vCONTAAGUARDANDO:= 0;
   vACHOUFORNECEDOR:= 0;
   vCONTROLAVALOR  := 0;

   SELECT nextval('${schema}.seq_aguardando')
   INTO vCODIGOAGUARDANDO;

   SELECT nextval('${schema}.seq_fornecedor')
   INTO vCODIGOFORNECEDOR;
         
   FOR REC IN
   SELECT *
   FROM ${schema}.COMPROVANTE
   WHERE PARCEIRO_ID= pPAR_CODIGO
     AND AGENCIABANCARIA_ID= pAGE_CODIGO
     AND COM_DATAPAGAMENTO >= vDATAEXTRATO
     AND COM_DATAPAGAMENTO <= vDATAEXTRATO
   LOOP
    vDETALHECOMP:= REC.COM_DOCUMENTO;    
    --SELECT replace(vDETALHECOMP, '.', '')
    SELECT trim(leading '0' from (replace(vDETALHECOMP, '.', ''))) 
     INTO vDETALHECOMP;
        
    IF (vDETALHECOMP = vNUMERODOCUMENTO) AND (REC.COM_DATAPAGAMENTO = vDATAEXTRATO) AND (REC.COM_VALORPAGAMENTO = vDEBITO ) THEN

     vCNPJ          := REC.COM_CNPJ;
     SELECT replace(vCNPJ, '.', '')
     INTO vCNPJ;
     SELECT replace(vCNPJ, '/', '')
     INTO vCNPJ;
     SELECT replace(vCNPJ, '-', '')
     INTO vCNPJ;
     
     vBENEFICIARIO  := REC.COM_BENEFICIARIO;
     vCOMPROVANTE   := REC.ID;
     vDATAVENCIMENTO:= REC.COM_DATAVENCIMENTO;  
     vVALORDOCUMENTO:= REC.COM_VALORDOCUMENTO;
     vVALORPAGAMENTO:= REC.COM_VALORPAGAMENTO;

     IF (vVALORDOCUMENTO>0) AND (REC.COM_VALORDOCUMENTO<>REC.COM_VALORPAGAMENTO) AND (REC.COM_DATAPAGAMENTO<>REC.COM_DATAVENCIMENTO) THEN 
      IF (REC.COM_VALORDOCUMENTO > REC.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  :=1;
        vVALORDIFERENTE := REC.COM_VALORDOCUMENTO - REC.COM_VALORPAGAMENTO;
      END IF;

      IF (REC.COM_VALORDOCUMENTO < REC.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  := 2;
        vVALORDIFERENTE := REC.COM_VALORPAGAMENTO - REC.COM_VALORDOCUMENTO;
      END IF;

     END IF;
      

     IF vCNPJ <> '' THEN
     --INICIO - VERIFICAR CONTA CONTABIL...................................................
     FOR RCONTA IN
     SELECT *
     FROM ${schema}.CONTA  
     WHERE PARCEIRO_ID= pPAR_CODIGO 
       AND TRIM(CON_CNPJ) = TRIM(vCNPJ)
       AND CON_CNPJ <> ''
     LOOP 
      IF (vCODIGOCONTABIL = 0)THEN
       INSERT INTO ${schema}.FORNECEDOR    ( FOR_SEQUENCIA, FOR_CODIGO, FOR_CNPJ, FOR_PARCEIRO, FOR_CONTACONTABIL, FOR_VALORBOLETO, PAR_CODIGO, AGE_CODIGO
                                         )
                                  VALUES ( vCODIGOFORNECEDOR, RCONTA.ID, RCONTA.CON_CNPJ, RCONTA.CON_DESCRICAO, RCONTA.CON_CONTA, 
                                           RCONTA.CON_VALORBOLETO, pPAR_CODIGO, pAGE_CODIGO  
                                         ); 
       vCODIGOCONTABIL       := RCONTA.CON_CONTA;
       vACHOUFORNECEDOR      := 1; 
      END IF;    
     END LOOP; 

     IF (vCODIGOCONTABIL = 0)THEN
      vCONTAFORNECEDOR   := 0;
      vGUARDACONTA       := 0;
   
      FOR RCONTA IN
      SELECT *
      FROM ${schema}.CONTA  
      WHERE PARCEIRO_ID= pPAR_CODIGO 
        AND SUBSTRING(TRIM(CON_CNPJ), 1 , 8) = SUBSTRING(TRIM(vCNPJ), 1 , 8)
        AND CON_CNPJ <> ''
      LOOP 
       INSERT INTO ${schema}.FORNECEDOR ( FOR_SEQUENCIA, FOR_CODIGO, FOR_CNPJ, FOR_PARCEIRO, FOR_CONTACONTABIL, FOR_VALORBOLETO, PAR_CODIGO, AGE_CODIGO
                                         )
                                  VALUES ( vCODIGOFORNECEDOR, RCONTA.ID, RCONTA.CON_CNPJ, RCONTA.CON_DESCRICAO, RCONTA.CON_CONTA, 
                                           RCONTA.CON_VALORBOLETO, pPAR_CODIGO, pAGE_CODIGO  
                                         ); 
       vCONTAFORNECEDOR:= vCONTAFORNECEDOR + 1;
       vGUARDACONTA    := RCONTA.CON_CONTA;
       vACHOUFORNECEDOR:= 1;
      END LOOP; 

      IF (vCONTAFORNECEDOR=1) AND (vCODIGOCONTABIL = 0) THEN
       vCODIGOCONTABIL   := vGUARDACONTA; 
      END IF;

     END IF;    
   --FIM - VERIFICAR CONTA CONTABIL...................................................
    END IF;   
    END IF;
   END LOOP; 



   --INICIO - VERIFICAR NOTAFISCAL...................................................
   IF  vACHOUFORNECEDOR > 0 THEN
    vACHOUNOTA:= 0;
    
    FOR RFORNECEDOR IN 
    SELECT *
    FROM  ${schema}.FORNECEDOR
    WHERE FOR_SEQUENCIA = vCODIGOFORNECEDOR
      AND PAR_CODIGO= pPAR_CODIGO
      AND AGE_CODIGO= pAGE_CODIGO
    LOOP
       
     FOR RNOTA IN
     SELECT * 
     FROM ${schema}.NOTAFISCAL  
     WHERE ID>0 AND TNO_CODIGO=0
      AND PARCEIRO_ID= pPAR_CODIGO 
      AND TRIM(NOT_CNPJ) = TRIM(RFORNECEDOR.FOR_CNPJ)
     LOOP 
      --VERIFICAÇÃO 1 - INICIO........................................................................................................
      IF (RNOTA.NOT_DATAPARCELA = vDATAVENCIMENTO) AND (RNOTA.NOT_VALORPARCELA = vVALORDOCUMENTO) AND (vACHOUNOTA=0) THEN
       vHISTORICOFINAL   := 'Pagto. NFe '|| RNOTA.NOT_NUMERO || '/' || RNOTA.NOT_PARCELA || ' de ' || RNOTA.NOT_EMPRESA;
       vCODIGONOTA       := RNOTA.ID;   
       vNIVEL            := 1;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CONTACONTABIL; 
       END IF; 
      END IF;
     --VERIFICAÇÃO 1 - FIM........................................................................................................
 

      --VERIFICAÇÃO 2 - INICIO........................................................................................................
      IF (vDATAVENCIMENTO>RNOTA.NOT_DATASAIDA) AND (vDATAVENCIMENTO < (RNOTA.NOT_DATASAIDA + INTERVAL '60 DAY')) AND (RNOTA.NOT_VALORNOTA = vVALORDOCUMENTO) AND (vACHOUNOTA=0) THEN     
       vHISTORICOFINAL   := 'Pagto. NFe '|| RNOTA.NOT_NUMERO || ' de ' || RNOTA.NOT_EMPRESA;
       vCODIGONOTA       := RNOTA.ID;   
       vNIVEL            := 2;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CONTACONTABIL; 
       END IF; 
      END IF;
     --VERIFICAÇÃO 2 - FIM........................................................................................................

      --VERIFICAÇÃO 3 - INICIO........................................................................................................
      IF (vDATAVENCIMENTO>RNOTA.NOT_DATASAIDA) AND (vDATAVENCIMENTO < (RNOTA.NOT_DATASAIDA + INTERVAL '60 DAY')) 
      AND (RNOTA.NOT_VALORNOTA > vVALORDOCUMENTO) AND (vVALORDOCUMENTO > 0 ) AND (RNOTA.NOT_VALORNOTA > 0) AND (vACHOUNOTA=0) THEN   

      vQUEBRAPARCELA   := RNOTA.NOT_VALORNOTA / vVALORDOCUMENTO;
      SELECT round( vQUEBRAPARCELA, 3 )
      INTO vQUEBRAPARCELA;

      IF ((vQUEBRAPARCELA= 1.000) OR (vQUEBRAPARCELA= 2.000) OR (vQUEBRAPARCELA= 3.000) OR (vQUEBRAPARCELA= 4.000) OR (vQUEBRAPARCELA= 5.000))   THEN
--       vHISTORICOFINAL   := 'Pagto. NFe '|| RNOTA.NOT_NUMERO || '/' || RNOTA.NOT_PARCELA || ' de ' || RNOTA.NOT_EMPRESA;
       vHISTORICOFINAL   := 'Pagto. Parcial NFe '|| RNOTA.NOT_NUMERO || ' de ' || RNOTA.NOT_EMPRESA;

       vCODIGONOTA       := RNOTA.ID;   
       vNIVEL            := 3;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CONTACONTABIL; 
       END IF; 
      END IF; 
      END IF;
     --VERIFICAÇÃO 3 - FIM........................................................................................................

      --VERIFICAÇÃO 4 - INICIO........................................................................................................
      IF (RNOTA.NOT_DATAPARCELA = vDATAVENCIMENTO) AND ((RNOTA.NOT_VALORPARCELA+RFORNECEDOR.FOR_VALORBOLETO) = vVALORDOCUMENTO) AND (vACHOUNOTA=0) THEN
       vHISTORICOFINAL   := 'Pagto. NFe '|| RNOTA.NOT_NUMERO || '/' || RNOTA.NOT_PARCELA || ' de ' || RNOTA.NOT_EMPRESA;
       vCODIGONOTA       := RNOTA.ID;   
       vNIVEL            := 4;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CONTACONTABIL; 
       END IF; 
      END IF;
     --VERIFICAÇÃO 4 - FIM........................................................................................................
      
     END LOOP; 
    END LOOP;
    END IF;


    --INICIO - VERIFICAR NOTAFISCAL...................................................


   

   IF (vNIVEL = 0) THEN
    FOR RREGRA IN
    SELECT *
    FROM ${schema}.REGRA
    WHERE PARCEIRO_ID= pPAR_CODIGO
    LOOP 
     IF (RREGRA.REG_DESCRICAO = vHISTORICO) OR (RREGRA.REG_DESCRICAO = vBENEFICIARIO) THEN
      vCODIGOCONTABIL := RREGRA.REG_CONTA;
      vHISTORICOFINAL := RREGRA.REG_HISTORICO;
      vNIVEL          := 5; 
      vASSOCIADO      := TRUE;        
     END IF;  

    END LOOP;
    
   END IF;   

   IF (vHISTORICOFINAL='')  and (vCODIGOCONTABIL > 0)  THEN
     vHISTORICOFINAL :=  'Pagto de '||vBENEFICIARIO;
     vNIVEL          := 9; 
     vASSOCIADO      := TRUE; 
   END IF;  

  IF vCODIGOCONTABIL = 0 THEN
   vCODIGOCONTABIL:= NULL;
  END IF;
  
  IF vCODIGONOTA = 0 THEN
   vCODIGONOTA:= NULL;
  END IF;
  
  IF vCOMPROVANTE = 0 THEN
   vCOMPROVANTE:= NULL;
  END IF;

  IF vCONTROLAVALOR = 0 THEN
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datainicio, datafim, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, pPAR_CODIGO, pAGE_CODIGO, pDATAINICIO, pDATAFIM, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vDEBITO, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, vCODIGOCONTABIL, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );
   END IF;
   
  IF vCONTROLAVALOR = 2 THEN
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datainicio, datafim, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, pPAR_CODIGO, pAGE_CODIGO, pDATAINICIO, pDATAFIM, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDOCUMENTO, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, vCODIGOCONTABIL, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );

      vHISTORICOFINAL   := 'Juros '||vHISTORICOFINAL;                               
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datainicio, datafim, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, pPAR_CODIGO, pAGE_CODIGO, pDATAINICIO, pDATAFIM, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDIFERENTE, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, 284, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );
   END IF;

  IF vCONTROLAVALOR = 1 THEN
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datainicio, datafim, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, pPAR_CODIGO, pAGE_CODIGO, pDATAINICIO, pDATAFIM, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDOCUMENTO, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, vCODIGOCONTABIL, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );

      vHISTORICOFINAL   := 'Desconto '||vHISTORICOFINAL;                               
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datainicio, datafim, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, pPAR_CODIGO, pAGE_CODIGO, pDATAINICIO, pDATAFIM, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDIFERENTE, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, 285, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );
   END IF;



   DELETE FROM ${schema}.FORNECEDOR WHERE FOR_SEQUENCIA= vCODIGOFORNECEDOR;    
   DELETE FROM ${schema}.AGUARDANDO WHERE AGU_SEQUENCIA= vCODIGOAGUARDANDO;    
   
  vRETORNO:= vRETORNO + 1;
  END LOOP;
  CLOSE cEXTRATOBANCARIO;
  

  RETURN COALESCE(vRETORNO ,0);

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION ${schema}.processa_inteligente(int8, int8, date, date)
  OWNER TO postgres;
