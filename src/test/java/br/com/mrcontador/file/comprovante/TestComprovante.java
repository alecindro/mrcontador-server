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
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.BancoCodigoBancario;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestComprovante {
	
	@Autowired
	private ParserComprovanteDefault defaultParser;
	
	@Test
	public void testSicoob() throws FileNotFoundException {
		Banco banco = new Banco();
		banco.setBan_codigobancario(BancoCodigoBancario.SICOOB.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/sicoob.pdf"), banco, null);
		
	}
	
	
	public void testCredCrea() throws FileNotFoundException {
		Banco banco = new Banco();
		banco.setBan_codigobancario(BancoCodigoBancario.CREDCREA.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/credcrea.pdf"), banco, null);
		
	}
	
	
	public void testSantander() throws FileNotFoundException {
		Banco banco = new Banco();
		banco.setBan_codigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander.pdf"), banco, null);
		
	}
	
	public void testBradesco() throws FileNotFoundException {
		Banco banco = new Banco();
		banco.setBan_codigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		defaultParser.process(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco.pdf"), banco, null);
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
