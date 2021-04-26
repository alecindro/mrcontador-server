package br.com.mrcontador.file.comprovante;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.banco.ComprovanteInter;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;

public class TesteInterComprovante {



	public static void main(String[] args) throws Exception {
		TesteInterComprovante teste = new TesteInterComprovante();
		teste.teste1("/home/alecindro/Documents/drcontabil/docs/ESTRATEGIA_SAUDE/COMPROVANTES DE PAGAMENTOS/01 A 03.2021");

	}

	private void teste1(String file) throws Exception {
		File _file = new File(file);
		for (File f : _file.listFiles()) {
			FileInputStream inputstream = new FileInputStream(f);
			PDDocument document = PDDocument.load(inputstream);
			Splitter splitter = new Splitter();
			PDFTextStripper stripper = new PdfReaderPreserveSpace();
			List<PDDocument> pages = splitter.split(document);
			Agenciabancaria agencia = new Agenciabancaria();
			agencia.setAgeAgencia("0001");
			agencia.setAgeNumero("85676578");
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			parceiro.setParCnpjcpf("10539433000173");
			agencia.setBanCodigobancario(BancoCodigoBancario.INTER.getCodigoBancario());

			ComprovanteInter cb = new ComprovanteInter();
			for (PDDocument pddocument : pages) {
				try {
					String comprovante = stripper.getText(pddocument);
					cb.parse(comprovante, agencia, parceiro);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			document.close();
		}
	}



}
