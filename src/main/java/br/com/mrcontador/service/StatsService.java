package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.repository.InteligentRepository;
import br.com.mrcontador.repository.InteligentRepository.StatsCount;
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
	
	public StatsDTO getStats(Long parceiroId) {
		StatsDTO statsDTO = new StatsDTO();
		statsDTO.setInteligentStats(inteligentRepo.getInteligentStats(parceiroId));
		statsDTO.setComprovanteStats(comprovanteRepo.getComprovanteStats(parceiroId));
		statsDTO.setExtratoStats(extratoRepo.getExtratoStats(parceiroId));
		statsDTO.setNfsStats(notafiscalRepo.getNotaFiscalStats(parceiroId));
		List<StatsCount> counts = inteligentRepo.getStatsCount(parceiroId);
		Optional<StatsCount> countInteligent = counts.stream().filter(stats -> stats.getTipo().contentEquals("INTELIGENT")).findFirst();
		Optional<StatsCount> countComprovante = counts.stream().filter(stats -> stats.getTipo().contentEquals("COMPROVANTE")).findFirst();
		Optional<StatsCount> countExtrato = counts.stream().filter(stats -> stats.getTipo().contentEquals("EXTRATO")).findFirst();
		Optional<StatsCount> countNfs = counts.stream().filter(stats -> stats.getTipo().contentEquals("NFS")).findFirst();
		statsDTO.setInteligentCount(countInteligent.isPresent()?countInteligent.get().getTotal():0);
		statsDTO.setExtratoCount(countExtrato.isPresent()?countExtrato.get().getTotal():0);
		statsDTO.setComprovanteCount(countComprovante.isPresent()?countComprovante.get().getTotal():0);
		statsDTO.setNotafiscalCount(countNfs.isPresent()?countNfs.get().getTotal():0);
		return statsDTO;
	}


}
