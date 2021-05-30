package br.com.mrcontador.service.dto;

import java.util.List;

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;

public class InteligentStatsDTO {
	private List<InteligentStats> inteligentStats;
	private Parceiro parceiro;
	
	public List<InteligentStats> getInteligentStats() {
		return inteligentStats;
	}
	public void setInteligentStats(List<InteligentStats> inteligentStats) {
		this.inteligentStats = inteligentStats;
	}
	public Parceiro getParceiro() {
		return parceiro;
	}
	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
	

}
