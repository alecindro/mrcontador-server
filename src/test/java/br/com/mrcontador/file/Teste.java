package br.com.mrcontador.file;

import java.text.MessageFormat;

public class Teste {

	
	public static void main(String[] args) {
		String x = "passe {0}";
		Long value = 323782L;
		System.out.println(String.valueOf(value));
		System.out.println(MessageFormat.format(x, String.valueOf(value)));
	}

}
