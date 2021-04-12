package br.com.mrcontador.file.extrato.banco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.dto.ListOfxDto;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.service.ExtratoService;

public class OfxBancoDoBrasil extends OfxParserBanco{
	
	protected OfxDTO processBanco(BankAccountDetails bancoDetails) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco(bancoDetails.getBankId());
		dto.setConta(bancoDetails.getAccountNumber());
		dto.setAgencia(bancoDetails.getBranchId());
		dto.setConta(bancoDetails.getAccountNumber());
		return dto;
	}
	
	public void process(ListOfxDto listOfxDto,AggregateUnmarshaller<ResponseEnvelope> unmarshaller, InputStream stream) throws IOException, OFXParseException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.ISO_8859_1));
		ResponseEnvelope envelope = unmarshaller.unmarshal(in);
		BankingResponseMessageSet bankingResponseMessageSet=  (BankingResponseMessageSet) envelope
				.getMessageSet(MessageSetType.banking);
		List<BankStatementResponseTransaction> responses = bankingResponseMessageSet.getStatementResponses();
			for (BankStatementResponseTransaction transaction : responses) {
				OfxDTO dto = process(transaction.getMessage());
				listOfxDto.add(dto);
			}
	}
	
	@Override
	public void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			extratoService.callExtratoBB(extratos,parceiroId);
   }


}
