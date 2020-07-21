package br.com.mrcontador.domain;

import br.com.mrcontador.erros.MrContadorException;

public enum BancoCodigoBancario {

	BB("001"), BRADESCO("0237"), CAIXA("0104"), CREDCREA("085"), ITAU("341"), SANTANDER("033"), SICOOB("756"),
	UNICRED("136");
	
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
