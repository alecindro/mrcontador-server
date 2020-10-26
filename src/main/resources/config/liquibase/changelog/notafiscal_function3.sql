CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscal3("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
REC   RECORD; 
vRETORNO NUMERIC;
vRETORNOCONTA numeric;
vHISTORICOFINAL TEXT;
vTAXA NUMERIC;
BEGIN
  vRETORNO:= 0;
FOR REC IN 
SELECT NF.ID AS NOTAFISCAL_ID, NF.NOT_NUMERO AS vNUMERONOTA, 
NF.not_valorparcela AS vPARCELANOTA,NF.NOT_EMPRESA AS vEMPRESANOTA, 
I.ID AS INTELIGENT_ID, I.HISTORICOFINAL as vHISTORICOFINAL, I.TIPO_VALOR as vTIPOVALOR, I.datalancamento as vDATAEXTRATO, I.numerodocumento as vNUMERODOCUMENTO,
I.numerocontrole as vNUMEROCONTROLE, I.periodo as vPERIODO, I.cnpj as vCNPJ, I.beneficiario as vBENEFICIARIO, I.parceiro_id as vPARCEIROID, 
I.agenciabancaria_id as vAGENCIABANCARIAID, I.extrato_id as vCODIGEXTRATO,I.comprovante_id as vCOMPROVANTEID,
C.com_valorpagamento  as vVALORPAGAMENTO, C.com_juros as vCOMJUROS, C.com_multa as vCOMMULTA, C.com_desconto AS vDESCONTO
    FROM ${schema}.INTELIGENT I INNER JOIN
    ${schema}.COMPROVANTE C ON I.COMPROVANTE_ID = C.ID
    , ${schema}.NOTAFISCAL NF 
    WHERE I.ASSOCIADO = false
    and I.tipo_valor = 'PRINCIPAL'
    and (C.COM_CNPJ = NF.NOT_CNPJ or substring(C.COM_CNPJ,1,8) = substring(NF.NOT_CNPJ,1,8))
    and NF.NOT_VALORPARCELA+10 >= C.COM_VALORDOCUMENTO
    and NF.NOT_VALORPARCELA <= C.COM_VALORDOCUMENTO
    AND C.TIPO_COMPROVANTE = 'TITULO'
    and NF.tno_codigo =0
    AND NF.ID = pNOT_CODIGO
 loop
    vTAXA:= REC.vVALORPAGAMENTO - REC.vPARCELANOTA - REC.vCOMJUROS - REC.vCOMMULTA + REC.vDESCONTO;
 	    vHISTORICOFINAL   := 'Pagto. NFe '|| REC.vNUMERONOTA || '/' || REC.vPARCELANOTA || ' de ' || REC.vEMPRESANOTA;
    IF (vTAXA > 0) THEN
	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = REC.NOTAFISCAL_ID, tipo_inteligent ='C'  where id = REC.INTELIGENT_ID;
	   	INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
	     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, notafiscal_id) VALUES ('Pagto. de Taxa bancária','TAXA',REC.vDATAEXTRATO,REC.vNUMERODOCUMENTO,
	     		REC.vNUMEROCONTROLE,REC.vPERIODO, vTAXA*-1,false,
	     		REC.vCNPJ,REC.vBENEFICIARIO, 'C',REC.vCOMPROVANTEID,REC.vPARCEIROID,REC.vAGENCIABANCARIAID, REC.vCODIGEXTRATO, REC.NOTAFISCAL_ID);
   	else
	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = REC.NOTAFISCAL_ID  where id = REC.INTELIGENT_ID;
   	END IF;
   	SELECT ${schema}.PROCESSA_CONTA(CAST(REC.INTELIGENT_ID AS int8)) into vRETORNOCONTA;
	vRETORNO:= vRETORNO + 1;
 END LOOP; 
RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
