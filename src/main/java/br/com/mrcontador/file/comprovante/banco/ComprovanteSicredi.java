package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.util.MrContadorUtil;

public class ComprovanteSicredi extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		String line = StringUtils.normalizeSpace(_lines[3].trim());
		if (line.equals("Boletos Eletrônicos")) {
			return parseTitulo(_lines, agenciabancaria, parceiro,"Boletos Eletrônicos");
		}
		if (line.equals("Boletos")) {
			return parseTitulo(_lines, agenciabancaria, parceiro,"Boletos");
		}
		if (line.equals("TED Outra Titularidade")) {
			return parseTed(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Tributos")) {
			return parseTributos(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Comprovante de inclusão de Favorecido")) {
			return null;
		}
		if (line.equals("Rejeitar Boletos Eletrônicos")) {
			return null;
		}

		throw new ComprovanteException("Comprovante não identificado");
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro, String obs)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				
			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
				
			if (line.contains("CPF/CNPJ do Pagador Efetivo:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ do Pagador Efetivo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ do Beneficiário:")) {
					String value = StringUtils.substringAfter(line, "CPF/CNPJ do Beneficiário:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				
			}
			if (line.contains("Razão Social do Beneficiário:")) {
					String value = StringUtils.substringAfter(line, "Razão Social do Beneficiário:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
			}
			if (line.contains("Data de Vencimento:")) {
				String value = StringUtils.substringAfter(line, "Data de Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor do Título (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor do Título (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Pago (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor Pago (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nº Ident. DDA:")) {
				String value = StringUtils.substringAfter(line, "Nº Ident. DDA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Descrição do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue(obs);
		diffValue.setLine(2);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseTed(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				
			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ:")) {
					String value = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				
			}
			if (line.contains("Favorecido:")) {
					String value = StringUtils.substringAfter(line, "Favorecido:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
			}
			if (line.contains("Data Transferência:")) {
				String value = StringUtils.substringAfter(line, "Data Transferência:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor a Transferir (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor a Transferir (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Número de Controle:")) {
				String value = StringUtils.substringAfter(line, "Número de Controle:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("TED Outra Titularidade");
		diffValue.setLine(2);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	private List<Comprovante> parseTributos(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				
			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Associado:")) {
					String value = StringUtils.substringAfter(line, "Associado:").trim();
					
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Total (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor Total (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Descrição do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Descrição do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Tipo de Documento:")) {
				String value = StringUtils.substringAfter(line, "Tipo de Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(FORNECEDOR);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	protected void validateAgencia(Optional<DiffValue> agencia, Optional<DiffValue> conta,
			Agenciabancaria agenciabancaria) throws ComprovanteException {
		if (agencia.isPresent()) {
			String agenciaOriginal = MrContadorUtil.removeZerosFromEnd(agenciabancaria.getAgeAgencia());
			String agenciaDoc = MrContadorUtil.removeZerosFromEnd(agencia.get().getNewValue());
			if (!MrContadorUtil.compareWithoutDigit(agenciaOriginal, agenciaDoc)) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}

		}
		if (conta.isPresent()) {
			if (!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeNumero(), conta.get().getNewValue())) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}
		}
	}
	
}
