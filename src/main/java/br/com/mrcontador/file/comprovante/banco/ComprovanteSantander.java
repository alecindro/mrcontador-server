package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteSantander extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {

		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 6) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		if (StringUtils.normalizeSpace(_lines[3].trim()).equals("COMPROVANTE DE PAGAMENTO")) {
			return parseComprovante(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[3].trim()).equals("COMPROVANTE DE RECOLHIMENTO - FGTS RESCISORIO")) {
			return parseFgtsRescisorio(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[3].trim()).equals("COMPROVANTE DE PAGAMENTO RECOLHIMENTO - FGTS GRF")) {
			return parseFgtsGrf(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[3].trim()).equals("COMPROVANTE DE PAGAMENTO DE DAS")) {
			return parseDAS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Títulos > 2ª via de Comprovante")) {
			return parseTitulo(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException(StringUtils.normalizeSpace(_lines[0].trim()));
	}

	private List<Comprovante> parseComprovante(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta Corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);

				String lineB = StringUtils.substringAfter(line, "Conta Corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Arrecadacao")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "Empresa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String lineA = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;

		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseFgtsRescisorio(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta Corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);

				String lineB = StringUtils.substringAfter(line, "Conta Corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "Empresa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Validade:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Validade:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificador:")) {
				String lineA = StringUtils.substringAfter(line, "Identificador:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Recolhido:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Recolhido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE RECOLHIMENTO - FGTS RESCISORIO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseFgtsGrf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta Corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);

				String lineB = StringUtils.substringAfter(line, "Conta Corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "Empresa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Validade:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Validade:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Autenticacao:")) {
				String lineA = StringUtils.substringAfter(line, "Autenticacao:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Recolhido:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Recolhido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO RECOLHIMENTO - FGTS GRF");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseDAS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta Corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);

				String lineB = StringUtils.substringAfter(line, "Conta Corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "Empresa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Numero do Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Numero do Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DE DAS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta Corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				String lineB = StringUtils.substringAfter(line, "Conta Corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Nosso Número:")) {
				String lineA = StringUtils.substringAfter(line, "Nosso Número:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Instituição Financeira Favorecida:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("Dados do Beneficiário Original")) {
				int j = i + 1;
				while (j < i + 3) {
					String lineA = StringUtils.normalizeSpace(lines[j]).trim();
					if (lineA.contains("Razão Social:")) {
						lineA = StringUtils.substringAfter(lineA, "Razão Social:").trim();
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(FORNECEDOR);
						diffValue.setNewValue(lineA);
						diffValue.setLine(j);
						list.add(diffValue);
					}
					if (lineA.contains("CNPJ:")) {
						lineA = StringUtils.substringAfter(lineA, "CNPJ:").trim();
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(CNPJ_BEN);
						diffValue.setNewValue(lineA);
						diffValue.setLine(i);
						list.add(diffValue);
					}
					j = j + 1;
				}

			}
			if (line.contains("Dados do Pagador Efetivo")) {
				int j = i + 1;
				while (j < i + 3) {
					String lineA = StringUtils.normalizeSpace(lines[j]).trim();
					if (lineA.contains("CNPJ:")) {
						lineA = StringUtils.substringAfterLast(lineA, "CNPJ:").trim();
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(CNPJ_PAG);
						diffValue.setNewValue(lineA);
						diffValue.setLine(i);
						list.add(diffValue);
					}
					j = j + 1;
				}
			}

			if (line.contains("Data de Vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Nominal:")) {
				String lineA = StringUtils.substringAfter(line, "R$").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total a Cobrar:")) {
				String lineA = StringUtils.substringAfterLast(line, "R$").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data da Transação:")) {
				String lineA = StringUtils.substringAfterLast(line, "Data da Transação:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;

		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Títulos > 2ª via de Comprovante");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

}
