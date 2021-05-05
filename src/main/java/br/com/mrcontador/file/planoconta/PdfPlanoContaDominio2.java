package br.com.mrcontador.file.planoconta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.file.dto.PlanoContaDetail;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

public class PdfPlanoContaDominio2 extends PlanoContaPdf implements PdfReader {

	public PdfPlanoContaDominio2() throws IOException {
		super();
	}

	@Override
	public PlanoConta process(List<PDDocument> pages) throws IOException {
		PlanoConta planoConta = new PlanoConta();
		if (pages.isEmpty()) {
			throw new FileException("error.pdf.empty");
		}
		boolean first = true;
		int numberOfPages = pages.size();
		int numberPage = 1;
		for (PDDocument page : pages) {
			String pdfFileInText = super.getText(page);
			String lines[] = pdfFileInText.split("\\n");
			if (numberOfPages == numberPage) {
				lines = Arrays.copyOf(lines, lines.length - 1);
			}
			numberPage++;
			if (first) {
				parseHeader(planoConta, lines);
				first = false;
			}
			parseBody(planoConta, lines);
		}
		return planoConta;
	}

	private void parseBody(PlanoConta planoConta, String[] lines) {
		for (int i = 4; i < lines.length; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			PlanoContaDetail planoContaDetail = new PlanoContaDetail();
			String[] values = StringUtils.split(line, StringUtils.SPACE);
			planoContaDetail.setClassificacao(values[0]);
			planoContaDetail.setCodigo(values[1]);
			planoContaDetail.setT(values[2]);
			planoContaDetail.setGrau(values[values.length - 1]);
			values = ArrayUtils.removeAll(values, 0, 1, 2, values.length - 1);
			planoContaDetail.setDescricao(StringUtils.join(values, StringUtils.SPACE));
			String _cnpj = values[values.length - 1];
			if (StringUtils.isNumeric(_cnpj) && _cnpj.length() > 8) {
				planoContaDetail.setCnpj(_cnpj);
				values = ArrayUtils.remove(values, values.length - 1);
				planoContaDetail.setDescricao(StringUtils.join(values, StringUtils.SPACE));
			} else {
				String value = StringUtils.reverse(planoContaDetail.getDescricao());
				planoContaDetail.setDescricao("");
				planoContaDetail.setCnpj("");
				for (int j = 0; j < value.length(); j++) {
					CharSequence v = value.subSequence(j, j + 1);
					if (StringUtils.isNumeric(v) && planoContaDetail.getCnpj().length() < 14) {
						planoContaDetail.setCnpj(StringUtils.join(planoContaDetail.getCnpj(), v));
					} else {
						planoContaDetail.setDescricao(StringUtils.join(planoContaDetail.getDescricao(), v));
					}
				}
				planoContaDetail.setDescricao(StringUtils.reverse(planoContaDetail.getDescricao()));
				planoContaDetail.setCnpj(StringUtils.reverse(planoContaDetail.getCnpj()));
			}
			planoConta.addPlanoContaDetail(planoContaDetail);
		}
	}

	private void parseHeader(PlanoConta planoConta, String lines[]) {
		if (lines != null && lines.length > 5) {
			String line = lines[1];
			String[] lineCnpj = line.trim().split("\\s+");
			if (lineCnpj != null && lineCnpj.length >= 4) {
				planoConta.setCnpjCliente(lineCnpj[1].trim());
				planoConta.setDataPlanoDeContas(lineCnpj[3].trim());
			}
		}
	}
}
