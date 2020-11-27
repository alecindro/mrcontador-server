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

public class PdfBancoDoBrasil extends PdfParserExtrato {

	private static final String AGENCIA= "Agência";
	private static final String CONTA = "Conta corrente";
	private static final String SALDO_ANTERIOR = "Saldo Anterior";
	private static final String SALDO_FINAL = "SALDO";
	private static Logger log = LoggerFactory.getLogger(PdfBancoDoBrasil.class);

	public PdfBancoDoBrasil() throws IOException {
		super();
	}

	

	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
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

	protected List<OfxData> parseBody(String[] lines, int lineHeader) {
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
					data.setInfAdicional(StringUtils.normalizeSpace(StringUtils.substring(lines[i], 83)));
				}
			}

			datas.add(data);
		}
		return datas;
	}

	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String dtbalancete = StringUtils.left(StringUtils.trim(line), 10);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), dateFormatter)));
		String agencia = StringUtils.left(StringUtils.substringAfter(line, dtbalancete), 38);
		data.setAgenciaOrigem(StringUtils.normalizeSpace(agencia));
		String lote = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia), 13);
		data.setControle(StringUtils.normalizeSpace(lote));
		String historico = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote), 40);
		data.setHistorico(StringUtils.normalizeSpace(historico));
		String documento = StringUtils.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote + historico),
				line.length());
		String[] _valor =  StringUtils.split(documento);
		data.setDocumento(MrContadorUtil.removeDots(StringUtils.normalizeSpace(_valor[0])));
		
		//String valor = StringUtils.trim(StringUtils
		//		.left(StringUtils.substringAfter(line, dtbalancete + agencia + lote + historico + documento), line.length()));
		data.setTipoEntrada(getTipo(_valor[2]));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(_valor[1])));
		if(data.getTipoEntrada().equals(TipoEntrada.DEBIT)) {
			data.setValor(data.getValor().negate());
		}
	}

	protected TipoEntrada getTipo(String value) {
		String tipo = StringUtils.right(StringUtils.normalizeSpace(value), 1);
		TipoEntrada tipoEntrada = TipoEntrada.DEBIT;
		if (StringUtils.equals(tipo, "C")) {
			tipoEntrada = TipoEntrada.CREDIT;
		}
		return tipoEntrada;
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos) {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected int getLineHeader() {
		return 9;
	}
}
