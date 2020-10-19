package br.com.mrcontador.export;

import br.com.mrcontador.domain.Inteligent;

public enum TipoExtratoDominio {

	x,V,C,D;
	
	public static TipoExtratoDominio find(Inteligent inteligent) {
		if(inteligent.getDebito()!= null) {
			return TipoExtratoDominio.D;
		}
		if(inteligent.getCredito()!= null) {
			return TipoExtratoDominio.C;
		}
		return x;
	}
}
