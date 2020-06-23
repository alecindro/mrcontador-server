package br.com.mrcontador;

public class Test {
	
	public static void main(String[] args) {
		String value = "11.234.23423-00001/4234";
		System.out.print(value.replaceAll("\\D", ""));
	}

}
