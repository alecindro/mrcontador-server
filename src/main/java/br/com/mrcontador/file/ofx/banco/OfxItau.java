package br.com.mrcontador.file.ofx.banco;

import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;

import br.com.mrcontador.file.ofx.dto.OfxDTO;

public class OfxItau extends OfxParserBanco{
	
	@Override
	protected OfxDTO processBanco(BankAccountDetails bancoDetails) {
		OfxDTO dto = new OfxDTO();
		dto.setBanco("341");
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

}
