package br.com.mrcontador.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.PermissaoParceiro;
import br.com.mrcontador.repository.PermissaoParceiroRepository;

@Service
public class PermissaoParceiroService {

	@Autowired
	private PermissaoParceiroRepository repository;
	
	public List<PermissaoParceiro> findByUsuario(String usuario){
		return repository.findByUsuario(usuario);
	}
}
