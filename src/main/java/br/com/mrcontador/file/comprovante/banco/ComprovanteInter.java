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
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

public class ComprovanteInter extends ComprovanteBanco {

	private static Logger log = LoggerFactory.getLogger(ComprovanteInter.class);
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		for (int i = 0; i < 7; i++) {
			String line = StringUtils.normalizeSpace(_lines[i]);
			if (line.contains("Comprovante Transferencia")) {
				return parseTransferencia(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("Comprovante Pagamento")) {
				return parsePagamento(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("Agendamento")) {
				return null;
			}

		}
		throw new ComprovanteException("doc.not.comprovante");
	}

	private List<Comprovante> parseTransferencia(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta de origem")) {
				String agencia = StringUtils.normalizeSpace(lines[i + 2]).split(StringUtils.SPACE)[1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.normalizeSpace(lines[i + 3]).split(StringUtils.SPACE)[1];
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Conta de destino")) {
				String fornecedor = StringUtils.normalizeSpace(StringUtils.substringAfter(lines[i + 1], "Nome:"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(fornecedor);
				diffValue.setLine(i);
				list.add(diffValue);
				String cnpj = StringUtils.normalizeSpace(lines[i + 2]).split(StringUtils.SPACE)[1];
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CNPJ_BEN);
				diffValue2.setNewValue(cnpj);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("Data Efetivação")) {
				String dt_pagto = line.split(StringUtils.SPACE)[2];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(dt_pagto);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor da Transação")) {
				String valor = StringUtils.normalizeSpace(lines[i + 1]).split(StringUtils.SPACE)[2];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(valor);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Protocolo de transação")) {
				String documento = line.split(StringUtils.SPACE)[3];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(documento);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;

		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE TRANSFERÊNCIA");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parsePagamento(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Agência:")) {
				String agencia = StringUtils.substringAfter(line, "Agência:").split(StringUtils.SPACE)[1];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(agencia);
				diffValue.setLine(i);
				list.add(diffValue);
				String conta = StringUtils.substringAfter(line, "Conta:").split(StringUtils.SPACE)[1];
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CONTA);
				diffValue2.setNewValue(conta);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			if (line.contains("débito:")) {
				String value = StringUtils.reverse(StringUtils.reverse(line).split(StringUtils.SPACE)[0]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("vencimento:")) {
				String value = StringUtils.reverse(StringUtils.reverse(line).split(StringUtils.SPACE)[0]);
				if(MrContadorUtil.isDate(value, dateFormatter)) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				}

			}
			if (line.contains("Documento:")) {
				String documento = StringUtils.normalizeSpace(StringUtils.substringAfter(line, "Documento:"));
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(documento);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("desconto:")) {
				String value = StringUtils.reverse(StringUtils.reverse(line).split(StringUtils.SPACE)[0]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DESCONTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("acrescimo:")) {
				String value = StringUtils.reverse(StringUtils.reverse(line).split(StringUtils.SPACE)[0]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("total:")) {
				String value = StringUtils.reverse(StringUtils.reverse(line).split(StringUtils.SPACE)[0]);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição:")) {
				String descricao = StringUtils.substringAfter(line, "Descrição:");
				String favorecido = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 1]), "Favorecido:");
				if (StringUtils.isEmpty(descricao)) {
					descricao = "Comprovante Pagamento";
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(descricao);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(FORNECEDOR);
				diffValue2.setNewValue(descricao);
				diffValue2.setLine(i + 1);
				list.add(diffValue2);
				if (!StringUtils.isEmpty(favorecido)) {
					diffValue2.setNewValue(favorecido);
				}
			}
				i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	@Override
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
			periodos.forEach(p -> service.callComprovanteInter(parceiroId, agenciabancariaId, p));
		}

	}

}
