package br.com.mrcontador.file.ofx.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.service.dto.FileDTO;

public class ListOfxDto {
	
	private FileDTO fileDTO;
	private List<OfxDTO> ofxDTOs;
	public FileDTO getFileDTO() {
		return fileDTO;
	}
	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}
	public List<OfxDTO> getOfxDTOs() {
		if(ofxDTOs == null) {
			ofxDTOs = new ArrayList<>();
		}
		return ofxDTOs;
	}
	public void setOfxDTOs(List<OfxDTO> ofxDTOs) {
		this.ofxDTOs = ofxDTOs;
	} 
	
	public void add(OfxDTO ofxDTO) {
		getOfxDTOs().add(ofxDTO);
	}

	
	
}
