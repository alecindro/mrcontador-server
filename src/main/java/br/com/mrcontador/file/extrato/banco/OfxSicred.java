package br.com.mrcontador.file.extrato.banco;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.util.MrContadorUtil;

public class OfxSicred extends OfxParserBanco{
	
	public void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria) {
		String[] values = conta.split("0");
		String _conta = values[values.length-1];
		String _agencia = values[0];
		if (_agencia != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia(), _agencia) && !MrContadorUtil.only9(_agencia)) {
				throw new MrContadorException("agencia.notequals");
			}
		}
		if(banco != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getBanCodigobancario(),banco) && !MrContadorUtil.only9(banco)) {				
				throw new MrContadorException("banco.notequals");
			}
		}
		if(_conta != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(), _conta)) {
				if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(),_conta)){
				throw new MrContadorException("conta.notequals");
				}
			}
		}
		if(!parceiro.getId().equals(agenciaBancaria.getParceiro().getId())) {
			throw new MrContadorException("parceiro.notequals");
		}

	}

}
