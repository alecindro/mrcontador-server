CREATE OR REPLACE FUNCTION ${schema}.setup_function("parceiroId" bigint, "pPeriodo" character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
  pParceiroId ALIAS FOR $1;  
  pPeriodo ALIAS FOR $2; 
  vRETORNO NUMERIC;
  vRETORNO_REGRA NUMERIC;
  selected_parceiro ${schema}.parceiro%rowtype;
  REC RECORD;

  
BEGIN
  vRETORNO:= 0;
	select * from ${schema}.parceiro into selected_parceiro where id = pParceiroId;
	FOR REC IN
		select * from ${schema}.inteligent where periodo = pPeriodo and parceiro_id = pParceiroId and associado = false and tipo_valor <> 'PRINCIPAL'
	loop
IF (REC.tipo_valor = 'DESCONTO' and selected_parceiro.descontos_ativos is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.descontos_ativos , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (REC.tipo_valor = 'JUROS' and selected_parceiro.despesa_juros is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesa_juros , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (REC.tipo_valor = 'TAXA' and selected_parceiro.despesa_tarifa is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesa_tarifa , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%CADASTRO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%2ª VIA CARTÃO DÉBITO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%2ª VIA - CARTÃO POUPANÇA%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%EXCLUSÃO CCF%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%SUSTAÇÃO REVOGAÇÃO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%FOLHACHEQUE%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%CHEQUE ADMINISTRATIVO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%CHEQUE VISADO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%SAQUE PESSOAL%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%SAQUE TERMINAL%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%SAQUE CORRESPONDENTE%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%DEPOSITO IDENTIFICADO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%EXTRATO MÊS%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%EXTRATO MOVIMENTO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%MICROFILME%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%DOC/TED%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%TRANSF.RECURSO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;	
		vRETORNO:= 1;	
	 ELSEIF (UPPER(REC.historico) like '%ORDEM PAGAMENTO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;	
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%ADIANT. DEPOSITANTE%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;	
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%ANUIDADE%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;	
	 ELSEIF (UPPER(REC.historico) like '%RETIRADA%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%PAGAMENTOCONTAS%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;	
	 ELSEIF (UPPER(REC.historico) like '%AVAL.EMERG CREDITO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%VENDACÂMBIO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 ELSEIF (UPPER(REC.historico) like '%COMPRACÂMBIO%' and selected_parceiro.despesas_bancarias is not null) THEN
		update ${schema}.inteligent set conta_id = selected_parceiro.despesas_bancarias , associado = true where id = REC.id;
		vRETORNO:= 1;
	 END IF;
	END LOOP;	
	select * from ${schema}.regra_function(pParceiroId,pPeriodo) into vRETORNO_REGRA;
	 
	RETURN COALESCE(vRETORNO ,0);	
END;
$function$
;
