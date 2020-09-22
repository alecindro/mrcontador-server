package br.com.mrcontador.service.mapper;

import java.time.LocalDate;
import java.util.List;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.service.dto.FileDTO;

public class ArquivoMapper implements EntityMapper<FileDTO,Arquivo>{

	@Override
	public Arquivo toEntity(FileDTO dto) {
		Arquivo arquivo = new Arquivo();
		arquivo.setDataCadastro(LocalDate.now());
		arquivo.setEtag(dto.geteTag());
		arquivo.setNome(dto.getName());
		arquivo.setNomeOriginal(dto.getOriginalFilename());
		arquivo.setParceiro(dto.getParceiro());
		arquivo.sets3Dir(dto.getS3Dir());
		arquivo.sets3Url(dto.getS3Url());
		arquivo.setTamanho(dto.getSize());
		arquivo.setTipoArquivo(dto.getContentType());
		arquivo.setTipoDoc(dto.getTipoDocumento().getTipoDoc());
		arquivo.setUsuario(dto.getUsuario());
		return arquivo;
	}

	@Override
	public FileDTO toDto(Arquivo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Arquivo> toEntity(List<FileDTO> dtoList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDTO> toDto(List<Arquivo> entityList) {
		// TODO Auto-generated method stub
		return null;
	}

}
