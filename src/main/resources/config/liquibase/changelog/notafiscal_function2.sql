CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscal2("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare

pNOT_CODIGO ALIAS for $1;
REC   RECORD; 
vRETORNO NUMERIC;
vHISTORICOFINAL TEXT;
vTAXA NUMERIC;
vRETORNOCONTA numeric;
vNOTAFISCALID numeric;
vNUMERONOTA TEXT;
vVALORPARCELANOTA numeric;
vEMPRESANOTA TEXT;
vNPARCELA TEXT;
vNOTDATAPARCELA DATE;
vNOTCNPJ TEXT;
vCONTATARIFA numeric;
vPARCEIROID numeric;

BEGIN
  vRETORNO:= 0;  
SELECT NF.ID, NF.NOT_NUMERO, 
NF.NOT_VALORPARCELA,NF.NOT_EMPRESA, NF.not_parcela, NF.NOT_DATAPARCELA, NF.NOT_CNPJ, NF.PARCEIRO_ID 
INTO  vNOTAFISCALID, vNUMERONOTA,  vVALORPARCELANOTA, vEMPRESANOTA, 
vNPARCELA, vNOTDATAPARCELA, vNOTCNPJ, vPARCEIROID  
from ${schema}.NOTAFISCAL NF WHERE NF.tno_codigo =0 AND NF.ID  = pNOT_CODIGO;

SELECT DESPESA_TARIFA INTO vCONTATARIFA FROM  ${schema}.PARCEIRO WHERE ID = vPARCEIROID;


FOR REC IN 
SELECT I.ID AS INTELIGENT_ID, I.HISTORICOFINAL as vHISTORICOFINAL, I.TIPO_VALOR as vTIPOVALOR, 
I.datalancamento as vDATAEXTRATO, I.numerodocumento as vNUMERODOCUMENTO,
I.numerocontrole as vNUMEROCONTROLE, I.periodo as vPERIODO, I.cnpj as vCNPJ, 
I.beneficiario as vBENEFICIARIO, 
I.agenciabancaria_id as vAGENCIABANCARIAID, 
I.extrato_id as vCODIGEXTRATO, I.comprovante_id as vCOMPROVANTEID,
C.com_valorpagamento as vVALORPAGAMENTO, C.com_juros as vCOMJUROS, 
C.com_multa as vCOMMULTA, C.com_desconto AS vDESCONTO
    FROM ${schema}.INTELIGENT I INNER JOIN
    ${schema}.COMPROVANTE C ON I.COMPROVANTE_ID = C.ID
    WHERE I.ASSOCIADO = false
     and I.tipo_valor = 'PRINCIPAL'
    and (C.COM_CNPJ = vNOTCNPJ or substring(vNOTCNPJ,1,8) = substring(vNOTCNPJ,1,8))
    and vVALORPARCELANOTA +10 >= C.COM_VALORDOCUMENTO
    AND C.COM_DATAVENCIMENTO >= vNOTDATAPARCELA
    AND C.TIPO_COMPROVANTE = 'TITULO'
	AND I.PARCEIRO_ID = vPARCEIROID
	ORDER BY C.COM_VALORDOCUMENTO ASC
	LIMIT 1
	
 LOOP
    vTAXA:= REC.vVALORPAGAMENTO - vVALORPARCELANOTA - REC.vCOMJUROS - REC.vCOMMULTA + REC.vDESCONTO;
 	vHISTORICOFINAL   := 'Pagto. NFe '|| vNUMERONOTA || '/' || vNPARCELA || ' de ' || vEMPRESANOTA;
    IF (vTAXA > 0) THEN
    	 update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = vNOTAFISCALID, tipo_inteligent ='C'  where id = REC.INTELIGENT_ID;
    	 vHISTORICOFINAL   := 'Pagto. de taxa bancária ref. '|| vNUMERONOTA || '/' || vNPARCELA || ' de ' || vEMPRESANOTA;
	   	IF (vCONTATARIFA IS NOT NULL) THEN 
		INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
	     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, notafiscal_id, historicofinal, conta_id) VALUES ('Pagto. de Taxa bancária','TAXA',REC.vDATAEXTRATO,REC.vNUMERODOCUMENTO,
	     		REC.vNUMEROCONTROLE,REC.vPERIODO, vTAXA*-1,true,
	     		REC.vCNPJ,REC.vBENEFICIARIO, 'C',REC.vCOMPROVANTEID,vPARCEIROID,REC.vAGENCIABANCARIAID, REC.vCODIGEXTRATO, vNOTAFISCALID, vCONTATARIFA);
		SELECT ${schema}.PROCESSA_CONTA(CAST(REC.INTELIGENT_ID AS int8)) into vRETORNOCONTA;
		ELSE
		 update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = vNOTAFISCALID, tipo_inteligent ='C'  where id = REC.INTELIGENT_ID;
    	 vHISTORICOFINAL   := 'Pagto. de taxa bancária ref. '|| vNUMERONOTA || '/' || vNPARCELA || ' de ' || vEMPRESANOTA;
		INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
	     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, notafiscal_id, historicofinal) VALUES ('Pagto. de Taxa bancária','TAXA',REC.vDATAEXTRATO,REC.vNUMERODOCUMENTO,
	     		REC.vNUMEROCONTROLE,REC.vPERIODO, vTAXA*-1,false,
	     		REC.vCNPJ,REC.vBENEFICIARIO, 'C',REC.vCOMPROVANTEID,vPARCEIROID,REC.vAGENCIABANCARIAID, REC.vCODIGEXTRATO, vNOTAFISCALID);
		SELECT ${schema}.PROCESSA_CONTA(CAST(REC.INTELIGENT_ID AS int8)) into vRETORNOCONTA;
		END IF;		
   	else
 	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = vNOTAFISCALID  where id = REC.INTELIGENT_ID;
		SELECT ${schema}.PROCESSA_CONTA(CAST(REC.INTELIGENT_ID AS int8)) into vRETORNOCONTA;
   	END IF;
   	vRETORNO:= vRETORNO + 1;
 END LOOP; 
RETURN vRETORNO;
END;
$function$
;
