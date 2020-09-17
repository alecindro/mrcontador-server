package br.com.mrcontador.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Authority;
import br.com.mrcontador.repository.AuthorityRepository;

@Service
public class AuthorityService {

	private final AuthorityRepository authorityRepository;

	public AuthorityService(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}
	
	public Optional<Authority> findById(String id){
		return authorityRepository.findById(id);
	}
	
	
}
