package br.com.mrcontador.export;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Inteligent;

public abstract class AExportLancamento implements ExportLancamento {

	@Override
	public byte[] process(List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void process(StringBuilder builder, List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		processFirstLine(builder, inteligents.get(0));
		Map<String, List<Inteligent>> debitos = inteligents.stream().filter(i -> i.getExtrato().getExtDebito() != null)
				.collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		Map<String, List<Inteligent>> creditos = inteligents.stream()
				.filter(i -> i.getExtrato().getExtCredito() != null)
				.collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		processDebitos(builder, debitos, contaAgenciaBancaria);
		processCreditos(builder, creditos, contaAgenciaBancaria);
	}

	
	protected abstract void processFirstLine(StringBuilder builder, Inteligent inteligent);
	
	private void processDebitos(StringBuilder builder, Map<String, List<Inteligent>> debitos,
			Conta contaAgenciaBancaria) {
		for (String key : debitos.keySet()) {
			List<Inteligent> list = debitos.get(key);
			String tipoInteligente = list.get(0).getTipoInteligent();
		
			switch (tipoInteligente) {
			case "x":
				processDebitoX(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case "D":
				processDebitoD(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case "C":
				processDebitoC(builder, list, contaAgenciaBancaria.getConConta());
				break;
			default:
				break;
			}
		}
	}
	
	private void processCreditos(StringBuilder builder, Map<String, List<Inteligent>> creditos,
			Conta contaAgenciaBancaria) {
		for (String key : creditos.keySet()) {
			List<Inteligent> list = creditos.get(key);
			String tipoInteligente = list.get(0).getTipoInteligent();
			switch (tipoInteligente) {
			case "x":
				processCreditoX(builder, list, contaAgenciaBancaria.getConConta(), tipoInteligente);
				break;
			case "D":
				processCreditoD(builder, list, contaAgenciaBancaria.getConConta(), tipoInteligente);
				break;
			case "C":
				processCreditoC(builder, list, contaAgenciaBancaria.getConConta(), tipoInteligente);
				break;
			default:
				break;
			}
		}
	}
	
	protected abstract void processDebitoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processDebitoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processDebitoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processCreditoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta,
			String tipoInteligente);
	protected abstract void processCreditoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta,
			String tipoInteligente);
	protected abstract void processCreditoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta,
			String tipoInteligente);

}
