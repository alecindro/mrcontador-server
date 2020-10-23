CREATE OR REPLACE FUNCTION ds_demo.processa_notafiscal_semdata("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
REC   RECORD; 
vRETORNO NUMERIC;
vHISTORICOFINAL TEXT;
vTAXA NUMERIC;

BEGIN
  vRETORNO:= 0;
FOR REC IN 
SELECT NF.ID AS NOTAFISCAL_ID, NF.NOT_NUMERO AS vNUMERONOTA, 
NF.NOT_PARCELA AS vPARCELANOTA,NF.NOT_EMPRESA AS vEMPRESANOTA, 
I.ID AS INTELIGENT_ID, I.HISTORICOFINAL as vHISTORICOFINAL, I.TIPO_VALOR as vTIPOVALOR, I.datalancamento as vDATAEXTRATO, I.numerodocumento as vNUMERODOCUMENTO,
I.numerocontrole as vNUMEROCONTROLE, I.periodo as vPERIODO, I.cnpj as vCNPJ, I.beneficiario as vBENEFICIARIO, I.parceiro_id as vPARCEIROID, I.agenciabancaria_id as vAGENCIABANCARIAID, I.extrato_id as vCODIGEXTRATO,
C.com_valorpagamento as vVALORPAGAMENTO, C.com_juros as vCOMJUROS, C.com_multa as vCOMMULTA, C.com_desconto AS vDESCONTO
    FROM ds_demo.INTELIGENT I INNER JOIN
    ds_demo.COMPROVANTE C ON I.COMPROVANTE_ID = C.ID
    , ds_demo.NOTAFISCAL NF 
    WHERE I.ASSOCIADO = false
    and (C.COM_CNPJ = NF.NOT_CNPJ or substring(C.COM_CNPJ,1,8) = substring(NF.NOT_CNPJ,1,8))
    and NF.NOT_VALORPARCELA+10 >= C.COM_VALORDOCUMENTO
    and NF.NOT_VALORPARCELA <= C.COM_VALORDOCUMENTO
    AND C.TIPO_COMPROVANTE = 'TITULO'
    and NF.tno_codigo =0
    AND NF.ID = pNOT_CODIGO

 LOOP
    vTAXA:= vVALORPAGAMENTO-vPARCELANOTA-vCOMJUROS-vCOMMULTA+vDESCONTO;
 	IF (REC.vTIPOVALOR = 'PRINCIPAL') THEN
	    vHISTORICOFINAL   := 'Pagto. NFe '|| REC.vNUMERONOTA || '/' || REC.vPARCELANOTA || ' de ' || REC.vEMPRESANOTA;
    ELSE
	    vHISTORICOFINAL := REC.vHISTORICOFINAL || ' NFe '|| REC.vNUMERONOTA || '/' || REC.vPARCELANOTA || ' de ' || REC.vEMPRESANOTA;
    END IF;
    
    IF (vTAXA > 0) THEN
	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = REC.NOTAFISCAL_ID, tipo_inteligent ='C'  where id = REC.INTELIGENT_ID;
	   	INSERT INTO  ${schema}.INTELIGENT (historico, historicofinal,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
	     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id) VALUES ('Pagto. de Taxa bancária','Pagto. de Taxa bancária','TAXA',vDATAEXTRATO,vNUMERODOCUMENTO,vNUMEROCONTROLE,vPERIODO, VTAXA,false,
	     		vCNPJ,vBENEFICIARIO, 'C',vCOMPROVANTE,vPARCEIROID,vAGENCIABANCARIAID, vCODIGEXTRATO);
   	ELSE
	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = REC.NOTAFISCAL_ID  where id = REC.INTELIGENT_ID;
   	END IF;
   	
   	SELECT ${schema}.PROCESSA_CONTA(REC.INTELIGENT_ID);
	vRETORNO:= vRETORNO + 1;
 END LOOP; 
	if (vRETORNO > 0) THEN
	update ds_demo.notafiscal set processado = true where ID = pNOT_CODIGO;
	end if;
RETURN COALESCE(vRETORNO ,0);
END;
$function$
;
