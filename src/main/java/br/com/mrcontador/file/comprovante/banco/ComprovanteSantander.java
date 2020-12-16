package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ComprovanteSantander extends ComprovanteBanco {
	
	private static Logger log = LoggerFactory.getLogger(ComprovanteSantander.class);


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
		throw new ComprovanteException("parsercomprovante.notfound");
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
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
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Instituição Financeira Favorecida:")) {
				String lineA = StringUtils.normalizeSpace(lines[i + 1]).trim();
				lineA = MrContadorUtil.removeDots(lineA);
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
						lineA = MrContadorUtil.removeDots(lineA);
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
						lineA = MrContadorUtil.removeDots(lineA);
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
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
	
	public void callFunction(List<Comprovante> comprovantes, ComprovanteService service) {
		comprovantes.forEach(comprovante ->{
			service.callComprovanteSantander(comprovante.getId());	
		});
		Comprovante comprovante = comprovantes.parallelStream().findFirst().get();
		LocalDate date = comprovante.getComDatavencimento().minusMonths(4);
		service.callNotaFiscal(comprovante.getParceiro().getId(), date);
    }

}
