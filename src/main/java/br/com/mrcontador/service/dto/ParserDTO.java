package br.com.mrcontador.service.dto;

import org.apache.tika.metadata.Metadata;

public class ParserDTO {

	private String content;
	private Metadata metadata;

	public ParserDTO() {

	}

	public ParserDTO(String content, Metadata metadata) {
		this.content = content;
		this.metadata = metadata;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
