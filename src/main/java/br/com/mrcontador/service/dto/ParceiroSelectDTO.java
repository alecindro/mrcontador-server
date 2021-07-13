package br.com.mrcontador.service.dto;

import br.com.mrcontador.domain.Parceiro;

public class ParceiroSelectDTO {
	
	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void to(Parceiro parceiro) {
		this.id = parceiro.getId();
		this.name = parceiro.getParRazaosocial();
	}

}
