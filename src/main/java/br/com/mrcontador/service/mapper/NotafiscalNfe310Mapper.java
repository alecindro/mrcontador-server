package br.com.mrcontador.service.mapper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;

public class NotafiscalNfe310Mapper  {

	public List<Notafiscal> toEntity(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, Arquivo arquivo) {
		List<Notafiscal> list = new ArrayList<>();
		com.fincatto.documentofiscal.nfe310.classes.NFTipo nfTipo = nfe.getNota().getInfo().getIdentificacao().getTipo();
		if(nfe.getNota().getInfo().getCobranca() == null || nfe.getNota().getInfo().getCobranca().getDuplicatas()==null) {
			list.add(parse(nfe, parceiro, arquivo,nfTipo));
			return list;
		}
		for (com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaInfoDuplicata nfNotaInfoParcela : nfe.getNota().getInfo().getCobranca().getDuplicatas()) {
			Notafiscal nf = parse(nfe, parceiro, arquivo,nfTipo);	
			nf.setNotParcela(nfNotaInfoParcela.getNumeroDuplicata());
			nf.setNotValorparcela(new BigDecimal(nfNotaInfoParcela.getValorDuplicata()));
			nf.setNotDataparcela(nfNotaInfoParcela.getDataVencimento());
			list.add(nf);
		}
		return list;
	}
	
	private Notafiscal parse(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfe, Parceiro parceiro, Arquivo arquivo, com.fincatto.documentofiscal.nfe310.classes.NFTipo nfTipo) {
		Notafiscal nf = new Notafiscal();
		nf.setArquivo(arquivo);
		nf.setParceiro(parceiro);
		nf.setTnoCodigo(Integer.valueOf(nfTipo.getCodigo()));
		nf.setNotNumero(nfe.getNota().getInfo().getIdentificacao().getNumeroNota());
		nf.setNotDatasaida(nfe.getNota().getInfo().getIdentificacao().getDataHoraSaidaOuEntrada());
		nf.setNotDescricao(nfe.getNota().getInfo().getIdentificacao().getNaturezaOperacao());
		nf.setNotValornota(new BigDecimal(nfe.getNota().getInfo().getTotal().getIcmsTotal().getValorTotalNFe()));
		if(nfTipo.equals(com.fincatto.documentofiscal.nfe310.classes.NFTipo.SAIDA)) {
			nf.setNotCnpj(nfe.getNota().getInfo().getDestinatario().getCnpj() != null
					? nfe.getNota().getInfo().getDestinatario().getCnpj()
					: nfe.getNota().getInfo().getDestinatario().getCpf());
			nf.setNotEmpresa(nfe.getNota().getInfo().getDestinatario().getRazaoSocial());
			
		}else{
			nf.setNotEmpresa(nfe.getNota().getInfo().getEmitente().getRazaoSocial());
			nf.setNotCnpj(nfe.getNota().getInfo().getEmitente().getCnpj() != null
					? nfe.getNota().getInfo().getEmitente().getCnpj()
					: nfe.getNota().getInfo().getEmitente().getCpf());
				
		}
		return nf;
	}
 
}
