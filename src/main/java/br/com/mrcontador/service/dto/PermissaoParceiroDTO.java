package br.com.mrcontador.service.dto;

import java.util.List;

import br.com.mrcontador.domain.PermissaoParceiro;

public class PermissaoParceiroDTO {
	
	public List<PermissaoParceiro> permissaos;

	public List<PermissaoParceiro> getPermissaos() {
		return permissaos;
	}

	public void setPermissaos(List<PermissaoParceiro> permissaos) {
		this.permissaos = permissaos;
	}
	
	

}
