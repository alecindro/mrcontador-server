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

public class ComprovanteCredCrea extends ComprovanteBanco {
	
	private static Logger log = LoggerFactory.getLogger(ComprovanteCaixa.class);

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		String line = StringUtils.normalizeSpace(_lines[1].trim());
		if (isConvenio(_lines)) {
			return parseConvenio(_lines, agenciabancaria, parceiro);
		}
		if (isConvenio2(_lines)) {
			return parseConvenio2(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("COMPROVANTE DE PAGAMENTO")) {
			return parseComprovPagto(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("doc.not.comprovante");
	}

	private boolean isConvenio(String[] lines) {
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("DADOS DO PAGAMENTO")) {
				if (StringUtils.normalizeSpace(lines[i + 1]).contains("Convênio")) {
					if (StringUtils.normalizeSpace(lines[i + 2]).contains("Data Da Aplicação"))
						return true;
				}
			}
			i = i + 1;
		}
		return false;
	}

	private boolean isConvenio2(String[] lines) {
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("DADOS DO PAGAMENTO")) {
				if (StringUtils.normalizeSpace(lines[i + 2]).contains("Convênio")) {
					if (StringUtils.normalizeSpace(lines[i + 3]).contains("Data/Hora Transação"))
						return true;
				}
			}
			i = i + 1;
		}
		return false;
	}

	private List<Comprovante> parseComprovPagto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência")) {
				String value = StringUtils.substringAfter(line, "Agência").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta/DV")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta/DV").trim(), "-");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ")) {
				if (StringUtils.normalizeSpace(lines[i - 1]).contains("Conta/DV")) {
					String value = StringUtils.substringAfter(line, "CPF/CNPJ").trim();
					value = MrContadorUtil.removeDots(value);
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_PAG);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("Beneficiário")) {
				String value = StringUtils.substringAfter(line, "Beneficiário").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ")) {
				if (StringUtils.normalizeSpace(lines[i - 1]).contains("Beneficiário")) {
					String value = StringUtils.substringAfter(line, "CPF/CNPJ").trim();
					value = MrContadorUtil.removeDots(value);
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("Data Do Vencimento")) {
				String value = StringUtils.substringAfter(line, "Data Do Vencimento").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Data Do Pagamento")) {
				String value = StringUtils.substringAfter(line, "Data Do Pagamento").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Título")) {
				String value = StringUtils.substringAfter(line, "Valor Título").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor")) {
				if (StringUtils.normalizeSpace(lines[i - 1]).contains("Data Do Pagamento")) {
					String value = StringUtils.substringAfter(line, "Valor").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(VALOR_PGTO);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("Protocolo")) {
				String value = StringUtils.substringAfter(line, "Protocolo").trim();
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
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parseConvenio(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência")) {
				String value = StringUtils.substringAfter(line, "Agência").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta/DV")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta/DV").trim(), "-");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Convênio")) {
				String value = StringUtils.substringAfter(line, "Convênio").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Da Aplicação")) {
				String value = StringUtils.substringAfter(line, "Data Da Aplicação").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value.split("\\s")[0]);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificação da Fatura")) {
				String value = StringUtils.substringAfter(line, "Identificação da Fatura").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor")) {
				String value = StringUtils.substringAfter(line, "Valor").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}

			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO - CONVÊNIO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
		return comprovantes;
	}

	private List<Comprovante> parseConvenio2(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência")) {
				String value = StringUtils.substringAfter(line, "Agência").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta/DV")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta/DV").trim(), "-");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificação")) {
				String value = StringUtils.substringAfter(line, "Identificação").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value.split("\\s")[0]);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Sequência de Autenticação")) {
				String value = StringUtils.substringAfter(line, "Sequência de Autenticação").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor")) {
				String value = StringUtils.substringAfter(line, "Valor").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}

			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO - CONVÊNIO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro,TipoComprovante.OUTROS));
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
			service.callComprovanteCredicrea(comprovante.getId());	
		});
		Comprovante comprovante = comprovantes.parallelStream().findFirst().get();
		LocalDate date = comprovante.getComDatavencimento().minusMonths(4);
		service.callNotaFiscal(comprovante.getParceiro().getId(), date);
    }


}
