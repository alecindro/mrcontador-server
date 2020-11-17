package br.com.mrcontador.file.comprovante.banco;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffText;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.file.comprovante.ParserComprovante;
import br.com.mrcontador.file.comprovante.TipoComprovante;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.util.MrContadorUtil;

public abstract class ComprovanteBanco implements ParserComprovante {

	private static final String diff = "$";
	
	public Comprovante save(Comprovante comprovante, ComprovanteService service) {
			return service.save(comprovante);
	}
	
	public void callFunction(Comprovante comprovante, ComprovanteService service) {
		service.callComprovante(comprovante);
    }
	

	public List<Comprovante> parse(String comprovante, String pattern, Agenciabancaria agenciabancaria,
			Parceiro parceiro, TipoComprovante tipoComprovante) throws DiffException, ComprovanteException {
		DiffText d = new DiffText();
		Map<Integer, DiffValue> map = d.doDiff(pattern, comprovante);
		List<DiffValue> list = new ArrayList<DiffValue>();
		for (DiffValue _values : map.values()) {
			List<DiffValue> _list = diffpatch(_values.getOldValue(), _values.getNewValue());
			if (_list != null && !_list.isEmpty()) {
				list.addAll(_list);
			}
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,tipoComprovante));
		return comprovantes;
	}

	private List<DiffValue> diffpatch(String original, String revision) throws DiffException {
		if (original == null || revision == null) {
			return null;
		}
		List<DiffValue> changes = new ArrayList<>();
		Patch<String> patchs = DiffUtils.diffInline(original, revision);
		List<AbstractDelta<String>> deltas = patchs.getDeltas();
		for (AbstractDelta<String> delta : deltas) {
			if (delta.getType().equals(DeltaType.CHANGE)) {
				if (delta.getSource().getLines().get(0).trim().startsWith(diff)) {
					DiffValue diffValues = new DiffValue();
					diffValues.setOldValue(delta.getSource().getLines().get(0));
					diffValues.setNewValue(delta.getTarget().getLines().get(0));
					diffValues.setPosition(delta.getTarget().getPosition());
					changes.add(diffValues);
				}
			}
		}
		for (AbstractDelta<String> delta : deltas) {
			if (delta.getType().equals(DeltaType.INSERT)) {
				List<DiffValue> sublist = changes.stream()
						.filter(diffvalue -> diffvalue.getPosition() == delta.getTarget().getPosition())
						.collect(Collectors.toList());
				DiffValue anterior = sublist.stream().min(Comparator.comparing(DiffValue::getPosition))
						.orElse(null);
				if(anterior != null) {
				changes.remove(anterior);
				anterior.setNewValue(anterior.getNewValue().concat(" ").concat(delta.getTarget().getLines().get(0)));
				changes.add(anterior);
				}
			}
		}

		return changes;
	}

	protected static final String AGENCIA = "$ag";
	protected static final String CONTA = "$conta";// split - conta e digito
	protected static final String DOCUMENTO = "$doc";
	protected static final String FORNECEDOR = "$2";
	protected static final String CNPJ_BEN = "$cnpj_ben";
	protected static final String CNPJ_PAG = "$cnpj";
	protected static final String DATA_VCTO = "$data_venc";
	protected static final String DATA_PGTO = "$pagto";
	protected static final String VALOR_PGTO = "$valor_pag";
	protected static final String VALOR_DOC = "$valor_doc";
	protected static final String JUROS = "$juros";
	protected static final String MULTA = "$multa";
	protected static final String DESCONTO = "$desconto";
	
	// private static final String PARCEIRO = "$1";
	protected static final String OBS = "$4";
	private static DateTimeFormatter formatterBar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static DateTimeFormatter formatterPoint = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	private static int count = 1;

	protected Comprovante toEntity(List<DiffValue> diffValues, Agenciabancaria agenciabancaria, Parceiro parceiro, TipoComprovante tipoComprovante) throws ComprovanteException {
		Comprovante comprovante = new Comprovante();
try {
		Optional<DiffValue> agencia = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(AGENCIA)).findFirst();
		Optional<DiffValue> conta = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(CONTA)).findFirst();
		Optional<DiffValue> documento = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(DOCUMENTO)).findFirst();
		Optional<DiffValue> fornecedor = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(FORNECEDOR)).findFirst();
		Optional<DiffValue> cnpj_beneficiario = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(CNPJ_BEN)).findFirst();
		//Optional<DiffValue> cnpj_pagador = diffValues.stream()
		//		.filter(diffvalue -> diffvalue.getOldValue().trim().equals(CNPJ_PAG)).findFirst();
		Optional<DiffValue> data_vcto = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(DATA_VCTO)).findFirst();
		Optional<DiffValue> data_pagto = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(DATA_PGTO)).findFirst();
		Optional<DiffValue> valor_pagamento = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(VALOR_PGTO)).findFirst();
		Optional<DiffValue> valor_documento = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(VALOR_DOC)).findFirst();
		Optional<DiffValue> obs = diffValues.stream().filter(diffvalue -> diffvalue.getOldValue().trim().equals(OBS))
				.findFirst();
		Optional<DiffValue> multa = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(MULTA)).findFirst();
		Optional<DiffValue> juros = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(JUROS)).findFirst();
		Optional<DiffValue> desconto = diffValues.stream()
				.filter(diffvalue -> diffvalue.getOldValue().trim().equals(DESCONTO)).findFirst();
		comprovante.setComCnpj(cnpj_beneficiario.isPresent() ? MrContadorUtil.onlyNumbers(cnpj_beneficiario.get().getNewValue().trim()) : "");
		comprovante.setComDocumento(documento.isPresent() ? StringUtils.normalizeSpace(documento.get().getNewValue()) : "");
		comprovante.setComObservacao(obs.isPresent() ? StringUtils.normalizeSpace(obs.get().getNewValue()) : "");
		comprovante.setComValordocumento(valor_documento.isPresent()
				? new BigDecimal(MrContadorUtil.onlyMoney(valor_documento.get().getNewValue()))
				: BigDecimal.ZERO);
		comprovante.setComValorpagamento(valor_pagamento.isPresent()
				? new BigDecimal(MrContadorUtil.onlyMoney(valor_pagamento.get().getNewValue()))
				: BigDecimal.ZERO);
		comprovante.setJuros(juros.isPresent()  && MrContadorUtil.containsNumber(juros.get().getNewValue())
				? new BigDecimal(MrContadorUtil.onlyMoney(juros.get().getNewValue()))
				: BigDecimal.ZERO);
		comprovante.setMulta(multa.isPresent() && MrContadorUtil.containsNumber(multa.get().getNewValue())
				? new BigDecimal(MrContadorUtil.onlyMoney(multa.get().getNewValue()))
				: BigDecimal.ZERO);
		comprovante.setDesconto(desconto.isPresent() && MrContadorUtil.containsNumber(desconto.get().getNewValue())
				? (new BigDecimal(MrContadorUtil.onlyMoney(desconto.get().getNewValue())).negate())
				: BigDecimal.ZERO);		
		comprovante.setComBeneficiario(fornecedor.isPresent() ? StringUtils.normalizeSpace(fornecedor.get().getNewValue()) : "");
		comprovante.setComDatapagamento(data_pagto.isPresent() ? toDate(data_pagto.get().getNewValue()) : null);
		comprovante.setComDatavencimento(data_vcto.isPresent() ? toDate(data_vcto.get().getNewValue()) : comprovante.getComDatapagamento());
		comprovante.setAgenciabancaria(agenciabancaria);
		comprovante.setParceiro(parceiro);
		comprovante.setTipoComprovante(tipoComprovante);
		validateAgencia(agencia, conta, agenciabancaria);
		comprovante.setProcessado(false);
		count = count + 1;
}catch (Exception e) {
	e.printStackTrace();
}
		return comprovante;
	}

	protected void validateAgencia(Optional<DiffValue> agencia, Optional<DiffValue> conta,
			Agenciabancaria agenciabancaria) throws ComprovanteException {
		if (agencia.isPresent()) {
			if (!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeAgencia(), agencia.get().getNewValue())) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}

		}
		if (conta.isPresent()) {
			if (!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeNumero(), conta.get().getNewValue())) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}
		}
	}

	protected void validateParceiro(Optional<DiffValue> cnpj_pagador, Parceiro parceiro, List<DiffValue> diffValues) throws ComprovanteException {
		if (cnpj_pagador.isPresent()) {
			String cnpjComprovante = MrContadorUtil
					.removeZerosFromInital(MrContadorUtil.onlyNumbers(cnpj_pagador.get().getNewValue()));
			String cnpj = MrContadorUtil.removeZerosFromInital(MrContadorUtil.onlyNumbers(parceiro.getParCnpjcpf()));
			if (!cnpj.equalsIgnoreCase(cnpjComprovante)) {
				throw new ComprovanteException("comprovante.parceironotequal");
			}
		}
	}

	protected LocalDate toDate(String value) {		
		if(value.trim().equals("00/00/0000")) {
			return null;
		}		
		if (StringUtils.contains(value, ".")){ 
		return LocalDate.parse(value.trim(), formatterPoint);
		}
		return LocalDate.parse(value.trim(), formatterBar);
	}

}
