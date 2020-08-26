package br.com.mrcontador.service.dto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.file.TipoDocumento;

public class FileS3 {
	
	private Integer page;
	private ByteArrayOutputStream outputStream;
	private List<Comprovante> comprovantes;
	private FileDTO fileDTO;
	private TipoDocumento tipoDocumento;
	private String messageErro;
	
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(ByteArrayOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public List<Comprovante> getComprovantes() {
		if(comprovantes == null) {
			comprovantes = new ArrayList<>();
		}
		return comprovantes;
	}
	public void setComprovantes(List<Comprovante> comprovantes) {
		this.comprovantes = comprovantes;
	}
	public FileDTO getFileDTO() {
		return fileDTO;
	}
	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getMessageErro() {
		return messageErro;
	}
	public void setMessageErro(String messageErro) {
		this.messageErro = messageErro;
	}
	
	
	
	
	

}
