package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

public class ComprovanteCaixa extends ComprovanteBanco {
	
	private static Logger log = LoggerFactory.getLogger(ComprovanteCaixa.class);
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {

		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim().contains("Comprovante de Pagamento de Boleto")) {
			return parsePagtoBoleto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim().contains("Comprovante de pagamento de GPS")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de GPS")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim().contains("Comprovante de pagamento de FGTS")) {
			return parseFGTS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de FGTS")) {
			return parseFGTS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim().contains("Comprovante de pagamento com código de barras")) {
			return parsePagtoSefaz(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento com código de barras")) {
			return parsePagtoSefaz(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim()
				.contains("TEV")) {
			return parsePagtoTEV(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.contains("TEV")) {
			return parsePagtoTEV(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim()
				.contains("concessionária")) {
			return parsePagtoConcessionaria(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.contains("concessionária")) {
			return parsePagtoConcessionaria(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[0]).trim().contains("tributos do governo")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("tributos do governo")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de Pagamento de Boleto")) {
			return parsePagtoBoleto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("2ª Via - Comprovante de Pagamento de Boleto")) {
			return parsePagtoBoleto(_lines, agenciabancaria, parceiro);
		}
		
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.contains("Comprovante de pagamento de água, luz, telefone e gás")) {
			return parsePagtoAgua(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim()
				.contains("Comprovante de transferência entre contas da CAIXA - TEV")) {
			return parsePagtoTEV(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de GPS")) {
			return parsePagtoGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[2]).trim().contains("Comprovante de Autorização da Folha")) {
			return parseAutFolha(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de transferência eletrônica disponível")) {
			return parseCreditoTED(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de FGTS")) {
			return parseFGTS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Consulta Detalhes da Folha")) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de agendamento de transferência entre contas da CAIXA - TEV")) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de Simples Nacional")) {
			return parseSimplesNacional(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[1]).trim().contains("Comprovante de pagamento de tributos federais")) {
			return parseTributosFederais(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("doc.not.comprovante");
	}

	private List<Comprovante> parsePagtoBoleto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta de débito:").trim(), separador).trim();
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
			if (line.contains("Data de Efetivação")) {
				String[] values = StringUtils.split(line,StringUtils.SPACE);
				String value = values[values.length-1].trim();
				if(!MrContadorUtil.isDate(value, dateFormatter)) {
					value = lines[i+1].trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("barras:")) {
				String value = StringUtils.substringAfter(line, "barras:").trim();
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
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
		diffValue.setNewValue("Comprovante de pagamento de concessionária");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}
	
	private List<Comprovante> parsePagtoConcessionaria(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String conta = "";
				String[] values = null;
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "\\|";
				}
				values = StringUtils.substringAfter(line, "Conta de débito:").split(separador);
				if(values.length == 2) {
				  conta = values[1].split("\\.")[1];	
				}
				if(values.length == 3) {
					conta = values[2];
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(values[0].trim());
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta.trim());
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
		diffValue.setNewValue("Comprovante de pagamento de concessionária");
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta origem:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
		diffValue.setNewValue("Comprovante de pagamento de tributos do governo");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}
	
	private List<Comprovante> parsePagtoSefaz(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
			if (line.contains("operação:") && !line.contains("Código") && !line.contains("Data")) {
				String data = StringUtils.substringAfter(line, "operação:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(data);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
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
			if (line.toUpperCase().contains("DATA DÉBITO:")) {
				String data = StringUtils.substringAfter(line.toUpperCase(), "DATA DÉBITO:").trim();
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta origem:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
				String[] values = StringUtils.split(line, StringUtils.SPACE);
				String data = values[values.length-2];
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseFGTS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de débito:")) {
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("FGTS");
		diffValue2.setLine(i);
		list.add(diffValue2);
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
				String separador = "/";
				if(!line.contains(separador)) {
					separador = "|";
				}
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta de débito:"), separador)
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfterLast(line, separador).trim();
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
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("Tributos Federais");
		diffValue2.setLine(i);
		list.add(diffValue2);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
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
			periodos.forEach(p -> service.callComprovanteCEF(parceiroId, agenciabancariaId, p));
		}
	}
}
