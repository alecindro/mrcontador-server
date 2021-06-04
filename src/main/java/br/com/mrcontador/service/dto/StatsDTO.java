package br.com.mrcontador.service.dto;

import java.util.List;

import br.com.mrcontador.repository.ComprovanteRepository.ComprovanteStats;
import br.com.mrcontador.repository.ExtratoRepository.ExtratoStats;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;
import br.com.mrcontador.repository.NotafiscalRepository.NFSStats;

public class StatsDTO {
	
	private Long inteligentCount;
	private Long extratoCount;
	private Long comprovanteCount;
	private Long notafiscalCount;
	private List<InteligentStats> inteligentStats;
	private List<ExtratoStats> extratoStats;
	private List<ComprovanteStats> comprovanteStats;
	private List<NFSStats> nfsStats;
	public Long getInteligentCount() {
		return inteligentCount;
	}
	public void setInteligentCount(Long inteligentCount) {
		this.inteligentCount = inteligentCount;
	}
	public Long getExtratoCount() {
		return extratoCount;
	}
	public void setExtratoCount(Long extratoCount) {
		this.extratoCount = extratoCount;
	}
	public Long getComprovanteCount() {
		return comprovanteCount;
	}
	public void setComprovanteCount(Long comprovanteCount) {
		this.comprovanteCount = comprovanteCount;
	}
	public Long getNotafiscalCount() {
		return notafiscalCount;
	}
	public void setNotafiscalCount(Long notafiscalCount) {
		this.notafiscalCount = notafiscalCount;
	}
	public List<InteligentStats> getInteligentStats() {
		return inteligentStats;
	}
	public void setInteligentStats(List<InteligentStats> inteligentStats) {
		this.inteligentStats = inteligentStats;
	}
	public List<ExtratoStats> getExtratoStats() {
		return extratoStats;
	}
	public void setExtratoStats(List<ExtratoStats> extratoStats) {
		this.extratoStats = extratoStats;
	}
	public List<ComprovanteStats> getComprovanteStats() {
		return comprovanteStats;
	}
	public void setComprovanteStats(List<ComprovanteStats> comprovanteStats) {
		this.comprovanteStats = comprovanteStats;
	}
	public List<NFSStats> getNfsStats() {
		return nfsStats;
	}
	public void setNfsStats(List<NFSStats> nfsStats) {
		this.nfsStats = nfsStats;
	}
	@Override
	public String toString() {
		return "StatsDTO [inteligentCount=" + inteligentCount + ", extratoCount=" + extratoCount + ", comprovanteCount="
				+ comprovanteCount + ", notafiscalCount=" + notafiscalCount + ", inteligentStats=" + inteligentStats
				+ ", extratoStats=" + extratoStats + ", comprovanteStats=" + comprovanteStats + ", nfsStats=" + nfsStats
				+ "]";
	}
	
	
	
}
