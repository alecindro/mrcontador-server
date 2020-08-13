package br.com.mrcontador.file.extrato.banco;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.extrato.OfxParser;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.ListOfxDto;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.util.MrContadorUtil;

public abstract class OfxParserBanco implements OfxParser {
	
	@Override
	public void process(ListOfxDto listOfxDto, AggregateUnmarshaller<ResponseEnvelope> unmarshaller, InputStream stream) throws IOException, OFXParseException {
		ResponseEnvelope envelope = unmarshaller.unmarshal(stream);
		BankingResponseMessageSet bankingResponseMessageSet=  (BankingResponseMessageSet) envelope
				.getMessageSet(MessageSetType.banking);
		List<BankStatementResponseTransaction> responses = bankingResponseMessageSet.getStatementResponses();
			for (BankStatementResponseTransaction transaction : responses) {
				OfxDTO dto = process(transaction.getMessage());
				listOfxDto.add(dto);
			}
	}

	protected OfxDTO process(BankStatementResponse bankStatementResponse) {
		OfxDTO dto = processBanco(bankStatementResponse.getAccount());
		dto.setDataList(processDataBanco(bankStatementResponse.getTransactionList().getTransactions()));
		return dto;
	}

	protected OfxDTO processBanco(BankAccountDetails bancoDetails) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(bancoDetails.getBankId());
		dto.setConta(bancoDetails.getAccountNumber());
		dto.setAgencia(bancoDetails.getBranchId());
		if (bancoDetails.getBranchId() == null) {
			if (bancoDetails.getAccountNumber() != null) {
				String[] contaAgencia = bancoDetails.getAccountNumber().split("/");
				dto.setConta(contaAgencia[0]);
				if(contaAgencia.length>1) {
					dto.setAgencia(contaAgencia[1]);
				}
			}
		}
		return dto;
	}

	protected List<OfxData> processDataBanco(List<Transaction> transactions) {
		List<OfxData> list = new ArrayList<OfxData>();
		for (Transaction transaction : transactions) {
			OfxData ofxData = new OfxData();
			ofxData.setControle(transaction.getId());
			ofxData.setDocumento(transaction.getCheckNumber());
			ofxData.setHistorico(transaction.getMemo());
			ofxData.setValor(new BigDecimal(transaction.getAmount()));
			ofxData.setTipoEntrada(TipoEntrada.valueOf(transaction.getTransactionType().toString()));
			ofxData.setLancamento(transaction.getDatePosted());
			list.add(ofxData);
		}
		
		return list;
	}
	
	public void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria) {		
		if (agencia != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia(), agencia) && !MrContadorUtil.only9(agencia)) {
				throw new MrContadorException("agencia.notequals");
			}
		}
		if(banco != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getBanCodigobancario(),banco) && !MrContadorUtil.only9(banco)) {				
				throw new MrContadorException("banco.notequals");
			}
		}
		if(conta != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(), conta)) {
				if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia()+agenciaBancaria.getAgeNumero(),conta)&& !MrContadorUtil.only9(conta)){
				throw new MrContadorException("conta.notequals");
				}
			}
		}
		if(!parceiro.getId().equals(agenciaBancaria.getParceiro().getId())) {
			throw new MrContadorException("parceiro.notequals");
		}

	}

}
