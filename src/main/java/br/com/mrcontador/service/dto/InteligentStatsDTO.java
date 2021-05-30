package br.com.mrcontador.service.dto;

import java.util.List;

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;

public class InteligentStatsDTO {
	private List<InteligentStats> stats;
	private Parceiro parceiro;
	
	public List<InteligentStats> getStats() {
		return stats;
	}
	public void setStats(List<InteligentStats> stats) {
		this.stats = stats;
	}
	public Parceiro getParceiro() {
		return parceiro;
	}
	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
	

}
