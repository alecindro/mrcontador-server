package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;

public class TesteSatanderComprovante {
	
	private static final String usuario = "teste";
	
	public static void main(String[] args) throws Exception {
		TesteSatanderComprovante teste = new TesteSatanderComprovante();
		teste.teste1("C:\\Users\\alecindro.castilho\\Documents\\study\\mrcontador\\docs\\wtf sushi\\teste2\\COMPROVANTES\\02.2021\\comprovante (1).pdf");
		//teste.teste1();
		/*try (Stream<Path> filePathStream=Files.walk(Paths.get("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"))) {
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
		}*/
	}
	
	private void teste1(String file) throws Exception {
		FileInputStream inputstream = new FileInputStream(new File(file));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1651");
		agencia.setAgeNumero("13-000889-8");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("24.408.746/0001-05");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		String comprovante = stripper.getText(document);
		ComprovanteSantander cb = new ComprovanteSantander();
		cb.parse(comprovante, agencia, parceiro);
		
		document.close();
	}
	
	
	private void caixa(String file) throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1651");
		agencia.setAgeNumero("13-000889-8");
		Parceiro parceiro = new Parceiro();
		//parceiro.setParCnpjcpf("018.158.321/0001-84");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load(file));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteFacade p = new ParserComprovanteFacade();
		String periodo =  p.process(dto, agencia);
		System.out.println(periodo);
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
