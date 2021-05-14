CREATE OR REPLACE FUNCTION ${schema}.processa_contaInteligent("inteligenId" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
  pInteligentId ALIAS FOR $1;  
  vRETORNO NUMERIC;
  vRETORNO_SETUP NUMERIC;
  vCOUNTCONTA NUMERIC;
  vCOUNTID NUMERIC;
  REC_INTELIGENT  ${schema}.inteligent%rowtype;
  REC_REGRA  ${schema}.regra%rowtype;
  REC_EXTRATO  ${schema}.extrato%rowtype;
BEGIN
vRETORNO := 0;	
		select * from ${schema}.inteligent into REC_INTELIGENT where id =  pInteligentId;
		SELECT COUNT(ID) INTO vCOUNTCONTA from ${schema}.conta where parceiro_id = REC_INTELIGENT.parceiro_id and con_cnpj = REC_INTELIGENT.cnpj  and con_classificacao like '2.%';
		IF( vCOUNTCONTA = 1) THEN
    		SELECT ID INTO vCOUNTID from ${schema}.conta where parceiro_id = REC_INTELIGENT.parceiro_id and con_cnpj = REC_INTELIGENT.cnpj  and con_classificacao like '2.%';
 			update ${schema}.inteligent set associado = true, conta_id = vCOUNTID where id = pInteligentId;
			vRETORNO :=	vRETORNO +1;
		ELSE
			select COUNT(ID) INTO vCOUNTCONTA from ${schema}.conta where parceiro_id = REC_INTELIGENT.parceiro_id and substring(con_cnpj,1,8) = substring(REC_INTELIGENT.cnpj,1,8)  and con_classificacao like '2.%';
			IF( vCOUNTCONTA = 1) THEN
    		SELECT ID INTO vCOUNTID from ${schema}.conta where parceiro_id = REC_INTELIGENT.parceiro_id and substring(con_cnpj,1,8) = substring(REC_INTELIGENT.cnpj,1,8) and con_classificacao like '2.%';
 			update ${schema}.inteligent set associado = true, conta_id = vCOUNTID where id = pInteligentId;
			vRETORNO :=	vRETORNO +1;
			END IF;
		END IF;
		IF (vRETORNO = 0) THEN
			SELECT * FROM ${schema}.extrato into REC_EXTRATO WHERE id = REC_INTELIGENT.extrato_id;
			SELECT * FROM ${schema}.regra into REC_REGRA WHERE parceiro_id = REC_INTELIGENT.parceiro_id AND ((REC_INTELIGENT.historico = REG_DESCRICAO AND TIPO_REGRA = 'HISTORICO')
			OR (REC_EXTRATO.info_adicional = REG_DESCRICAO AND TIPO_REGRA = 'INFORMACAO_ADICIONAL') OR (REC_INTELIGENT.BENEFICIARIO = REG_DESCRICAO AND TIPO_REGRA = 'BENEFICIARIO'));
		    IF (REC_REGRA.id is not null) then		    
			  UPDATE ${schema}.INTELIGENT SET CONTA_ID = REC_REGRA.CONTA_ID, HISTORICOFINAL = REC_REGRA.REG_HISTORICO, REGRA_ID = REC_REGRA.ID, ASSOCIADO = TRUE
		      WHERE ID = REC_INTELIGENT.ID;
	  		END IF;
	  	END IF;	
return vRETORNO;
END;
$function$;