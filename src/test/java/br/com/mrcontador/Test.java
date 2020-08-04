package br.com.mrcontador;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import br.com.mrcontador.util.MrContadorUtil;

public class Test {
	
	
	
	public static void main(String[] args) {
		String t = "CIELO VENDAS D�BITO";
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(t); 
		 
		String utf8EncodedString = StandardCharsets.US_ASCII.decode(buffer).toString();
		System.out.println(utf8EncodedString);
//		String value = "11.234.23423-00001/4234";
	//	System.out.println(value.replaceAll("\\D", ""));
		//System.out.println(MrContadorUtil.removeZerosFromInital("00000013123232"));	
	}

}
