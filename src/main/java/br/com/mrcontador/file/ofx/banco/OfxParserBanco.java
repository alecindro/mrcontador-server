package br.com.mrcontador.file.ofx.banco;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.common.Transaction;

import br.com.mrcontador.file.TipoEntrada;
import br.com.mrcontador.file.ofx.OfxParser;
import br.com.mrcontador.file.ofx.dto.OfxDTO;
import br.com.mrcontador.file.ofx.dto.OfxData;

public abstract class OfxParserBanco implements OfxParser {

	public OfxDTO process(BankStatementResponse bankStatementResponse) {
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

}
