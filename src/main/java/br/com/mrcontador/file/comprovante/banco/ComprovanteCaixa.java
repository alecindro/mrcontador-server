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
import br.com.mrcontador.file.comprovante.TipoComprovante;
import br.com.mrcontador.util.MrContadorUtil;

public class ComprovanteCaixa extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {

		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de Pagamento de Boleto")) {
			return parsePagtoBoleto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("2ª Via - Comprovante de Pagamento de Boleto")) {
			return parsePagtoBoleto(_lines, agenciabancaria, parceiro);
		}
		
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.equals("Comprovante de pagamento de água, luz, telefone e gás")) {
			return parsePagtoAgua(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.equals("Comprovante de transferência entre contas da CAIXA - TEV")) {
			return parsePagtoTEV(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de pagamento de GPS")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[2]).trim().equals("Comprovante de Autorização da Folha")) {
			return parseAutFolha(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de transferência eletrônica disponível")) {
			return parseCreditoTED(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de pagamento de FGTS")) {
			return parseFGTS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Consulta Detalhes da Folha")) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de agendamento de transferência entre contas da CAIXA - TEV")) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de pagamento de Simples Nacional")) {
			return parseSimplesNacional(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().equals("Comprovante de pagamento de tributos federais")) {
			return parseTributosFederais(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("Comprovante não identificado");
	}

	private List<Comprovante> parsePagtoBoleto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("CPF/CNPJ:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta de débito:").trim(), "/").trim();
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Beneficiário original / Cedente")) {
				int j = i + 1;
				while (j < lines.length) {
					String lineJ = StringUtils.normalizeSpace(lines[j]).trim();
					String lineJ1 = StringUtils.normalizeSpace(lines[j - 1]).trim();
					if (lineJ.contains("Nome/Razão Social:") && !lineJ1.contains("Nome Fantasia:")) {
						String beneficiario = lineJ1 + " "
								+ StringUtils.substringAfter(lineJ, "Nome/Razão Social:").trim();
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(FORNECEDOR);
						diffValue.setNewValue(beneficiario);
						diffValue.setLine(j);
						list.add(diffValue);
						break;
					}
					if (lineJ.contains("Nome/Razão Social:")) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(FORNECEDOR);
						diffValue.setNewValue(StringUtils.substringAfter(lineJ, "Nome/Razão Social:").trim());
						diffValue.setLine(j);
						list.add(diffValue);
						break;
					}
					j = j + 1;
				}
				while (j < lines.length) {
					String lineJ = StringUtils.normalizeSpace(lines[j]).trim();
					if (lineJ.contains("CPF/CNPJ:")) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(CNPJ_BEN);
						String value = StringUtils.substringAfter(lineJ, "CPF/CNPJ:").trim();
						value = MrContadorUtil.onlyNumbers(value);
						diffValue.setNewValue(value);
						diffValue.setLine(j);
						list.add(diffValue);
						break;
					}
					j = j + 1;
				}
			}
			if (line.contains("Data do Vencimento:")) {
				String value = StringUtils.substringAfter(line, "Data do Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Nominal do Boleto:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal do Boleto:").trim();
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
			if (line.contains("Data/hora da operação:")) {
				String value = StringUtils.substringAfter(line, "Data/hora da operação:").trim().split("\\s")[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String value = StringUtils.substringAfter(line, "Código da operação:").trim();
				value = MrContadorUtil.removeDots(value);
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
		diffValue.setNewValue("Comprovante de Pagamento de Boleto");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoAgua(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(line, ".").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Empresa:")) {
				String empresa = StringUtils.substringAfter(line, "Empresa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(empresa);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String valor = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String data = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de pagamento de água, luz, telefone e gás");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoTEV(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta origem:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta origem:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Nome destinatário:")) {
				String fornecedor = StringUtils.substringAfter(line, "Nome destinatário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(fornecedor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String valor = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String data = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de transferência entre contas da CAIXA - TEV");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Convênio:")) {
				String fornecedor = StringUtils.substringAfter(line, "Convênio:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(fornecedor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String valor = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String data = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de pagamento de GPS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseAutFolha(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, ".").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Nome da Folha:")) {
				String fornecedor = StringUtils.substringAfter(line, "Nome da Folha:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(fornecedor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total Folha:")) {
				String valor = StringUtils.substringAfter(line, "Valor Total Folha:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data débito:")) {
				String data = StringUtils.substringAfter(line, "Data débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de Autorização da Folha");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseCreditoTED(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta origem:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta origem:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("CNPJ/CEI empresa:") && StringUtils.normalizeSpace(lines[i + 1]).contains("Banco:")) {
				String cnpj = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
				cnpj = MrContadorUtil.removeDots(cnpj);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(cnpj);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nome:")) {
				String valor = StringUtils.substringAfter(line, "Nome:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ:") && !StringUtils.normalizeSpace(lines[i + 1]).contains("Banco:")) {
				String data = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor:")) {
				String data = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Hora da")) {
				String data = StringUtils.substringAfter(line, "Hora da").trim().split("\\s")[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de transferência eletrônica disponível");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parseFGTS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("CNPJ/CEI empresa:")) {
				String cnpj = StringUtils.substringAfter(line, "CNPJ/CEI empresa:").trim();
				cnpj = MrContadorUtil.removeDots(cnpj);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(cnpj);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor recolhido:")) {
				String data = StringUtils.substringAfter(line, "Valor recolhido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Débito:")) {
				String data = StringUtils.substringAfter(line, "Data de Débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de pagamento de FGTS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
		return comprovantes;
	}
	
	private List<Comprovante> parseSimplesNacional(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Valor:")) {
				String data = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String data = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Convênio:")) {
				String data = StringUtils.substringAfter(line, "Convênio:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de pagamento de Simples Nacional");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseTributosFederais(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), "/")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, "/").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("NÚMERO DO CPF OU CNPJ")) {
				String data = StringUtils.substringAfter(line, "NÚMERO DO CPF OU CNPJ").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DATA DE VENCIMENTO")) {
				String data = StringUtils.substringAfter(line, "DATA DE VENCIMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("VALOR TOTAL")) {
				String data = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de débito:")) {
				String data = StringUtils.substringAfter(line, "Data de débito:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Código da operação:")) {
				String data = StringUtils.substringAfter(line, "Código da operação:").trim();
				data = MrContadorUtil.removeDots(data);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de pagamento de tributos federais");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

}
