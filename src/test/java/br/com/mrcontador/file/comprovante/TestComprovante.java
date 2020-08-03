package br.com.mrcontador.file.comprovante;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestComprovante {
	
	@Autowired
	private ParserComprovanteDefault defaultParser;
	
	@Test
	public void testSicoob() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.SICOOB.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/sicoob.pdf"), agencia, null);
		
	}
	
	
	public void testCredCrea() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.CREDCREA.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/credcrea.pdf"), agencia, null);
		
	}
	
	
	public void testSantander() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander.pdf"), agencia, null);
		
	}
	
	public void testBradesco() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco.pdf"), agencia, null);
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
