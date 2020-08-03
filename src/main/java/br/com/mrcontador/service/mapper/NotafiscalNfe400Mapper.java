package br.com.mrcontador.service.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoParcela;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;


public class NotafiscalNfe400Mapper {

	public List<Notafiscal> toEntity(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, Arquivo arquivo) {
		List<Notafiscal> list = new ArrayList<>();
		if(nfe.getNota().getInfo().getCobranca().getParcelas()==null) {
			list.add(parse(nfe, parceiro, arquivo));
			return list;
		}
		for (NFNotaInfoParcela nfNotaInfoParcela : nfe.getNota().getInfo().getCobranca().getParcelas()) {
			Notafiscal nf = parse(nfe, parceiro, arquivo);	
			nf.setNotParcela(nfNotaInfoParcela.getNumeroParcela());
			nf.setNotValorparcela(new BigDecimal(nfNotaInfoParcela.getValorParcela()));
			nf.setNotDataparcela(nfNotaInfoParcela.getDataVencimento());
			list.add(nf);
		}
		return list;
	}
	
	private Notafiscal parse(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, Arquivo arquivo) {
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
		return nf;
	}
}
