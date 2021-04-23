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
		if (line.trim().equals("COMPROVANTE DE TRANSFERÊNCIA")) {
			return parseTransferencia(_lines, agenciabancaria, parceiro);
		}
		if(line.trim().equals("COMPROVANTE DE TED")) {
			return parseTed(_lines, agenciabancaria, parceiro);
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
	
	private List<Comprovante> parseTed(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim()).toUpperCase();

			if (line.contains("DADOS DA CONTA ORIGEM")) {
				String agencia = StringUtils.normalizeSpace(lines[i+2]).split(StringUtils.SPACE)[1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i+2);
				list.add(diffValue);
				String conta = StringUtils.normalizeSpace(lines[i+3]).split(StringUtils.SPACE)[1];
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i+3);
				list.add(diffValue2);
			}

			if (line.contains("DADOS DA CONTA DESTINO")) {
				String value = StringUtils.remove(StringUtils.normalizeSpace(lines[i+4]),"Conta/Nome Favorecido");
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i+4);
				list.add(diffValue);
				String value2 = StringUtils.remove(StringUtils.normalizeSpace(lines[i+5]),"CNPJ Favorecido");
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CNPJ_BEN);
				diffValue2.setNewValue(value2.trim());
				diffValue2.setLine(i+5);
				list.add(diffValue2);
			}
		

			if (line.contains("VALOR A PAGAR")) {
				String[] values = StringUtils.normalizeSpace(line).split(StringUtils.SPACE);
				String value = values[values.length-1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(VALOR_DOC);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
		
			if (line.contains("DOCUMENTO")) {
				String value = StringUtils.normalizeSpace(line.trim().split(StringUtils.SPACE)[1]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
			if (line.contains("DATA/HORA TRANSAÇÃO")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(line, "DATA/HORA TRANSAÇÃO"));
				value = value.split(StringUtils.SPACE)[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.toUpperCase().contains("FINALIDADE")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(line.toUpperCase(), "FINALIDADE"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
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
	
	
	private List<Comprovante> parseTransferencia(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim()).toUpperCase();
			if (line.contains("DADOS DA CONTA ORIGEM")) {
				String agencia = StringUtils.normalizeSpace(lines[i+2]).split(StringUtils.SPACE)[1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i+2);
				list.add(diffValue);
				String conta = StringUtils.normalizeSpace(lines[i+3]).split(StringUtils.SPACE)[1];
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i+3);
				list.add(diffValue2);
			}

			if (line.contains("DADOS DA CONTA DESTINO")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(lines[i+2], "Conta/DV"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i+2);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(OBS);
				diffValue2.setNewValue("Comprovante de Transferência "+value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}

			if (line.contains("DOCUMENTO")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(line, "DOCUMENTO"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		

			if (line.contains("DATA DA TRANSFERÊNCIA")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(line, "DATA DA TRANSFERÊNCIA"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		
			if (line.contains("VALOR A PAGAR")) {
				String value = StringUtils.normalizeSpace(StringUtils.remove(line, "VALOR A PAGAR"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
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
	@Override
	public void callFunction(List<Comprovante> comprovantes, ComprovanteService service,
			ExtratoService extratoService) {
		if(!comprovantes.isEmpty()) {
			Comprovante comprovante = comprovantes.stream().findFirst().get();
			Long parceiroId = comprovante.getParceiro().getId();
			Long agenciabancariaId = comprovante.getAgenciabancaria().getId();
			Set<String> periodos = comprovantes.stream().map(c -> c.getPeriodo()).collect(Collectors.toSet());
			periodos.forEach(p -> service.callComprovanteCredicrea(parceiroId, agenciabancariaId, p));
		}
	}

}
