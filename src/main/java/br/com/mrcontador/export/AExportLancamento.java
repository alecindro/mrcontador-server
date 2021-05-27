package br.com.mrcontador.export;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.file.extrato.tipo.TipoValor;

public abstract class AExportLancamento implements ExportLancamento {

	
	protected abstract void processFirstLine(StringBuilder builder, Inteligent inteligent);
	protected abstract byte[] process(StringBuilder builder);
	protected abstract void processDebitoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processDebitoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processDebitoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processDebitoVarios(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processCreditoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processCreditoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta);
	protected abstract void processCreditoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta);

	
	@Override
	public byte[] process(List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		StringBuilder builder = new StringBuilder();
		process(builder, inteligents, contaAgenciaBancaria);
		return process(builder);
	}
	
	private void process(StringBuilder builder, List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		inteligents.sort(Comparator.comparing(Inteligent::getDatalancamento));
		processFirstLine(builder, inteligents.get(0));
		
		Map<LocalDate,Map<String, List<Inteligent>>> debitos = inteligents.stream().filter(i -> i.getExtrato().getExtDebito() != null && i.getTipoValor() != null)
				.collect(Collectors.groupingBy(Inteligent::getDatalancamento, Collectors.groupingBy(Inteligent::getNumerodocumento)));
		Map<LocalDate,List<Inteligent>> debitosSemTipo = inteligents.stream().filter(i -> i.getExtrato().getExtDebito() != null && i.getTipoValor() == null)
				.collect(Collectors.groupingBy(Inteligent::getDatalancamento));
		Map<LocalDate,Map<String, List<Inteligent>>> creditos = inteligents.stream()
				.filter(i -> i.getExtrato().getExtCredito() != null && i.getTipoValor() != null)
				.collect(Collectors.groupingBy(Inteligent::getDatalancamento, Collectors.groupingBy(Inteligent::getNumerodocumento)));
		Map<LocalDate,List<Inteligent>> creditosSemTipo = inteligents.stream().filter(i -> i.getExtrato().getExtCredito() != null && i.getTipoValor() == null)
				.collect(Collectors.groupingBy(Inteligent::getDatalancamento));
		Set<LocalDate> dates = inteligents.stream().map(Inteligent::getDatalancamento).collect(Collectors.toSet());
		for(LocalDate _date : dates) {
			if(debitos.containsKey(_date)) {
				processDebitos(builder, debitos.get(_date), contaAgenciaBancaria);
			}
			if(debitosSemTipo.containsKey(_date)) {
				processDebitosSemTipoValor(builder, debitosSemTipo.get(_date), contaAgenciaBancaria);
			}
			if(creditos.containsKey(_date)) {
				processDebitos(builder, creditos.get(_date), contaAgenciaBancaria);
			}
			if(creditosSemTipo.containsKey(_date)) {
				processCreditosSemTipoValor(builder, creditosSemTipo.get(_date), contaAgenciaBancaria);
			}
			
		}
	}
	private void processDebitosSemTipoValor(StringBuilder builder, List<Inteligent> list,
			Conta contaAgenciaBancaria) {
			processDebitoX(builder, list, contaAgenciaBancaria.getConConta());
	}
	private void processCreditosSemTipoValor(StringBuilder builder, List<Inteligent> list,
			Conta contaAgenciaBancaria) {
			processCreditoX(builder, list, contaAgenciaBancaria.getConConta());
	}
	
	private void processDebitos(StringBuilder builder, Map<String, List<Inteligent>> debitos,
			Conta contaAgenciaBancaria) {
		for (String key : debitos.keySet()) {
			List<Inteligent> list = debitos.get(key);
			switch (tipoInteligenteDebito(list)) {
			case x:
				processDebitoX(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case D:
				processDebitoD(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case C:
				processDebitoC(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case V:
				processDebitoVarios(builder, list, contaAgenciaBancaria.getConConta());
				break;
			default:
				break;
			}
		}
	}
	
	private TipoExtratoDominio tipoInteligenteDebito(List<Inteligent> list) {
		if(list.size() == 1) {
			return TipoExtratoDominio.x;
		}
		try {
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.DESCONTO.name()) || i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count() >= 2L) {
			return TipoExtratoDominio.V;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.JUROS.name()) || i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count() >= 2L) {
			return TipoExtratoDominio.C;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.DESCONTO.name())).count()>0) {
			return TipoExtratoDominio.D;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.JUROS.name())).count()>0) {
			return TipoExtratoDominio.C;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count()>0) {
			return TipoExtratoDominio.C;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private TipoExtratoDominio tipoInteligenteCredito(List<Inteligent> list) {
		if(list.size() == 1) {
			return TipoExtratoDominio.x;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.DESCONTO.name()) || i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count() >= 2L) {
			return TipoExtratoDominio.V;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.JUROS.name()) || i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count() >= 2L) {
			return TipoExtratoDominio.D;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.DESCONTO.name())).count()>0) {
			return TipoExtratoDominio.C;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.JUROS.name())).count()>0) {
			return TipoExtratoDominio.D;
		}
		if(list.stream().filter(i -> i.getTipoValor().contentEquals(TipoValor.TAXA.name())).count()>0) {
			return TipoExtratoDominio.D;
		}
		return null;
	}
	
	private void processCreditos(StringBuilder builder, Map<String, List<Inteligent>> creditos,
			Conta contaAgenciaBancaria) {
		for (String key : creditos.keySet()) {
			List<Inteligent> list = creditos.get(key);
			switch (tipoInteligenteCredito(list)) {
			case x:
				processCreditoX(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case D:
				processCreditoD(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case C:
				processCreditoC(builder, list, contaAgenciaBancaria.getConConta());
				break;
			default:
				break;
			}
		}
	}
	
}
