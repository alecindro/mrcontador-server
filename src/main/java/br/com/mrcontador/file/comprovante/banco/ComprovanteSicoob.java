package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteSicoob extends ComprovanteBanco {

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
				throw new ComprovanteException("Comprovante não identificado");
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
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_BEN);
				diffValue.setNewValue(lineA);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF/CNPJ Pagador:")) {
				String lineA = StringUtils.substringAfter(line, "CPF/CNPJ Pagador:").trim();
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
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private static final String quebra_linha = "OUVIDORIA       SICOOB:     \\d+";

}
