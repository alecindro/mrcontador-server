package br.com.mrcontador.service.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.dto.PlanoContaDetail;
import br.com.mrcontador.util.MrContadorUtil;

public class PlanoContaMapper implements EntityMapper<PlanoContaDetail, Conta> {

	@Override
	public Conta toEntity(PlanoContaDetail dto) {
		Conta conta = new Conta();
		conta.setConGrau(StringUtils.isNumeric(dto.getGrau())? Integer.valueOf(dto.getGrau()):-1);
		conta.setConClassificacao(StringUtils.normalizeSpace(dto.getClassificacao()));
		conta.setConCnpj(MrContadorUtil.removeZerosFromInital(MrContadorUtil.onlyNumbers(StringUtils.normalizeSpace(dto.getCnpj()))));
		if(conta.getConCnpj().length()>14) {
			conta.setConCnpj(StringUtils.reverse(StringUtils.reverse(conta.getConCnpj()).substring(0, 15)));
		}
		if (StringUtils.isNumeric(dto.getCodigo().trim())) {
			conta.setConConta(Integer.valueOf(dto.getCodigo().trim()));
		}	
		conta.setConTipo(StringUtils.normalizeSpace(dto.getT()));
		conta.setConDescricao(StringUtils.substring(StringUtils.normalizeSpace(dto.getDescricao()),0,254));
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
