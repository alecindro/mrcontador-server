package br.com.mrcontador.file;

import br.com.mrcontador.service.dto.FileDTO;

public interface FileParser {
	
	public void process(FileDTO dto) throws Exception;

}
