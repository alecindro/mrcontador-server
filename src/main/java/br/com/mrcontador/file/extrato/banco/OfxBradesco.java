package br.com.mrcontador.file.extrato.banco;

import java.util.List;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.service.ExtratoService;

public class OfxBradesco extends OfxParserBanco{

	@Override
	public void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId) {
			extratoService.callExtratoBradesco(extratos,parceiroId);
   }
}
