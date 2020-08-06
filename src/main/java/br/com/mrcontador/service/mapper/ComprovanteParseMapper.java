package br.com.mrcontador.service.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.util.MrContadorUtil;

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
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
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
		comprovante.setComDocumento(documento.isPresent()?documento.get().getNewValue():"");
		comprovante.setComObservacao(obs.isPresent()?obs.get().getNewValue():"");
		comprovante.setComValordocumento(valor_documento.isPresent()?new BigDecimal(valor_documento.get().getNewValue()):BigDecimal.ZERO);
		comprovante.setComValorpagamento(valor_pagamento.isPresent()? new BigDecimal(valor_pagamento.get().getNewValue()): BigDecimal.ZERO);
		comprovante.setComBeneficiario(fornecedor.isPresent()?fornecedor.get().getNewValue():"");
		comprovante.setComDatapagamento(data_pagto.isPresent()?toDate(data_pagto.get().getNewValue()):null);
		comprovante.setComDatavencimento(data_vcto.isPresent()?toDate(data_vcto.get().getNewValue()):null);
		comprovante.setAgenciabancaria(agenciabancaria);
		comprovante.setParceiro(parceiro);
		validateAgencia(agencia, conta, agenciabancaria);
		validateParceiro(parceiro_desc, parceiro);
		return comprovante;
	}
	
	private void validateAgencia(Optional<DiffValue> agencia, Optional<DiffValue> conta, Agenciabancaria agenciabancaria) {
		if(agencia.isPresent()) {
			if(!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeAgencia(), agencia.get().getNewValue())) {
				throw new MrContadorException("comprovante.agencianotequal");
			}
			
		}
		if(conta.isPresent()) {
			if(!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeNumero(), conta.get().getNewValue())) {
				throw new MrContadorException("comprovante.agencianotequal");
			}
		}		
	}
	
	private void validateParceiro(Optional<DiffValue> parceiro_desc, Parceiro parceiro) {
		if(parceiro_desc.isPresent()) {
			String cnpjComprovante = MrContadorUtil.removeZerosFromInital(MrContadorUtil.onlyNumbers(parceiro_desc.get().getNewValue()));
			String cnpj = MrContadorUtil.removeZerosFromInital(parceiro.getParCnpjcpf());
			if(!cnpj.equalsIgnoreCase(cnpjComprovante)) {
				throw new MrContadorException("comprovante.parceironotequal");
			}
		}
	}
	
	private LocalDate toDate(String value) {
		return LocalDate.parse(value, formatter);
	}
	

}
