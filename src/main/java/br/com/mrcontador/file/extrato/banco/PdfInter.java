package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

public class PdfInter extends PdfParserExtrato {
	
	protected DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public PdfInter() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos, Long parceiroId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLineHeader() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	protected TipoEntrada getTipo(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		String reverse = StringUtils.reverse(line);
		String[] values = reverse.split(StringUtils.SPACE);
		String valor = StringUtils.reverse(values[2]);
		String signal = StringUtils.reverse(values[3]);
		String dataLancamento = line.split(StringUtils.SPACE)[0];
		String lancamento = StringUtils.substringBefore(StringUtils.remove(line, dataLancamento), signal);
		data.setHistorico(lancamento);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dataLancamento), dateFormatter)));
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if(signal.contains("-")) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
			data.setValor(data.getValor().negate());
		}
		
	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.CREDCREA.getCodigoBancario());
		boolean isExtrato = false;
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);			
			if (line.contains("Agência:")) {
				String agencia = StringUtils.substringAfter(line, "Agência:").split(StringUtils.SPACE)[0];
				dto.setAgencia(MrContadorUtil.removeZerosFromInital(agencia.trim()));
				String conta = StringUtils.substringAfter(line, "Conta:").split(StringUtils.SPACE)[0];
				dto.setConta(MrContadorUtil.removeZerosFromInital(conta.trim()));
			}
			if(line.toUpperCase().contains("EXTRATO")) {
				isExtrato = true;
			}
		}
		if(!isExtrato) {
			throw new ExtratoException("doc.not.extrato");
		}
		return dto;
	}

	@Override
	protected List<OfxData> parseBody(String[] lines, int lineHeader) {
		List<OfxData> datas = new ArrayList<>();
		int start = 0;
		for(String line : lines) {
			start = start + 1;
			if(line.contains("Lançamentos") && line.contains("Valor") && line.contains("Saldo")) {
				break;
			}
		}
		for (int i = start; i<lines.length;i++) {
			String 	line = StringUtils.normalizeSpace(lines[i]);
			if(!isData(line)) {
				break;
			}
			PdfData data = new PdfData();
			readLine(line, data, i);
			datas.add(data);
		}
		return datas;
	}
	
	private boolean isData(String line) {
		String data = line.split(StringUtils.SPACE)[0];
		return MrContadorUtil.isDate(data, dateFormatter);
	}

}
