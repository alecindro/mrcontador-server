package br.com.mrcontador.export;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.util.MrContadorUtil;

public class ExportDominio extends AExportLancamento {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	protected byte[] process(StringBuilder builder) {
		return builder.toString().toUpperCase().getBytes(Charset.forName("ISO-8859-1"));
	}

	protected void processFirstLine(StringBuilder builder, Inteligent inteligent) {
		builder.append("|");
		builder.append("0000|");
		builder.append(inteligent.getParceiro().getParCnpjcpf());
		builder.append("|");
		builder.append(System.getProperty("line.separator"));
	}

	/*
	 * private void process(StringBuilder builder, List<Inteligent> inteligents,
	 * Conta contaAgenciaBancaria) { processFirstLine(builder, inteligents.get(0));
	 * Map<String, List<Inteligent>> debitos = inteligents.stream().filter(i ->
	 * i.getExtrato().getExtDebito() != null)
	 * .collect(Collectors.groupingBy(Inteligent::getNumerodocumento)); Map<String,
	 * List<Inteligent>> creditos = inteligents.stream() .filter(i ->
	 * i.getExtrato().getExtCredito() != null)
	 * .collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
	 * processDebitos(builder, debitos, contaAgenciaBancaria);
	 * processCreditos(builder, creditos, contaAgenciaBancaria); }
	 */

	protected void processDebitoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6000|");
			builder.append("x");
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

	protected void processDebitoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		Inteligent principal = inteligents.get(0);
		Extrato extrato = principal.getExtrato();
		builder.append("|6000|");
		builder.append("C");
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		builder.append("|6100|");
		builder.append(formatter.format(extrato.getExtDatalancamento()));
		builder.append("||");
		builder.append(conta);
		builder.append("|");
		builder.append(MrContadorUtil.toMoneyExportDominio(
				extrato.getExtDebito().signum() < 0 ? extrato.getExtDebito().negate() : extrato.getExtDebito()));
		builder.append("||");
		builder.append(extrato.getExtHistorico());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("||");
			builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			builder.append("||");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	protected void processDebitoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		builder.append("|6000|");
		builder.append("D");
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			if (_inteligent.getDebito() != null) {
				builder.append("|");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("||");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			} else {
				builder.append("||");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("|");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getCredito()));
			}
			builder.append("||");
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

	protected void processCreditoX(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6000|");
			builder.append("x");
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
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	protected void processCreditoC(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		builder.append("|6000|");
		builder.append("C");
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
			if (_inteligent.getTipoValor().contentEquals("DESCONTO")) {
				builder.append("|");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("||");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			} else {
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

	protected void processCreditoD(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		builder.append("|6000|");
		builder.append("D");
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
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	/*
	 * private String codHistorico(Inteligent inteligent) { if
	 * (inteligent.getTipoValor() != null) { switch (inteligent.getTipoValor()) {
	 * case "PRINCIPAL": return "53"; case "TAXA": return "20"; default: return "";
	 * } } return ""; }
	 */

	@Override
	protected void processDebitoVarios(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		Inteligent principal = inteligents.get(0);
		Extrato extrato = principal.getExtrato();
		builder.append("|6000|");
		builder.append("C");
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		builder.append("|6100|");
		builder.append(formatter.format(extrato.getExtDatalancamento()));
		builder.append("||");
		builder.append(conta);
		builder.append("|");
		builder.append(MrContadorUtil.toMoneyExportDominio(
				extrato.getExtDebito().signum() < 0 ? extrato.getExtDebito().negate() : extrato.getExtDebito()));
		builder.append("||");
		builder.append(extrato.getExtHistorico());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			if (_inteligent.getDebito() != null) {
				builder.append("|");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("||");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));
			} else {
				builder.append("||");
				builder.append(_inteligent.getConta().getConConta());
				builder.append("|");
				builder.append(MrContadorUtil.toMoneyExportDominio(_inteligent.getDebito().negate()));

			}
			builder.append("||");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}

	}
}
