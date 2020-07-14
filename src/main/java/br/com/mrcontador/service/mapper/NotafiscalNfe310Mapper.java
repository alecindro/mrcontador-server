package br.com.mrcontador.service.mapper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaInfoDuplicata;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;

public class NotafiscalNfe310Mapper  {

	public List<Notafiscal> toEntity(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, Arquivo arquivo) {
		List<Notafiscal> list = new ArrayList<>();
		for (NFNotaInfoDuplicata duplicata : nfe.getNota().getInfo().getCobranca().getDuplicatas()) {
			Notafiscal nf = new Notafiscal();
			nf.setArquivo(arquivo);
			nf.setParceiro(parceiro);
			nf.setNotCnpj(nfe.getNota().getInfo().getEmitente().getCnpj() != null
					? nfe.getNota().getInfo().getEmitente().getCnpj()
					: nfe.getNota().getInfo().getEmitente().getCpf());
			nf.setNotNumero(nfe.getNota().getInfo().getIdentificacao().getNumeroNota());
			nf.setNotDatasaida(nfe.getNota().getInfo().getIdentificacao().getDataHoraSaidaOuEntrada());
			nf.setNotDescricao(nfe.getNota().getInfo().getIdentificacao().getNaturezaOperacao());
			nf.setNotEmpresa(nfe.getNota().getInfo().getEmitente().getRazaoSocial());
			nf.setNotValornota(new BigDecimal(nfe.getNota().getInfo().getTotal().getIcmsTotal().getValorTotalNFe()));
			nf.setNotParcela(duplicata.getNumeroDuplicata());
			nf.setNotValorparcela(new BigDecimal(duplicata.getValorDuplicata()));
			nf.setNotDataparcela(duplicata.getDataVencimento());
			list.add(nf);
		}
		return list;
	}
	

 
}
