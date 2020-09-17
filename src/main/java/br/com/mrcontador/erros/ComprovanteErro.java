package br.com.mrcontador.erros;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.service.dto.FileDTO;

public class ComprovanteErro {
	
	private int page;
	private String comprovante;
	private FileDTO fileDTO;
	private Agenciabancaria agencia;
	private String erro;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getComprovante() {
		return comprovante;
	}
	public void setComprovante(String comprovante) {
		this.comprovante = comprovante;
	}
	public Agenciabancaria getAgencia() {
		return agencia;
	}
	public void setAgencia(Agenciabancaria agencia) {
		this.agencia = agencia;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	public FileDTO getFileDTO() {
		return fileDTO;
	}
	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}
	@Override
	public String toString() {
		return "ComprovanteErro [page=" + page + ", comprovante=" + comprovante + ", erro=" + erro + "]";
	}
	
	
	

}
