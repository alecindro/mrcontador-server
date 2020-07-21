package br.com.mrcontador.service.mapper;

import java.util.List;

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteParseMapper {
	
	//$1 = parceiro
	//$ag = agencia
	//$conta = conta
	//$2 = fornecedor
	//$pagto = data pagamento
	//$valor_doc = valor documento
	//$valor_pag = valor do pagamento
	//$doc = documento
	//$cnpj = cnpj do pagador
	//$cnpj_ben = cnpj do beneficiario
	//$data_venc = data de vencimento
	//$3 = banco
	//$4 = observacao
	
	public List<Comprovante> toEntity(List<DiffValue> diffValues){
		return null;
	}

}
