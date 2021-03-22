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

public class ComprovanteSicredi extends ComprovanteBanco {

	private static Logger log = LoggerFactory.getLogger(ComprovanteSicredi.class);

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		if (isNotParse(_lines)) {
			return null;
		}
		for (String line : _lines) {
			line = StringUtils.normalizeSpace(line).trim();
			if (line.equals("Boletos")) {
				return parseTitulo(_lines, agenciabancaria, parceiro, "Boletos");
			}
			if (line.equals("TED Outra Titularidade")) {
				return parseTed(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("Tributos")) {
				return parseTributos(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("Boletos Eletrônicos")) {
				return parseTitulo(_lines, agenciabancaria, parceiro, "Boletos Eletrônicos");
			}
			if (line.contains("SIMPLES NACIONAL")) {
				return parseSimplesNacional(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("Tributos FGTS")) {
				return parseTributosFgts(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("Contas de Consumo")) {
				return parseConsumo(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("Comprovante de inclusão de Favorecido")) {
				return null;
			}
			if (line.equals("Rejeitar Boletos Eletrônicos")) {
				return null;
			}
			if (line.contains("Senha do Cartão")) {
				return null;
			}
		}

		throw new ComprovanteException("doc.not.comprovante");
	}

	private boolean isNotParse(String[] lines) {
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line).trim();
			if (line.contains("Autenticação Eletrônica:")) {
				if (StringUtils.contains(line, "Transação pendente de autorização")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private List<Comprovante> parseTributosFgts(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
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
			if (line.contains("(R$):")) {
				String value = StringUtils.substringAfter(line, "(R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("FGTS")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(line);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
		}
		
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(DOCUMENTO);
		diffValue.setNewValue("FGTS");
		diffValue.setLine(i);
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
			if (line.contains("Cooperativa:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Corrente:")) {
				String value = StringUtils.substringAfter(line, "Corrente:").trim();
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
			if (line.contains("(R$):")) {
				String value = StringUtils.substringAfter(line, "(R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(FORNECEDOR);
		diffValue.setNewValue("SIMPLES NACIONAL");
		diffValue.setLine(i);
		list.add(diffValue);
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(DOCUMENTO);
		diffValue2.setNewValue("SIMPLES NACIONAL");
		diffValue2.setLine(i);
		list.add(diffValue2);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
		return comprovantes;
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro,
			String obs) throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CPF/CNPJ do Pagador Efetivo:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ do Pagador Efetivo:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ do Beneficiário:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ do Beneficiário:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Razão Social do Beneficiário:")) {
				String value = StringUtils.substringAfter(line, "Razão Social do Beneficiário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
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
			if (line.contains("Valor do Título (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor do Título (R$):").trim();
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
			if (line.contains("Nº Ident. DDA:")) {
				String value = StringUtils.substringAfter(line, "Nº Ident. DDA:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Descrição do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Descrição do Pagamento:").trim();
				value = MrContadorUtil.removeDots(value);
				Optional<DiffValue> op = list.stream().filter(diff -> diff.getOldValue().contentEquals(DOCUMENTO))
						.findFirst();
				if (!StringUtils.isBlank(value)) {

					DiffValue diffValue = new DiffValue();

					diffValue.setOldValue(DOCUMENTO);
					if (op.isPresent()) {
						diffValue = op.get();
					}
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				} else {
					if (op.isEmpty()) {
						DiffValue diffValue = new DiffValue();
						diffValue.setOldValue(DOCUMENTO);
						diffValue.setNewValue("Boleto");
						diffValue.setLine(i);
						list.add(diffValue);

					}
				}
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue(obs);
		diffValue.setLine(2);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TITULO));
		return comprovantes;
	}

	private List<Comprovante> parseTed(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ:")) {
				String value = StringUtils.substringAfter(line, "CPF/CNPJ:").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Favorecido:")) {
				String value = StringUtils.substringAfter(line, "Favorecido:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Transferência:")) {
				String value = StringUtils.substringAfter(line, "Data Transferência:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor a Transferir (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor a Transferir (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Número de Controle:")) {
				String value = StringUtils.substringAfter(line, "Número de Controle:").trim();
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
		diffValue.setNewValue("TED Outra Titularidade");
		diffValue.setLine(2);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}

	private List<Comprovante> parseTributos(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
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
			if (line.contains("Valor Total (R$):")) {
				String value = StringUtils.substringAfter(line, "Valor Total (R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Descrição do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Descrição do Pagamento:").trim();
				value = MrContadorUtil.removeDots(value);
				if (StringUtils.isBlank(value)) {
					value = "Tributos";
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Tipo de Documento:")) {
				String value = StringUtils.substringAfter(line, "Tipo de Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(FORNECEDOR);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRIBUTOS));
		return comprovantes;
	}
	
	private List<Comprovante> parseConsumo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Cooperativa Origem:")) {
				String value = StringUtils.substringAfter(line, "Cooperativa Origem:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(AGENCIA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Conta Origem:")) {
				String value = StringUtils.substringAfter(line, "Conta Origem:").trim();
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
			if (line.contains("(R$):")) {
				String value = StringUtils.substringAfter(line, "(R$):").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Descrição do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Descrição do Pagamento:").trim();
				value = MrContadorUtil.removeDots(value);
				if (StringUtils.isBlank(value)) {
					value = "Contas de consumo";
				}
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Tipo de Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Tipo de Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
     		}
			if (line.contains("Nome da Empresa:")) {
				String value = StringUtils.substringAfter(line, "Nome da Empresa:").trim();
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(FORNECEDOR);
				diffValue2.setNewValue(value);
				diffValue2.setLine(i);
				list.add(diffValue2);
			}
			i = i + 1;
		}
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.OUTROS));
		return comprovantes;
	}


	protected void validateAgencia(Optional<DiffValue> agencia, Optional<DiffValue> conta,
			Agenciabancaria agenciabancaria) throws ComprovanteException {
		if (agencia.isPresent()) {
			String agenciaOriginal = MrContadorUtil.removeZerosFromEnd(agenciabancaria.getAgeAgencia());
			String agenciaDoc = MrContadorUtil.removeZerosFromEnd(agencia.get().getNewValue());
			if (!MrContadorUtil.compareWithoutDigit(agenciaOriginal, agenciaDoc)) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}

		}
		if (conta.isPresent()) {
			if (!MrContadorUtil.compareWithoutDigit(agenciabancaria.getAgeNumero(), conta.get().getNewValue())) {
				throw new ComprovanteException("comprovante.agencianotequal");
			}
		}
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
			periodos.forEach(p -> service.callComprovanteSicredi(parceiroId, agenciabancariaId, p));
		}
	}

}
