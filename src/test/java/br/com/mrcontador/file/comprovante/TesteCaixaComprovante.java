package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;

public class TesteCaixaComprovante {
	
	private static final String usuario = "teste";
	
	public static void main(String[] args) throws Exception {
		TesteCaixaComprovante teste = new TesteCaixaComprovante();
		//teste.teste1();
		try (Stream<Path> filePathStream=Files.walk(Paths.get("/home/alecindro/Documents/drcontabil/docs/comprovantes/caixa"))) {
		    filePathStream.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		            try {
						teste.caixa(filePath.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
		}
	}
	
	private void teste1() throws Exception {
		caixa("/home/alecindro/Documents/drcontabil/docs/comprovantes/caixa3/Nutrifrios Comercial de Alimentos LTDA - ND-1047477-1 - 09.06.2020.pdf");
	}
	
	private void caixa(String file) throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1011");
		agencia.setAgeNumero("842-4");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("01.282.978/0001-56");		
		agencia.setBanCodigobancario(BancoCodigoBancario.CAIXA.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load(file));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		List<FileS3> errors =  p.process(dto, agencia);
		System.out.println("========== ERROS ==================");
		System.out.println("========== "+ file + " ==================");
		for(FileS3 erro : errors) {			
			System.out.println(erro.toString());
		}
	}
	
	private ByteArrayOutputStream load(String folder) throws IOException {
		File initialFile = new File(folder);	
		InputStream stream = new FileInputStream(initialFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		stream.close();
		return baos;
		
	}

}
