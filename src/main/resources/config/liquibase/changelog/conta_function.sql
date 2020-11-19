CREATE OR REPLACE FUNCTION ds_demo.processa_conta("pINT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pINT_CODIGO ALIAS for $1;
vRETORNO NUMERIC;
vCONTAID NUMERIC;
vCNPJ TEXT;
vPARCEIROID NUMERIC;
vCOUNT_CONTA NUMERIC;
BEGIN
vRETORNO := 0;	
SELECT CNPJ,parceiro_id INTO vCNPJ, vPARCEIROID FROM ds_demo.INTELIGENT where id = pINT_CODIGO;

SELECT COUNT(ID) INTO vCOUNT_CONTA FROM ds_demo.CONTA WHERE (CON_CNPJ = vCNPJ  or substring(CON_CNPJ,1,8) = substring(vCNPJ,1,8)) AND PARCEIRO_ID = vPARCEIROID and substring(con_classificacao,1,2) = '2.';
	IF(vCOUNT_CONTA = 1) THEN
		SELECT ID INTO vCONTAID FROM ds_demo.CONTA WHERE (CON_CNPJ = vCNPJ  or substring(CON_CNPJ,1,8) = substring(vCNPJ,1,8)) AND PARCEIRO_ID = vPARCEIROID and substring(con_classificacao,1,2) = '2.';
			IF(vCONTAID is NOT null) then
			  update ds_demo.INTELIGENT SET ASSOCIADO = TRUE, CONTA_ID = vCONTAID WHERE ID = pINT_CODIGO;
			  vRETORNO := 1;
			END IF;
	END IF;
return vRETORNO;
END;
$function$
;
