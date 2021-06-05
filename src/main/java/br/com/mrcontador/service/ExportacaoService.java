package br.com.mrcontador.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Exportacao;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.ExportacaoRepository;

@Service
public class ExportacaoService {

	@Autowired
	private ExportacaoRepository repository;
	
	public Exportacao save(Parceiro parceiro, String periodo, String user) {
		Exportacao exportacao = new Exportacao();
		exportacao.setDataCadastro(LocalDate.now());
		exportacao.setParceiro(parceiro);
		exportacao.setPeriodo(periodo);
		exportacao.setUsuario(user);
		return repository.save(exportacao);
	}
}
