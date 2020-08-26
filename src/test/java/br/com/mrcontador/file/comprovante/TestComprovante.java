package br.com.mrcontador.file.comprovante;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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
import br.com.mrcontador.service.dto.FileS3;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestComprovante {
	
	@Autowired
	private ParserComprovanteDefault defaultParser;
	private static final String usuario = "teste";
	

	
	public void testBradesco() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("0017807");
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("028.733.282/0001-55");	
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/comprovantes.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);		
	}

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
	
	@Test
	public void testSantander2() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1651");
		agencia.setAgeNumero("13-000889");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("24.408.746/0001-05");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander/GerarPDF_04082020105944.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		List<FileS3> errors =  p.process(dto, agencia);
		for(FileS3 erro : errors) {
			System.out.println(erro.toString());
		}
		
	}
	

	public void testItau() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1575");
		agencia.setAgeNumero("12906");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("08.892.611/0001-01");		
		agencia.setBanCodigobancario(BancoCodigoBancario.ITAU.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/itau/Comprovantes_Itaú_Moto_Bombas_01.2020_.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		defaultParser.process(dto, agencia);
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
