package br.com.mrcontador.domain;

import br.com.mrcontador.erros.MrContadorException;

public enum BancoCodigoBancario {

	BB("1"), BRADESCO("237"), CAIXA("104"), CREDCREA("85"), ITAU("341"), SANTANDER("33"), SICOOB("756"),ITAU2("999999999"),
	UNICRED("136"), SICRED("748"), SAFRA("422");
	
	private String codigoBancario;
	
	private BancoCodigoBancario(String codigoBancario) {
		this.codigoBancario = codigoBancario;
	}

	public String getCodigoBancario() {
		return codigoBancario;
	}
	
	public static BancoCodigoBancario find(String codigoBancario) {
		for(BancoCodigoBancario bancoCodigoBancario : BancoCodigoBancario.values()) {
			if(bancoCodigoBancario.getCodigoBancario().equals(codigoBancario)) {
				return bancoCodigoBancario;
			}
		}
		throw new MrContadorException("codigobanco.notfound", codigoBancario);
	}
	
	
}
