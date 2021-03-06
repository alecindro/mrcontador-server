package br.com.mrcontador.file.comprovante.banco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.ExtratoService;
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
int i = 12;		
		while(i>=0) {
			String line = StringUtils.normalizeSpace(_lines[i]).trim().toUpperCase();
			if (line.equals("COMPROVANTE DE PAGAMENTO DE TITULO")) {
				return parseTitulo(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("COMPROVANTE DE PAGAMENTO DE TRIBUTOS")) {
				return parsePagtoTributos(_lines, agenciabancaria, parceiro);
			}
			if (line.equals("COMPROVANTE DE PAGAMENTO DE CONVENIO")) {
				return parsePagtoConvenio(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("ARRECADACAO DE DARF")) {
				return parseDarf(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("ARRECADACAO DE GPS")) {
				return parseGPS(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("TRANSFERÊNCIA ENTRE CONTAS")) {
				return parseTransferencia(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("COMPROVANTE DE TRANSFERÊNCIA TED")) {
				return parseTed(_lines, agenciabancaria, parceiro);
			}
			if (line.contains("COMPROVANTE DE TRANSFERÊNCIA DOC")) {
				return parseDoc(_lines, agenciabancaria, parceiro);
			}
			if(line.contains("DAS - SIMPLES NACIONAL")) {
				return parsePagtoDAS(_lines,agenciabancaria,parceiro);
			}
			i = i-1;
		}

	/*			
		if (StringUtils.normalizeSpace(_lines[5]).contains("Comprovante de Pagamento de Convênio")) {
			return parsePagtoTributos(_lines, agenciabancaria, parceiro);
		}*/
		throw new ComprovanteException("doc.not.comprovante");
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim()).toUpperCase();

			if (line.contains("CONTA")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.split(StringUtils.substringAfter(value, "CONTA"))[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("ID DO DOCUMENTO")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "ID DO DOCUMENTO").trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("BENEFICIÁRIO")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(CNPJ_BEN);
				if (StringUtils.normalizeSpace(lines[i + 9]).trim().equals("Sacador/Avalista") && !StringUtils
						.substringAfter(StringUtils.normalizeSpace(lines[i + 10]).trim(), "Razão Social:").isBlank()) {
					String value = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 10]).trim(), "Razão Social:").trim();
					if(!StringUtils.isBlank(value)) {
					diffValue.setNewValue(value);
					diffValue.setLine(i + 10);
					String value2 = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[i + 11]).trim(), "CNPJ/CPF:").trim();
					diffValue2.setNewValue(value2);
					diffValue2.setLine(i + 11);
					}

				} 
				if(diffValue.getNewValue() == null)
				{
					int j = i+1;
					while (!StringUtils.normalizeSpace(lines[j]).contains("Razão Social")) {
						j = j+1;
					}
					String value = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[j]).trim(), "Razão Social:").trim();
					diffValue.setNewValue(value);
					diffValue.setLine(j);
					while (!StringUtils.normalizeSpace(lines[j]).contains("CNPJ/CPF:")) {
						j = j+1;
					}
					
					String value2 = StringUtils
							.substringAfter(StringUtils.normalizeSpace(lines[j]).trim(), "CNPJ/CPF:").trim();
					value2 = MrContadorUtil.onlyNumbers(value2);
					diffValue2.setNewValue(value2);
					diffValue2.setLine(j);
				}
				list.add(diffValue);
				list.add(diffValue2);
			}
			if (line.contains("PAGADOR") && !line.contains("PAGADOR FINAL")) {
				String value = StringUtils.substringAfter(StringUtils.normalizeSpace(lines[i + 4]).trim(), "CNPJ/CPF:")
						.trim();
				value = MrContadorUtil.onlyNumbers(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i + 4);
				list.add(diffValue);
			}

			if (line.contains("DATA DE VENCIMENTO")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DATA DE VENCIMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DATA DO PAGAMENTO")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DATA DO PAGAMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("VALOR NOMINAL")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR NOMINAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("VALOR PAGO")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR PAGO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("ENCARGOS")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "ENCARGOS").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(JUROS);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DESCONTOS")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DESCONTOS").trim();
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
	
	private List<Comprovante> parseTransferencia(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim()).toUpperCase();

			if (line.contains("CONTA")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.split(StringUtils.substringAfter(value, "CONTA"))[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("AUTENTICAÇÃO DOCUMENTO")) {
				String value = StringUtils.normalizeSpace(lines[i+2]).trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("CREDITADO")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "CREDITADO");
				diffValue.setNewValue(value);
								list.add(diffValue);
			}
		

			if (line.contains("DATA")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DATA").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		
			if (line.contains("VALOR")) {
				String  value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR").trim();
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
		diffValue.setNewValue("Comprovante de Transferência entre Contas");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro, TipoComprovante.TRANSFERENCIA));
		return comprovantes;
	}
	
	private List<Comprovante> parseTed(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());

			if (line.contains("CONTA")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.split(StringUtils.substringAfter(value, "CONTA"))[0];
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("AUTENTICAÇÃO DOCUMENTO")) {
				String value = StringUtils.normalizeSpace(lines[i+2]).trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("FAVORECIDO")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "FAVORECIDO");
				diffValue.setNewValue(value);
								list.add(diffValue);
			}
		

			if (line.contains("DATA DA TRANSF.")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DATA DA TRANSF.").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		
			if (line.contains("VALOR")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			
			if (line.contains("CNPJ")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "CNPJ").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("FINALIDADE")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "FINALIDADE").trim();
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

	private List<Comprovante> parseDoc(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
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

			if (line.contains("Autenticação Documento:")) {
				String value = StringUtils.normalizeSpace(lines[i+2]).trim();
				value = MrContadorUtil.removeDots(value);
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}

			if (line.contains("Favorecido:")) {
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				String value = StringUtils.substringAfter(line, "Favorecido:");
				diffValue.setNewValue(value);
								list.add(diffValue);
			}
		

			if (line.contains("Data da Transf.:")) {
				String value = StringUtils.substringAfter(line, "Data da Transf.:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		
			if (line.contains("Valor:")) {
				String value = StringUtils.substringAfter(line, "Valor:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
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
			if (line.contains("Finalidade:")) {
				String value = StringUtils.substringAfter(line, "Finalidade:").trim();
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
		DiffValue diffValue3 = new DiffValue();
		diffValue3.setOldValue(FORNECEDOR);
		diffValue3.setNewValue("DARF");
		diffValue3.setLine(i);
		list.add(diffValue3);
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
				DiffValue diffValue2 = new DiffValue();
				diffValue2.setOldValue(FORNECEDOR);
				diffValue2.setNewValue(MrContadorUtil.onlyLetters(value));
				diffValue2.setLine(i);
				list.add(diffValue2);
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
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("Pagamento de Tributos");
		diffValue2.setLine(i);
		list.add(diffValue2);
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
		DiffValue diffValue3 = new DiffValue();
		diffValue3.setOldValue(FORNECEDOR);
		diffValue3.setNewValue("GPS");
		diffValue3.setLine(i);
		list.add(diffValue3);
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
	
	private List<Comprovante> parsePagtoDAS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim()).toUpperCase();
			if (line.contains("CONTA")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils
						.substringBefore(StringUtils.substringAfter(value, "CONTA").trim(), StringUtils.SPACE).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DATA DO PAGAMENTO")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DATA DO PAGAMENTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("VALOR NOMINAL")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR NOMINAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("VALOR TOTAL")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "VALOR TOTAL").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("DESCONTO")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DESCONTO").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DESCONTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if(line.contains("DESCRIÇÃO")) {
				String value = StringUtils.remove(line, ":");
				value = StringUtils.substringAfter(value, "DESCRIÇÃO").trim();
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
		diffValue.setNewValue("DAS - Simples Nacional");
		diffValue.setLine(i);
		list.add(diffValue);
		DiffValue diffValue2 = new DiffValue();
		diffValue2.setOldValue(FORNECEDOR);
		diffValue2.setNewValue("DAS - Simples Nacional");
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
	@Override
	public void callFunction(List<Comprovante> comprovantes, ComprovanteService service,
			ExtratoService extratoService) {
		if(!comprovantes.isEmpty()) {
			Comprovante comprovante = comprovantes.stream().findFirst().get();
			Long parceiroId = comprovante.getParceiro().getId();
			Long agenciabancariaId = comprovante.getAgenciabancaria().getId();
			Set<String> periodos = comprovantes.stream().map(c -> c.getPeriodo()).collect(Collectors.toSet());
			periodos.forEach(p -> service.callComprovanteUnicred(parceiroId, agenciabancariaId, p));
		}
	}

}
