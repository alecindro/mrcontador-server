package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
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

public class ComprovanteSicoob extends ComprovanteBanco {
	
	private static Logger log = LoggerFactory.getLogger(ComprovanteSicoob.class);

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] comprovantesText = comprovante.split(quebra_linha);
		List<Comprovante> comprovantes = new ArrayList<>();
		if (comprovantesText.length > 2) {
			comprovantesText[1] = comprovantesText[1].replaceFirst("\\n", "");
		}
		comprovantesText = ArrayUtils.remove(comprovantesText, comprovantesText.length - 1);
		for (String _comprovante : comprovantesText) {
			String[] _lines = _comprovante.split("\\r?\\n");
			if (StringUtils.normalizeSpace(_lines[3].trim()).contains("Comprovante de - Pagamento de Título")) {
				comprovantes.addAll(parsePagtoTitulo(_lines, agenciabancaria, parceiro));
			} else {
				throw new ComprovanteException("doc.not.comprovante");
			}
		}
		return comprovantes;
	}

	private List<Comprovante> parsePagtoTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Coop.:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Coop.:"), "/CC").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Conta:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Conta:"), "/").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nº documento:")) {
				String lineA = StringUtils.substringBefore(StringUtils.substringAfter(line, "Nº documento:"), "\\s")
						.trim();
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Nome/Razão Social do Beneficiário:")) {
				String lineA = StringUtils.substringAfter(line, "Nome/Razão Social do Beneficiário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ Beneficiário:")) {
				String lineA = StringUtils.substringAfter(line, "CPF/CNPJ Beneficiário:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ Pagador:")) {
				String lineA = StringUtils.substringAfter(line, "CPF/CNPJ Pagador:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Pagamento:")) {
				String lineA = StringUtils.substringAfter(line, "Data Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Vencimento:")) {
				String lineA = StringUtils.substringAfter(line, "Data Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Documento:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Documento:").trim();
				lineA = MrContadorUtil.removeDots(lineA);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Pago:")) {
				String lineA = StringUtils.substringAfter(line, "Valor Pago:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Observação:")) {
				String lineA = StringUtils.substringAfter(line, "Observação:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private static final String quebra_linha = "OUVIDORIA       SICOOB:     \\d+";
	

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
			periodos.forEach(p -> service.callComprovanteSicoob(parceiroId, agenciabancariaId, p));
		}
	}

}
