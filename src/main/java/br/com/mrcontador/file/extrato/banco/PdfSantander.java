package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.PdfParserExtrato;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.util.MrContadorUtil;


public class PdfSantander extends PdfParserExtrato {

	private static final String EMPRESARIAL = "Internet Banking Empresarial";
	private static final String AGENCIA = "Agência:";	
	private static final String CONTA = "Conta Corrente:";
	private static final String SALDO_ANTERIOR = "SALDO ANTERIOR";
	private static final int DATA_COLUMN = 21;
	private static final int HISTORY_COLUMN = 45;
	private static final int DCTO_COLUMN = 201;
	private static final int VALUE_COLUMN = 226;
	private static final int SALDO_COLUMN = 246;
	private static final String CONTAEMPRESARIAL = "Conta:";
	private static final int DATA_EMPRESARIAL_COLUMN = 14;
	private static final int HISTORY_EMPRESARIAL_COLUMN = 36;
	private static final int DCTO_EMPRESARIAL_COLUMN = 133;
	private static final int VALUE_EMPRESARIAL_COLUMN = 152;
	private static final int SALDO_EMPRESARIAL_COLUMN = 169;
	
	private static Logger log = LoggerFactory.getLogger(PdfSantander.class);
	private boolean isempresarial = false;
	
	public PdfSantander() throws IOException {
		super();
	}

	@Override
	protected int getLineHeader() {
		if(isempresarial) {
			return 7;
		}
		return 6;
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos) {
		// TODO Auto-generated method stub

	}

	@Override
	protected TipoEntrada getTipo(String value) {
		if (MrContadorUtil.isNegative(value)) {
			return TipoEntrada.DEBIT;
		}
		return TipoEntrada.CREDIT;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String date = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_COLUMN, HISTORY_COLUMN));
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(date), dateFormatter)));
		String historico = StringUtils.normalizeSpace(StringUtils.substring(line, HISTORY_COLUMN, DCTO_COLUMN));
		data.setHistorico(historico.trim());
		String numDoc = StringUtils.normalizeSpace(StringUtils.substring(line, DCTO_COLUMN, VALUE_COLUMN));
		data.setDocumento(numDoc);
		String valor = StringUtils.normalizeSpace(StringUtils.substring(line, VALUE_COLUMN, SALDO_COLUMN));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}

	}
	
	private void readLineEmpresarial(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String date = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_EMPRESARIAL_COLUMN, HISTORY_EMPRESARIAL_COLUMN));
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(date), dateFormatter)));
		String historico = StringUtils.normalizeSpace(StringUtils.substring(line, HISTORY_EMPRESARIAL_COLUMN, DCTO_EMPRESARIAL_COLUMN));
		data.setHistorico(historico.trim());
		String numDoc = StringUtils.normalizeSpace(StringUtils.substring(line, DCTO_EMPRESARIAL_COLUMN, VALUE_EMPRESARIAL_COLUMN));
		data.setDocumento(numDoc);
		String valor = StringUtils.normalizeSpace(StringUtils.substring(line, VALUE_EMPRESARIAL_COLUMN, SALDO_EMPRESARIAL_COLUMN));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}

	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		if (StringUtils.normalizeSpace(lines[0]).contains(EMPRESARIAL)) {
			isempresarial = true;
			lineHeader = getLineHeader();
			return parseDataBancoEmpresarial(lines, lineHeader);
		}
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SANTANDER.name());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if (line.contains(AGENCIA)) {
				dto.setAgencia(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA), CONTA).trim());
				dto.setConta(StringUtils.substringAfter(line, CONTA).trim());
			}
		}
		return dto;
	}
	
	private OfxDTO parseDataBancoEmpresarial(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SANTANDER.name());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if (line.contains(AGENCIA)) {
				dto.setAgencia(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA), CONTAEMPRESARIAL).trim());
				dto.setConta(StringUtils.substringAfter(line, CONTAEMPRESARIAL).trim());
			}
		}
		return dto;
	}

	@Override
	protected List<OfxData> parseBody(String[] lines, int lineHeader) {
		if(isempresarial) {
			return parseBodyEmpresarial(lines, getLineHeader());
		}
		List<OfxData> datas = new ArrayList<>();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if (StringUtils.normalizeSpace(line).contains(SALDO_ANTERIOR)) {
				continue;
			}

			if (!StringUtils.isNumeric(StringUtils
					.normalizeSpace(StringUtils.remove(StringUtils.substring(line, DATA_COLUMN, HISTORY_COLUMN), "/")))
					|| StringUtils.substring(line, DATA_COLUMN, HISTORY_COLUMN).isBlank()) {
				continue;
			}
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}
	
	private List<OfxData> parseBodyEmpresarial(String[] lines, int lineHeader) {
		List<OfxData> datas = new ArrayList<>();
		lineHeader = getLineHeader();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if (StringUtils.normalizeSpace(line).contains(SALDO_ANTERIOR)) {
				continue;
			}

			if (!StringUtils.isNumeric(StringUtils
					.normalizeSpace(StringUtils.remove(StringUtils.substring(line, DATA_EMPRESARIAL_COLUMN, HISTORY_EMPRESARIAL_COLUMN), "/")))
					|| StringUtils.substring(line, DATA_EMPRESARIAL_COLUMN, HISTORY_EMPRESARIAL_COLUMN).isBlank()) {
				continue;
			}
			readLineEmpresarial(line, data, i);
			datas.add(data);
		}
		return datas;
	}
	
	

}
