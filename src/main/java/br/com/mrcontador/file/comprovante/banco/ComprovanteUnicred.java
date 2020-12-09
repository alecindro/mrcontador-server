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
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

public class ComprovanteUnicred extends ComprovanteBanco {

	private static Logger log = LoggerFactory.getLogger(ComprovanteUnicred.class);

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		String line = StringUtils.normalizeSpace(_lines[9]).trim();
		if (line.equals("Comprovante de Pagamento de Titulo")) {
			return parseTitulo(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("Comprovante de Pagamento de Tributos")) {
			return parsePagtoTributos(_lines, agenciabancaria, parceiro);
		}

		if (line.equals("Comprovante de Pagamento de Convênio")) {
			return parsePagtoConvenio(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("Comprovante de Pagamento de Tributos")) {
			return parsePagtoTributos(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).contains("ARRECADACAO DE DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5]).contains("ARRECADACAO DE DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).contains("ARRECADACAO DE GPS")) {
			return parseGPS(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5]).contains("ARRECADACAO DE GPS")) {
			return parseGPS(_lines, agenciabancaria, parceiro);
		}

		throw new ComprovanteException("Comprovante não identificado");
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());

			if (line.contains("Conta:")) {
				String value = StringUtils.split(StringUtils.substringAfter(line, "Conta:"))[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("ID do Documento:")) {
				String value = StringUtils.substringAfter(line, "ID do Documento:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Beneficiário")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CNPJ_BEN);
				if (StringUtils.normalizeSpace(lines[i + 9]).trim().equals("Sacador/Avalista") && !StringUtils
						.substringAfter(StringUtils.normalizeSpace(lines[i + 10]).trim(), "Razão Social:").isBlank()) {
					String value = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 10]).trim(), "Razão Social:").trim();
					diffValue.setNewValue(value);
					diffValue.setLine(i + 10);
					String value2 = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 11]).trim(), "CNPJ/CPF:").trim();
					diffValue2.setNewValue(value2);
					diffValue2.setLine(i + 11);

				} else {
					String value = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 4]).trim(), "Razão Social:").trim();
					diffValue.setNewValue(value);
					diffValue.setLine(i + 4);
					String value2 = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 6]).trim(), "CNPJ/CPF:").trim();
					value2 = MrContadorUtil.onlyNumbers(value2);
					diffValue2.setNewValue(value2);
					diffValue2.setLine(i + 6);
				}
				list.add(diffValue);
				list.add(diffValue2);
			}
			if (line.contains("Pagador") && !line.contains("Pagador Final")) {
				String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 4]).trim(), "CNPJ/CPF:")
						.trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 4);
				list.add(diffValue);
			}

			if (line.contains("Data de Vencimento:")) {
				String value = StringUtils.substringAfter(line, "Data de Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Pago:")) {
				String value = StringUtils.substringAfter(line, "Valor Pago:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Encargos:")) {
				String value = StringUtils.substringAfter(line, "Encargos:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descontos:")) {
				String value = StringUtils.substringAfter(line, "Descontos:").trim();
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
		diffValue.setNewValue("Comprovante de Pagamento de Titulo");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parseDarf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "Data do Pagamento:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF ou CNPJ")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "CPF ou CNPJ"), ".")
						.trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Vencimento")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Data Vencimento"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Principal")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Principal"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Total:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
		}
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(DOCUMENTO);
		diffValue2.setNewValue("DARF");
		diffValue2.setLine(i);
		list.add(diffValue2);
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("ARRECADACAO DE DARF");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoConvenio(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta:").trim(), "Usuário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfter(line, "Valor Total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Id. do Documento:")) {
				String value = StringUtils.substringAfter(line, "Id. do Documento:").trim();
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
		diffValue.setNewValue("Comprovante de Pagamento de Convênio");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoTributos(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta:")) {
				String value = StringUtils.split(StringUtils.substringAfter(line, "Conta:").trim())[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfter(line, "Valor Total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Id. do Documento:")) {
				String value = StringUtils.substringAfter(line, "Id. do Documento:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor dos Descontos:")) {
				String value = StringUtils.substringAfter(line, "Descontos:").trim();
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
		diffValue.setNewValue("Comprovante de Pagamento de Tributos");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
		return comprovantes;
	}

	private List<Comprovante> parseGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "Data do Pagamento:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Vencimento:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "Data de Vencimento:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
			if (line.contains("Valor INSS:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor INSS:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Total:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		}
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(DOCUMENTO);
		diffValue2.setNewValue("GPS");
		diffValue2.setLine(i);
		list.add(diffValue2);
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("ARRECADACAO DE GPS");
		diffValue.setLine(i);
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
			PDFTextStripper stripper = new PDFTextStripper();
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
			service.callComprovanteUnicred(comprovante.getId());	
		});
		Comprovante comprovante = comprovantes.parallelStream().findFirst().get();
		LocalDate date = comprovante.getComDatavencimento().minusMonths(4);
		service.callNotaFiscal(comprovante.getParceiro().getId(), date);
    }

}
