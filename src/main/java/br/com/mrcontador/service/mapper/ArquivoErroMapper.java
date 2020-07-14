package br.com.mrcontador.service.mapper;

import java.time.ZonedDateTime;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.service.dto.FileDTO;

public class ArquivoErroMapper{

	
	public ArquivoErro toEntity(FileDTO dto) {
		ArquivoErro arquivo = new ArquivoErro();
		arquivo.setDataCadastro(ZonedDateTime.now());
		arquivo.setNome(dto.getName());
		arquivo.setNomeOriginal(dto.getOriginalFilename());
		arquivo.sets3Dir(dto.getS3Dir());
		arquivo.sets3Url(dto.getS3Url());
		arquivo.setTamanho(dto.getSize());
		arquivo.setTipoArquivo(dto.getContentType());
		arquivo.setContador(dto.getContador());
		arquivo.setUsuario(arquivo.getUsuario());
		return arquivo;
	}
}
