package br.com.mrcontador.export;

import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;

public class ExportLancamentoFactory {
	
	
	public static ExportLancamento get(SistemaPlanoConta sistemaPlanoConta) {
		if(SistemaPlanoConta.DOMINIO_SISTEMAS.equals(sistemaPlanoConta)) {
			return new ExportDominio();
		}
		throw new MrContadorException("sismtea.not.implemented");
	}

		
}
