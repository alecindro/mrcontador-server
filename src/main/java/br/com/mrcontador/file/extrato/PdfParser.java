package br.com.mrcontador.file.extrato;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.extrato.banco.PdfBancoDoBrasil;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.util.MrContadorUtil;

public abstract class PdfParser extends PdfReaderPreserveSpace{
	
	protected DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	protected Map<String, Header> mapHeader;

	public PdfParser() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public abstract OfxDTO process(List<PDDocument> pages) throws IOException;
	
	protected void parseHeader(String lineHeader) {
		mapHeader = new HashMap<String, PdfBancoDoBrasil.Header>();
		int endOfLine = lineHeader.length() - 1;
		char[] characters = lineHeader.toCharArray();
		Header header = new Header(0);

		boolean space = false;
		boolean first = true;
		for (int i = 0; i < characters.length; i++) {
			if (!Character.isLetter(characters[i])) {
				header.addCharacter(characters[i]);
				continue;
			}
			if (Character.isLetter(characters[i]) && first) {
				header.addCharacter(characters[i]);
				if (i < endOfLine) {
					if (!Character.isLetter(characters[i + 1])) {
						first = false;
					}
				}
				continue;
			}
			if (Character.isLetter(characters[i]) && space) {
				header.addCharacter(characters[i]);
				if (i < endOfLine) {
					if (!Character.isLetter(characters[i + 1])) {
						space = false;
					}
				}
				continue;
			}
			header.setEnd(i - 1);
			mapHeader.put(header.getDescricao().trim(),
					new Header(header.getDescricao(), header.getBegin(), header.getEnd()));
			space = true;
			if (i < endOfLine) {
				if (!Character.isLetter(characters[i + 1])) {
					space = false;
				}
			}
			header = new Header(i);
			header.addCharacter(characters[i]);
		}
		header.setEnd(endOfLine);
		mapHeader.put(header.getDescricao(), header);
	}

	public class Header {
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
			if (this.descricao == null) {
				this.descricao = "";
			}
			this.descricao = this.descricao + value;
		}

	}
	public void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria) {		
		if (agencia != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia(), agencia) && !MrContadorUtil.only9(agencia)) {
				throw new MrContadorException("agencia.notequals");
			}
		}
		if(banco != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getBanCodigobancario(),banco) && !MrContadorUtil.only9(banco)) {				
				throw new MrContadorException("banco.notequals");
			}
		}
		if(conta != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(), conta)) {
				if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia()+agenciaBancaria.getAgeNumero(),conta)&& !MrContadorUtil.only9(conta)){
				throw new MrContadorException("conta.notequals");
				}
			}
		}
		if(!parceiro.getId().equals(agenciaBancaria.getParceiro().getId())) {
			throw new MrContadorException("parceiro.notequals");
		}

	}

}
