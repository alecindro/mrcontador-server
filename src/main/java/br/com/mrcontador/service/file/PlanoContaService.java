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
	private CnpjClient cnpjClient;
	@Autowired
	private ContaRepository contaRepository;

	private Logger log = LoggerFactory.getLogger(PlanoContaService.class);

	public void save(PlanoConta planoConta, FileDTO dto) {
		Parceiro parceiro = null;
		Optional<Parceiro> oParceiro = findParceiro(planoConta.getCnpjCliente());
		if (oParceiro.isPresent()) {
			parceiro = oParceiro.get();
			if(contaRepository.findFirstByParceiro(parceiro).isPresent()) {
				throw new MrContadorException("error.conta.exists",parceiro.getParCnpjcpf());
			}
		} else {
			PessoaJuridica pessoaJuridica = getPessoa(planoConta.getCnpjCliente());
			parceiro = saveParceiro(pessoaJuridica);
		}
		dto.setParceiro(parceiro);
		PlanoContaMapper mapper = new PlanoContaMapper();
		List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), parceiro);
		contaRepository.saveAll(contas);
		

	}

	public Optional<Parceiro> findParceiro(String cnpj) {
		return parceiroRepository.findByParCnpjcpf(MrContadorUtil.onlyNumbers(cnpj));
	}

	private Parceiro saveParceiro(PessoaJuridica pessoaJuridica) {
		ParceiroPJMapper mapper = new ParceiroPJMapper();
		Parceiro parceiro = mapper.toEntity(pessoaJuridica);
		parceiro.setEnabled(true);
		return parceiroRepository.save(parceiro);
	}

	private PessoaJuridica getPessoa(String cnpj) {
		if (cnpj == null) {
			throw new MrContadorException("planoconta.cnpj.error");
		}
		cnpj = MrContadorUtil.onlyNumbers(cnpj);
		try {
			return cnpjClient.fromRecitaWs(cnpj);
		} catch (Exception e) {
			log.error(e.getMessage());
			PessoaJuridica pessoaJuridica = new PessoaJuridica();
			pessoaJuridica.setCnpj(cnpj);
			return pessoaJuridica;
		}
	}

}
