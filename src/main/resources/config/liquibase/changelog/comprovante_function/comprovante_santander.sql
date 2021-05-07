CREATE OR REPLACE FUNCTION ${schema}.comprovante_santander("parceiroId" bigint, "agenciaID" bigint, "pPeriodo" character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pParceiroId ALIAS FOR $1;  
  pAgenciaId ALIAS FOR $2; 
  pPeriodo ALIAS FOR $3;    
  vRETORNO NUMERIC;
  REC_INTELIGENT   RECORD;
  selected_comprovante ${schema}.comprovante%rowtype;
  vHISTORICOFINAL TEXT;
  vTIPOINTELIGENTE TEXT;
  vMINDATE DATE;
  vRETORNO_NOTA NUMERIC;
  vDEBITO_INTELIGENT NUMERIC;
  
BEGIN
  vRETORNO:= 0;
   FOR REC_INTELIGENT IN
    SELECT  *
    FROM  ${schema}.INTELIGENT i 
    WHERE i.PARCEIRO_ID= pParceiroId
      AND i.AGENCIABANCARIA_ID= pAgenciaId
      AND i.ASSOCIADO = FALSE
      AND i.COMPROVANTE_ID is null
      and i.periodo = pPeriodo
    ORDER BY i.datalancamento asc 
   LOOP
 	SELECT * FROM ${schema}.comprovante into selected_comprovante 
	    where periodo = pPeriodo 
	 	and agenciabancaria_id = pAgenciaId
	 	and parceiro_id = pParceiroId
	 	and processado = false
	 	and com_datapagamento = REC_INTELIGENT.datalancamento
	 	and com_valorpagamento*-1 = REC_INTELIGENT.debito
	 	limit 1;
	 	vTIPOINTELIGENTE := 'x';
	 	IF (selected_comprovante.id is not null)THEN
	 	vDEBITO_INTELIGENT := REC_INTELIGENT.debito;
	 		vHISTORICOFINAL   := 'Pagto. de '|| selected_comprovante.com_beneficiario;
     		UPDATE  ${schema}.INTELIGENT SET COMPROVANTE_ID= selected_comprovante.id,CNPJ= selected_comprovante.com_cnpj,BENEFICIARIO= selected_comprovante.com_beneficiario,
     		 TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE ID = REC_INTELIGENT.id;
     		 UPDATE ${schema}.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = selected_comprovante.id; 
 	 		IF ((selected_comprovante.com_multa > 0) OR (selected_comprovante.com_juros >0)) THEN
 	 			vDEBITO_INTELIGENT := vDEBITO_INTELIGENT + selected_comprovante.com_juros + selected_comprovante.com_multa;
    			vTIPOINTELIGENTE := 'C';
    			vHISTORICOFINAL   := 'Pagto. de Juros de '|| selected_comprovante.com_beneficiario;
        	UPDATE  ${schema}.INTELIGENT SET debito = vDEBITO_INTELIGENT , tipo_inteligent = vTIPOINTELIGENTE WHERE ID = REC_INTELIGENT.id;
     		INSERT INTO  ${schema}.INTELIGENT (historico,tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Pagto. de Juros','JUROS',
     		REC_INTELIGENT.datalancamento,REC_INTELIGENT.numerodocumento,REC_INTELIGENT.numerocontrole,REC_INTELIGENT.periodo,  
     		(selected_comprovante.com_juros + selected_comprovante.com_multa)*-1,false,
     		selected_comprovante.com_cnpj,selected_comprovante.com_beneficiario, REC_INTELIGENT.tipo_inteligent,selected_comprovante.id,pParceiroId,pAgenciaId, REC_INTELIGENT.extrato_id, vHISTORICOFINAL);
		   	end if;    
     		IF (selected_comprovante.com_desconto < 0) THEN
    	    	vTIPOINTELIGENTE := 'D';
        		vHISTORICOFINAL   := 'Receb. de Desconto de '|| selected_comprovante.com_beneficiario;
      			UPDATE  ${schema}.INTELIGENT SET tipo_inteligent = vTIPOINTELIGENTE WHERE ID = REC_INTELIGENT.id;
     	        INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,credito,associado,
     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, historicofinal) VALUES ('Receb. de desconto','DESCONTO',
     		REC_INTELIGENT.datalancamento,REC_INTELIGENT.numerodocumento,REC_INTELIGENT.numerocontrole,REC_INTELIGENT.periodo, selected_comprovante.com_desconto*-1,false,
     		selected_comprovante.com_cnpj,selected_comprovante.com_beneficiario, REC_INTELIGENT.tipo_inteligent,selected_comprovante.id,pParceiroId,pAgenciaId, REC_INTELIGENT.extrato_id, vHISTORICOFINAL);
   			END IF;
		    vRETORNO = vRETORNO +1;
		ELSE
		 	SELECT * FROM ${schema}.comprovante into selected_comprovante 
	    		where periodo = pPeriodo 
	 			and agenciabancaria_id = pAgenciaId
	 			and parceiro_id = pParceiroId
	 			and processado = false
	 			and com_datapagamento - REC_INTELIGENT.datalancamento  between -3 and 3
	 			and com_valorpagamento*-1 = REC_INTELIGENT.debito
	 			AND tipo_comprovante ='TRANSFERENCIA'
	 			order by com_datapagamento desc
	 			limit 1;
	 			  IF (selected_comprovante.id is not null)THEN
	 			  raise info 'id %',selected_comprovante.id;
	 			  vHISTORICOFINAL   := 'Pagto. de '|| selected_comprovante.com_beneficiario;
	 			  UPDATE  ${schema}.INTELIGENT SET COMPROVANTE_ID= selected_comprovante.id,CNPJ= selected_comprovante.com_cnpj,BENEFICIARIO= selected_comprovante.com_beneficiario,
     		 	   TIPO_INTELIGENT = vTIPOINTELIGENTE, TIPO_VALOR = 'PRINCIPAL', HISTORICOFINAL = vHISTORICOFINAL WHERE ID = REC_INTELIGENT.id;
     			   UPDATE ${schema}.COMPROVANTE SET PROCESSADO = TRUE WHERE ID = selected_comprovante.id; 
     			   vRETORNO = vRETORNO +1;
	 			  END IF;  	
		END IF;
	END  LOOP;
	IF (vRETORNO > 0) THEN
	SELECT MIN(COM_DATAVENCIMENTO) INTO vMINDATE FROM ${schema}.comprovante  
	    where periodo = pPeriodo 
	 	and agenciabancaria_id = pAgenciaId
	 	and parceiro_id = pParceiroId;
	 	SELECT * FROM ${schema}.processa_notafiscalgeral(pParceiroId, vMINDATE) INTO vRETORNO_NOTA;
	END IF;
  RETURN COALESCE(vRETORNO ,0);

END;
$function$
;
