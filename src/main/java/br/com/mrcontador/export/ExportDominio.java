package br.com.mrcontador.export;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.util.MrContadorUtil;

public class ExportDominio implements ExportLancamento {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public byte[] process(List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		StringBuilder builder = new StringBuilder();
		process(builder, inteligents, contaAgenciaBancaria);
		return builder.toString().toUpperCase().getBytes(Charset.forName("ISO-8859-1"));
	}

	private void processFirstLine(StringBuilder builder, Inteligent inteligent) {
		builder.append("|");
		builder.append("0000|");
		builder.append(inteligent.getParceiro().getParCnpjcpf());
		builder.append("|");
		builder.append(System.getProperty("line.separator"));
	}

	private void process(StringBuilder builder, List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		processFirstLine(builder, inteligents.get(0));
		Map<String, List<Inteligent>> debitos = inteligents.stream().filter(i -> i.getDebito() != null)
				.collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		Map<String, List<Inteligent>> creditos = inteligents.stream()
				.filter(i -> i.getCredito() != null)
				.collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		processDebitos(builder, debitos, contaAgenciaBancaria);
		processCreditos(builder, creditos, contaAgenciaBancaria);
	}

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

	private void processDebitoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6000|");
			builder.append(_inteligent.getTipoInteligent());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			builder.append(conta);
			builder.append("|");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			builder.append("|");
			// builder.append(_inteligent.getNotafiscal() != null ? "53" : "20");
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processDebitoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		Inteligent principal = inteligents.get(0);
		builder.append("|6000|");
		builder.append(principal.getTipoInteligent());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		builder.append("|6100|");
		builder.append(formatter.format(principal.getDatalancamento()));
		builder.append("||");
		builder.append(conta);
		builder.append("|");
		builder.append(MrContadorUtil.toMoneyExportDominio(
				principal.getDebito().signum() < 0 ? principal.getDebito().negate() : principal.getDebito()));
		builder.append("||");
		builder.append(principal.getHistoricofinal());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("||");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			builder.append("|");
			// builder.append(codHistorico(_inteligent));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processDebitoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {	
		builder.append("|6000|");
		builder.append(inteligents.get(0).getTipoInteligent());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			if (_inteligent.getTipoValor().contentEquals("DESCONTO")) {
				builder.append("|");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("|");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getCredito()));
			} else {
				builder.append(_inteligent.getConta().getConConta());
				builder.append("||");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			}
			builder.append("|");
			// builder.append(codHistorico(_inteligent));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
		Inteligent principal = inteligents.get(0);
		builder.append("|6100|");
		builder.append(formatter.format(principal.getDatalancamento()));
		builder.append("||");
		builder.append(conta);
		builder.append("|");
		builder.append(MrContadorUtil.toMoneyExportDominio(
				principal.getDebito().signum() < 0 ? principal.getDebito().negate() : principal.getDebito()));

		builder.append("||");
		builder.append(principal.getHistoricofinal());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
	}

	private void processCreditoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta, String tipoInteligente) {
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6000|");
			builder.append(tipoInteligente);
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));		
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(conta);
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getCredito()));
			builder.append("|");
			// builder.append(_inteligent.getNotafiscal() != null ? "53" : "20");
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processCreditoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta, String tipoInteligente) {
		builder.append("|6000|");
		builder.append(tipoInteligente);
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));		
		Inteligent principal = inteligents.stream().filter(i -> i.getTipoInteligent().contentEquals("PRINCIPAL"))
				.findFirst().get();
		builder.append("|6100|");
		builder.append(formatter.format(principal.getDatalancamento()));
		builder.append("|");
		builder.append(conta);
		builder.append("||");
		builder.append(MrContadorUtil.toMoneyExportDominio(principal.getCredito()));
		builder.append("||");
		builder.append(principal.getHistoricofinal());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			if(_inteligent.getTipoValor().contentEquals("DESCONTO")) {
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("||");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			}else {
				builder.append("||");
				builder.append(_inteligent.getConta().getConConta());
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getCredito()));					
			}
			builder.append("|");
			// builder.append(codHistorico(_inteligent));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processCreditoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta, String tipoInteligente) {
		builder.append("|6000|");
		builder.append(tipoInteligente);
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));		
		Inteligent principal = inteligents.stream().filter(i -> i.getTipoInteligent().contentEquals("PRINCIPAL"))
				.findFirst().get();
		builder.append("|6100|");
		builder.append(formatter.format(principal.getDatalancamento()));
		builder.append("|");
		builder.append(conta);
		builder.append("||");
		builder.append(MrContadorUtil.toMoneyExportDominio(principal.getCredito()));
		builder.append("||");
		builder.append(principal.getHistoricofinal());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("||");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getCredito()));	
			builder.append("|");
			// builder.append(codHistorico(_inteligent));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private String codHistorico(Inteligent inteligent) {
		if (inteligent.getTipoValor() != null) {
			switch (inteligent.getTipoValor()) {
			case "PRINCIPAL":
				return "53";
			case "TAXA":
				return "20";
			default:
				return "";
			}
		}
		return "";
	}
}
