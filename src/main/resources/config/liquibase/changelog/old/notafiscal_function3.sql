CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscal3("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
REC   RECORD; 
vRETORNO NUMERIC;
vHISTORICOFINAL TEXT;
vTAXA NUMERIC;
vNOTCNPJ TEXT;
vNOTDATAPARCELA DATE;
vNOTVALORPARCELA NUMERIC;
vNOTNUMERO TEXT;
vVALORPAGAMENTO NUMERIC;
vCOMJUROS NUMERIC;
vCOMMULTA NUMERIC;
vDESCONTO NUMERIC;
vINTELIGENTID NUMERIC;
vDATALANCAMENTO DATE;
vNUMERODOCUMENTO TEXT;
vBENEFICIARIO TEXT;
vNUMEROCONTROLE TEXT;
vPERIODO TEXT;
vCOMPROVANTEID NUMERIC;
vPARCEIROID NUMERIC;
vAGENCIABANCARIAID NUMERIC;
vEXTRATOID NUMERIC;
vICNPJ TEXT;



BEGIN
  vRETORNO:= 0;
  SELECT NOT_CNPJ, NOT_DATAPARCELA, NOT_VALORPARCELA, NOT_NUMERO, PARCEIRO_ID
   INTO vNOTCNPJ, vNOTDATAPARCELA, vNOTVALORPARCELA,vNOTNUMERO, vPARCEIROID
   FROM ${schema}.NOTAFISCAL WHERE ID = pNOT_CODIGO;
  

SELECT C.COM_VALORPAGAMENTO, C.COM_JUROS,C.COM_MULTA,C.COM_DESCONTO, I.ID, I.DATALANCAMENTO, I.NUMERODOCUMENTO, 
I.NUMEROCONTROLE, I.PERIODO, I.CNPJ, I.BENEFICIARIO, I.COMPROVANTE_ID, I.AGENCIABANCARIA_ID, I.EXTRATO_ID
INTO vVALORPAGAMENTO,vCOMJUROS, vCOMMULTA, vDESCONTO, vINTELIGENTID, vDATALANCAMENTO, vNUMERODOCUMENTO, vNUMEROCONTROLE,vPERIODO, vICNPJ, vBENEFICIARIO, vCOMPROVANTEID, vAGENCIABANCARIAID, vEXTRATOID
FROM ${schema}.INTELIGENT I INNER JOIN COMPROVANTE C ON I.COMPROVANTE_ID = C.ID
    WHERE I.ASSOCIADO = false
    AND I.tipo_valor = 'PRINCIPAL'
    AND substring(C.COM_CNPJ,1,8) = substring(vNOTCNPJ,1,8)
    AND vNOTVALORPARCELA between C.COM_VALORDOCUMENTO and C.COM_VALORDOCUMENTO+10
    AND C.TIPO_COMPROVANTE = 'TITULO'
    AND C.PARCEIRO_ID = vPARCEIROID
    AND I.PARCEIRO_ID = vPARCEIROID
    order by ID DESC
	FETCH FIRST ROW ONLY;
    

    vTAXA:= REC.vVALORPAGAMENTO - vNOTVALORPARCELA - REC.vCOMJUROS - REC.vCOMMULTA + REC.vDESCONTO;
 	    vHISTORICOFINAL   := 'Pagto. NFe '|| REC.vNUMERONOTA || '/' || REC.vNPARCELA || ' de ' || REC.vEMPRESANOTA;
    update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = pNOT_CODIGO  where id = vINTELIGENTID;
       IF (vTAXA > 0) THEN
       	update ${schema}.INTELIGENT set tipo_inteligent ='C'  where id = REC.INTELIGENT_ID;
	   	vHISTORICOFINAL   := 'Pagto. de juros NFe '|| REC.vNUMERONOTA || '/' || REC.vNPARCELA || ' de ' || REC.vEMPRESANOTA;
	   	INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
	     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, notafiscal_id) VALUES ('Pagto. de Taxa bancária','TAXA',vDATALANCAMENTO,vNUMERODOCUMENTO,
	     		vNUMEROCONTROLE,vPERIODO, vTAXA*-1,false,
	     		vICNPJ,vBENEFICIARIO, 'C',vCOMPROVANTEID,vPARCEIROID,vAGENCIABANCARIAID, vEXTRATOID, pNOT_CODIGO);
	     		vRETORNO:= vRETORNO + 1;
   	END IF;
   	
   	SELECT ${schema}.PROCESSA_CONTA(CAST(REC.INTELIGENT_ID AS int8)) into vRETORNO;
	vRETORNO:= vRETORNO + 1;

RETURN COALESCE(vRETORNO ,0);
END;
$function$
;