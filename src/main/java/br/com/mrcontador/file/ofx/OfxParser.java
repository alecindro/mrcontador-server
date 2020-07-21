package br.com.mrcontador.file.ofx;

import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;

import br.com.mrcontador.file.ofx.dto.OfxDTO;

public interface OfxParser {
	
	public OfxDTO process(BankStatementResponse bankStatementResponse);

}
