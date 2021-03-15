package br.com.mrcontador.service.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Notafiscal;

public class InteligentNfDTO {

	private List<Inteligent> inteligents;
	private List<Notafiscal> notafiscals;
	
	public List<Inteligent> getInteligents() {
	if(inteligents == null) {
		inteligents = new ArrayList<Inteligent>();
	}
		return inteligents;
	}
	public void setInteligents(List<Inteligent> inteligents) {
		this.inteligents = inteligents;
	}
	public List<Notafiscal> getNotafiscals() {
		if(notafiscals == null) {
			notafiscals = new ArrayList<Notafiscal>();
		}
		return notafiscals;
	}
	public void setNotafiscals(List<Notafiscal> notafiscals) {
		this.notafiscals = notafiscals;
	}
	
	
}
