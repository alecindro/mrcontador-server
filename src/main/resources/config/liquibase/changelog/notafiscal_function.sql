CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscalbb("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pNOT_CODIGO ALIAS FOR $1;  
  
  
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
  REC   RECORD;
  RNOTA RECORD;
  RREGRA RECORD;
  RCONTA RECORD;
  RFORNECEDOR RECORD;
  RAGENCIABANCARIA RECORD;
  
  
  vCNPJ TEXT;
  vBENEFICIARIO TEXT;
  vCOMPROVANTE NUMERIC;
  vDATAVENCIMENTO DATE;  
  vDATAPAGAMENTO DATE;    
  vVALORDOCUMENTO NUMERIC;
  vVALORPAGAMENTO NUMERIC;
  vVALORDIFERENTE NUMERIC;
  vCODIGOINTELIGENTE NUMERIC;
  vCODIGOCOMPROVANTE NUMERIC;
  pPAR_CODIGO NUMERIC;  
  pAGE_CODIGO NUMERIC;  



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
  vAPLICACAO BOOLEAN;
  vAGENCIAAPLICACAO NUMERIC;  

  vNUMERONOTA TEXT;
  vCNPJNOTA TEXT;
  vEMPRESANOTA TEXT;
  vDATASAIDANOTA DATE;    
  vVALORNOTA NUMERIC;
  vDATAPARCELANOTA DATE;    
  vVALORPARCELANOTA NUMERIC;
  vPARCELANOTA VARCHAR(10);
  vPARCEIRONOTA NUMERIC;
  
  
  cEXTRATOBANCARIO CURSOR FOR
  SELECT ID, NOT_NUMERO, NOT_CNPJ, NOT_EMPRESA, NOT_DATASAIDA, NOT_VALORNOTA, NOT_DATAPARCELA, NOT_VALORPARCELA, NOT_PARCELA, PARCEIRO_ID 
  FROM ${schema}.NOTAFISCAL  
  WHERE ID>0 AND TNO_CODIGO=0
    AND ID = pNOT_CODIGO;
  

  
BEGIN
 
  vRETORNO:= 0;

  
   OPEN cEXTRATOBANCARIO;
  LOOP
  FETCH cEXTRATOBANCARIO INTO vCODIGONOTA, vNUMERONOTA, vCNPJNOTA, vEMPRESANOTA, vDATASAIDANOTA, vVALORNOTA, vDATAPARCELANOTA, vVALORPARCELANOTA, vPARCELANOTA, vPARCEIRONOTA ;
  EXIT WHEN NOT FOUND;


   vCODIGOCONTABIL   := 0;
   vHISTORICOFINAL   := '';
   vNIVEL            := 0;  
   vASSOCIADO        := FALSE;  
   vCONTAAGUARDANDO  := 0;
   vACHOUFORNECEDOR  := 0;
   vCONTROLAVALOR    := 0;
   vCODIGOINTELIGENTE:= 0;
   vVALORDIFERENTE   := 0;

   SELECT nextval('${schema}.seq_aguardando')
   INTO vCODIGOAGUARDANDO;

   SELECT nextval('${schema}.seq_fornecedor')
   INTO vCODIGOFORNECEDOR;


     IF vCNPJNOTA <> '' THEN
     --INICIO - VERIFICAR CONTA CONTABIL...................................................
     FOR RCONTA IN
     SELECT *
     FROM ${schema}.CONTA  
     WHERE PARCEIRO_ID= vPARCEIRONOTA 
       AND TRIM(CON_CNPJ) = TRIM(vCNPJNOTA)
       AND CON_CNPJ <> ''
     LOOP 
      IF (vCODIGOCONTABIL = 0)THEN
       INSERT INTO ${schema}.FORNECEDOR    ( FOR_SEQUENCIA, FOR_CODIGO, FOR_CNPJ, FOR_PARCEIRO, FOR_CONTACONTABIL, FOR_VALORBOLETO, PAR_CODIGO
                                         )
                                  VALUES ( vCODIGOFORNECEDOR, RCONTA.ID, RCONTA.CON_CNPJ, RCONTA.CON_DESCRICAO, RCONTA.CON_CONTA, 
                                           RCONTA.CON_VALORBOLETO, vPARCEIRONOTA  
                                         ); 
       vCODIGOCONTABIL       := RCONTA.ID;
       vACHOUFORNECEDOR      := 1; 
      END IF;    
     END LOOP; 

     IF (vCODIGOCONTABIL = 0)THEN
      vCONTAFORNECEDOR   := 0;
      vGUARDACONTA       := 0;
   
      FOR RCONTA IN
      SELECT *
      FROM ${schema}.CONTA  
      WHERE PARCEIRO_ID= vPARCEIRONOTA 
        AND SUBSTRING(TRIM(CON_CNPJ), 1 , 8) = SUBSTRING(TRIM(vCNPJNOTA), 1 , 8)
        AND CON_CNPJ <> ''
      LOOP 
       INSERT INTO ${schema}.FORNECEDOR ( FOR_SEQUENCIA, FOR_CODIGO, FOR_CNPJ, FOR_PARCEIRO, FOR_CONTACONTABIL, FOR_VALORBOLETO, PAR_CODIGO
                                         )
                                  VALUES ( vCODIGOFORNECEDOR, RCONTA.ID, RCONTA.CON_CNPJ, RCONTA.CON_DESCRICAO, RCONTA.CON_CONTA, 
                                           RCONTA.CON_VALORBOLETO, vPARCEIRONOTA  
                                         ); 
       vCONTAFORNECEDOR:= vCONTAFORNECEDOR + 1;
       vGUARDACONTA    := RCONTA.ID;
       vACHOUFORNECEDOR:= 1;
      END LOOP; 

      IF (vCONTAFORNECEDOR=1) AND (vCODIGOCONTABIL = 0) THEN
       vCODIGOCONTABIL   := vGUARDACONTA; 
      END IF;

     END IF;    
   --FIM - VERIFICAR CONTA CONTABIL...................................................
    END IF;   






   --INICIO - VERIFICAR NOTAFISCAL...................................................
   IF  vACHOUFORNECEDOR > 0 THEN
    vACHOUNOTA:= 0;
    
    FOR RFORNECEDOR IN 
    SELECT *
    FROM  ${schema}.FORNECEDOR
    WHERE FOR_SEQUENCIA = vCODIGOFORNECEDOR
      AND PAR_CODIGO= vPARCEIRONOTA 
    LOOP
       
     FOR RNOTA IN
     SELECT  INTELIGENT.ID, COM_CNPJ, COM_BENEFICIARIO, COM_DATAVENCIMENTO, COM_DATAPAGAMENTO, COM_VALORDOCUMENTO, COM_VALORPAGAMENTO, NUMERODOCUMENTO, 
             INTELIGENT.COMPROVANTE_ID, INTELIGENT.EXTRATO_ID, INTELIGENT.AGENCIABANCARIA_ID 
     FROM ${schema}.INTELIGENT 
       INNER JOIN ${schema}.COMPROVANTE ON COMPROVANTE.ID=INTELIGENT.COMPROVANTE_ID
     WHERE INTELIGENT.PARCEIRO_ID= vPARCEIRONOTA
        AND ASSOCIADO= FALSE
        AND COALESCE(INTELIGENT.NOTAFISCAL_ID,0) = 0  
        AND TRIM(COM_CNPJ) = TRIM(RFORNECEDOR.FOR_CNPJ)
     ORDER BY INTELIGENT.ID

     LOOP 
      --VERIFICAÇÃO 1 - INICIO........................................................................................................
      IF (RNOTA.COM_DATAVENCIMENTO= vDATAPARCELANOTA) AND (RNOTA.COM_VALORDOCUMENTO=vVALORPARCELANOTA) AND (vACHOUNOTA=0) THEN
       vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA;
       vCODIGOINTELIGENTE:= RNOTA.ID; 
       vNIVEL            := 1;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       vCODIGEXTRATO    := RNOTA.EXTRATO_ID;
       pAGE_CODIGO      := RNOTA.AGENCIABANCARIA_ID;
       vDATAEXTRATO     := RNOTA.COM_DATAPAGAMENTO;
       vNUMERODOCUMENTO := RNOTA.NUMERODOCUMENTO;
       vCOMPROVANTE     := RNOTA.COMPROVANTE_ID;
       vCNPJ            := RNOTA.COM_CNPJ;
       vBENEFICIARIO    := RNOTA.COM_BENEFICIARIO;

       
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CODIGO; 
       END IF; 

      IF (RNOTA.COM_VALORDOCUMENTO>0) AND (RNOTA.COM_VALORDOCUMENTO<>RNOTA.COM_VALORPAGAMENTO) AND (RNOTA.COM_DATAPAGAMENTO<>RNOTA.COM_DATAVENCIMENTO) THEN 
       IF (RNOTA.COM_VALORDOCUMENTO > RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  :=1;
        vVALORDIFERENTE := RNOTA.COM_VALORDOCUMENTO - RNOTA.COM_VALORPAGAMENTO;
       END IF;

       IF (RNOTA.COM_VALORDOCUMENTO < RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  := 2;
        vVALORDIFERENTE := RNOTA.COM_VALORPAGAMENTO - RNOTA.COM_VALORDOCUMENTO;
       END IF;
      END IF;

       
      END IF;
     --VERIFICAÇÃO 1 - FIM........................................................................................................


      --VERIFICAÇÃO 2 - INICIO........................................................................................................
      IF (RNOTA.COM_DATAVENCIMENTO>vDATASAIDANOTA) AND (RNOTA.COM_DATAVENCIMENTO < (vDATASAIDANOTA + INTERVAL '60 DAY')) AND (vVALORNOTA = RNOTA.COM_VALORDOCUMENTO) AND (vACHOUNOTA=0) THEN     
       vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || ' de ' || vEMPRESANOTA;
       vCODIGOINTELIGENTE:= RNOTA.ID;   
       vNIVEL            := 2;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       vCODIGEXTRATO    := RNOTA.EXTRATO_ID;
       pAGE_CODIGO      := RNOTA.AGENCIABANCARIA_ID;
       vDATAEXTRATO     := RNOTA.COM_DATAPAGAMENTO;
       vNUMERODOCUMENTO := RNOTA.NUMERODOCUMENTO;
       vCOMPROVANTE     := RNOTA.COMPROVANTE_ID;
       vCNPJ            := RNOTA.COM_CNPJ;
       vBENEFICIARIO    := RNOTA.COM_BENEFICIARIO;

       
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CODIGO; 
       END IF; 

      IF (RNOTA.COM_VALORDOCUMENTO>0) AND (RNOTA.COM_VALORDOCUMENTO<>RNOTA.COM_VALORPAGAMENTO) AND (RNOTA.COM_DATAPAGAMENTO<>RNOTA.COM_DATAVENCIMENTO) THEN 
       IF (RNOTA.COM_VALORDOCUMENTO > RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  :=1;
        vVALORDIFERENTE := RNOTA.COM_VALORDOCUMENTO - RNOTA.COM_VALORPAGAMENTO;
       END IF;

       IF (RNOTA.COM_VALORDOCUMENTO < RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  := 2;
        vVALORDIFERENTE := RNOTA.COM_VALORPAGAMENTO - RNOTA.COM_VALORDOCUMENTO;
       END IF;
      END IF;
       
      END IF;
     --VERIFICAÇÃO 2 - FIM........................................................................................................

   
      --VERIFICAÇÃO 3 - INICIO........................................................................................................
      IF (RNOTA.COM_DATAVENCIMENTO>vDATASAIDANOTA ) AND (RNOTA.COM_DATAVENCIMENTO < (vDATASAIDANOTA + INTERVAL '60 DAY')) 
      AND (vVALORNOTA > RNOTA.COM_VALORDOCUMENTO) AND (RNOTA.COM_VALORDOCUMENTO > 0 ) AND (vVALORNOTA > 0) AND (vACHOUNOTA=0) THEN   

      vQUEBRAPARCELA   := vVALORNOTA / RNOTA.COM_VALORDOCUMENTO;
      SELECT round( vQUEBRAPARCELA, 3 )
      INTO vQUEBRAPARCELA;

      IF ((vQUEBRAPARCELA= 1.000) OR (vQUEBRAPARCELA= 2.000) OR (vQUEBRAPARCELA= 3.000) OR (vQUEBRAPARCELA= 4.000) OR (vQUEBRAPARCELA= 5.000))   THEN
       vHISTORICOFINAL   := 'Pagto. Parcial NFe '|| vNUMERONOTA || ' de ' || vEMPRESANOTA;
       vCODIGOINTELIGENTE:= RNOTA.ID;    
       vNIVEL            := 3;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       vCODIGEXTRATO    := RNOTA.EXTRATO_ID;
       pAGE_CODIGO      := RNOTA.AGENCIABANCARIA_ID;
       vDATAEXTRATO     := RNOTA.COM_DATAPAGAMENTO;
       vNUMERODOCUMENTO := RNOTA.NUMERODOCUMENTO;
       vCOMPROVANTE     := RNOTA.COMPROVANTE_ID;
       vCNPJ            := RNOTA.COM_CNPJ;
       vBENEFICIARIO    := RNOTA.COM_BENEFICIARIO;

       
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CODIGO; 
       END IF; 

       IF (RNOTA.COM_VALORDOCUMENTO>0) AND (RNOTA.COM_VALORDOCUMENTO<>RNOTA.COM_VALORPAGAMENTO) AND (RNOTA.COM_DATAPAGAMENTO<>RNOTA.COM_DATAVENCIMENTO) THEN 
       IF (RNOTA.COM_VALORDOCUMENTO > RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  :=1;
        vVALORDIFERENTE := RNOTA.COM_VALORDOCUMENTO - RNOTA.COM_VALORPAGAMENTO;
       END IF;

       IF (RNOTA.COM_VALORDOCUMENTO < RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  := 2;
        vVALORDIFERENTE := RNOTA.COM_VALORPAGAMENTO - RNOTA.COM_VALORDOCUMENTO;
       END IF;
       END IF;
       
      END IF; 
      END IF;
     --VERIFICAÇÃO 3 - FIM........................................................................................................

      --VERIFICAÇÃO 4 - INICIO........................................................................................................
      IF (vDATAPARCELANOTA = RNOTA.COM_DATAVENCIMENTO) AND ((vVALORPARCELANOTA+RFORNECEDOR.FOR_VALORBOLETO) = RNOTA.COM_VALORDOCUMENTO) AND (vACHOUNOTA=0) THEN
       vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || '/' || vPARCELANOTA || ' de ' || vEMPRESANOTA;
       vCODIGOINTELIGENTE:= RNOTA.ID;   
       vNIVEL            := 4;        
       vASSOCIADO        := TRUE;  
       vACHOUNOTA        := 1;
       vCODIGEXTRATO    := RNOTA.EXTRATO_ID;
       pAGE_CODIGO      := RNOTA.AGENCIABANCARIA_ID;
       vDATAEXTRATO     := RNOTA.COM_DATAPAGAMENTO;
       vNUMERODOCUMENTO := RNOTA.NUMERODOCUMENTO;
       vCOMPROVANTE     := RNOTA.COMPROVANTE_ID;
       vCNPJ            := RNOTA.COM_CNPJ;
       vBENEFICIARIO    := RNOTA.COM_BENEFICIARIO;

       
       IF (vCONTAFORNECEDOR>1) THEN
        vCODIGOCONTABIL   := RFORNECEDOR.FOR_CODIGO; 
       END IF; 

       IF (RNOTA.COM_VALORDOCUMENTO>0) AND (RNOTA.COM_VALORDOCUMENTO<>RNOTA.COM_VALORPAGAMENTO) AND (RNOTA.COM_DATAPAGAMENTO<>RNOTA.COM_DATAVENCIMENTO) THEN 
       IF (RNOTA.COM_VALORDOCUMENTO > RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  :=1;
        vVALORDIFERENTE := RNOTA.COM_VALORDOCUMENTO - RNOTA.COM_VALORPAGAMENTO;
       END IF;

       IF (RNOTA.COM_VALORDOCUMENTO < RNOTA.COM_VALORPAGAMENTO) THEN
        vCONTROLAVALOR  := 2;
        vVALORDIFERENTE := RNOTA.COM_VALORPAGAMENTO - RNOTA.COM_VALORDOCUMENTO;
       END IF;
      END IF;
      
      END IF;
     --VERIFICAÇÃO 4 - FIM........................................................................................................
      
     END LOOP; 
    END LOOP;
    END IF;


    --INICIO - VERIFICAR NOTAFISCAL...................................................


  IF vCODIGOCONTABIL = 0 THEN
   vCODIGOCONTABIL:= NULL;
  END IF;
  
  IF vCODIGONOTA = 0 THEN
   vCODIGONOTA:= NULL;
  END IF;
  
  IF vCOMPROVANTE = 0 THEN
   vCOMPROVANTE:= NULL;
  END IF;


  IF vCODIGOINTELIGENTE > 0 THEN

    IF vCONTROLAVALOR = 0 THEN  
      UPDATE ${schema}.INTELIGENT SET CONTA_ID= vCODIGOCONTABIL,
                                    NOTAFISCAL_ID= vCODIGONOTA,
                                    HISTORICOFINAL= vHISTORICOFINAL,
                                    ASSOCIADO= vASSOCIADO
      WHERE ID = vCODIGOINTELIGENTE;

      UPDATE ${schema}.NOTAFISCAL SET PROCESSADO= TRUE  
      WHERE ID = vCODIGONOTA;
      
      vRETORNO:= vRETORNO + 1;
    END IF;



    IF vCONTROLAVALOR = 2 THEN
      UPDATE ${schema}.INTELIGENT SET CONTA_ID= vCODIGOCONTABIL,
                                   NOTAFISCAL_ID= vCODIGONOTA,
                                   HISTORICOFINAL= vHISTORICOFINAL,
                                   DEBITO= vVALORDOCUMENTO,
                                   ASSOCIADO= vASSOCIADO
      WHERE ID = vCODIGOINTELIGENTE;

      UPDATE ${schema}.NOTAFISCAL SET PROCESSADO= TRUE  
      WHERE ID = vCODIGONOTA;
      
      vRETORNO:= vRETORNO + 1;

      SELECT (replace(vHISTORICOFINAL, 'Pagto.', 'Pagto. de Juros sobre')) 
      INTO vHISTORICOFINAL;

      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO, vPARCEIRONOTA , pAGE_CODIGO, vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDIFERENTE, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, 1126, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );
    END IF;


  IF vCONTROLAVALOR = 1 THEN
      UPDATE ${schema}.INTELIGENT SET CONTA_ID= vCODIGOCONTABIL,
                                   NOTAFISCAL_ID= vCODIGONOTA,
                                   HISTORICOFINAL= vHISTORICOFINAL,
                                   DEBITO= vVALORDOCUMENTO,
                                   ASSOCIADO= vASSOCIADO
      WHERE ID = vCODIGOINTELIGENTE;

      UPDATE ${schema}.NOTAFISCAL SET PROCESSADO= TRUE  
      WHERE ID = vCODIGONOTA;
      
      vRETORNO:= vRETORNO + 1;
                                    

      vHISTORICOFINAL   := 'Desconto '||vHISTORICOFINAL;    
                                 
      INSERT INTO ${schema}.INTELIGENT ( extrato_id, parceiro_id, agenciabancaria_id, datalancamento,
                                       historico, NUMERODOCUMENTO, NUMEROCONTROLE, DEBITO, CREDITO, comprovante_id,
                                       CNPJ, BENEFICIARIO, conta_id, notafiscal_id, HISTORICOFINAL, periodo, associado
                                      )
                              VALUES ( vCODIGEXTRATO,  vPARCEIRONOTA, pAGE_CODIGO,  vDATAEXTRATO,
                                       vHISTORICO, vNUMERODOCUMENTO, vNUMEROCONTROLE, vVALORDIFERENTE, vCREDITO, vCOMPROVANTE,
                                       vCNPJ, vBENEFICIARIO, 1129, vCODIGONOTA, vHISTORICOFINAL, concat(extract(month from vDATAEXTRATO),extract(year from vDATAEXTRATO)), vASSOCIADO
                                     );
   END IF;


     

   END IF;



   DELETE FROM ${schema}.FORNECEDOR WHERE FOR_SEQUENCIA= vCODIGOFORNECEDOR;    
   DELETE FROM ${schema}.AGUARDANDO WHERE AGU_SEQUENCIA= vCODIGOAGUARDANDO;    
   

  END LOOP;
  CLOSE cEXTRATOBANCARIO;
  

  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
