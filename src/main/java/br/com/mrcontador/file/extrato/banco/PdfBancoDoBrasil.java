package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.extrato.PdfParser;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.util.MrContadorUtil;

public class PdfBancoDoBrasil extends PdfParser {

	// private static final String DT_BALANCETE = "Dt. balancete";
	// private static final String DT_MOVIMENTO = "Dt. movimento";
	// private static final String AG_ORIGEM = "Ag. origem";
	// private static final String LOTE = "Lote";
	// private static final String HISTORICO = "Histórico";
	// private static final String DOCUMENTO = "Documento";
	// private static final String VALOR = "Valor R$";
	private static final String AGENCIA= "Agência";
	private static final String CONTA = "Conta corrente";
	private static final String SALDO_ANTERIOR = "Saldo Anterior";
	private static final String SALDO_FINAL = "SALDO";
	private static Logger log = LoggerFactory.getLogger(PdfBancoDoBrasil.class);

	public PdfBancoDoBrasil() throws IOException {
		super();
	}

	public OfxDTO process(List<PDDocument> pages) throws IOException {
		int lineHeader = 9;
		
		if (pages.isEmpty()) {
			throw new FileException("error.pdf.empty");
		}
		StringBuilder builder = new StringBuilder();
		for (PDDocument page : pages) {
			String pdfFileInText = super.getText(page);
			builder.append(pdfFileInText);
		}
		String lines[] = builder.toString().split("\\n");
		OfxDTO dto = parseDataBanco(lines, lineHeader);
		dto.setDataList(parseBody(lines, lineHeader));
		return dto;
	}

	private OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.BB.name());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if(line.contains(AGENCIA)) {
				dto.setAgencia(StringUtils.substringAfter(line, AGENCIA).trim());
			}
			if(line.contains(CONTA)) {
				dto.setConta(StringUtils.substringBefore(StringUtils.substringAfter(line, CONTA).trim(),StringUtils.SPACE));
			}
		}
		return dto;
	}

	private List<OfxData> parseBody(String[] lines, int lineHeader) {
		List<OfxData> datas = new ArrayList<>();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if (StringUtils.normalizeSpace(line).contains(SALDO_ANTERIOR)) {
				continue;
			}
			if (StringUtils.deleteWhitespace(line).contains(SALDO_FINAL)) {
				break;
			}
			readLine(line, data, i);
			if (i < lines.length - 1) {
				if (StringUtils.isWhitespace(StringUtils.left(lines[i + 1], 83))) {
					i = i + 1;
					data.setInfAdicional(StringUtils.trim(StringUtils.substring(lines[i], 83)));
				}
			}

			datas.add(data);
		}
		return datas;
	}

	private void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String dtbalancete = StringUtils.left(StringUtils.trim(line), 10);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), dateFormatter)));
		String agencia = StringUtils.left(StringUtils.substringAfter(line, dtbalancete), 38);
		String lote = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia), 13);
		data.setControle(StringUtils.trim(lote));
		String historico = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote), 40);
		data.setHistorico(StringUtils.trim(historico));
		String documento = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote + historico),
				27);
		data.setDocumento(StringUtils.trim(documento));
		String valor = StringUtils.trim(StringUtils
				.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote + historico + documento), 19));
		data.setTipoEntrada(getTipo(valor));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		if(data.getTipoEntrada().equals(TipoEntrada.DEBIT)) {
			data.setValor(data.getValor().negate());
		}
	}

	private static TipoEntrada getTipo(String value) {
		String tipo = StringUtils.right(StringUtils.normalizeSpace(value), 1);
		TipoEntrada tipoEntrada = TipoEntrada.DEBIT;
		if (StringUtils.equals(tipo, "C")) {
			tipoEntrada = TipoEntrada.CREDIT;
		}
		return tipoEntrada;
	}
}
