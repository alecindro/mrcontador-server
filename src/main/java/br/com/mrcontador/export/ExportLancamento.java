package br.com.mrcontador.export;

import java.util.List;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Inteligent;

public interface ExportLancamento {

	public String process(List<Inteligent> inteligents, Agenciabancaria agencia, String codigoEmpresa, String cnpjParceiro);
}
