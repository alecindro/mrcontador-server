package br.com.mrcontador.service.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
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
	
	public List<Comprovante> toEntity(List<DiffValue> diffValues, Agenciabancaria agenciabancaria, Parceiro parceiro){
		List<Comprovante> comprovantes = new ArrayList<>();
		for(DiffValue diff : diffValues) {
			Comprovante comprovante = new Comprovante();
			comprovante.setAgenciabancaria(agenciabancaria);
			comprovante.setParceiro(parceiro);
			
		}
		return null;
	}
	
	private void validateAgencia(String agencia, String conta, Agenciabancaria agenciabancaria) {
		
	}

}
