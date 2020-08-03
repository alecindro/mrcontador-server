package br.com.mrcontador.service.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteParseMapper {
	private static final String AGENCIA = "$ag";
	private static final String CONTA = "$conta";//split - conta e digito
	private static final String DOCUMENTO = "$doc";
	private static final String FORNECEDOR = "$2";
	private static final String CNPJ_BEN = "$cnpj_ben";
	private static final String CNPJ_PAG = "$cnpj";
	private static final String DATA_VCTO = "$data_venc";
	private static final String DATA_PGTO = "$pagto";
	private static final String VALOR_PGTO = "$valor_pag";
	private static final String VALOR_DOC = "$valor_doc";
	private static final String PARCEIRO = "$1";
	private static final String OBS = "$4";
	
	//$1 = parceiro
	//$ag = agencia
	//$conta = conta
	//$2 = fornecedor
	//$pagto = data pagamento
	//$valor_doc = valor documento
	//$valor_pag = valor do pagamento
	//$doc = documento
	//$cnpj = cnpj do pagador
	//$cnpj_ben = cnpj do beneficiario
	//$data_venc = data de vencimento
	//$3 = banco
	//$4 = observacao
	
	public Comprovante toEntity(List<DiffValue> diffValues, Agenciabancaria agenciabancaria, Parceiro parceiro){
		Comprovante comprovante = new Comprovante();
		comprovante.setAgenciabancaria(agenciabancaria);
		comprovante.setParceiro(parceiro);
		Optional<DiffValue> agencia = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(AGENCIA)).findFirst();
		Optional<DiffValue> conta  = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(CONTA)).findFirst();
		Optional<DiffValue> documento   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(DOCUMENTO)).findFirst();
		Optional<DiffValue> fornecedor   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(FORNECEDOR)).findFirst();
		Optional<DiffValue> cnpj_beneficiario   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(CNPJ_BEN)).findFirst();
		Optional<DiffValue> cnpj_pagador   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(CNPJ_PAG)).findFirst();
		Optional<DiffValue> data_vcto   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(DATA_VCTO)).findFirst();
		Optional<DiffValue> data_pagto   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(DATA_PGTO)).findFirst();
		Optional<DiffValue> valor_pagamento   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(VALOR_PGTO)).findFirst();
		Optional<DiffValue> valor_documento   =diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(VALOR_DOC)).findFirst();
		Optional<DiffValue> parceiro_desc   =diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(PARCEIRO)).findFirst();
		Optional<DiffValue> obs   = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().equals(OBS)).findFirst();
		comprovante.setComBeneficiario(cnpj_beneficiario.isPresent()?cnpj_beneficiario.get().getNewValue():"");
		comprovante.setComCnpj(cnpj_pagador.isPresent()?cnpj_pagador.get().getNewValue():"");
		//comprovante.setComDatapagamento(comDatapagamento);
		return comprovante;
	}
	
	private void validateAgencia(String agencia, String conta, Agenciabancaria agenciabancaria) {
		
	}
	
	private void validateParceiro() {
		
	}
	
	private LocalDateTime to(String value) {
		return null;
	}

}
