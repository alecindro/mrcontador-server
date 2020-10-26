package br.com.mrcontador.file.planoconta;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.file.dto.PlanoContaDetail;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

public class PdfPlanoContaDominio extends PlanoContaPdf implements PdfReader {

	private static final String CLASSIFICACAO = "Classificação";
	private static final String CODIGO = "Código";
	private static final String T = "T";
	private static final String DESCRICAO = "Descrição";
	private static final String CPNJ = "CPNJ";
	private static final String GRAU = "Grau";
	private PlanoConta planoConta;
	private Map<String,Header> mapHeader;
	
	public PdfPlanoContaDominio() throws IOException {
		super();
		
		planoConta = new PlanoConta();
	}


	@Override
	public PlanoConta process(List<PDDocument> pages) throws IOException {
		if(pages.isEmpty()) {
			throw new FileException("error.pdf.empty");
		}
		boolean first = true;
		int numberOfPages = pages.size();
		int numberPage = 1;
		for(PDDocument page : pages) {
			
			String pdfFileInText = super.getText(page);
			String lines[] = pdfFileInText.split("\\n");
			if(numberOfPages == numberPage) {
				lines = Arrays.copyOf(lines, lines.length-1);
			}
			numberPage++;
			
			
		
			if(first) {
				parseHeader(lines);
				first = false;
			}
			parseBody(lines);
		}
		return planoConta;
		
		
	}
	
	private void parseBody(String[] lines) {
		for(int i=4;i<lines.length;i++) {
			String line = lines[i];
			PlanoContaDetail planoContaDetail = new PlanoContaDetail();
			int	maxRowSize = line.length();
			for(String key : mapHeader.keySet()) {
				Header header = mapHeader.get(key);				
				String value = line.substring(header.getBegin(), header.getEnd()>maxRowSize?maxRowSize:header.getEnd()).trim();
				switch (key.trim()) {
				case CLASSIFICACAO:
					planoContaDetail.setClassificacao(value);
					break;
				case CODIGO:
					planoContaDetail.setCodigo(value);
					break;
				case CPNJ:
					value = StringUtils.normalizeSpace(value);
					if(value == "") {
						value = null;
					}
					planoContaDetail.setCnpj(value);
					break;
				case DESCRICAO:
					planoContaDetail.setDescricao(value);
					break;
				case GRAU:
					planoContaDetail.setGrau(value);
					break;
				case T:
					planoContaDetail.setT(value);
					break;
				default:
					break;
				}
			}
			planoConta.addPlanoContaDetail(planoContaDetail);
		}
	}
	
	private void parseHeader(String lines[]) {
		
	
		if (lines != null && lines.length > 5) {
			String line = lines[1];
			String[] lineCnpj = line.trim().split("\\s+");
			if (lineCnpj != null && lineCnpj.length >= 4) {
				planoConta.setCnpjCliente(lineCnpj[1].trim());
				planoConta.setDataPlanoDeContas(lineCnpj[3].trim());
			}
		}
		mapHeader = new HashMap<String, PdfPlanoContaDominio.Header>();
		String _header = lines[3];
		int endOfLine = _header.length() - 1;
		 char[] characters = _header.toCharArray();
		Header header = new Header(0);
		
		boolean space = false;
		boolean first = true;
		for(int i=0;i<characters.length;i++) {
			if(!Character.isLetter(characters[i]) ) {
				header.addCharacter(characters[i]);
				continue;
			}
			if(Character.isLetter(characters[i]) && first) {
				header.addCharacter(characters[i]);
				if(i<endOfLine) {
					if(!Character.isLetter(characters[i+1]) ) {
						first = false;
					}
				}
				continue;
			}
			
			if(Character.isLetter(characters[i]) && space) {
				header.addCharacter(characters[i]);
				if(i<endOfLine) {
					if(!Character.isLetter(characters[i+1]) ) {
						space = false;
					}
				}
				continue;
			}
			header.setEnd(i-1);
			mapHeader.put(header.getDescricao().trim(), new Header(header.getDescricao(), header.getBegin(), header.getEnd()));
			space = true;
			if(i<endOfLine) {
				if(!Character.isLetter(characters[i+1]) ) {
					space = false;
				}
			}
			header = new Header(i);
			header.addCharacter(characters[i]);
		}
		header.setEnd(endOfLine);
		mapHeader.put(header.getDescricao(), header);
	}
	
	
		  
	
	public class Header{
		String descricao;
		int begin;
		int end;
		
		public Header() {
			
		}
		
		public Header(int begin) {
			super();
			this.begin = begin;
		}
		
		public Header(String descricao, int begin, int end) {
			super();
			this.descricao = descricao;
			this.begin = begin;
			this.end = end;
		}
		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public int getBegin() {
			return begin;
		}
		public void setBegin(int begin) {
			this.begin = begin;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		public void addCharacter(char value) {
			if(this.descricao == null) {
				this.descricao = "";
			}
			this.descricao = this.descricao + value;
		}
		
		
		
	}







}
