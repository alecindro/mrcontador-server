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
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.AgenciaException;
import br.com.mrcontador.erros.ExtratoException;
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
	private int value_column;
	private static final int SALDO_COLUMN = 246;
	private static final String CONTAEMPRESARIAL = "Conta:";
	private static final int DATA_EMPRESARIAL_COLUMN = 14;
	private static final int HISTORY_EMPRESARIAL_COLUMN = 36;
	private static final int DCTO_EMPRESARIAL_COLUMN = 133;
	private static final int VALUE_EMPRESARIAL_COLUMN = 152;
	private static final int SALDO_EMPRESARIAL_COLUMN = 169;
	private static final String EXTRATO = "EXTRATO";
	
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
	protected TipoEntrada getTipo(String value) {
		if (MrContadorUtil.isNegative(value)) {
			return TipoEntrada.DEBIT;
		}
		return TipoEntrada.CREDIT;
	}

	@Override
	protected void readLine(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);		
		String date = StringUtils.normalizeSpace(line).split(StringUtils.SPACE)[0];
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(date), dateFormatter)));
		String valor = StringUtils.normalizeSpace(StringUtils.substring(line,value_column)).split(StringUtils.SPACE)[0];
		String[] values = StringUtils.substring(line, 0, value_column).trim().split(StringUtils.SPACE);
		String numDoc = StringUtils.normalizeSpace(values[values.length-1]);
		String historico = StringUtils.normalizeSpace(StringUtils.substringBefore(StringUtils.substringAfter(line, date),numDoc));
		data.setHistorico(historico.trim());
		data.setDocumento(numDoc);
			data.setValor(new BigDecimal(MrContadorUtil.onlyMoney(valor)));
		int signum = data.getValor().signum();
		data.setTipoEntrada(TipoEntrada.CREDIT);
		if (signum == -1) {
			data.setTipoEntrada(TipoEntrada.DEBIT);
		}

	}
	
	private void readLineEmpresarial(String line, PdfData data, int numberRow) {
		log.info("Linha {} ", numberRow);
		String date = StringUtils.normalizeSpace(line).split(StringUtils.SPACE)[0];
		data.setLancamento(Date.valueOf(LocalDate.parse(StringUtils.trim(date), dateFormatter)));
		String valor = StringUtils.normalizeSpace(StringUtils.substring(line,value_column).split(StringUtils.SPACE)[0]);
		String[] values = StringUtils.substring(line, 0, value_column).trim().split(StringUtils.SPACE);
		String numDoc = StringUtils.normalizeSpace(values[values.length-1]);
		String historico = StringUtils.normalizeSpace(StringUtils.substringBefore(StringUtils.substringAfter(line, date),numDoc));
		data.setHistorico(historico.trim());
		data.setDocumento(numDoc);
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
		boolean isExtrato = false;
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		for (int i = 0; i < lineHeader; i++) {
			if(lines[i].toUpperCase().contains("VALOR")) {
				value_column = lines[i].toUpperCase().indexOf("VALOR")-1;
			}
			String line = StringUtils.normalizeSpace(lines[i]);
			if (line.contains(AGENCIA)) {
				dto.setAgencia(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA), CONTA).trim());
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
	
	private OfxDTO parseDataBancoEmpresarial(String[] lines, int lineHeader) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		boolean isExtrato = false;
		for (int i = 0; i < lineHeader; i++) {
			if(lines[i].toUpperCase().contains("VALOR")) {
				value_column = lines[i].toUpperCase().indexOf("VALOR")-1;
			}
			String line = StringUtils.normalizeSpace(lines[i]);
			if (line.contains(AGENCIA)) {
				dto.setAgencia(StringUtils.substringBefore(StringUtils.substringAfter(line, AGENCIA), CONTAEMPRESARIAL).trim());
				dto.setConta(StringUtils.substringAfter(line, CONTAEMPRESARIAL).trim());
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
		/*if(isempresarial) {
			return parseBodyEmpresarial(lines, getLineHeader());
		}*/
		List<OfxData> datas = new ArrayList<>();
		for (int i = lineHeader; i < lines.length; i++) {
			String line = lines[i];
			PdfData data = new PdfData();
			if (StringUtils.normalizeSpace(line).contains(SALDO_ANTERIOR)) {
				continue;
			}
			if(StringUtils.normalizeSpace(line).contains("Saldo de Conta Corrente")) {
				break;
			}
			String _data = StringUtils.normalizeSpace(line).split(StringUtils.SPACE)[0];
			if (!StringUtils.isNumeric(StringUtils
					.normalizeSpace(StringUtils.remove(_data, "/")))
					|| _data.isBlank()) {
				continue;
			}
			if(StringUtils.normalizeSpace(line).toUpperCase().contains("INTERNET BANKING")) {
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
	
	@Override
	protected void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			log.info("Processando callExtrato");
			extratoService.callExtratoSantander(extratos, parceiroId);
   }
	
	public void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria) {	
		if(!parceiro.getId().equals(agenciaBancaria.getParceiro().getId())) {
			throw new ExtratoException("parceiro.notequals", parceiro.getParRazaosocial());
		}
		if (agencia != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia(), agencia) && !MrContadorUtil.only9(agencia)) {
				throw new AgenciaException("agencia.notequals");
			}
		}
		if(banco != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getBanCodigobancario(),banco) && !MrContadorUtil.only9(banco)) {				
				throw new AgenciaException("banco.notequals");
			}
		}
		if(conta != null) {
			String _conta = StringUtils.join(StringUtils.splitByWholeSeparator(conta,"-"));
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(), _conta)) {
				if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia()+agenciaBancaria.getAgeNumero(),conta)&& !MrContadorUtil.only9(conta)){
				throw new AgenciaException("conta.notequals");
				}
			}
		}
	}
	

}
