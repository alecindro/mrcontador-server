package br.com.mrcontador.file;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;

public class TestTika {
	//planoconta_2020-07-11T19%3A01%3A42.709967.pdf
	
	public static void main(final String[] args) throws IOException {
	/*	LocalDateTime z = LocalDateTime.now();
		ContentType tupe = new ContentType("application/pdf");
		tupe.getBaseType();
		tupe.getPrimaryType();
		tupe.getSubType();
		com.google.common.net.MediaType media = com.google.common.net.MediaType.parse("application/pdf");
		String value = z.getYear()+"_"+z.getMonthValue()+"_"+z.getDayOfMonth()+"_"+z.getHour()+"_"+z.getMinute()+"_"+z.getSecond();
		System.out.println(value);*/
		test();
	}
	
	public static void test() throws IOException{
		 
	      File file = new File("/home/alecindro/Documents/drcontabil/docs/caixa.ofx");
	      Tika tika = new Tika();
	      
	      //detecting the file type using detect method
	      String filetype = tika.detect(file);
	      System.out.println(filetype);
	}
}
