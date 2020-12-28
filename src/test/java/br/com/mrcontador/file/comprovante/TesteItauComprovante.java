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
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;

public class TesteItauComprovante {
	
	private static final String usuario = "teste";
	
	public static void main(String[] args) throws Exception {
		TesteItauComprovante teste = new TesteItauComprovante();
		teste.caixa("/home/alecindro/Documents/drcontabil/docs/comprovantes/itau/itau.pdf");
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
		agencia.setAgeAgencia("1575");
		agencia.setAgeNumero("12906");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("08.892.611/0001-01");		
		agencia.setBanCodigobancario(BancoCodigoBancario.ITAU.getCodigoBancario());
		String comprovante = stripper.getText(pages.get(147));
		ComprovanteItau cb = new ComprovanteItau();
		cb.parse(comprovante, agencia, parceiro);
		
		document.close();
	}
	
	private void caixa(String file) throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1575");
		agencia.setAgeNumero("12906");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("08.892.611/0001-01");		
		agencia.setBanCodigobancario(BancoCodigoBancario.ITAU.getCodigoBancario());
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
