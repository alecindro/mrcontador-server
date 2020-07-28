package br.com.mrcontador.service.file;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.client.CnpjClient;
import br.com.mrcontador.client.dto.PessoaJuridica;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.repository.ContaRepository;
import br.com.mrcontador.repository.ParceiroRepository;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.mapper.ParceiroPJMapper;
import br.com.mrcontador.service.mapper.PlanoContaMapper;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class PlanoContaService {

	@Autowired
	private ParceiroRepository parceiroRepository;

	@Autowired
	private ContaRepository contaRepository;

	private Logger log = LoggerFactory.getLogger(PlanoContaService.class);


	


	

}
