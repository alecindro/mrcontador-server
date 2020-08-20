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
import liquibase.pro.packaged.cn;

public class ComprovanteBradesco extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {

		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 6) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Boleto de Cobrança")) {
			return parseBoletoCobranca(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Água, Luz, Telefone e Gás")) {
			return parseConcessionaria(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferência entre Contas Bradesco")) {
			return parseTEC(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("IMPOSTO/TAXAS")) {
			return parseImpostosTaxas(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferências Para Contas de Outros Bancos (TED)")) {
			return parseTED(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferências Para Contas de Outros Bancos (DOC)")) {
			return parseDOC(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4].trim()).equals("COMPROVANTE DE PAGAMENTO DO SIMPLES NACIONAL")) {
			return parseSimplesNacional(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("FGTS")) {
			return parseFGTS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5].trim()).equals("COMPROVANTE DE PAGAMENTO DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("Comprovante não identificado");
	}

	private List<Comprovante> parseImpostosTaxas(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Descrição:").trim(), "IDENTIFICADOR:")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
				if (StringUtils.isBlank(lineA)) {
					lineA = StringUtils.substringBefore(StringUtils.normalizeSpace(lines[i + 1]), "Data do vencimento:")
							.trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor principal:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Valor principal:").trim(),
						"Desconto:");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor principal:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.substringAfter(line, "Data do vencimento:").trim(), "Valor principal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor do pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do pagamento: R$").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseBoletoCobranca(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Razao Social Sacador")) {
				String lineA = StringUtils.substringAfter(line, "Razao Social Sacador").trim();
				String lineB = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i+2]), "CPF/CNPJ Sacador").trim();
				int _cnpj = i+2;
				int _razao = i;
				DiffValue sacador = new DiffValue();
				DiffValue cnpj = new DiffValue();
				
				if(lineA.equalsIgnoreCase("Não informado")) {
					lineA = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i-5]), "Razao Social").trim();
					_razao = i-5;
					lineB = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i-1]), "CPF/CNPJ Beneficiário:").trim();
					_cnpj = i-1;
				}
				cnpj.setOldValue(CNPJ_BEN);
				cnpj.setNewValue(lineB);
				cnpj.setLine(_cnpj);
				list.add(cnpj);
				sacador.setOldValue(FORNECEDOR);
				sacador.setNewValue(lineA);
				sacador.setLine(_razao);
				list.add(sacador);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data de vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String lineA = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor total:")) {
				String lineA = StringUtils.substringAfter(line, "Valor total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição:")) {
				String lineA = StringUtils.substringAfter(line, "Descrição:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Boleto de Cobrança");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseConcessionaria(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Concessionária:")) {
				String lineA = StringUtils.substringAfter(line, "Concessionária:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
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
			if (line.contains("Descrição:")) {
				String lineA = StringUtils.substringAfter(line, "Descrição:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Água, Luz, Telefone e Gás");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseTEC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nome do favorecido:")) {
				String lineA = StringUtils.substringAfter(line, "Nome do favorecido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
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
			if (line.contains("Descrição:")) {
				String lineA = StringUtils.substringAfter(line, "Descrição:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Transferência entre Contas Bradesco");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseTED(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta de crédito:")) {
				String lineA = StringUtils.substringAfter(line, "Conta de crédito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
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
			if (line.contains("Finalidade:")) {
				String lineA = StringUtils.substringAfter(line, "Finalidade:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Transferências Para Contas de Outros Bancos (TED)");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseDOC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nome do favorecido:")) {
				String lineA = StringUtils.substringAfter(line, "Nome do favorecido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				if (StringUtils.normalizeSpace(lines[i - 1]).contains("Nome do favorecido:")) {
					String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(lineA);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("CPF:")) {
				String lineA = StringUtils.substringAfter(line, "CPF:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String lineA = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor total:")) {
				String lineA = StringUtils.substringAfter(line, "Valor total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Finalidade:")) {
				String lineA = StringUtils.substringAfter(line, "Finalidade:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Transferências Para Contas de Outros Bancos (DOC)");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseSimplesNacional(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência de Débito:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência de Débito")), "Conta de Débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta de Débito:")) {
				String lineA = StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta de Débito:")).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
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
			if (line.contains("Número do Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Número do Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DO SIMPLES NACIONAL");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	private List<Comprovante> parseDarf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência de Débito:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência de Débito")), "Conta de Débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta de Débito:")) {
				String lineA = StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta de Débito:")).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor do Principal:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do Principal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
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
			if (line.contains("Número de Referencia:")) {
				String lineA = StringUtils.substringAfter(line, "Número de Referencia:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DARF");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseFGTS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				
				String lineB = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta:")), "|Tipo:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("IDENTIF. EMPRESA:")) {
				String lineA = StringUtils.substringAfter(StringUtils.substringBefore(line, "IDENTIF. EMPRESA:"),"CNPJ/CEI:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String lineA = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor do pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("IDENTIF. EMPRESA:")) {
				String lineA = StringUtils.substringAfter(line, "IDENTIF. EMPRESA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DO SIMPLES NACIONAL");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}


}
