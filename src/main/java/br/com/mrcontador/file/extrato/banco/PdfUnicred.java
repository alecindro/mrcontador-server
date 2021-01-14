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
import br.com.mrcontador.erros.ExtratoException;
import br.com.mrcontador.file.extrato.PdfParserExtrato;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.util.MrContadorUtil;

public class PdfUnicred extends PdfParserExtrato{
	
	private static final String AGENCIA= "Agência";
	private static final String EXTRATO= "EXTRATO";
	private static final String CONTA = "Conta";
	private static final int DATA_COLUMN = 14;
	private static final int HISTORY_COLUMN = 59;
	private static final int DCTO_COLUMN = 34;
	private static final int VALUE_COLUMN = 131;
	private static final int SALDO_COLUMN = 162;
	private static final String BREAK = "Lançamentos Bloqueados";
	private static Logger log = LoggerFactory.getLogger(PdfUnicred.class);
	public PdfUnicred() throws IOException {
		super();
	}

	@Override
	protected int getLineHeader() {
		return 6;
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos, Agenciabancaria agencia) {
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
		log.info(numberRow +" - " +line );
		String date = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_COLUMN, DCTO_COLUMN));
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(date), dateFormatter)));
		String historico = StringUtils.normalizeSpace(StringUtils.substring(line, HISTORY_COLUMN, VALUE_COLUMN));
		data.setHistorico(historico.trim());
		String numDoc = StringUtils.normalizeSpace(StringUtils.substring(line, SALDO_COLUMN));
		numDoc = MrContadorUtil.onlyNumbers(numDoc);
		data.setDocumento(numDoc);
		String valor = StringUtils.normalizeSpace(StringUtils.substring(line, VALUE_COLUMN, SALDO_COLUMN));
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
		dto.setBanco(BancoCodigoBancario.UNICRED.getCodigoBancario());
		boolean isExtrato = false;
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if(line.contains(AGENCIA) && line.contains(CONTA)) {
				dto.setAgencia(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA),CONTA).trim());
					dto.setConta(StringUtils.substringAfter(line, CONTA).trim());
			}
			if(line.toUpperCase().contains(EXTRATO)) {
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
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			log.info(i +" - " +line );
			if(StringUtils.normalizeSpace(line).contains(BREAK)) {
				break;
			}
			readLine(line, data, i);
			datas.add(data);
			if (i < lines.length - 1) {
				if(StringUtils.normalizeSpace(lines[i + 1]).contains(BREAK)) {
					break;
				}
				if (!MrContadorUtil.isDate(StringUtils.substring(lines[i + 1], DATA_COLUMN,DCTO_COLUMN).trim(), dateFormatter)) {
					i = i + 1;
					data.setInfAdicional(StringUtils.normalizeSpace(StringUtils.substring(lines[i], DATA_COLUMN)));
				}
			}

			
		}
		return datas;
	}
	
	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			log.info("Processando callExtrato");
			extratoService.callExtratoUnicred(extratos, parceiroId);
   }

}
