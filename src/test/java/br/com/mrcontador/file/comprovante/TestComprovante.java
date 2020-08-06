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
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestComprovante {
	
	@Autowired
	private ParserComprovanteDefault defaultParser;
	private static final String usuario = "teste";

	

	public void testSicoob() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.SICOOB.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");	
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/sicoob.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);		
	}
	
	
	public void testCredCrea() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setBanCodigobancario(BancoCodigoBancario.CREDCREA.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");	
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/credcrea.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);
		
	}
	
	@Test
	public void testSantander() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1512");
		agencia.setAgeNumero("13-002405");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");	
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);
		
	}
	
	
	public void testBradesco() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("128527");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");		
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
