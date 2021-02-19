CREATE OR REPLACE FUNCTION ${schema}.processa_conta("parceiroId" bigint, "pPeriodo" character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
  pParceiroId ALIAS FOR $1;  
  pPeriodo ALIAS FOR $2; 
  vRETORNO NUMERIC;
  vRETORNO_SETUP NUMERIC;
  REC_CONTA RECORD;
  REC_INTELIGENT RECORD;
BEGIN
vRETORNO := 0;	
	FOR REC_CONTA IN
		select * from ${schema}.conta where parceiro_id = pParceiroId and (con_cnpj is not null and con_cnpj <> '')
	loop
		for REC_INTELIGENT IN
		select * from ${schema}.inteligent where periodo = pPeriodo and parceiro_id = pParceiroId and associado = false and (CNPJ = REC_CONTA.con_cnpj  or substring(CNPJ,1,8) = substring(REC_CONTA.con_cnpj,1,8)) 
		loop
			update ${schema}.inteligent set associado = true, conta_id = REC_CONTA.id where id = REC_INTELIGENT.id;
			vRETORNO :=	vRETORNO +1;		
		end loop;
	end loop;
select * from ${schema}.setup_function(pParceiroId,pPeriodo) into vRETORNO_SETUP;
return vRETORNO;
END;
$function$
;
