package br.com.mrcontador.util;

public class MrContadorUtil {

	public static String onlyNumbers(String cnpj) {
		if (cnpj != null) {
			return cnpj.replaceAll("\\D", "");
		}
		return cnpj;
	}

}
