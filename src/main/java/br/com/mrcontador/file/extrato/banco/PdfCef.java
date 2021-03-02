package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.erros.ExtratoException;
import br.com.mrcontador.file.extrato.PdfParserExtrato;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.util.MrContadorUtil;

public class PdfCef extends PdfParserExtrato {

	private static final String CONTA = "Conta:";
	private static final int DATA_COLUMN = 13;
	private static final int DESCRICAO_COLUMN = 76;
	private static final int DCTO_COLUMN = 43;
	private static final int VALUE_COLUMN = 182;
	private static final int SALDO_COLUMN = 244;
	private static final String BREAK = "Lançamentos do Dia";
	private static Logger log = LoggerFactory.getLogger(PdfCef.class);
	private static final String EXTRATO = "EXTRATO";
	private static final String DOC = "CAIXA";

	public PdfCef() throws IOException {
		super();
	}
	
	@Override
	protected TipoEntrada getTipo(String value) {
		String tipo = StringUtils.right(StringUtils.normalizeSpace(value), 1);
		TipoEntrada tipoEntrada = TipoEntrada.DEBIT;
		if (StringUtils.equals(tipo, "C")) {
			tipoEntrada = TipoEntrada.CREDIT;
		}
		return tipoEntrada;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String[] values = StringUtils.split(line, StringUtils.SPACE);
		String dtbalancete = StringUtils.normalizeSpace(values[0]);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), dateFormatter)));
		String[] historicos = Arrays.copyOfRange(values, 2, values.length);
		historicos = Arrays.copyOfRange(historicos, 0, historicos.length-4);
		String historico = StringUtils.join(historicos, StringUtils.SPACE);
		data.setHistorico(StringUtils.normalizeSpace(historico));
		String documento = values[1];
		data.setDocumento(MrContadorUtil.removeDots(StringUtils.normalizeSpace(documento)));
		String valor = values[values.length-4];
		data.setTipoEntrada(getTipo(values[values.length-3]));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		if(data.getTipoEntrada().equals(TipoEntrada.DEBIT)){
			data.setValor(data.getValor().negate());
		}
		
	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.CAIXA.getCodigoBancario());
		boolean isExtrato = false;
		boolean isDoc = false;
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);			
			if (line.contains(CONTA)) {
				String[] values = StringUtils.substringAfter(line, CONTA).split("/");
				dto.setAgencia(MrContadorUtil.removeZerosFromInital(values[0].trim()));
				dto.setConta(MrContadorUtil.removeZerosFromInital(values[2].trim()));
			}
			if(line.toUpperCase().contains(EXTRATO)) {
				isExtrato = true;
			}
			/*if(StringUtils.deleteWhitespace(line).toUpperCase().contains(DOC)) {
				isDoc = true;
			}*/
		}
		if(!isExtrato) {
			throw new ExtratoException("doc.not.extrato");
		}
		/*if(!isDoc) {
			throw new ExtratoException("doc.not.extrato.caixa");
		}*/
		return dto;
	}

	@Override
	protected List<OfxData> parseBody(String[] lines, int lineHeader) {
		List<OfxData> datas = new ArrayList<>();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if(StringUtils.normalizeSpace(line).trim().contains(BREAK)) {
				break;
			}
			if (StringUtils.deleteWhitespace(line).isEmpty()) {
				continue;
			}
			String[] columns = StringUtils.split(line, StringUtils.SPACE);
			String date = columns[0];
			String value = null;
			if(columns.length>4) {
				value = columns[columns.length-4];
			}
			
			if (!StringUtils.isNumeric(StringUtils.normalizeSpace(StringUtils.remove(date, "/")))
					|| value == null || value.isBlank()) {
				continue;
			}
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}

	@Override
	protected int getLineHeader() {
		return 10;
	}
	
	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			log.info("Processando callExtrato");
			extratoService.callExtratoCEF(extratos, parceiroId);
   }

}
