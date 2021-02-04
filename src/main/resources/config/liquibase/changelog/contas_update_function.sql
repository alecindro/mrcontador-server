CREATE OR REPLACE FUNCTION ${schema}.processa_conta_update("pINT_PARCEIRO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pINT_PARCEIRO ALIAS for $1;
vRETORNO NUMERIC;
vCOUNTCONTA NUMERIC;
vCONTAID NUMERIC;
REC   RECORD; 
BEGIN
vRETORNO := 0;
for REC in 
SELECT CNPJ as vCNPJ, id  as vINTELIGENT_ID FROM ${schema}.INTELIGENT where parceiro_id = pINT_PARCEIRO and associado = false and (CNPJ is not null AND TRIM(CNPJ) <> '')
loop
RAISE INFO 'vINTELIGENT_ID: %, CNPJ: %, PARCEIRO: %', REC.vINTELIGENT_ID, REC.vCNPJ, pINT_PARCEIRO;
SELECT COUNT(ID) INTO vCOUNTCONTA FROM ${schema}.CONTA WHERE (CON_CNPJ = REC.vCNPJ  or substring(CON_CNPJ,1,8) = substring(REC.vCNPJ,1,8)) AND PARCEIRO_ID = pINT_PARCEIRO and substring(con_classificacao,1,2) = '2.';
RAISE INFO 'count conta: %', vCOUNTCONTA;
IF(vCOUNTCONTA = 1) then
RAISE INFO 'count conta2: %', vCOUNTCONTA;
		SELECT ID INTO vCONTAID FROM ${schema}.CONTA WHERE (CON_CNPJ = REC.vCNPJ  or substring(CON_CNPJ,1,8) = substring(REC.vCNPJ,1,8)) AND PARCEIRO_ID = pINT_PARCEIRO and substring(con_classificacao,1,2) = '2.';
	RAISE INFO 'id conta: %', vCONTAID;	
	update ${schema}.INTELIGENT SET ASSOCIADO = TRUE, CONTA_ID = vCONTAID WHERE ID = REC.vINTELIGENT_ID;
			  vRETORNO := vRETORNO + 1;
			
	END IF;
end loop;	
return vRETORNO;
END;
$function$
;
;
