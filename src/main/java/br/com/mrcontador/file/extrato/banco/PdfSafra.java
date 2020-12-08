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

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.PdfParserExtrato;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.util.MrContadorUtil;

public class PdfSafra extends PdfParserExtrato {

	private static final String AGENCIA = "AG:";
	private static final String CONTA = "CONTA:";
	private static final String SALDO_CONTA_CORRENTE = "SALDO CONTA CORRENTE";
	private static final int DATA_COLUMN = 8;
	private static final int DESCRICAO_COLUMN = 21;
	private static final int DCTO_COLUMN = 137;
	private static final int COMPLEMENTO_COLUMN = 81;
	private static final int VALUE_COLUMN = 174;
	private static Logger log = LoggerFactory.getLogger(PdfSafra.class);
	private DataSafra dataSafra;

	public PdfSafra() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos, Agenciabancaria agencia) {
		// TODO Auto-generated method stub

	}

	@Override
	protected TipoEntrada getTipo(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String actualDate = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN));
		data.setLancamento(Date.valueOf(dataSafra.getDataLancamento(actualDate)));
		String historico = StringUtils.substring(line, DESCRICAO_COLUMN, COMPLEMENTO_COLUMN);
		data.setHistorico(StringUtils.normalizeSpace(historico));
		String documento = StringUtils.substring(line, DCTO_COLUMN, VALUE_COLUMN);
		data.setDocumento(MrContadorUtil.removeDots(StringUtils.normalizeSpace(documento)));
		String valor = StringUtils.substring(line, VALUE_COLUMN);
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}
	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SAFRA.getCodigoBancario());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if (line.contains(AGENCIA) && line.contains(CONTA)) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA), "|").trim();
				String conta = StringUtils.substringAfter(line, CONTA);
				dto.setAgencia(agencia);
				dto.setConta(conta);
				String nextLine = StringUtils.normalizeSpace(lines[i + 1]);
				String lastPeriodo = StringUtils.substringAfterLast(nextLine, StringUtils.SPACE);
				dataSafra = new DataSafra(lastPeriodo);
			}
		}
		return dto;
	}

	@Override
	protected List<OfxData> parseBody(String[] lines, int lineHeader) {
		List<OfxData> datas = new ArrayList<>();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if (!StringUtils.isNumeric(StringUtils.normalizeSpace(
					StringUtils.remove(StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN), "/")))
					|| !MrContadorUtil.isMoney(StringUtils.substring(line, VALUE_COLUMN).trim())) {
				continue;
			}
			if (StringUtils.normalizeSpace(line).contains(SALDO_CONTA_CORRENTE)) {
				continue;
			}
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}

	@Override
	protected int getLineHeader() {
		return 9;
	}

	public class DataSafra {

		private LocalDate periodoFinal;

		public DataSafra(String lastPeriodo) {
			periodoFinal = LocalDate.parse(lastPeriodo, dateFormatter);
		}

		public LocalDate getDataLancamento(String dataLancamento) {
			String[] diaMes = StringUtils.split(dataLancamento, "/");
			Integer dia = Integer.valueOf(diaMes[0]);
			Integer mes = Integer.valueOf(diaMes[1]);

			if (periodoFinal.getDayOfMonth() - dia < 0 && periodoFinal.getDayOfMonth() - mes < 0) {
				periodoFinal = periodoFinal.withDayOfMonth(dia).withMonth(mes).withYear(periodoFinal.getYear() - 1);
				return periodoFinal;
			}
			periodoFinal = periodoFinal.withDayOfMonth(dia).withMonth(mes).withYear(periodoFinal.getYear());
			return periodoFinal;
		}
	}
	
	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			log.info("Processando callExtrato");
			extratoService.callExtratoSafra(extratos, parceiroId);
   }

}
