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

}
