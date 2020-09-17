package br.com.mrcontador.service.mapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.client.dto.AtividadePrincipal;
import br.com.mrcontador.client.dto.AtividadesSecundaria;
import br.com.mrcontador.client.dto.PessoaJuridica;
import br.com.mrcontador.client.dto.Qsa;
import br.com.mrcontador.domain.Atividade;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.Socio;
import br.com.mrcontador.util.MrContadorUtil;

public class ParceiroPJMapper implements EntityMapper<PessoaJuridica,Parceiro> {

	public static final String ATIVIDADE_PRINCIPAL = "Principal";
	public static final String ATIVIDADE_SECUNDARIA = "Secundária";

	@Override
	public PessoaJuridica toDto(Parceiro dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Parceiro toEntity(PessoaJuridica entity) {
		Parceiro parceiro = new Parceiro();
		parceiro.setAbertura(entity.getAbertura());
		toAtividade(entity.getAtividadePrincipal()).forEach(atividade -> parceiro.addAtividade(atividade));
		toAtividadeSecundaria(entity.getAtividadesSecundarias()).forEach(atividade -> parceiro.addAtividade(atividade));
		parceiro.setBairro(entity.getBairro());
		parceiro.setCapitalSocial(entity.getCapitalSocial());
		parceiro.setCep(MrContadorUtil.onlyNumbers(entity.getCep()));
		parceiro.setCidade(entity.getMunicipio());
		parceiro.setComplemento(entity.getComplemento());
		parceiro.setDataSituacao(entity.getDataSituacao());
		parceiro.setDataSituacaoEspecial(entity.getDataSituacaoEspecial());
		parceiro.setEfr(entity.getEfr());
		parceiro.setEmail(entity.getEmail());
		parceiro.setEstado(entity.getUf());
		parceiro.setLogradouro(entity.getLogradouro());
		parceiro.setMotivoSituacao(entity.getMotivoSituacao());
		parceiro.setNaturezaJuridica(entity.getNaturezaJuridica());
		parceiro.setNumero(entity.getNumero());
		parceiro.setParCnpjcpf(MrContadorUtil.onlyNumbers(entity.getCnpj()));
		parceiro.setParDatacadastro(ZonedDateTime.now());
		parceiro.setParDescricao(entity.getFantasia());
		parceiro.setParRazaosocial(entity.getNome());
		parceiro.setParTipopessoa(TipoPessoa.JURIDICA.getValue());
		parceiro.setPorte(entity.getPorte());
		parceiro.setSituacaoEspecial(entity.getSituacaoEspecial());
		toSocio(entity.getQsa()).forEach(socio -> parceiro.addSocio(socio));
		parceiro.setStatus(entity.getStatus());
		parceiro.setTelefone(entity.getTelefone());
		parceiro.setTipo(entity.getTipo());
		parceiro.setUltimaAtualizacao(entity.getUltimaAtualizacao());
		parceiro.setEnabled(true);		
		return parceiro;
	}

	@Override
	public List<PessoaJuridica> toDto(List<Parceiro> dtoList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Parceiro> toEntity(List<PessoaJuridica> entityList) {
		List<Parceiro> parceiros = new ArrayList<Parceiro>();	
		for(PessoaJuridica pj : entityList) {
			parceiros.add(toEntity(pj));
		}
		return parceiros;
	}

	private List<Atividade> toAtividade(List<AtividadePrincipal> principais) {
		List<Atividade> atividades = new ArrayList<>();
		for (AtividadePrincipal principal : principais) {
			Atividade atividade = new Atividade();
			atividade.setCode(principal.getCode());
			atividade.setDescricao(principal.getText());
			atividade.setTipo(ATIVIDADE_PRINCIPAL);
			atividades.add(atividade);
		}
		return atividades;
	}

	private List<Atividade> toAtividadeSecundaria(List<AtividadesSecundaria> secundarias) {
		List<Atividade> atividades = new ArrayList<>();
		for (AtividadesSecundaria secundaria : secundarias) {
			Atividade atividade = new Atividade();
			atividade.setCode(secundaria.getCode());
			atividade.setDescricao(secundaria.getText());
			atividade.setTipo(ATIVIDADE_SECUNDARIA);
			atividades.add(atividade);
		}
		return atividades;
	}

	private List<Socio> toSocio(List<Qsa> qsas) {
		List<Socio> socios = new ArrayList<>();
		for (Qsa qsa : qsas) {
			Socio socio = new Socio();
			socio.setDescricao(qsa.getQual());
			socio.setNome(qsa.getNome());
			socios.add(socio);
		}
		return socios;
	}

}
