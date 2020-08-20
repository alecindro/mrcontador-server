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

public class ComprovanteItau extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 6) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		String line = StringUtils.normalizeSpace(_lines[0].trim());
		if (line.equals("Banco Itaú - Comprovante de Agendamento")) {
			return null;
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento de concessionárias")) {
			return parseConcessionarias(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Comprovante de Pagamento SIMPLES NACIONAL")) {
			return parseSimplesNacional(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Comprovante de pagamento DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Transferência")) {
			return parseTransferencia(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento com código de barras")) {
			return parsePagtoCodBarras(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento")
				&& StringUtils.normalizeSpace(_lines[1].trim()).equals("FGTS- GRRF")) {
			return parseFgtsGrf(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento")
				&& StringUtils.normalizeSpace(_lines[1].trim()).equals("GRF - Guia de Recolhimento do FGTS")) {
			return parseFgts(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento")
				&& StringUtils.normalizeSpace(_lines[1].trim()).equals("DOC C – outra titularidade")) {
			return parseDocC(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento")
				&& StringUtils.normalizeSpace(_lines[1].trim()).contains("TED C")) {
			return parseTEDC(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("GPS")) {
			return parseGPS(_lines, agenciabancaria, parceiro);
		}
		if (contaisIdentificaoNoMeu(_lines)) {
			return parseBoletoIdentificaoNoMeu(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto")) {
			return parseBoleto(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("Comprovante não identificado");

	}

	private boolean contaisIdentificaoNoMeu(String[] lines) {
		for (String line : lines) {
			if (StringUtils.normalizeSpace(line.trim()).equals("Identificação no meu")) {
				return true;
			}
		}
		return false;
	}

	private List<Comprovante> parseGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		String ctrl = "";
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Data do pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor do INSS:")) {
				String value = StringUtils.substringAfter(line, "Valor do INSS:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor total")) {
				String value = StringUtils.substringAfter(line, "Valor total").trim();
				value = StringUtils.remove(value, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificação do extrato:")) {
				String value = StringUtils.substringAfter(line, "Identificação do extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Agência/Conta:")) {
				String value = StringUtils.substringAfter(line, "Agência/Conta:").trim();
				String[] values = value.split("\\s");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(values[0]);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(values[1]);
				diffValue2.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Agência:") && !line.contains("Agência/Conta:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:").trim(), "Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:") && !line.contains("Agência/Conta:")) {
				String value = StringUtils.substringAfter(line, "Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CTRL")) {
				ctrl = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL").trim(), ".");
			}
		}
		Optional<DiffValue> optional = list.stream().filter(diff -> diff.getOldValue().equals(OBS)).findFirst();
		if (optional.isEmpty()) {
			DiffValue diffValue = new DiffValue();
			diffValue.setOldValue(OBS);
			diffValue.setNewValue(ctrl);
			list.add(diffValue);
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("Previdência Social");
		list.add(diffValue);
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(DOCUMENTO);
		diffValue2.setNewValue("GPS - Guia da Previdência Social");
		list.add(diffValue2);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseBoleto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência/conta:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Agência/conta:"), "CNPJ:")
						.trim();
				String[] values = lineA.split("/");
				if (values != null) {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(AGENCIA);
					diffValue.setNewValue(values[0]);
					diffValue.setLine(i);
					list.add(diffValue);
					if (values.length == 2) {
						DiffValue diffValue2 = new DiffValue();
						diffValue2.setOldValue(CONTA);
						diffValue2.setNewValue(values[1]);
						diffValue2.setLine(i);
						list.add(diffValue2);

					}
				}
			}
			if (line.contains("CNPJ:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "CNPJ:").trim(), "Empresa:")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de vencimento:") && line.contains("Beneficiário:")
					&& !line.contains("CPF/CNPJ do beneficiário:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String benef = _line.substring(0, _line.length() - 10).trim();
				String data = _line.substring(_line.length() - 10, _line.length()).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(benef);
				diffValue.setLine(i + 1);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DATA_VCTO);
				diffValue2.setNewValue(data);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
			}
			if (line.contains("Data de vencimento:") && line.contains("CPF/CNPJ do beneficiário:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String data = _line.substring(_line.length() - 10, _line.length()).trim();
				if (MrContadorUtil.onlyNumbers(data).length() == 8) {
					String cnpj_ben = _line.substring(_line.length() - 29, _line.length() - 10).trim();
					String razao = StringUtils
							.substringBefore(StringUtils.substringAfter(_line, "Razão Social:"), cnpj_ben).trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(razao);
					diffValue.setLine(i + 1);
					list.add(diffValue);
					DiffValue diffValue2 = new DiffValue();
					diffValue2.setOldValue(DATA_VCTO);
					diffValue2.setNewValue(data);
					diffValue2.setLine(i + 1);
					list.add(diffValue2);
					DiffValue diffValue3 = new DiffValue();
					diffValue3.setOldValue(CNPJ_BEN);
					diffValue3.setNewValue(cnpj_ben);
					diffValue3.setLine(i + 1);
					list.add(diffValue3);
				} else {
					String razao = StringUtils.substringAfter(_line, "Razão Social:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(razao);
					diffValue.setLine(i + 1);
					list.add(diffValue);
					String _line2 = StringUtils.normalizeSpace(lines[i + 2]);
					data = _line2.substring(_line2.length() - 10, _line2.length()).trim();
					String cnpj_ben = StringUtils.substringBefore(_line2, data).trim();
					DiffValue diffValue2 = new DiffValue();
					diffValue2.setOldValue(DATA_VCTO);
					diffValue2.setNewValue(data);
					diffValue2.setLine(i + 2);
					list.add(diffValue2);
					DiffValue diffValue3 = new DiffValue();
					diffValue3.setOldValue(CNPJ_BEN);
					diffValue3.setNewValue(cnpj_ben);
					diffValue3.setLine(i + 1);
					list.add(diffValue3);
				}
			}

			if (line.trim().contains("Valor do boleto")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(_line.trim());
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.trim().contains("Valor do documento")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(_line.trim());
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.trim().contains("Valor do pagamento")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1].trim();
				if (MrContadorUtil.onlyNumbers(value).length() > 3) {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(VALOR_PGTO);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				} else {
					_line = StringUtils.normalizeSpace(lines[i + 2]);
					values = _line.split("\\s");
					value = values[values.length - 1].trim();
					if (MrContadorUtil.onlyNumbers(value).length() > 3) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(VALOR_PGTO);
						diffValue.setNewValue(value);
						diffValue.setLine(i + 2);
						list.add(diffValue);
					}
				}
			}
			if (line.contains("Data de pagamento:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1].trim();
				if (MrContadorUtil.onlyNumbers(value).length() == 8) {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(DATA_PGTO);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				} else {
					_line = StringUtils.normalizeSpace(lines[i + 2]);
					values = _line.split("\\s");
					value = values[values.length - 1].trim();
					if (MrContadorUtil.onlyNumbers(value).length() == 8) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(DATA_PGTO);
						diffValue.setNewValue(value);
						diffValue.setLine(i + 2);
						list.add(diffValue);
					}
				}
			}
			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
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
		diffValue.setNewValue("Comprovante de pagamento de boleto");
		diffValue.setLine(0);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseBoletoIdentificaoNoMeu(String[] lines, Agenciabancaria agenciabancaria,
			Parceiro parceiro) throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência/conta:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Agência/conta:"), "CNPJ:")
						.trim();
				String[] values = lineA.split("/");
				if (values != null) {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(AGENCIA);
					diffValue.setNewValue(values[0]);
					diffValue.setLine(i);
					list.add(diffValue);
					if (values.length == 2) {
						DiffValue diffValue2 = new DiffValue();
						diffValue2.setOldValue(CONTA);
						diffValue2.setNewValue(values[1]);
						diffValue2.setLine(i);
						list.add(diffValue2);

					}
				}
			}
			if (line.contains("CNPJ:") && !line.contains("CPF/CNPJ:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "CNPJ:").trim(), "Empresa:")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificação no meu")) {
				String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 1]), "comprovante:")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("Data de vencimento:") && line.contains("Beneficiário:")
					&& !line.contains("CPF/CNPJ do beneficiário:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String data = _line.substring(_line.length() - 10, _line.length()).trim();
				String razao = StringUtils.substringBefore(_line, data).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(razao);
				diffValue.setLine(i + 1);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DATA_VCTO);
				diffValue2.setNewValue(data);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
			}
			if (line.contains("Data de vencimento:") && line.contains("CPF/CNPJ do beneficiário:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String data = _line.substring(_line.length() - 10, _line.length()).trim();
				if (MrContadorUtil.onlyNumbers(data).length() == 8) {
					String cnpj_ben = _line.substring(_line.length() - 29, _line.length() - 10).trim();
					String razao = StringUtils
							.substringBefore(StringUtils.substringAfter(_line, "Razão Social:"), cnpj_ben).trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(razao);
					diffValue.setLine(i + 1);
					list.add(diffValue);
					DiffValue diffValue2 = new DiffValue();
					diffValue2.setOldValue(DATA_VCTO);
					diffValue2.setNewValue(data);
					diffValue2.setLine(i + 1);
					list.add(diffValue2);
					DiffValue diffValue3 = new DiffValue();
					diffValue3.setOldValue(CNPJ_BEN);
					diffValue3.setNewValue(cnpj_ben);
					diffValue3.setLine(i + 1);
					list.add(diffValue3);
				} else {
					String razao = StringUtils.substringAfter(_line, "Razão Social:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(FORNECEDOR);
					diffValue.setNewValue(razao);
					diffValue.setLine(i + 1);
					list.add(diffValue);
					String _line2 = StringUtils.normalizeSpace(lines[i + 2]);
					data = _line2.substring(_line2.length() - 10, _line2.length()).trim();
					String cnpj_ben = StringUtils.substringBefore(_line2, data).trim();
					cnpj_ben = cnpj_ben.substring(cnpj_ben.length() - 18, cnpj_ben.length());
					DiffValue diffValue2 = new DiffValue();
					diffValue2.setOldValue(DATA_VCTO);
					diffValue2.setNewValue(data);
					diffValue2.setLine(i + 2);
					list.add(diffValue2);
					DiffValue diffValue3 = new DiffValue();
					diffValue3.setOldValue(CNPJ_BEN);
					diffValue3.setNewValue(cnpj_ben);
					diffValue3.setLine(i + 1);
					list.add(diffValue3);
				}
			}
			if (line.trim().contains("Valor do boleto")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.trim().contains("Valor do documento")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.trim().contains("Valor do pagamento")) {
				int lin = i + 1;
				String _line = StringUtils.normalizeSpace(lines[lin]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1].trim();
				if (MrContadorUtil.onlyNumbers(value).isEmpty()) {
					lin = lin + 1;
					_line = StringUtils.normalizeSpace(lines[lin]);
					values = _line.split("\\s");
					value = values[values.length - 1].trim();

				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(lin);
				list.add(diffValue);

			}
			if (line.contains("Data de pagamento:")) {
				String _line = StringUtils.normalizeSpace(lines[i + 1]);
				String[] values = _line.split("\\s");
				String value = values[values.length - 1].trim();
				if (MrContadorUtil.onlyNumbers(value).length() == 8) {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(DATA_PGTO);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				} else {
					_line = StringUtils.normalizeSpace(lines[i + 2]);
					values = _line.split("\\s");
					value = values[values.length - 1].trim();
					if (MrContadorUtil.onlyNumbers(value).length() == 8) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(DATA_PGTO);
						diffValue.setNewValue(value);
						diffValue.setLine(i + 2);
						list.add(diffValue);
					}
				}
			}
			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseTEDC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Agência:")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta corrente:")) {
				String lineA = StringUtils.substringAfter(line, "Conta corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
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
			if (line.contains("CPF / CNPJ:")) {
				String lineA = StringUtils.substringAfter(line, "CPF / CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor da TED:")) {
				String lineA = StringUtils.substringAfter(line, "Valor da TED:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("TED solicitada em")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "TED solicitada em").trim(),
						"às");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(DOCUMENTO);
		diffValue.setNewValue("TED C – outra titularidade");
		diffValue.setLine(1);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseConcessionarias(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Agência:"), "Conta:")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				String lineB = StringUtils.substringAfter(line, "Conta:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}

			if (line.contains("Valor do documento:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Operação efetuada em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Operação efetuada em").trim(), "\s").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		String lineA = StringUtils.normalizeSpace(lines[1]).trim();
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue(lineA);
		diffValue.setLine(1);
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
			if (line.contains("Agência/Conta:")) {
				String lineA = StringUtils.substringAfter(line, "Agência/Conta:").trim();
				String[] values = lineA.split("\\s");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(values[0]);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(values[1]);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}

			if (line.contains("Valor total")) {
				String lineA = StringUtils.substringAfter(line, "Valor total").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Número do documento:")) {
				String value = StringUtils.substringAfter(line, "Número do documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;

		}
		String lineA = StringUtils.normalizeSpace(lines[1]).trim();
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue(lineA);
		diffValue.setLine(1);
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
			if (line.contains("Agência/Conta:")) {
				String lineA = StringUtils.substringAfter(line, "Agência/Conta:").trim();
				String[] values = lineA.split("\\s");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(values[0]);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(values[1]);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Valor principal:")) {
				String lineA = StringUtils.substringAfter(line, "Valor principal:").trim();
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
			if (line.contains("Data do pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data do vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Agente arrecadador:")) {
				String value = StringUtils.substringAfter(line, "Agente arrecadador:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
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
		diffValue.setNewValue("Comprovante de pagamento DARF");
		diffValue.setLine(1);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseTransferencia(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 2]);
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i + 2);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(lineA, "Conta corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i + 2);
				list.add(diffValue2);
			}
			if (line.contains("Dados da conta creditada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]).trim();
				String nome = StringUtils.substringAfter(lineA, "Nome:");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(nome);
				diffValue.setLine(i + 1);
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
			if (line.contains("Transferência efetuada em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Transferência efetuada em"), "às").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoCodBarras(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 2]);
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i + 2);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(lineA, "Conta:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i + 2);
				list.add(diffValue2);
			}

			if (line.contains("Valor do documento:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Operação efetuada em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Operação efetuada em"), "às").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		String lineA = StringUtils.normalizeSpace(lines[1]).trim();
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue(lineA);
		diffValue.setLine(1);
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
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]);
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i + 1);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(lineA, "Conta:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
			}

			if (line.contains("Valor Recolhido:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Recolhido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
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
			if (line.contains("Operação efetuada em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Operação efetuada em"), "às").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		String lineA = StringUtils.normalizeSpace(lines[1]).trim();
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue(lineA);
		diffValue.setLine(1);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseFgts(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]);
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i + 1);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(lineA, "Conta:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
			}

			if (line.contains("Valor Recolhido:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Recolhido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
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
			if (line.contains("Operação efetuada em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Operação efetuada em"), "às").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		String lineA = StringUtils.normalizeSpace(lines[1]).trim();
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue(lineA);
		diffValue.setLine(1);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseDocC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 2]);
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta corrente:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i + 2);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(lineA, "Conta corrente:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i + 2);
				list.add(diffValue2);
			}
			
			if(line.contains("CPF / CNPJ:")) {
				String lineA = StringUtils.substringAfter(line, "CPF / CNPJ:");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(1);
				list.add(diffValue);
			}
			if(line.contains("Nome do favorecido:")) {
				String lineA = StringUtils.substringAfter(line, "Nome do favorecido:");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(1);
				list.add(diffValue);
			}

			if (line.contains("Valor do DOC:")) {
				String lineA = StringUtils.substringAfter(line, "Valor do DOC:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
	
			if (line.contains("DOC solicitado em")) {
				String lineA = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "DOC solicitado em"), "às").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Finalidade:")) {
				String value = StringUtils.substringAfter(line, "Finalidade:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	/*
	 * private static final String TED_C =
	 * "                                                      Banco         Itaú     -  Comprovante                 de    Pagamento\n"
	 * +
	 * "                                                                      TED       C   –   outra      titularidade\n"
	 * + "               Identificação       no   extrato: $4\n" +
	 * "         Dados      da   conta    debitada:\n" +
	 * "                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n"
	 * +
	 * "                                      Agência:      $ag                           Conta     corrente:     $conta\n"
	 * + "         Dados      da   TED:\n" +
	 * "                         Nome       do  favorecido: $2\n" +
	 * "                                     CPF     / CNPJ:      $cnpj_ben\n" +
	 * "              Número        do  banco,      nome      ou\n" +
	 * "                                                 ISPB:    001    - BANCO         DO    - ISPB     RASIL      SA\n"
	 * +
	 * "                                            Agência:      3412     EMPRESARIAL               SERRA        GAUCHA\n"
	 * + "                                 Conta      corrente:     00000007186-2\n"
	 * +
	 * "                                    Valor    da   TED:    R$    $valor_pag\n"
	 * +
	 * "                                        Finalidade:       01-CREDITO            EM    CONTA        CORRENTE\n"
	 * +
	 * "         TED     solicitada     em    $pagto          às  11:05:44       via  bankline.\n"
	 * + "         Autenticação:\n" +
	 * "         1CE5B41568282068284B06A756C3A267EE05C2A8\n" +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 * 
	 * private static final String BOLETO = "         $4\n" +
	 * "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          $ag/$conta                          CNPJ:      $cnpj                         Empresa:        MOTO        BOMBAS         F   LTDAME\n"
	 * + "         COOPERATIVA               CENTRAL           DE    CREDITO\n" +
	 * "         / AILOS                                                                  08591     04002      20012      442701      00003      978012       1  81770000024520\n"
	 * +
	 * "          Beneficiário:                                                                                                              Data  de  vencimento:\n"
	 * + "          $2                    $data_venc\n" +
	 * "                                                                                                                                     Valor  do boleto  (R$);\n"
	 * +
	 * "                                                                                                                                     $valor_doc\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Mora/Multa    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "                                                                                                                                     $valor_pag\n"
	 * +
	 * "                                                                                                                                     Data  de  pagamento:\n"
	 * +
	 * "                                                                                                                                      $pagto\n"
	 * + "          Autenticação   mecânica:\n" +
	 * "          2C47F73482B4931C67C06FBC6FB03556DAEB2BD1\n" +
	 * "         Operação        efetuada       em    26/02/2020         via   ,CTRL       $doc.\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 * 
	 * private static final String _BOLETO =
	 * "         Comprovante                 de    pagamento               de    boleto\n"
	 * + "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          1575/12906-7                          CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n"
	 * + "                   Identificação        no   meu\n" +
	 * "                               comprovante:         TRANSPORTADORA                     PLIMO\n"
	 * + "          Pagador   final:\n" +
	 * "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
	 * +
	 * "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
	 * +
	 * "                                                                                  00190     00009      01719      026005      00975      906173       1  81790000006512\n"
	 * + "          Beneficiário:TRANSPORTADORA            PLIMOR     LTDA\n" +
	 * "         .                                                                               CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
	 * +
	 * "          Razão   Social: TRANSPORTADORA            PLIMOR    LTDA                        88.085.485/0001-04                          28/02/2020\n"
	 * +
	 * "                                                                                                                                     Valor  do documento     (R$);\n"
	 * +
	 * "                                                                                                                                     65,12\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          65,12\n"
	 * +
	 * "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
	 * +
	 * "                                                                                                                                      28/02/2020\n"
	 * +
	 * "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
	 * +
	 * "          BD2FF20B9E32CE535F10AC60EB5F02FDA92A0643                                                                                    Não\n"
	 * +
	 * "         Operação        efetuada       em    28/02/2020         às   18:32:33h       via   bankline,      CTRL      29239.\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 * 
	 * private static final String BOLETO2 =
	 * "         Comprovante                 de    pagamento               de    boleto\n"
	 * + "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          $ag/$conta                          CNPJ:      08.892.611/0001-01                         Empresa:\n"
	 * +
	 * "                                                                                                                                             MOTO        BOMBAS         F   LTDA\n"
	 * +
	 * "                                                                                                                                             ME\n"
	 * + "                   Identificação        no   meu\n" +
	 * "                               comprovante:\n" +
	 * "                                                    jacuzzi\n" +
	 * "          Pagador   final:\n" +
	 * "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
	 * +
	 * "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
	 * +
	 * "                                                                                  34191     12879      06508      040273      81290      020007       6  82120000055486\n"
	 * +
	 * "          Beneficiário:  $2                                                              CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
	 * +
	 * "          Razão   Social: JACUZZI    DO   BRASIL    IND  COM.LTD                          $cnpj_ben                                   $data_venc\n"
	 * +
	 * "                                                                                                                                     Valor  do documento     (R$);\n"
	 * +
	 * "                                                                                                                                     $valor_doc\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA                   $cnpj                                       $valor_pag\n"
	 * +
	 * "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
	 * +
	 * "                                                                                                                                      $pagto\n"
	 * +
	 * "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
	 * +
	 * "          6F1B1B69BA2ACF435AA022D7C3D368083E56B6B2                                                                                    Não\n"
	 * +
	 * "         Operação        efetuada       em    01/04/2020         às   19:31:09h       via   bankline,      CTRL      $doc.\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
	 * +
	 * "                                                                                                                                                                                                 14\n"
	 * + "\n";
	 
	private static final String CONCESSIONARIA = "                                   Banco         Itaú    -  $4\n"
			+ "                                                                            $2\n"
			+ "               Identificação       no   extrato:    tim\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n"
			+ "                                      Agência:      $ag               Conta:     $conta \n"
			+ "         Dados      do   pagamento:\n"
			+ "                        Código      de   barras:    846900000064             212401090114            004224010621            101392412850\n"
			+ "                   Valor     do  documento:         R$    $valor_pag\n"
			+ "         Operação        efetuada       em    $pagto             às   17:59:31h       via   EMPRESA           PLUS,      CTRL      $doc           .\n"
			+ "         Autenticação:\n" + "         9C04474D6B9FBD00673E32966BB55612D199EEB2\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
			+ "                                                                                                                                          ";

	private static final String SIMPLES_NACIONAL = "                                                        Comprovante           de  Pagamento          $4\n"
			+ "                                                            Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n"
			+ "                                      Código      de   barras:     85810000011           66670328201            21071720115           79301010700\n"
			+ "                                  Data     do   pagamento:         $pagto    \n"
			+ "                              Número       do   documento:         $doc                 \n"
			+ "                                                  Valor     total  R$   $valor_pag\n"
			+ "         Autenticação:         01201594031006373900\n"
			+ "         Operação        efetuada       via  EMPRESA           PLUS,      CTRL       202004292063739.\n"
			+ "         Autenticação         Digital    Itaú:   B8867C193AA26337AB2E7B39E02F1C5E916D05AC\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                          Agência/Conta:           $ag      $conta \n"
			+ "                                    Nome       da   empresa:       MOTO       BOMBAS          F  LTDA      ME\n"
			+ "          - Pagamento          efetuado      em    sábado,       domingo       ou   feriado,     será   quitado      no   próximo      dia   útil.\n"
			+ "         - O   cliente    assume       total   responsabilidade           por   eventuais       danos     decorrentes         de   inexatidão       ou  insuficiência        nas\n"
			+ "         informações         por   ele   inseridas.\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
			+ "                                                                                                                                                                                                  9\n"
			+ "\n";
	/*
	 * private static final String GPS =
	 * "                                             Comprovante           de   Pagamento          de  $4\n"
	 * + "                    Agente      arrecadador:\n" +
	 * "                                     CNC:341        Banco      Itaú   S/A\n"
	 * + "                    Data    do   pagamento:         $pagto\n" +
	 * "                              Competência:          02/2020\n" +
	 * "                                Identificador:      $doc\n" +
	 * "                Código      de   pagamento:         4308\n" +
	 * "                             Valor    do   INSS:    R$    $valor_doc\n" +
	 * "                Valor    outras     entidades:      R$    0,00\n" +
	 * "              Valor    atual.   mon/jur/mul:        R$    0,00\n" +
	 * "                                   Valor    total:  R$    $valor_pag\n" +
	 * "         Autenticação:         0087\n" +
	 * "               Modelo      aprovado        pela   SRF-ADE          conjunto      CORAT/COTEC               nº001,     de   2006\n"
	 * + "         Identificação        do  extrato:     GPS     011\n" +
	 * "         Nome      do   contribuinte:       MOTO        BOMBAS         FLORIANOPOLIS\n"
	 * + "         Dados      da   conta    debitada:\n" +
	 * "                                      Agência:      $ag           Conta:     $conta\n"
	 * +
	 * "         Pagamento          efetuado      via   EMPRESA           PLUS,      CTRL      202002280480087\n"
	 * +
	 * "         Autenticação         Digital    Itaú:   65CC6F5CECFB88BD727CBC9E5282FD10409B8230\n"
	 * +
	 * "         - As   informações         fornecidas       para    o  pagamento          são   de   inteira    responsabilidade           do   cliente.    Pagamentos\n"
	 * +
	 * "         e/ou    dados     fornecidos       indevidamente           deverão       ser  regularizados         diretamente         com    a  delegacia       da   Receita\n"
	 * +
	 * "         Federal.      Pagamentos          efetuados        em   sábado,       domingo       ou   feriado     terão    a  quitação      no   próximo      dia   útil\n"
	 * + "         seguinte.\n" +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 * 
	 * private static final String GPS2 =
	 * "                                            Comprovante           de   Pagamento          de  GPS      - Guia    da   Previdência        Social\n"
	 * +
	 * "                                                            Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n"
	 * +
	 * "                                      Código      de   barras:     85800000006           56320270430            81013499740           03962020018\n"
	 * +
	 * "                                  Data     do   pagamento:         $pagto\n"
	 * +
	 * "                                             Competência:          01/2020\n"
	 * +
	 * "                                              Identificador:       $doc          \n"
	 * + "                               Código      de   pagamento:         4308\n"
	 * +
	 * "                                                  Valor     total  R$   $valor_pag\n"
	 * + "         Autenticação:         2853\n" +
	 * "         MODELO          APROVADO             PELO      SRF-ADE         CONJUNTO             CORAT/COTEC               Nº001,      DE    2006\n"
	 * +
	 * "         Operação        efetuada       via  EMPRESA           PLUS,      CTRL       202001316822853.\n"
	 * +
	 * "         Autenticação         Digital    Itaú:   F73302E5BD14EDA2AFD51CCF89DB5F421ACA9F3C\n"
	 * + "         Dados      da   conta    debitada:\n" +
	 * "                                          Agência/Conta:           $ag      $conta \n"
	 * +
	 * "                                    Nome       da   empresa:       MOTO       BOMBAS          F  LTDA      ME\n"
	 * +
	 * "          - Pagamento          efetuado      em    sábado,       domingo       ou   feriado,     será   quitado      no   próximo      dia   útil.\n"
	 * +
	 * "         - O   cliente    assume       total   responsabilidade           por   eventuais       danos     decorrentes         de   inexatidão       ou  insuficiência        nas\n"
	 * + "         informações         por   ele   inseridas.\n" +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 *
	private static final String DARF = "                                                                    Comprovante           de   pagamento         $4\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                   Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n"
			+ "                                  Data     do   pagamento:         $pagto\n"
			+ "                                 Período      de   apuração:       31/01/2020\n"
			+ "                         Número       do   CPF     ou   CNPJ:      08892611000101\n"
			+ "                                      Código      da   receita:    $2\n"
			+ "                               Número        de   referência:      00000000000000000\n"
			+ "                                  Data    do   vencimento:         $data_venc\n"
			+ "                                           Valor    principal:     R$   $valor_doc\n"
			+ "                                           Valor    da   multa:    R$   0,00\n"
			+ "                           Valor    dos   juros/encargos:          R$   0,00\n"
			+ "                                                 Valor     total:  R$   $valor_pag\n" + "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 MODELO           APROVADO            PELA      SRF-ADE          CONJUNTO            CORAT/COTEC               Nº   001,    DE    2006\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Operação         efetuada      via   Itaú   Empresas        na   internet.     CTRL:      $doc\n"
			+ "                 Autenticação          Digital   Itaú:   2A79E1F8A13BA565C393942072377E6A2EFC1BE3\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Nome       do   contribuinte:       MOTOBOMBAS                FLORIANOPOLIS                LTDA\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Dados      da   conta     debitada:\n"
			+ "                 Agência/Conta:           $ag      $conta     - 7\n"
			+ "                 CNPJ:       08892611000101\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";

	private static final String TRANSFERENCIA = "                                                    Banco         Itaú     - $4\n"
			+ "                                                          de    conta        corrente         para      conta       corrente\n"
			+ "               Identificação       no   extrato:    TBI    5821.05215-5              C/C\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                      Nome      da   empresa:       MOTO        BOMBAS          F  LTDA      ME\n"
			+ "                                      Agência:      $ag                               Conta     corrente:     $conta     - 7\n"
			+ "         Dados      da   conta    creditada:\n"
			+ "                                         Nome:      $2                                              \n"
			+ "                                      Agência:      5821                              Conta     corrente:     05215      - 5\n"
			+ "                                           Valor:   R$    $valor_pag\n"
			+ "         Transferência         efetuada       em    $pagto             às   18:32:24      via   , CTRL      $doc     .\n"
			+ "         Autenticação:\n" + "         8FA50B7BDEADECCD44C9B0B24BEB6BB1C2307B19\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
	/*
	 * private static final String BOLETO3 =
	 * "         Comprovante                 de    pagamento               de    boleto\n"
	 * + "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n"
	 * + "                   Identificação        no   meu\n" +
	 * "                               comprovante:         casas     da   agua\n" +
	 * "          Pagador   final:\n" +
	 * "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
	 * +
	 * "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
	 * +
	 * "                                                                                  00190     00009      03199      366000      00039      603170       4  81450000016920\n"
	 * +
	 * "          Beneficiário:CASAS     DA  AGUA    MATERIAIS      PARA   CONSTRUCAO\n"
	 * +
	 * "         LTDA                                                                            CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
	 * +
	 * "          Razão   Social:CASAS     DA  AGUA    MATERIAIS     PARA    CONSTRUCAO\n"
	 * +
	 * "         LTDA                                                                             13.501.187/0001-59                          $data_venc\n"
	 * +
	 * "                                                                                                                                     Valor  do documento     (R$);\n"
	 * +
	 * "                                                                                                                                     $valor_doc\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
	 * +
	 * "                                                                                                                                     1,72\n"
	 * +
	 * "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          $valor_pag\n"
	 * +
	 * "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
	 * +
	 * "                                                                                                                                      $pagto\n"
	 * +
	 * "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
	 * +
	 * "          97EC97ABF6070179A5F1BD2FE034174714A21A64                                                                                    Não\n"
	 * +
	 * "         Operação        efetuada       em    29/01/2020         às   18:24:02h       via   bankline,      CTRL      $doc .\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
	 * ;
	 * 
	 * private static final String BOLETO4 =
	 * "         Comprovante                 de    pagamento               de    boleto\n"
	 * + "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n"
	 * + "                   Identificação        no   meu\n" +
	 * "                               comprovante:         LUIZ     CORREA\n" +
	 * "          Pagador   final:\n" +
	 * "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
	 * +
	 * "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
	 * +
	 * "                                                                                  23793     38128      60007      007366      90000      050808       8  81470000094900\n"
	 * +
	 * "          Beneficiário:  IUGU   SERVICOS     NA   INTERNET     S.A                       CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
	 * +
	 * "          Razão   Social: IUGU   SERVICOS      NA  INTERNET      S.A                      15.111.975/0001-64                           $data_venc\n"
	 * +
	 * "                                                                                                                                     Valor  do documento     (R$);\n"
	 * +
	 * "                                                                                                                                     $valor_doc\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
	 * +
	 * "                                                                                                                                     19,29\n"
	 * +
	 * "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          $valor_pag\n"
	 * +
	 * "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
	 * +
	 * "          CONTABILIDADE                LUIZ     CORREA          SS   EPP                  04.656.282/0001-30                          $pagto\n"
	 * +
	 * "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
	 * +
	 * "          B6104FA71FFEB528D7D02A5B097A0BE0B1C2216D                                                                                    Não\n"
	 * +
	 * "         Operação        efetuada       em    28/01/2020         às   18:31:22h       via   bankline,      CTRL      $doc8.\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h)."
	 * ;
	 * 
	 * private static final String BOLETO5 =
	 * "         Comprovante                 de    pagamento               de    boleto\n"
	 * + "         Dados         da    conta       debitada\n" +
	 * "         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n"
	 * + "                   Identificação        no   meu\n" +
	 * "                               comprovante:         $4\n" +
	 * "          Pagador   final:\n" +
	 * "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
	 * +
	 * "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
	 * +
	 * "                                                                                  00190     00009      03075      800007      82177      940176       8  81030000004500\n"
	 * +
	 * "          Beneficiário:  IUGU   SERVICOS     NA   INTERNET     S.A                       CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
	 * +
	 * "          Razão   Social: IUGU   SERVICOS      NA  INTERNET      S.A                      15.111.975/0001-64                           $data_venc\n"
	 * +
	 * "                                                                                                                                     Valor  do documento     (R$);\n"
	 * +
	 * "                                                                                                                                     $valor_doc\n"
	 * +
	 * "                                                                                                                                     (-)Desconto    (R$):\n"
	 * +
	 * "                                                                                                                                     0,00\n"
	 * +
	 * "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
	 * +
	 * "                                                                                                                                     2,03\n"
	 * +
	 * "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
	 * +
	 * "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA                   08.892.611/0001-01                          $valor_pag\n"
	 * +
	 * "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
	 * + "         BERNARDI           E   MACHADO           MEDICINA\n" +
	 * "         OCUPACIONAL                                                                      07.879.569/0001-18                          $pagto\n"
	 * +
	 * "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
	 * +
	 * "          C96545F962CA61BBDD3798589222D22FB2D686D5                                                                                    Não\n"
	 * +
	 * "         Operação        efetuada       em    06/01/2020         às   18:45:11h       via   bankline,      CTRL      $doc .\n"
	 * +
	 * "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
	 * +
	 * "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
	 * +
	 * "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
	 * ;
	 *
	private static final String PAGTO_COD_BARRAS = "                                Banco         Itaú     -  Comprovante                 de    Pagamento               com      código         de    barras\n"
			+ "                                                                            0024     - $2\n"
			+ "               Identificação       no   extrato:    $4\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n"
			+ "                                      Agência:      $ag               Conta:     $conta \n"
			+ "         Dados      do   pagamento:\n"
			+ "                        Código      de   barras:    856600000009             486100242008            420001781442            500000977610\n"
			+ "                   Valor     do  documento:         R$    $valor_pag\n"
			+ "         Operação        efetuada       em    $pagto             às   18:31:24h       via   EMPRESA           PLUS,      CTRL      $doc          .\n"
			+ "         Autenticação:\n" + "         FFA5A2C400E9AB69F74D1BDFFCD89E05571254DF\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";

	private static final String GRRF = "                                                     Banco         Itaú     -  Comprovante                 de    Pagamento\n"
			+ "                                                                                  $2\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                Agência:       $ag               Conta:     $conta      \n"
			+ "                                    Nome:      MOTO       BOMBAS          F  LTDA      ME\n"
			+ "         Dados      do   pagamento:\n"
			+ "                  Código      de   barras:     858200000180            770102392027            001220150590701889261124\n"
			+ "                  Código      Convênio:        0239\n"
			+ "                  Data    de   Validade:       22/01/2020\n"
			+ "                          Identificador:       505970188926112\n"
			+ "                    Valor    Recolhido:        R$   $valor_pag\n"
			+ "                          Descrição       do\n"
			+ "                           Pagamento:          multa    fgts\n"
			+ "             Código      da   Operação:        00039804353310435300895\n"
			+ "         Operação        efetuada       em    $pagto            às   14:53:28      via   EMPRESA           PLUS,      CTRL      $doc           .\n"
			+ "         Autenticação         Digital    Itaú:   C3BEE7EA528DBD60082BB439949AE8656D2142D0\n"
			+ "               Identificação       no   extrato:    $4\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";

	private static final String GRF = "                                                      Banco         Itaú     -  Comprovante                 de    Pagamento\n"
			+ "                                                         GRF        - Guia       de    Recolhimento                 do    FGTS\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                Agência:       $ag               Conta:     $conta\n"
			+ "                                    Nome:      MOTO       BOMBAS          F  LTDA      ME\n"
			+ "         Dados      do   pagamento:\n"
			+ "                  Código      de   barras:     858000000305            116201792004            131636053802889261100010\n"
			+ "                                    CNPJ:      $cnpj           \n"
			+ "                  Código      Convênio:        0179\n"
			+ "                  Data    de   Validade:       31/01/2020\n"
			+ "                        Competência:           12/2019\n"
			+ "                    Valor    Recolhido:        R$   $valor_pag\n"
			+ "                          Descrição       do\n" + "                           Pagamento:          FGTS\n"
			+ "         Operação        efetuada       em    $pagto            às   18:44:22      via   EMPRESA           PLUS,      CTRL      $doc           .\n"
			+ "         Autenticação         Digital    Itaú:   6C17F848896DF6C30D9D40A2DE3E95F61E7644C4\n"
			+ "               Identificação       no   extrato:    $4\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";

	private static final String DOC_C = "                                                      Banco         Itaú     -  Comprovante                 de    Pagamento\n"
			+ "                                                                     DOC        C    –   outra      titularidade\n"
			+ "               Identificação       no   extrato:    DOC      INT    $doc  \n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n"
			+ "                                      Agência:      $ag                            Conta     corrente:     $conta \n"
			+ "         Dados      do   DOC:\n"
			+ "                         Nome       do  favorecido:       MAQUINAS            HIDRA       HIDROSUL\n"
			+ "                                     CPF     / CNPJ:      $cnpj_ben     \n"
			+ "              Número        do  banco,      nome      ou\n"
			+ "                                                 ISPB:    237    - BANCO         BRA     - ISPB     ESCO       S.A\n"
			+ "                                            Agência:      3271     CANOAS-CTO\n"
			+ "                                 Conta      corrente:     00000000425-1\n"
			+ "                                   Valor    do   DOC:     R$    $valor_pag\n"
			+ "                                        Finalidade:       01-CREDITO            EM    CONTA        CORRENTE\n"
			+ "         DOC      solicitado     em    $pagto             às   19:01:54       via  bankline.\n"
			+ "         Autenticação:\n" + "         DFDF6970CC18A493EFF20D2E3C8F49F78CDCE8A4\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
	*/
}
