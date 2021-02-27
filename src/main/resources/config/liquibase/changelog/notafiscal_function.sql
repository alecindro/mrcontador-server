CREATE OR REPLACE FUNCTION ${schema}.processa_notafiscal("pNOT_CODIGO" bigint)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
pNOT_CODIGO ALIAS for $1;
vRETORNO NUMERIC;
vHISTORICOFINAL TEXT;
selected_nota ${schema}.notafiscal%rowtype;
selected_inteligent2 ${schema}.inteligent%rowtype;
vInteligent_id numeric;
vDatalancamento DATE;
vNumerodocumento TEXT;
vNumerocontrole TEXT;
vPeriodo TEXT;
vCnpj TEXT;
vBeneficiario TEXT;
vComprovante_id NUMERIC;
vParceiro_id NUMERIC;
vAgenciabancaria_id NUMERIC;
vExtrato_id NUMERIC;
vPagamento NUMERIC;
vTAXA  NUMERIC;
vCom_juros NUMERIC;
vCom_multa NUMERIC;
vCom_desconto NUMERIC;

begin
  vTAXA := 0;
  vRETORNO:= 0;  
SELECT * 
INTO selected_nota 
from ${schema}.NOTAFISCAL NF WHERE NF.tno_codigo =0 AND NF.ID  = pNOT_CODIGO;

SELECT i.id, i.datalancamento, i.numerodocumento, i.numerocontrole, i.periodo, i.cnpj,
i.beneficiario, i.comprovante_id, i.parceiro_id, i.agenciabancaria_id, i.extrato_id, c.COM_VALORPAGAMENTO,  c.com_juros, c.com_multa, c.com_desconto 
into vInteligent_id, vDatalancamento, vNumerodocumento, vNumerocontrole, vPeriodo, vCnpj, vBeneficiario, vComprovante_id,
vParceiro_id, vAgenciabancaria_id, vExtrato_id, vPagamento, vCom_juros, vCom_multa, vCom_desconto
from ${schema}.inteligent i
    inner join ${schema}.comprovante c 
    on i.comprovante_id = c.id
    WHERE 
    i.tipo_valor = 'PRINCIPAL'
    and (i.cnpj = selected_nota.not_cnpj or substring(i.cnpj,1,8) = substring(selected_nota.not_cnpj,1,8))
    and c.TIPO_COMPROVANTE = 'TITULO'
      and (c.com_valordocumento >= selected_nota.not_valorparcela AND c.COM_DATAVENCIMENTO = selected_nota.not_dataparcela) 
	AND i.PARCEIRO_ID = selected_nota.parceiro_id
    AND i.notafiscal_id IS NULL
	order by c.com_valordocumento asc,c.com_datapagamento ASC limit 1;
	
if(vInteligent_id is null) then
SELECT i.id, i.datalancamento, i.numerodocumento, i.numerocontrole, i.periodo, i.cnpj,
i.beneficiario, i.comprovante_id, i.parceiro_id, i.agenciabancaria_id, i.extrato_id, c.COM_VALORPAGAMENTO,  c.com_juros, c.com_multa, c.com_desconto 
into vInteligent_id, vDatalancamento, vNumerodocumento, vNumerocontrole, vPeriodo, vCnpj, vBeneficiario, vComprovante_id,
vParceiro_id, vAgenciabancaria_id, vExtrato_id, vPagamento, vCom_juros, vCom_multa, vCom_desconto
from ${schema}.inteligent i
    inner join ${schema}.comprovante c 
    on i.comprovante_id = c.id
    WHERE 
    i.tipo_valor = 'PRINCIPAL'
    and (i.cnpj = selected_nota.not_cnpj or substring(i.cnpj,1,8) = substring(selected_nota.not_cnpj,1,8))
    and c.TIPO_COMPROVANTE = 'TITULO'
      and (c.com_valordocumento >= selected_nota.not_valorparcela AND c.COM_DATAVENCIMENTO >= selected_nota.not_dataparcela) 
	AND i.PARCEIRO_ID = selected_nota.parceiro_id
    AND i.notafiscal_id IS NULL
	order by c.com_valordocumento asc,c.com_datapagamento ASC limit 1;
end if;

	if(vInteligent_id is null) then
	SELECT i.id, i.datalancamento, i.numerodocumento, i.numerocontrole, i.periodo, i.cnpj,
	i.beneficiario, i.comprovante_id, i.parceiro_id, i.agenciabancaria_id, i.extrato_id, c.COM_VALORPAGAMENTO,  c.com_juros, c.com_multa, c.com_desconto 
	into vInteligent_id, vDatalancamento, vNumerodocumento, vNumerocontrole, vPeriodo, vCnpj, vBeneficiario, vComprovante_id,
	vParceiro_id, vAgenciabancaria_id, vExtrato_id, vPagamento, vCom_juros, vCom_multa, vCom_desconto
	from ${schema}.inteligent i
    inner join ${schema}.comprovante c 
    on i.comprovante_id = c.id
    WHERE 
    i.tipo_valor = 'PRINCIPAL'
    and (i.cnpj = selected_nota.not_cnpj or substring(i.cnpj,1,8) = substring(selected_nota.not_cnpj,1,8))
    and c.TIPO_COMPROVANTE = 'TRANSFERENCIA'
      and (c.com_valordocumento >= selected_nota.not_valorparcela AND c.COM_DATAVENCIMENTO >= selected_nota.not_dataparcela) 
	AND i.PARCEIRO_ID = selected_nota.parceiro_id
    AND i.notafiscal_id IS NULL
	order by c.com_valordocumento asc,c.com_datapagamento ASC limit 1;
	end if;
 
	if (vInteligent_id is not null) then
    vTAXA:= vPagamento - selected_nota.not_valorparcela - vCom_juros - vCom_multa + vCom_desconto;
 	vHISTORICOFINAL   := 'Pagto. NFe '|| selected_nota.not_numero || '/' || selected_nota.not_parcela || ' de ' || selected_nota.NOT_EMPRESA;
    IF (vTAXA > 0 and vTAXA < 3.5) then
   	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = selected_nota.id, debito = (vPagamento-vTAXA - vCom_juros - vCom_multa + vCom_desconto) *-1, tipo_inteligent ='C'  where id = vInteligent_id;
	    vHISTORICOFINAL   := 'Pagto. de taxa bancária ref. '|| selected_nota.not_numero || '/' || selected_nota.not_parcela || ' de ' || selected_nota.NOT_EMPRESA;
   			INSERT INTO  ${schema}.INTELIGENT (historico, tipo_valor,datalancamento,numerodocumento,numerocontrole,periodo,debito,associado,
			     		cnpj,beneficiario,tipo_inteligent,comprovante_id,parceiro_id,agenciabancaria_id, extrato_id, notafiscal_id, historicofinal) VALUES 
						('Pagto. de Taxa bancária','TAXA',vDatalancamento,vNumerodocumento,vNumerocontrole,vPeriodo, vTAXA*-1,false,
			     		vCnpj,vBeneficiario, 'C',vComprovante_id,vParceiro_id,vAgenciabancaria_id,vExtrato_id, selected_nota.id, vHISTORICOFINAL);
   	elseif (vTAXA = 0) THEN
 	   	update ${schema}.INTELIGENT set historicofinal = vHISTORICOFINAL, notafiscal_id = selected_nota.id  where id = vInteligent_id;
   	END IF;
   vRETORNO:= vRETORNO + 1;
 
    if (vRETORNO > 0) THEN
	update ${schema}.notafiscal set processado = true where ID = selected_nota.id;
	end if;	
END IF;   	
RETURN vRETORNO;
END;
$function$
;
