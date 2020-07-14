package br.com.mrcontador.service.mapper;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.service.dto.FileDTO;

public class ArquivoErroMapper implements EntityMapper<FileDTO,ArquivoErro>{

	@Override
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

	@Override
	public FileDTO toDto(ArquivoErro entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArquivoErro> toEntity(List<FileDTO> dtoList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDTO> toDto(List<ArquivoErro> entityList) {
		// TODO Auto-generated method stub
		return null;
	}

}
