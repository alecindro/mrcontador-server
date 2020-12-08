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

public class PdfItau extends PdfParserExtrato {

	private static final String AGENCIA = "Agência/Conta:";
	private static final String SALDO = "SALDO";
	private static final int DATA_COLUMN = 10;
	private static final int D_COLUMN = 25;
	private static final int DESCRICAO_COLUMN = 38;
	private static final int DCTO_COLUMN = 96;
	private static final int VALUE_COLUMN = 141;
	private static final int SALDO_COLUMN = 234;
	private static final String PERIODO = "Extrato de";
	private static Logger log = LoggerFactory.getLogger(PdfItau.class);
	private DataItau dataItau;

	public PdfItau() throws IOException {
		super();
		
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
		String actualDate = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_COLUMN, D_COLUMN));
		data.setLancamento(Date.valueOf(dataItau.getDataLancamento(actualDate)));
		String historico = StringUtils.substring(line, DESCRICAO_COLUMN, DCTO_COLUMN);
		data.setHistorico(StringUtils.normalizeSpace(historico));
		String documento = StringUtils.substring(line, DCTO_COLUMN, VALUE_COLUMN);
		data.setDocumento(MrContadorUtil.removeDots(StringUtils.normalizeSpace(documento)));
		String valor = StringUtils.substring(line, VALUE_COLUMN, SALDO_COLUMN).trim();
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
		dto.setBanco(BancoCodigoBancario.ITAU.getCodigoBancario());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if(line.contains(PERIODO)) {
				String periodoInicial = StringUtils.substringBefore(StringUtils.substringAfter(line, PERIODO).trim(),StringUtils.SPACE);
				dataItau = new DataItau(periodoInicial);
			}
			if (line.contains(AGENCIA)) {
				String[] agenciaConta = StringUtils.split(StringUtils.substringAfter(line, AGENCIA),"/");
				dto.setAgencia(agenciaConta[0].trim());
				dto.setConta(agenciaConta[1].trim());
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
		
			if (StringUtils.deleteWhitespace(StringUtils.substring(line, DESCRICAO_COLUMN, DCTO_COLUMN)).contains(SALDO)) {
				continue;
			}
			if (StringUtils.deleteWhitespace(line).isEmpty()) {
				continue;
			}
			if (!StringUtils.isNumeric(StringUtils.normalizeSpace(StringUtils.remove(StringUtils.substring(line, DATA_COLUMN, D_COLUMN), "/")))
					|| StringUtils.substring(line, VALUE_COLUMN, SALDO_COLUMN).isBlank()) {
				continue;
			}
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}

	@Override
	protected int getLineHeader() {
		return 6;
	}
	
	public class DataItau {

		private LocalDate periodoFinal;

		public DataItau(String first) {
			periodoFinal = LocalDate.parse(first, dateFormatter);
		}

		public LocalDate getDataLancamento(String dataLancamento) {
			String[] diaMes = StringUtils.split(dataLancamento, "/");
			Integer dia = Integer.valueOf(diaMes[0]);
			Integer mes = Integer.valueOf(diaMes[1]);

			if (dia - periodoFinal.getDayOfMonth() < 0 && mes - periodoFinal.getDayOfMonth() < 0) {
				periodoFinal = periodoFinal.withDayOfMonth(dia).withMonth(mes).withYear(periodoFinal.getYear() + 1);
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
			extratoService.callExtratoItau(extratos, parceiroId);
   }

}
