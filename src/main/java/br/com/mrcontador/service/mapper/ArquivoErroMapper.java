package br.com.mrcontador.service.mapper;

import java.time.ZonedDateTime;
import java.util.Optional;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.service.dto.FileDTO;

public class ArquivoErroMapper{

	
	public ArquivoErro toEntity(FileDTO dto, Optional<Contador> contador ) {
		ArquivoErro arquivo = new ArquivoErro();
		arquivo.setDataCadastro(ZonedDateTime.now());
		arquivo.setNome(dto.getName());
		arquivo.setNomeOriginal(dto.getOriginalFilename());
		arquivo.setS3Dir(dto.getS3Dir());
		arquivo.setS3Url(dto.getS3Url());
		arquivo.setTamanho(dto.getSize());
		arquivo.setTipoArquivo(dto.getContentType());
		arquivo.setSchema(dto.getContador());
		arquivo.setUsuario(dto.getUsuario());
		arquivo.setParceiroId(dto.getParceiro().getId());
		arquivo.setParceiroName(dto.getParceiro().getParRazaosocial());
		arquivo.setIdAgencia(dto.getIdAgencia());
		arquivo.setValido(true);
		arquivo.setProcessado(false);
		arquivo.setContentType(dto.getContentType());
		if(contador.isPresent()) {
		arquivo.setContador(contador.get());
		}
		arquivo.setTipoDocumento(dto.getTipoDocumento());
		return arquivo;
	}
}
