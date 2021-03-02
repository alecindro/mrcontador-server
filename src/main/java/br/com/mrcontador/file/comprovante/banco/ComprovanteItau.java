package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.file.comprovante.PPDocumentDTO;
import br.com.mrcontador.file.comprovante.TipoComprovante;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

public class ComprovanteItau extends ComprovanteBanco {

	private static Logger log = LoggerFactory.getLogger(ComprovanteItau.class);

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
		if (StringUtils.normalizeSpace(_lines[1].trim()).contains("Tributos Estaduais")) {
			return parseTribEstaudais(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento de concessionárias")) {
			return parseConcessionarias(_lines, agenciabancaria, parceiro);
		}
		if (line.toUpperCase().contains("SIMPLES NACIONAL")) {
			return parseSimplesNacional(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("Comprovante de Transferência")
				&& StringUtils.normalizeSpace(_lines[1]).toUpperCase().contains("DADOS DO PAGADOR")) {
			return parseTransferencia2(_lines, agenciabancaria, parceiro);
		}

		if (line.contains("Comprovante de Transferência")) {
			return parseTransferencia(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento com código de barras")) {
			return parsePagtoCodBarras(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Banco Itaú - Comprovante de Pagamento")
				&& StringUtils.normalizeSpace(_lines[1].trim()).equals("FGTS- GRRF")) {
			return parseFgtsGrf(_lines, agenciabancaria, parceiro);
		}
		if (line.toUpperCase().contains("GRFGTS")) {
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

		throw new ComprovanteException("doc.not.comprovante");

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
			if (line.toUpperCase().contains("DATA DO PAGAMENTO:")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "DATA DO PAGAMENTO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("VALOR DO INSS:")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "VALOR DO INSS:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "VALOR TOTAL").trim();
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

			if (line.contains("identificador:")) {
				String value = StringUtils.substringAfter(line, "identificador:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("valor atual. mon/jur/mul:")) {
				String value = StringUtils.substringAfter(line, "valor atual. mon/jur/mul:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.toUpperCase().contains("AGÊNCIA")) {
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
				} else {
					if (line.contains("agência e conta:")) {
						String value = StringUtils.substringAfter(line, "agência e conta:").trim();
						String[] values = value.split("/");
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
					} else {
						if (line.contains("Agência:")) {
							String value = StringUtils
									.substringBefore(StringUtils.substringAfter(line, "Agência:").trim(), "Conta:")
									.trim();
							DiffValue diffValue = new DiffValue();
							diffValue.setOldValue(AGENCIA);
							diffValue.setNewValue(value);
							diffValue.setLine(i);
							list.add(diffValue);
							String value2 = StringUtils.substringAfter(line, "Conta:").trim();
							DiffValue diffValue2 = new DiffValue();
							diffValue2.setOldValue(CONTA);
							diffValue2.setNewValue(value2);
							diffValue2.setLine(i);
							list.add(diffValue2);
						}
					}
				}
			}

			if (line.contains("CTRL")) {
				ctrl = StringUtils.substringAfter(line, "CTRL").trim();
				ctrl = StringUtils.remove(ctrl, ":");
				ctrl = StringUtils.remove(ctrl, ".");
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
					cnpj_ben = MrContadorUtil.removeDots(cnpj_ben);
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
					cnpj_ben = MrContadorUtil.removeDots(cnpj_ben);
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
		diffValue.setNewValue("Comprovante de pagamento de boleto");
		diffValue.setLine(0);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
					cnpj_ben = MrContadorUtil.removeDots(cnpj_ben);
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
					cnpj_ben = MrContadorUtil.removeDots(cnpj_ben);
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
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
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
				String lineA = StringUtils.substringBefore(
						StringUtils.substringAfter(line, "Operação efetuada em").trim(), StringUtils.SPACE).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				value = MrContadorUtil.removeDots(value);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseTribEstaudais(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Identificação no extrato:")) {
				String lineA = StringUtils.substringAfter(line, "Identificação no extrato:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
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
				String lineA = StringUtils.substringBefore(
						StringUtils.substringAfter(line, "Operação efetuada em").trim(), StringUtils.SPACE).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("Tributos Estaduais");
		diffValue.setLine(1);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
		return comprovantes;
	}

	private List<Comprovante> parseSimplesNacional(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.toUpperCase().contains("AGÊNCIA")) {
				if (line.contains("Agência/Conta")) {
					String lineA = StringUtils.substringAfter(line, "Agência/Conta").trim();
					lineA = StringUtils.remove(lineA, ":");
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
				} else {
					if (line.toUpperCase().contains("AGÊNCIA E CONTA")) {
						String lineA = StringUtils.substringAfter(line.toUpperCase(), "AGÊNCIA E CONTA").trim();
						lineA = StringUtils.remove(lineA, ":");
						String[] values = lineA.split("/");
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
				}
			}

			if (line.toUpperCase().contains("VALOR TOTAL")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "VALOR TOTAL").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("DATA DO PAGAMENTO")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "DATA DO PAGAMENTO").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.toUpperCase().contains("NÚMERO DO DOCUMENTO")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "NÚMERO DO DOCUMENTO").trim();
				value = StringUtils.remove(value, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("IDENTIFICAÇÃO NO EXTRATO")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "IDENTIFICAÇÃO NO EXTRATO");
				value = StringUtils.remove(value, ":");
				value = MrContadorUtil.removeDots(value);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
		return comprovantes;
	}

	private List<Comprovante> parseDarf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.toUpperCase().contains("AGÊNCIA")) {
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
				} else {
					if (line.contains("agência e conta:")) {
						String value = StringUtils.substringAfter(line, "agência e conta:").trim();
						String[] values = value.split("/");
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
					} else {
						if (line.contains("Agência:")) {
							String value = StringUtils
									.substringBefore(StringUtils.substringAfter(line, "Agência:").trim(), "Conta:")
									.trim();
							DiffValue diffValue = new DiffValue();
							diffValue.setOldValue(AGENCIA);
							diffValue.setNewValue(value);
							diffValue.setLine(i);
							list.add(diffValue);
							String value2 = StringUtils.substringAfter(line, "Conta:").trim();
							DiffValue diffValue2 = new DiffValue();
							diffValue2.setOldValue(CONTA);
							diffValue2.setNewValue(value2);
							diffValue2.setLine(i);
							list.add(diffValue2);
						}
					}
				}
			}
			if (line.toUpperCase().contains("VALOR PRINCIPAL")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "VALOR PRINCIPAL").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("VALOR TOTAL")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "VALOR TOTAL").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("DATA DO PAGAMENTO")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "DATA DO PAGAMENTO").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("JUROS/ENCARGOS")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "JUROS/ENCARGOS").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("MULTA")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "MULTA").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(MULTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("DATA DO VENCIMENTO")) {
				String lineA = StringUtils.substringAfter(line, "DATA DO VENCIMENTO").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("AGENTE ARRECADADOR")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "AGENTE ARRECADADOR").trim();
				value = StringUtils.remove(value, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CTRL")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "CTRL"), ".");
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
		diffValue.setNewValue("Comprovante de pagamento DARF");
		diffValue.setLine(1);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
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
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseTransferencia2(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.toUpperCase().contains("DADOS DO PAGADOR")) {
				String lineA = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 3]).toUpperCase(),
						"AGÊNCIA/CONTA");
				lineA = StringUtils.remove(lineA, ":");
				String[] values = lineA.split("/");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(StringUtils.normalizeSpace(values[0]));
				diffValue.setLine(i + 3);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(StringUtils.normalizeSpace(values[1]));
				diffValue2.setLine(i + 3);
				list.add(diffValue2);
			}
			if (line.toUpperCase().contains("DADOS DO RECEBEDOR")) {
				String lineA = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 1]).toUpperCase(),
						"NOME DO RECEBEDOR");
				lineA = StringUtils.remove(lineA, ":");
				String lineB = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 3]).toUpperCase(),
						"CNPJ DO RECEBEDOR");
				lineB = StringUtils.remove(lineB, ":");

				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(StringUtils.normalizeSpace(lineA));
				diffValue.setLine(i + 1);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CNPJ_BEN);
				diffValue2.setNewValue(StringUtils.normalizeSpace(lineB));
				diffValue2.setLine(i + 2);
				list.add(diffValue2);
			}

			if (line.toUpperCase().contains("VALOR")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "VALOR").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("DATA DA TRANSFERÊNCIA")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "DATA DA TRANSFERÊNCIA").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
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
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:")
						.trim();
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
				value = MrContadorUtil.removeDots(value);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseFgtsGrf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.toUpperCase().contains("IDENTIFICAÇÃO NO EXTRATO")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "IDENTIFICAÇÃO NO EXTRATO").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Dados da conta debitada:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]);
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:")
						.trim();
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
			if (line.toUpperCase().contains("AGÊNCIA E CONTA")) {
				String value = StringUtils.substringAfter(line.toUpperCase(), "AGÊNCIA E CONTA").trim();
				value = StringUtils.remove(value, ":");
				String[] values = value.split("/");
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

			if (line.toUpperCase().contains("VALOR RECOLHIDO")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "VALOR RECOLHIDO").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("DATA DE VALIDADE")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "DATA DE VALIDADE").trim();
				lineA = StringUtils.remove(lineA, ":");
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
			if (line.toUpperCase().contains("PAGAMENTO EFETUADO EM")) {
				String lineA = StringUtils.substringAfter(line.toUpperCase(), "PAGAMENTO EFETUADO EM").trim();
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("código de convênio")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(line);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("Guia de Recolhimento do FGTS/GRF");
		diffValue.setLine(1);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
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
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(lineA, "Agência:"), "Conta:")
						.trim();
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
				value = MrContadorUtil.removeDots(value);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
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

			if (line.contains("CPF / CNPJ:")) {
				String lineA = StringUtils.substringAfter(line, "CPF / CNPJ:");
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(1);
				list.add(diffValue);
			}
			if (line.contains("Nome do favorecido:")) {
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
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "DOC solicitado em"), "às")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Finalidade:")) {
				String value = StringUtils.substringAfter(line, "Finalidade:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	public List<PPDocumentDTO> parseComprovante(FileDTO fileDTO) {
		InputStream stream = null;
		try {
			stream = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			PDDocument document = PDDocument.load(stream);
			List<PPDocumentDTO> list = new ArrayList<>();
			Splitter splitter = new Splitter();
			PDFTextStripper stripper = new PdfReaderPreserveSpace();
			for (PDDocument pdDocument : splitter.split(document)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				String comprovante = stripper.getText(pdDocument);
				pdDocument.save(output);
				PPDocumentDTO pddocumentDTO = new PPDocumentDTO(comprovante, output);
				list.add(pddocumentDTO);
				pdDocument.close();
			}
			document.close();
			return list;
		} catch (IOException e1) {
			throw new FileException("parsecomprovante.error", fileDTO.getOriginalFilename(), e1);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	
	@Override
	public void callFunction(List<Comprovante> comprovantes, ComprovanteService service,
			ExtratoService extratoService) {
		if(!comprovantes.isEmpty()) {
			Comprovante comprovante = comprovantes.stream().findFirst().get();
			Long parceiroId = comprovante.getParceiro().getId();
			Long agenciabancariaId = comprovante.getAgenciabancaria().getId();
			Set<String> periodos = comprovantes.stream().map(c -> c.getPeriodo()).collect(Collectors.toSet());
			periodos.forEach(p -> service.callComprovanteItau(parceiroId, agenciabancariaId, p));
		}
	}

}
