package br.com.mrcontador.service.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoParcela;

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.util.MrContadorUtil;


public class NotafiscalNfe400Mapper {
	
	private static final String CFOP_INTERESTADUAL = "6910";
	private static final String CFOP_ESTADUAL = "5910";

	public List<Notafiscal> toEntity(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, boolean isEmitente) {
		List<Notafiscal> list = new ArrayList<>();
		NFTipo nfTipo = nfe.getNota().getInfo().getIdentificacao().getTipo();
		if(nfe.getNota().getInfo().getCobranca() == null || nfe.getNota().getInfo().getCobranca().getParcelas()==null) {
			list.add(parse(nfe, parceiro, nfTipo,isEmitente,false));
			return list;
		}
		for (NFNotaInfoParcela nfNotaInfoParcela : nfe.getNota().getInfo().getCobranca().getParcelas()) {
			Notafiscal nf = parse(nfe, parceiro, nfTipo,isEmitente,true);	
			nf.setNotParcela(nfNotaInfoParcela.getNumeroParcela());
			nf.setNotValorparcela(new BigDecimal(nfNotaInfoParcela.getValorParcela()));
			nf.setNotDataparcela(nfNotaInfoParcela.getDataVencimento());
			nf.setPeriodo(MrContadorUtil.periodo(nf.getNotDataparcela()));
			list.add(nf);
		}
		return list;
	}
	
	private Notafiscal parse(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, NFTipo nfTipo, boolean isEmitente, boolean contemParcela) {
		Notafiscal nf = new Notafiscal();
		nf.setProcessado(false);
		nf.setParceiro(parceiro);
		nf.setTnoCodigo(Integer.valueOf(nfTipo.getCodigo()));
		nf.setNotNumero(nfe.getNota().getInfo().getIdentificacao().getNumeroNota());
		nf.setNotDatasaida(nfe.getNota().getInfo().getIdentificacao().getDataHoraEmissao().toLocalDate());
		if(nf.getNotDatasaida() == null) {
			nf.setNotDatasaida(nfe.getNota().getInfo().getIdentificacao().getDataHoraSaidaOuEntrada().toLocalDate());
		}
		nf.setNotDescricao(nfe.getNota().getInfo().getIdentificacao().getNaturezaOperacao());
		BigDecimal bonificacao = calculateBonificacao(nfe);
		BigDecimal valorNota = new BigDecimal(nfe.getNota().getInfo().getTotal().getIcmsTotal().getValorTotalNFe());
		nf.setNotValornota(valorNota.subtract(bonificacao));
		if(!contemParcela) {
			nf.setNotDataparcela(nf.getNotDatasaida());
			nf.setNotParcela("001");
			nf.setNotValorparcela(nf.getNotValornota());
			nf.setPeriodo(MrContadorUtil.periodo(nf.getNotDataparcela()));
		}
		if(isEmitente) {
			nf.setTnoCodigo(1);
			nf.setNotCnpj(nfe.getNota().getInfo().getDestinatario().getCnpj() != null
					? MrContadorUtil.removeZerosFromInital(MrContadorUtil.onlyNumbers(nfe.getNota().getInfo().getDestinatario().getCnpj()))
					: nfe.getNota().getInfo().getDestinatario().getCpf());
			nf.setNotEmpresa(nfe.getNota().getInfo().getDestinatario().getRazaoSocial());
			
		}else{
			nf.setTnoCodigo(0);
			nf.setNotEmpresa(nfe.getNota().getInfo().getEmitente().getRazaoSocial());
			nf.setNotCnpj(nfe.getNota().getInfo().getEmitente().getCnpj() != null
					? MrContadorUtil.removeZerosFromInital(MrContadorUtil.onlyNumbers(nfe.getNota().getInfo().getEmitente().getCnpj()))
					: nfe.getNota().getInfo().getEmitente().getCpf());
				
		}
		
		return nf;
	}
	
	private BigDecimal calculateBonificacao(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfe) {
		BigDecimal result = BigDecimal.ZERO;
		for(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem nfNotaItem : nfe.getNota().getInfo().getItens()) {
			if(nfNotaItem.getProduto()!= null && nfNotaItem.getProduto().getCfop() != null) {
				if(nfNotaItem.getProduto().getCfop().contentEquals(CFOP_ESTADUAL) || nfNotaItem.getProduto().getCfop().contentEquals(CFOP_INTERESTADUAL)){
						result = result.add(new BigDecimal(nfNotaItem.getProduto().getValorTotalBruto()));
				}
			}
		}
		return result;
	}
}
