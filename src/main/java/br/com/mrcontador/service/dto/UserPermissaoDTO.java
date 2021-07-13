package br.com.mrcontador.service.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.domain.PermissaoParceiro;

public class UserPermissaoDTO {

	private UserDTO user;
	private List<PermissaoParceiro> permissaos;
	
	
	public UserPermissaoDTO() {}
	
	public UserPermissaoDTO(UserDTO user, List<PermissaoParceiro> permissaos) {
		super();
		this.user = user;
		this.permissaos = permissaos;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public List<PermissaoParceiro> getPermissaos() {
		if(permissaos == null) {
			permissaos = new ArrayList<>();
		}
		return permissaos;
	}

	public void setPermissaos(List<PermissaoParceiro> permissaos) {
		this.permissaos = permissaos;
	}
	
	
	
	
}
