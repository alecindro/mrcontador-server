package br.com.mrcontador.file.extrato.banco;

import java.util.List;

import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.service.ExtratoService;

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
	
	@Override
	public void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			extratoService.callExtratoItau(extratos,parceiroId);
   }

}
