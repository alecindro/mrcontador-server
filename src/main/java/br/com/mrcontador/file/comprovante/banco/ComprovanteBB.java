package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteBB extends ComprovanteBanco{

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		
		String[] _lines = comprovante.split("\\r?\\n");

		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		
		
		String line = StringUtils.normalizeSpace(_lines[6].trim());
		if (line.equals("COMPROVANTE DE PAGAMENTO DE TITULOS")) {
			return parseTitulo(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).trim().equals("COMPROVANTE DE PAGAMENTO DE TITULOS")) {
			return parseTitulo(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).trim().equals("COMPROVANTE DE PAGAMENTO")) {
			return parseComprovPagto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5]).trim().equals("COMPROVANTE DE PAGAMENTO")) {
			return parseComprovPagto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[7]).trim().equals("COMPROVANTE DE PAGAMENTO")) {
			return parseComprovPagto(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).trim().equals("COMPROVANTE DE DEBITO AUTOMATICO")) {
			return parseComprovDebito(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[5]).trim().equals("COMPROVANTE DE DEBITO AUTOMATICO")) {
			return parseComprovDebito(_lines, agenciabancaria, parceiro);
		}
		throw new ComprovanteException("Comprovante não identificado");
	}
	
	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro) throws ComprovanteException{
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(),"CONTA:").trim();
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
				String value = StringUtils.normalizeSpace(lines[i+1]).trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("CNPJ:")) {
				if(StringUtils.normalizeSpace(lines[i+1]).trim().equals("PAGADOR:")){
					String value = StringUtils.substringAfter(line, "CNPJ:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i+1);
					list.add(diffValue);
				}			
			}
			if (line.contains("CNPJ:")) {
				if(StringUtils.normalizeSpace(lines[i-2]).trim().equals("PAGADOR:")){
					String value = StringUtils.substringAfter(line, "CNPJ:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_PAG);
					diffValue.setNewValue(value);
					diffValue.setLine(i+1);
					list.add(diffValue);
				}
			}
			if (line.contains("NR. DOCUMENTO")) {
				String value = StringUtils.substringAfter(line, "NR. DOCUMENTO").trim();
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
			i = i+1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO DE TITULOS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseComprovPagto(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro) throws ComprovanteException{
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(),"CONTA:").trim();
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
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("DOCUMENTO:")) {
				String value = StringUtils.substringAfter(line, "DOCUMENTO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("Data do pagamento")) {
				String value = StringUtils.substringAfter(line, "Data do pagamento").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("Valor Total")) {
				String value = StringUtils.substringAfter(line, "Valor Total").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			
			i = i+1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("COMPROVANTE DE PAGAMENTO");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseComprovDebito(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro) throws ComprovanteException{
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("AGENCIA:")) {
				String value = StringUtils.substringBefore(StringUtils.substringAfter(line, "AGENCIA:").trim(),"CONTA:").trim();
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
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("NR.REMESSA:")) {
				String value = StringUtils.substringAfter(line, "NR.REMESSA:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("DATA DO DEBITO:")) {
				String value = StringUtils.substringAfter(line, "DATA DO DEBITO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("DATA PREVISTA DO DEBITO:")) {
				String value = StringUtils.substringAfter(line, "DATA PREVISTA DO DEBITO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("VALOR DO DEBITO R$")) {
				String value = StringUtils.substringAfter(line, "VALOR DO DEBITO R$").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			if (line.contains("HISTORICO LANCAMENTO:")) {
				String value = StringUtils.substringAfter(line, "HISTORICO LANCAMENTO:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(OBS);
				diffValue.setNewValue(value);
				diffValue.setLine(i+1);
				list.add(diffValue);
			}
			
			i = i+1;
		}
	
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
}
