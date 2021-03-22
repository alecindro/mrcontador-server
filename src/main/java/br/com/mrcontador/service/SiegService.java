package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Sieg;
import br.com.mrcontador.repository.SiegRepository;

@Service
public class SiegService {

	@Autowired
	private SiegRepository siegRepository;
	private static String WRAP = "XhY";

	public Sieg save(Sieg sieg) {
		sieg.setApiKey(StringUtils.wrap(sieg.getApiKey(),WRAP));
		return siegRepository.save(sieg);
	}

	public Optional<Sieg> get() {
		return siegRepository.findAll().parallelStream().findFirst();
	}

	public void delete(Long id) {
		siegRepository.deleteById(id);
	}

	public List<Sieg> getActives() {
		return siegRepository.findByActive(true);
	}

}
