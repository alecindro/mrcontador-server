package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class PdfSicredi extends PdfParserExtrato{
	
	private static final String AGENCIA = "Cooperativa:";
	private static final String CONTA = "Corrente:";
	private static final String EXTRATO = "EXTRATO";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static Logger log = LoggerFactory.getLogger(PdfSicredi.class);


	public PdfSicredi() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos, Long parceiroId) {
		extratoService.callExtratoSicred(extratos, parceiroId);
		
	}

	@Override
	protected int getLineHeader() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	protected TipoEntrada getTipo(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", line);
		String[] cols = StringUtils.normalizeSpace(line).split(StringUtils.SPACE);
		String dtbalancete = cols[0].trim();
		if(StringUtils.isBlank(dtbalancete) || !MrContadorUtil.isDate(dtbalancete, formatter) || cols.length < 4) {
			return;
		}
		
		String valor = cols[cols.length-2];
		String descricao = StringUtils.substringAfter(line.substring(0, 95), cols[0]);
		
		String documento = StringUtils.substringAfter(StringUtils.substringBefore(line,valor.trim()),descricao);
		
		
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), formatter)));
		if(StringUtils.isBlank(documento)) {
			documento = MrContadorUtil.onlyNumbers(dtbalancete)+"_"+numberRow;
		}
		data.setDocumento(documento.trim());
		data.setHistorico(descricao.trim());
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor.trim())));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}		
	}
	
	
	private void readLineType2(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", line);
		String[] cols = StringUtils.normalizeSpace(line).split(StringUtils.SPACE);
		String dtbalancete = cols[0].trim();
		if(StringUtils.isBlank(dtbalancete) || !MrContadorUtil.isDate(dtbalancete, formatter) || cols.length < 4) {
			return;
		}
		
		String valor = cols[cols.length-2];
		String documento = StringUtils.substringBefore(line.substring(74),valor);
		String descricao = StringUtils.substringAfter(StringUtils.substringBefore(line, documento), cols[0]);
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(dtbalancete), formatter)));
		if(StringUtils.isBlank(documento)) {
			documento = MrContadorUtil.onlyNumbers(dtbalancete)+"_"+numberRow;
		}
		data.setDocumento(documento.trim());
		data.setHistorico(descricao.trim());
		data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor.trim())));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}		
	}

	@Override
	protected OfxDTO parseDataBanco(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SICRED.getCodigoBancario());
		boolean isExtrato = false;
		for (int i = 0; i < lineHeader; i++) {
			String line = StringUtils.normalizeSpace(lines[i]);
			if(line.contains(AGENCIA) && line.contains(CONTA)) {
				dto.setAgencia(MrContadorUtil.onlyNumbers(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA).trim(),StringUtils.SPACE).trim()));
					dto.setConta(StringUtils.substringBefore(StringUtils.substringAfter(line, CONTA).trim(),StringUtils.SPACE));
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
		Set<OfxData> datas = new HashSet<>();
		boolean type2 = true;
		String[] cols = StringUtils.normalizeSpace(lines[0]).split(StringUtils.SPACE);
		if(cols.length==2 && cols[1].toUpperCase().contentEquals("SICREDI") ) {
			type2= false;
		}
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			
			PdfData data = new PdfData();
			if(type2 ) {
			readLineType2(line, data, i);
			}else {
				readLine(line, data, i);
			}
			if(data.getLancamento()!=null) {
				datas.add(data);
			}
			
		}
		return new ArrayList<OfxData>(datas);
	}

}
