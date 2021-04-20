package br.com.mrcontador.export;

import java.util.List;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Inteligent;

public interface ExportLancamento {

	public byte[] process(List<Inteligent> inteligents, Conta contaAgenciaBancaria);
}
