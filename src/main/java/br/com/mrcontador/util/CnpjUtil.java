package br.com.mrcontador.util;

public class CnpjUtil {
	
	public static String parseCnpj(String cnpj) {
		return cnpj.replaceAll("\\D", "");
	}

}
