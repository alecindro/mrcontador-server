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
return vRETORNO;
END;
$function$;