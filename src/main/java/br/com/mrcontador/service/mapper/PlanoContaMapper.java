package br.com.mrcontador.service.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.dto.PlanoContaDetail;
import br.com.mrcontador.util.MrContadorUtil;

public class PlanoContaMapper implements EntityMapper<PlanoContaDetail, Conta> {

	@Override
	public Conta toEntity(PlanoContaDetail dto) {
		Conta conta = new Conta();
		conta.setCon_classificacao(StringUtils.normalizeSpace(dto.getClassificacao()));
		conta.setCon_cnpj(MrContadorUtil.onlyNumbers(StringUtils.normalizeSpace(dto.getCnpj())));
		if (StringUtils.isNumeric(dto.getCodigo().trim())) {
			conta.setCon_conta(Integer.valueOf(dto.getCodigo().trim()));
		}
		if (dto.getGrau() != null && StringUtils.isNumeric(dto.getGrau().trim())) {
			conta.setCon_grau(Integer.valueOf(dto.getGrau().trim()));
		}
		conta.setCon_tipo(StringUtils.normalizeSpace(dto.getT()));
		conta.setCon_descricao(StringUtils.normalizeSpace(dto.getDescricao()));
		return conta;
	}

	@Override
	public PlanoContaDetail toDto(Conta entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Conta> toEntity(List<PlanoContaDetail> dtoList) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Conta> toEntity(List<PlanoContaDetail> dtoList, Parceiro parceiro) {
		List<Conta> contas = new ArrayList<>();
		for(PlanoContaDetail dto : dtoList) {
			Conta conta = toEntity(dto);
			conta.setParceiro(parceiro);
			contas.add(conta);
		}
		return contas;
	}

	@Override
	public List<PlanoContaDetail> toDto(List<Conta> entityList) {
		// TODO Auto-generated method stub
		return null;
	}

}
