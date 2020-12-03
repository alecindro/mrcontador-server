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

public class PdfCredicrea extends PdfParserExtrato {

	private static final String CONTA = "Conta:";
	private static final String AGENCIA = "Agência:";
	private static final int DATA_COLUMN = 6;
	private static final int DESCRICAO_COLUMN = 25;
	private static final int DCTO_COLUMN = 94;
	private static final int DEB_COLUMN = 150;
	private static final int CRED_COLUMN = 123;
	
	private static final int SALDO_COLUMN = 175;
	private static Logger log = LoggerFactory.getLogger(PdfCredicrea.class);

	public PdfCredicrea() throws IOException {
		super();
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos) {
		// TODO Auto-generated method stub
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
		String dtbalancete = StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), dateFormatter)));
		String historico = StringUtils.substring(line, DESCRICAO_COLUMN, DCTO_COLUMN);
		data.setHistorico(StringUtils.normalizeSpace(historico));
		String documento = StringUtils.substring(line, DCTO_COLUMN, CRED_COLUMN);
		data.setDocumento(MrContadorUtil.removeDots(StringUtils.normalizeSpace(documento)));
		String valor = StringUtils.substring(line, CRED_COLUMN, DEB_COLUMN);
		if (!valor.isBlank()) {
			data.setTipoEntrada(TipoEntrada.CREDIT);
		} else {
			valor = StringUtils.substring(line, DEB_COLUMN, SALDO_COLUMN).trim();
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.CREDCREA.name());
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);			
			if (line.contains(AGENCIA) && line.contains(CONTA)) {
				String agencia = StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA),"|");
				dto.setAgencia(MrContadorUtil.removeZerosFromInital(agencia.trim()));
				String conta = StringUtils.substringAfter(line, CONTA);
				dto.setConta(MrContadorUtil.removeZerosFromInital(conta.trim()));
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
			
			if (StringUtils.deleteWhitespace(line).isEmpty()) {
				continue;
			}
			if (!StringUtils.isNumeric(StringUtils.normalizeSpace(StringUtils.remove(StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN), "/")))
					|| StringUtils.substring(line, DCTO_COLUMN, CRED_COLUMN).isBlank()) {
				continue;
			}
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}

	@Override
	protected int getLineHeader() {
		return 7;
	}

}
