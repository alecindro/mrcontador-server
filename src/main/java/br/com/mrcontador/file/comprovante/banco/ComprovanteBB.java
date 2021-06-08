package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ComprovanteBB extends ComprovanteBanco {

	private static Logger log = LoggerFactory.getLogger(ComprovanteBB.class);

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {

		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 10) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		for (int i = 0; i < 10; i++) {
			String line = StringUtils.normalizeSpace(_lines[i]).trim();
			if (line.equals("COMPROVANTE DE PAGAMENTO DE TITULOS")) {
				return parseTitulo(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("GUIA DA PREVIDENCIA SOCIAL")) {
				return parseGPS(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("COMPROVANTE DE PAGAMENTO") && StringUtils.normalizeSpace(_lines[i + 1]).trim()
					.equals("COMPROVANTE DE PAGAMENTO DE DARF/DARF SIMPLES")) {
				return parseComprovDarf(_lines, agenciabancaria, parceiro);
			}

			if (line.equals("COMPROVANTE DE PAGAMENTO")) {
				return parseComprovPagto(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("COMPROVANTE DE DEBITO AUTOMATICO")) {
				return parseComprovDebito(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("TED - TRANSFERENCIA ELETRONICA DISPONIVEL")) {
				return parseComprovTED(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("DE CONTA CORRENTE P/ CONTA CORRENTE")) {      
				return parseTransfConta(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("COMPROVANTE DE TED")) {
				return parseComprovTED2Via(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("DE CONTA CORRENTE PARA POUPANCA")) {
				return parseTransfPoupanca(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("PAG SALARIO ELETRON")) {
				return parsePagSalario(_lines, agenciabancaria, parceiro);
			}
			if(line.equals("COMPROVANTE DE PAGAMENTO DA FATURA DO CARTAO")) {
				return parseFaturaCartao(_lines, agenciabancaria, parceiro);
			}
			if(line.equals("COMPROVANTE PIX")) {
				return parsePIX(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("COMPROVANTE DE PAGAMENTO ELETRONICO")) {
				return null;
			}
		}
		throw new ComprovanteException("doc.not.comprovante");
	}
	
	private List<Comprovante> parsePIX(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("PAGO PARA:")) {
				String value = StringUtils.substringAfter(line, "PAGO PARA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DOCUMENTO:")) {
				String value = StringUtils.substringAfter(line, "DOCUMENTO:").trim();
				value = MrContadorUtil.removeDots(value);
				while (value.startsWith("0")) {
					value = value.substring(1, value.length());
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA:")) {
				String value = StringUtils.substringAfter(line, "DATA:").trim();
				value  = value.split(StringUtils.SPACE)[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR:")) {
				String value = StringUtils.substringAfter(line, "VALOR:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				String value = StringUtils.substringAfter(line, "CNPJ:").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CPF:")) {
				String value = StringUtils.substringAfter(line, "CPF:").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE PIX");
		diffValue.setLine(6);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}
	
	private List<Comprovante> parseFaturaCartao(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:") && !list.stream().anyMatch(diff -> diff.getOldValue().contentEquals(AGENCIA))) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:") && !list.stream().anyMatch(diff -> diff.getOldValue().contentEquals(CONTA))) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Referencia:")) {
				String value = StringUtils.substringAfter(line, "Referencia:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}		
		
			if (line.contains("Data")) {
				line = StringUtils.normalizeSpace(StringUtils.remove(line, ":"));
				String value = StringUtils.substringAfter(line, "Data").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DATA_VCTO);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Valor")) {
				line = StringUtils.normalizeSpace(StringUtils.remove(line, ":"));
				String value = StringUtils.substringAfter(line, "Valor").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(VALOR_PGTO);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("PAGAMENTO DE FATURA DO CARTAO");
		diffValue.setLine(i);
		list.add(diffValue);
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("FATURA DO CARTAO");
		diffValue2.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;	
	}

	
	private List<Comprovante> parsePagSalario(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:") && !list.stream().anyMatch(diff -> diff.getOldValue().contentEquals(AGENCIA))) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:") && !list.stream().anyMatch(diff -> diff.getOldValue().contentEquals(CONTA))) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("NR. DOCUMENTO:") && !list.stream().anyMatch(diff -> diff.getOldValue().contentEquals(DOCUMENTO))) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("BENEFICIARIO:")) {
				String value = StringUtils.substringAfter(line, "BENEFICIARIO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				String value = StringUtils.substringAfter(line, "CNPJ:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("PAGAMENTO:")) {
				String value = StringUtils.substringAfter(line, "PAGAMENTO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DATA_VCTO);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("VALOR:")) {
				String value = StringUtils.substringAfter(line, "VALOR:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(VALOR_PGTO);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("PAGAMENTO DE SALARIO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;	
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("BENEFICIARIO:")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				if (StringUtils.normalizeSpace(lines[i + 5]).trim().equals("SACADOR AVALISTA:")
						|| StringUtils.normalizeSpace(lines[i + 5]).trim().equals("BENEFICIARIO FINAL:")) {
					String value = StringUtils.normalizeSpace(lines[i + 6]).trim();
					diffValue.setNewValue(value);
					diffValue.setLine(i + 6);
				} else {
					String value = StringUtils.normalizeSpace(lines[i + 1]).trim();
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
				}
				list.add(diffValue);
			}
			if (line.contains("CNPJ:") && !StringUtils.normalizeSpace(lines[i - 2]).trim().equals("PAGADOR:")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				if (StringUtils.normalizeSpace(lines[i + 1]).trim().equals("SACADOR AVALISTA:")
						|| StringUtils.normalizeSpace(lines[i + 1]).trim().equals("BENEFICIARIO FINAL:")) {
					String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 3]).trim(), "CNPJ:")
							.trim();
					value = MrContadorUtil.onlyNumbers(value);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 3);
					list.add(diffValue);
				} else {
					String value = StringUtils.substringAfter(line, "CNPJ:").trim();
					value = MrContadorUtil.onlyNumbers(value);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("CNPJ:")) {
				if (StringUtils.normalizeSpace(lines[i - 2]).trim().equals("PAGADOR:")) {
					String value = StringUtils.substringAfter(line, "CNPJ:").trim();
					value = MrContadorUtil.onlyNumbers(value);
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_PAG);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				}
			}
			if (line.contains("NR. DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DATA DE VENCIMENTO")) {
				String value = StringUtils.substringAfter(line, "DATA DE VENCIMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DATA DO PAGAMENTO")) {
				String value = StringUtils.substringAfter(line, "DATA DO PAGAMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("VALOR DO DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "VALOR DO DOCUMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("VALOR COBRADO")) {
				String value = StringUtils.substringAfter(line, "VALOR COBRADO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("JUROS/MULTA")) {
				String value = StringUtils.substringAfter(line, "JUROS/MULTA").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DESCONTO/ABATIMENTO")) {
				String value = StringUtils.substringAfter(line, "DESCONTO/ABATIMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DESCONTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DE TITULOS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parseComprovPagto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Convenio")) {
				String value = StringUtils.substringAfter(line, "Convenio").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DOCUMENTO:")) {
				String value = StringUtils.substringAfter(line, "DOCUMENTO:").trim();
				value = MrContadorUtil.removeDots(value);
				while (value.startsWith("0")) {
					value = value.substring(1, value.length());
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("Data do pagamento")) {
				String value = StringUtils.substringAfter(line, "Data do pagamento").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("Valor Total")) {
				String value = StringUtils.substringAfter(line, "Valor Total").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseComprovDebito(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONVENIO:")) {
				String value = StringUtils.substringAfter(line, "CONVENIO:").trim();
				String doc = value.split(StringUtils.SPACE)[0];
				while (doc.startsWith("0")) {
					doc = doc.substring(1, doc.length());
				}
				String fornecedor = StringUtils.substringAfter(value, doc).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(fornecedor);
				diffValue.setLine(i + 1);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DOCUMENTO);
				diffValue2.setNewValue(doc);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
			}

			if (line.contains("DATA DO DEBITO:")) {
				String value = StringUtils.substringAfter(line, "DATA DO DEBITO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA PREVISTA DO DEBITO:")) {
				String value = StringUtils.substringAfter(line, "DATA PREVISTA DO DEBITO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("VALOR DO DEBITO R$")) {
				String value = StringUtils.substringAfter(line, "VALOR DO DEBITO R$").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("HISTORICO LANCAMENTO:")) {
				String value = StringUtils.substringAfter(line, "HISTORICO LANCAMENTO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseComprovTED(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("FAVORECIDO:")) {
				String value = StringUtils.substringAfter(line, "FAVORECIDO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DOCUMENTO:")) {
				String value = StringUtils.substringAfter(line, "DOCUMENTO:").trim();
				value = MrContadorUtil.removeDots(value);
				while (value.startsWith("0")) {
					value = value.substring(1, value.length());
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DEBITO EM:")) {
				String value = StringUtils.substringAfter(line, "DEBITO EM:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR:")) {
				String value = StringUtils.substringAfter(line, "VALOR:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("TED - TRANSFERENCIA ELETRONICA DISPONIVEL");
		diffValue.setLine(6);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseComprovTED2Via(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("FAVORECIDO")) {
				String value = StringUtils.substringAfter(line, "FAVORECIDO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("NR. DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA DA TRANSFERENCIA")) {
				String value = StringUtils.substringAfter(line, "DATA DA TRANSFERENCIA").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CNPJ")) {
				String value = StringUtils.substringAfter(line, "CNPJ").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CPF")) {
				String value = StringUtils.substringAfter(line, "CPF").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE TED");
		diffValue.setLine(3);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseTransfConta(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("TRANSFERIDO PARA")) {
				String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 1]), "CLIENTE:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("NR. DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA DA TRANSFERENCIA")) {
				String value = StringUtils.substringAfter(line, "DATA DA TRANSFERENCIA").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE TRANSFERENCIA DE CONTA CORRENTE P/ CONTA CORRENTE");
		diffValue.setLine(2);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseTransfPoupanca(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("TRANSFERIDO PARA")) {
				String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 1]), "CLIENTE:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("NR. DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA DA TRANSFERENCIA")) {
				String value = StringUtils.substringAfter(line, "DATA DA TRANSFERENCIA").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			i = i + 1;
		}

		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE TRANSFERENCIA DE CONTA CORRENTE PARA POUPANCA");
		diffValue.setLine(2);
		list.add(diffValue);

		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseComprovDarf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		String codigoReceita = "";
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DOCUMENTO:")) {
				String value = StringUtils.substringAfter(line, "DOCUMENTO:").trim();
				value = MrContadorUtil.removeDots(value);
				while (value.startsWith("0")) {
					value = value.substring(1, value.length());
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("CODIGO DA RECEITA")) {
				codigoReceita = StringUtils.substringAfter(line, "CODIGO DA RECEITA").trim();

			}
			if (line.contains("DATA DO PAGAMENTO")) {
				String value = StringUtils.substringAfter(line, "DATA DO PAGAMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("DATA DO VENCIMENTO")) {
				String value = StringUtils.substringAfter(line, "DATA DO VENCIMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("VALOR DO PRINCIPAL")) {
				String value = StringUtils.substringAfter(line, "VALOR DO PRINCIPAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("NUMERO DO CPNJ")) {
				String value = StringUtils.substringAfter(line, "NUMERO DO CPNJ").trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("VALOR DA MULTA")) {
				String value = StringUtils.substringAfter(line, "VALOR DA MULTA").trim();
				value = MrContadorUtil.onlyNumbers(value);
				if (value != "") {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(MULTA);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				}
			}
			if (line.contains("VALOR DOS JUROS")) {
				String value = StringUtils.substringAfter(line, "VALOR DOS JUROS").trim();
				value = MrContadorUtil.onlyNumbers(value);
				if (value != "") {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(JUROS);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				}
			}

			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DE DARF - " + codigoReceita);
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(), "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CONTA:")) {
				String value = StringUtils.substringAfter(line, "CONTA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("IDENTIFICADOR")) {
				String value = StringUtils.substringAfter(line, "IDENTIFICADOR").trim();
				value = MrContadorUtil.removeDots(value);
				while (value.startsWith("0")) {
					value = value.substring(1, value.length());
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("DATA DO PAGAMENTO")) {
				String value = StringUtils.substringAfter(line, "DATA DO PAGAMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("VALOR DO INSS")) {
				String value = StringUtils.substringAfter(line, "VALOR DO INSS").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}
			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.substringAfter(line, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 1);
				list.add(diffValue);
			}

			if (line.contains("MULTA")) {
				String value = StringUtils.substringAfter(line, "MULTA").trim();
				value = MrContadorUtil.onlyNumbers(value);
				if (value != "") {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(MULTA);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				}
			}
			if (line.contains("OUTRAS ENTIDADES")) {
				String value = StringUtils.substringAfter(line, "OUTRAS ENTIDADES").trim();
				value = MrContadorUtil.onlyNumbers(value);
				if (value != "") {
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(JUROS);
					diffValue.setNewValue(value);
					diffValue.setLine(i + 1);
					list.add(diffValue);
				}
			}

			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("INSS");
		diffValue.setLine(i);
		list.add(diffValue);

		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(OBS);
		diffValue2.setNewValue("GUIA PREVIDENCIA SOCIAL");
		diffValue2.setLine(i);
		list.add(diffValue2);
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
		if (!comprovantes.isEmpty()) {
			Comprovante comprovante = comprovantes.stream().findFirst().get();
			Long parceiroId = comprovante.getParceiro().getId();
			Long agenciabancariaId = comprovante.getAgenciabancaria().getId();
			Set<String> periodos = comprovantes.stream().map(c -> c.getPeriodo()).collect(Collectors.toSet());
			periodos.forEach(p -> service.callComprovanteBB(parceiroId, agenciabancariaId, p));
		}
	}

}
