package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

public class ComprovanteBradesco extends ComprovanteBanco {
	
	private static Logger log = LoggerFactory.getLogger(ComprovanteBradesco.class);

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
		if (StringUtils.normalizeSpace(_lines[1].trim()).equals("GPS")) {
			return parseGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5].trim()).equals("COMPROVANTE DE PAGAMENTO DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("doc.not.comprovante");
	}

	private List<Comprovante> parseImpostosTaxas(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|").trim();
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
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
			if (line.toUpperCase().contains("IDENTIFICADOR")) {
				String lineA =  StringUtils.substringBefore(StringUtils.substringAfter(line.toUpperCase(), "IDENTIFICADOR").trim(),StringUtils.SPACE);
				lineA = StringUtils.remove(lineA, ":");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("COD.CONTROLE")) {
				String lineA =  StringUtils.substringAfter(line.toUpperCase(), "COD.CONTROLE");
				lineA = StringUtils.remove(lineA, ":").trim();
				lineA = StringUtils.substringBefore(lineA,StringUtils.SPACE);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("REFERENCIA")) {
				String lineA =  StringUtils.substringAfter(line.toUpperCase(), "REFERENCIA").trim();
				lineA = StringUtils.remove(lineA, ":").trim();
				lineA = StringUtils.substringBefore(lineA, StringUtils.SPACE);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Órgão:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Órgão:").trim(),StringUtils.SPACE);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Multa:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Multa:").trim(),StringUtils.SPACE);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(MULTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Juros:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Juros:").trim(),StringUtils.SPACE);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
				lineB = MrContadorUtil.onlyNumbers(lineB);
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
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue fornecedor = list.stream().filter(diff -> diff.getOldValue().contentEquals(FORNECEDOR)).findAny().get();
				if(fornecedor.getNewValue().contentEquals("Não informado")) {
					fornecedor.setNewValue(lineA);
				}
				
			}
			if (line.contains("Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Documento:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
				lineA = MrContadorUtil.removeZerosFromInital(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
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
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
			if (line.contains("Valor:") || line.contains("Valor")) {
				String lineA = StringUtils.substringAfter(line, "Valor").trim();
				if(line.contains("Valor:")) {
					lineA = StringUtils.substringAfter(line, "Valor:").trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição:")) {
				String lineA = StringUtils.substringAfter(line, "Descrição:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseTEC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
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
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
			if (line.contains("Valor:") || line.contains("Valor")) {
				String lineA = StringUtils.substringAfter(line, "Valor").trim();
				if(line.contains("Valor:")) {
					lineA = StringUtils.substringAfter(line, "Valor:").trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Documento:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseTED(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		String taxaValue = null;
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
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				lineA = MrContadorUtil.onlyNumbers(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("favorecido:")) {
				String lineA = StringUtils.substringAfter(line, "favorecido:").trim();
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
			if (!line.contains("Valor total:") && (line.contains("Valor:") || line.contains("Valor"))) {
				String lineA = StringUtils.substringAfter(line, "Valor").trim();
				if(line.contains("Valor:")) {
					lineA = StringUtils.substringAfter(line, "Valor:").trim();
				}				
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(VALOR_PGTO);
				diffValue2.setNewValue(lineA);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Tarifa:")) {
				String lineA = StringUtils.substringAfter(line, "Tarifa:").trim();
				BigDecimal value = new BigDecimal(MrContadorUtil.onlyMoney(lineA));
				if(value.compareTo(BigDecimal.ZERO) == 1) {
					taxaValue = lineA;
				}
			}
			if (line.contains("Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Documento:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
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
		Comprovante comprovante = toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA);
		comprovantes.add(comprovante);
		if(taxaValue!= null) {
			comprovantes.add(fromTaxa(taxaValue, comprovante));
		}
		return comprovantes;
	}
	
	private Comprovante fromTaxa(String taxa, Comprovante comprovante) {
		Comprovante _taxa = new Comprovante();
		_taxa.setComCnpj(comprovante.getComCnpj());
		_taxa.setComDocumento(comprovante.getComDocumento());
		_taxa.setComObservacao(comprovante.getComObservacao() +" - Tarifa");
		_taxa.setComValordocumento(new BigDecimal(MrContadorUtil.onlyMoney(taxa)));
		_taxa.setComValorpagamento(_taxa.getComValordocumento());
		_taxa.setComBeneficiario(comprovante.getComBeneficiario()+" - Tarifa");
		_taxa.setComDatapagamento(comprovante.getComDatapagamento());
		_taxa.setComDatavencimento(comprovante.getComDatavencimento());
		_taxa.setAgenciabancaria(comprovante.getAgenciabancaria());
		_taxa.setParceiro(comprovante.getParceiro());
		_taxa.setTipoComprovante(TipoComprovante.TAXAS);		
		_taxa.setProcessado(false);
		return _taxa;
	}

	private List<Comprovante> parseDOC(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		String taxaValue = null;
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
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
					lineA = MrContadorUtil.onlyNumbers(lineA);
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(lineA);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("CPF:")) {
				String lineA = StringUtils.substringAfter(line, "CPF:").trim();
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
			if (!line.contains("Valor total:") && (line.contains("Valor:") || line.contains("Valor"))) {
				String lineA = StringUtils.substringAfter(line, "Valor").trim();
				if(line.contains("Valor:")) {
					lineA = StringUtils.substringAfter(line, "Valor:").trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor total:")) {
				String lineA = StringUtils.substringAfter(line, "Valor total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Finalidade:")) {
				String lineA = StringUtils.substringAfter(line, "Finalidade:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
			if (line.contains("Tarifa:")) {
				String lineA = StringUtils.substringAfter(line, "Tarifa:").trim();
				BigDecimal value = new BigDecimal(MrContadorUtil.onlyMoney(lineA));
				if(value.compareTo(BigDecimal.ZERO) == 1) {
					taxaValue = lineA;
				}
			}
			
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Transferências Para Contas de Outros Bancos (DOC)");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		Comprovante comprovante = toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA);
		comprovantes.add(comprovante);
		if(taxaValue!= null) {
			comprovantes.add(fromTaxa(taxaValue, comprovante));
		}
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
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência de Débito:")), "Conta").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta de Débito:") || line.contains("ContadeDébito:")) {
				String lineA = StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Conta de Débito:")).trim();
				if(line.contains("ContadeDébito:")) {
					lineA = StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "ContadeDébito:")).trim();
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Empresa:")) {
				String lineA = StringUtils.substringAfter(line, "CNPJ:").trim();
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("SIMPLES NACIONAL");
		diffValue2.setLine(i);
		list.add(diffValue2);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.onlyNumbers(lineA);
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
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
						StringUtils.deleteWhitespace(StringUtils.substringAfter(StringUtils.deleteWhitespace(line), "Conta:")), "|Tipo:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("IDENTIF. EMPRESA:")) {
				String lineA = StringUtils.substringAfter(StringUtils.substringBefore(line, "IDENTIF. EMPRESA:"),"CNPJ/CEI:").trim();
				if(StringUtils.isBlank(lineA)) {
					lineA = StringUtils.substringAfter(StringUtils.substringBefore(line, "IDENTIF. EMPRESA:"),"Órgão:").trim();
						
				}
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DO FGTS");
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
			if (line.contains("Conta de débito:")) {
				String lineA = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(line, "Agência:")), "|Conta:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				
				String lineB = StringUtils.substringBefore(
						StringUtils.deleteWhitespace(StringUtils.substringAfter(StringUtils.deleteWhitespace(line), "Conta:")), "|Tipo:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if(line.contains("VALOR DO INSS")) {
				String lineA = StringUtils.substringAfter(line, "VALOR DO INSS").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if(line.contains("ATM/MULTA E JUROS")) {
				String lineA = StringUtils.substringAfter(line, "ATM/MULTA E JUROS").trim();
				if(StringUtils.isBlank(lineA)) {
					String[] values = lines[i+1].split(StringUtils.SPACE);
					lineA = values[values.length-1];
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(MULTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if(line.contains("TOTAL")) {
				String lineA = StringUtils.substringAfter(line, "TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if(line.contains("da data de pagamento")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "da data de pagamento"),",").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
				String lineB = StringUtils.substringAfter(line, "sob o n.de protocolo").trim();
				lineB = MrContadorUtil.onlyNumbers(lineB);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(DOCUMENTO);
				diffValue2.setNewValue(lineB);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			i = i+1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("Previdência Social");
		diffValue.setLine(i);
		list.add(diffValue);
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(OBS);
		diffValue2.setNewValue("Pagamento de GPS");
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

}
