package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayOutputStream;

public class PPDocumentDTO {
	private String comprovante;
	private ByteArrayOutputStream outstream;
	
	
	
	public PPDocumentDTO(String comprovante, ByteArrayOutputStream outstream) {
		super();
		this.comprovante = comprovante;
		this.outstream = outstream;
	}
	public String getComprovante() {
		return comprovante;
	}
	public void setComprovante(String comprovante) {
		this.comprovante = comprovante;
	}
	public ByteArrayOutputStream getOutstream() {
		return outstream;
	}
	public void setOutstream(ByteArrayOutputStream outstream) {
		this.outstream = outstream;
	}
	
	

}
