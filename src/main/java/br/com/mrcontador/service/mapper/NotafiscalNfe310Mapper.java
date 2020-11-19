package br.com.mrcontador.service.mapper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;

public class NotafiscalNfe310Mapper  {

	public List<Notafiscal> toEntity(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, boolean isEmitente) {
		List<Notafiscal> list = new ArrayList<>();
		com.fincatto.documentofiscal.nfe310.classes.NFTipo nfTipo = nfe.getNota().getInfo().getIdentificacao().getTipo();
		if(nfe.getNota().getInfo().getCobranca() == null || nfe.getNota().getInfo().getCobranca().getDuplicatas()==null) {
			list.add(parse(nfe, parceiro, nfTipo,isEmitente,false));
			return list;
		}
		for (com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaInfoDuplicata nfNotaInfoParcela : nfe.getNota().getInfo().getCobranca().getDuplicatas()) {
			Notafiscal nf = parse(nfe, parceiro, nfTipo,isEmitente,true);
			nf.setNotParcela(nfNotaInfoParcela.getNumeroDuplicata());
			nf.setNotValorparcela(new BigDecimal(nfNotaInfoParcela.getValorDuplicata()));
			nf.setNotDataparcela(nfNotaInfoParcela.getDataVencimento());
			list.add(nf);
		}
		return list;
	}
	
	private Notafiscal parse(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, com.fincatto.documentofiscal.nfe310.classes.NFTipo nfTipo, boolean isEmitente, boolean contemParcela) {
		Notafiscal nf = new Notafiscal();
		nf.setParceiro(parceiro);
		nf.setNotNumero(nfe.getNota().getInfo().getIdentificacao().getNumeroNota());
		nf.setNotDatasaida(nfe.getNota().getInfo().getIdentificacao().getDataHoraEmissao().toLocalDate());
		nf.setNotDescricao(nfe.getNota().getInfo().getIdentificacao().getNaturezaOperacao());
		nf.setNotValornota(new BigDecimal(nfe.getNota().getInfo().getTotal().getIcmsTotal().getValorTotalNFe()));
		if(!contemParcela) {
			nf.setNotDataparcela(nf.getNotDatasaida());
			nf.setNotParcela("001");
			nf.setNotValorparcela(nf.getNotValorparcela());
		}
		if(isEmitente) {
			nf.setNotCnpj(nfe.getNota().getInfo().getDestinatario().getCnpj() != null
					? nfe.getNota().getInfo().getDestinatario().getCnpj()
					: nfe.getNota().getInfo().getDestinatario().getCpf());
			nf.setNotEmpresa(nfe.getNota().getInfo().getDestinatario().getRazaoSocial());
			nf.setTnoCodigo(1);
		}else{
			nf.setTnoCodigo(0);
			nf.setNotEmpresa(nfe.getNota().getInfo().getEmitente().getRazaoSocial());
			nf.setNotCnpj(nfe.getNota().getInfo().getEmitente().getCnpj() != null
					? nfe.getNota().getInfo().getEmitente().getCnpj()
					: nfe.getNota().getInfo().getEmitente().getCpf());
				
		}
		return nf;
	}
 
}
