CREATE OR REPLACE FUNCTION ${schema}.processa_conta("parceiroId" bigint, "pPeriodo" character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
  pParceiroId ALIAS FOR $1;  
  pPeriodo ALIAS FOR $2; 
  vRETORNO NUMERIC;
  vRETORNO_SETUP NUMERIC;
  vCOUNTCONTA NUMERIC;
  vCOUNTID NUMERIC;
  REC_INTELIGENT RECORD;
BEGIN
vRETORNO := 0;	
	FOR REC_INTELIGENT IN
		select * from ${schema}.inteligent where periodo = pPeriodo and parceiro_id = pParceiroId and associado = false and tipo_valor = 'PRINCIPAL'
	loop
		SELECT COUNT(ID) INTO vCOUNTCONTA from ${schema}.conta where parceiro_id = pParceiroId and con_cnpj = REC_INTELIGENT.cnpj  and con_classificacao like '2.%';
		IF( vCOUNTCONTA = 1) THEN
    		SELECT ID INTO vCOUNTID from ${schema}.conta where parceiro_id = pParceiroId and con_cnpj = REC_INTELIGENT.cnpj  and con_classificacao like '2.%';
 			update ${schema}.inteligent set associado = true, conta_id = vCOUNTID where id = REC_INTELIGENT.id;
			vRETORNO :=	vRETORNO +1;
		ELSE
			select COUNT(ID) INTO vCOUNTCONTA from ${schema}.conta where parceiro_id = pParceiroId and substring(con_cnpj,1,8) = substring(REC_INTELIGENT.cnpj,1,8)  and con_classificacao like '2.%';
			IF( vCOUNTCONTA = 1) THEN
    		SELECT ID INTO vCOUNTID from ${schema}.conta where parceiro_id = pParceiroId and substring(con_cnpj,1,8) = substring(REC_INTELIGENT.cnpj,1,8) and con_classificacao like '2.%';
 			update ${schema}.inteligent set associado = true, conta_id = vCOUNTID where id = REC_INTELIGENT.id;
			vRETORNO :=	vRETORNO +1;
			END IF;
		END IF;
	end loop;
	select * from ${schema}.setup_function(pParceiroId,pPeriodo) into vRETORNO_SETUP;
return vRETORNO;
END;
$function$
;
