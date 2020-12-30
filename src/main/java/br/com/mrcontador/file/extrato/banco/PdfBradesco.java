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

public class PdfBradesco extends PdfParserExtrato {

	private static final String AGENCIA = "Agência";
	private static final String SALDO_ANTERIOR = "SALDO ANTERIOR";
	private static final int DATA_COLUMN = 13;
	private static final int DESCRICAO_COLUMN = 33;
	private static final int DCTO_COLUMN = 88;
	private static final int CRED_COLUMN = 112;
	private static final int DEB_COLUMN = 143;
	private static final int SALDO_COLUMN = 173;
	private static final String BREAK = "Saldos Invest";
	private static final String EXTRATO_MENSAL = "EXTRATO MENSAL";
	private static final String EXTRATO_DE = "EXTRATO DE";
	private int lineHeader = 8;

	private static Logger log = LoggerFactory.getLogger(PdfBradesco.class);

	public PdfBradesco() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void extrasFunctions(ExtratoService service, List<Extrato> extratos, Agenciabancaria agencia) {
		extratos.forEach(ext->service.callExtraFunctionsBradesco(ext.getId()));
	}

	@Override
	protected TipoEntrada getTipo(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
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
		dto.setBanco(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		boolean isExtrato = false;
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			String nextLine = StringUtils.normalizeSpace(lines[i + 1]);
			if (line.contains(AGENCIA)) {
				if(MrContadorUtil.containsNumber(nextLine.substring(0, 5).trim())) {
				dto.setAgencia(nextLine.substring(0, 5).trim());
				dto.setConta(StringUtils.substringBefore(nextLine.substring(7, nextLine.length()).trim(), StringUtils.SPACE));
				}else{
					nextLine = StringUtils.normalizeSpace(lines[i + 2]);
					dto.setAgencia(nextLine.substring(0, 5).trim());
					dto.setConta(StringUtils.substringBefore(nextLine.substring(7, nextLine.length()).trim(), StringUtils.SPACE));
				}
			}
			if(line.toUpperCase().contains(EXTRATO_MENSAL)) {
				isExtrato = true;
			}
			if(line.toUpperCase().contains(EXTRATO_DE) & !isExtrato) {
				isExtrato = true;
				this.lineHeader = 6;
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
		Date lastDate = null;
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			if(StringUtils.normalizeSpace(line).toUpperCase().contains(EXTRATO_MENSAL)) {
				i = i +5;
				line = lines[i];
			}
			PdfData data = new PdfData();
			if(StringUtils.normalizeSpace(line).contains(BREAK)) {
				break;
			}
			if (StringUtils.normalizeSpace(line).contains(SALDO_ANTERIOR)) {
				continue;
			}
			if (StringUtils.deleteWhitespace(line).isEmpty()) {
				continue;
			}
			if (!StringUtils.isNumeric(StringUtils.normalizeSpace(StringUtils.remove(StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN), "/")))
					&& !StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN).isBlank()) {
				continue;
			}
			String actualDate = StringUtils.normalizeSpace(StringUtils.substring(line, DATA_COLUMN, DESCRICAO_COLUMN));
			if (StringUtils.isNumeric(StringUtils.remove(actualDate, "/"))) {
				lastDate = Date.valueOf(LocalDate.parse(StringUtils.trim(actualDate), dateFormatter));
			}
			data.setLancamento(lastDate);
			readLine(line, data, i);
			if (i < lines.length - 1) {				
				StringBuilder infoAdd = new StringBuilder();
				while (StringUtils.substring(lines[i + 1], DCTO_COLUMN, CRED_COLUMN).isBlank() && 
						!StringUtils.normalizeSpace(lines[i+1]).contains(BREAK) &&
						!StringUtils.normalizeSpace(lines[i+1]).contains(SALDO_ANTERIOR) &&
						!StringUtils.deleteWhitespace(lines[i+1]).isEmpty() &&
						!StringUtils.isNumeric(StringUtils.normalizeSpace(StringUtils.remove(StringUtils.substring(lines[i+1], DATA_COLUMN, DESCRICAO_COLUMN), "/"))) &&
						!StringUtils.substring(lines[i+1], DATA_COLUMN, DESCRICAO_COLUMN).contains("Total"))
						 {
					infoAdd.append(StringUtils.normalizeSpace(lines[i + 1]));
					infoAdd.append(StringUtils.SPACE);
					i = i + 1;
					if (i == lines.length) {
						break;
					}
				}
			}
			datas.add(data);
		}
		return datas;
	}

	@Override
	protected int getLineHeader() {
		// TODO Auto-generated method stub
		return lineHeader;
	}
	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			log.info("Processando callExtrato");
			extratoService.callExtratoBradesco(extratos, parceiroId);
   }
}
