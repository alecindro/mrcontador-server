package br.com.mrcontador.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.TipoAgencia;
import br.com.mrcontador.repository.AgenciabancariaRepository;
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.repository.ComprovanteRepository.ComprovanteStats;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.repository.ExtratoRepository.ExtratoStats;
import br.com.mrcontador.repository.InteligentRepository;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;
import br.com.mrcontador.repository.NotafiscalRepository;
import br.com.mrcontador.service.dto.StatsDTO;

@Service
public class StatsService {

	@Autowired
	private InteligentRepository inteligentRepo;
	@Autowired
	private ComprovanteRepository comprovanteRepo;
	@Autowired
	private ExtratoRepository extratoRepo;
	@Autowired
	private NotafiscalRepository notafiscalRepo;
	@Autowired
	private AgenciabancariaRepository agenciabancariaRepository;

	public StatsDTO getStats(Parceiro parceiro) {
		List<Agenciabancaria> agencias = agenciabancariaRepository.findByParceiroAndTipoAgencia(parceiro,
				TipoAgencia.CONTA);
		StatsDTO statsDTO = new StatsDTO();
		statsDTO.setInteligentStats(getInteligentStats(parceiro, agencias));
		statsDTO.setComprovanteStats(getComprovanteStats(parceiro, agencias));
		statsDTO.setExtratoStats(getExtratoStats(parceiro, agencias));
		statsDTO.setNfsStats(notafiscalRepo.getNotaFiscalStats(parceiro.getId()));
		statsDTO.setInteligentCount(
				statsDTO.getInteligentStats().isEmpty() ? 0 : statsDTO.getInteligentStats().get(0).getTotal());
		statsDTO.setExtratoCount(
				statsDTO.getExtratoStats().isEmpty() ? 0 : statsDTO.getExtratoStats().get(0).getTotal());
		statsDTO.setComprovanteCount(
				statsDTO.getComprovanteStats().isEmpty() ? 0 : statsDTO.getComprovanteStats().get(0).getTotal());
		Optional<Long> nfCount = getNfCount(parceiro);
		statsDTO.setNotafiscalCount(nfCount.isPresent() ? nfCount.get() : 0);
		return statsDTO;
	}

	public List<InteligentStats> getInteligentStats(Parceiro parceiro) {
		List<Agenciabancaria> agencias = agenciabancariaRepository.findByParceiroAndTipoAgencia(parceiro,
				TipoAgencia.CONTA);
		return getInteligentStats(parceiro, agencias);
	}

	private List<InteligentStats> getInteligentStats(Parceiro parceiro, List<Agenciabancaria> agencias) {
		List<InteligentStats> stats = new ArrayList<>();
		agencias.forEach(agencia -> {
			stats.addAll(inteligentRepo.getInteligentStats(parceiro.getId(), agencia.getId()));
		});
		return stats;
	}

	private List<ComprovanteStats> getComprovanteStats(Parceiro parceiro, List<Agenciabancaria> agencias) {
		List<ComprovanteStats> stats = new ArrayList<>();
		agencias.forEach(agencia -> {
			stats.addAll(comprovanteRepo.getComprovanteStats(parceiro.getId(), agencia.getId()));
		});
		return stats;
	}

	private List<ExtratoStats> getExtratoStats(Parceiro parceiro, List<Agenciabancaria> agencias) {
		List<ExtratoStats> stats = new ArrayList<>();
		agencias.forEach(agencia -> {
			stats.addAll(extratoRepo.getExtratoStats(parceiro.getId(), agencia.getId()));
		});
		return stats;
	}

	private Optional<Long> getNfCount(Parceiro parceiro) {
		return notafiscalRepo.getTotal(parceiro.getId());
	}

}
