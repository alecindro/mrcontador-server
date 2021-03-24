package br.com.mrcontador.export;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.util.MrContadorUtil;

public class ExportDominio implements ExportLancamento {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public String process(List<Inteligent> inteligents, Conta contaAgenciaBancaria) {
		StringBuilder builder = new StringBuilder();
		process(builder, inteligents, contaAgenciaBancaria);
		return builder.toString();
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
		Map<String, List<Inteligent>> mapByNumDoc = inteligents.stream()
				.collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		for (String key : mapByNumDoc.keySet()) {
			List<Inteligent> list = mapByNumDoc.get(key);
			String tipoInteligente = list.get(0).getTipoInteligent();
			builder.append("|6000|");
			builder.append(tipoInteligente);
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
			switch (tipoInteligente) {
			case "x":
				processX(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case "D":
				processD(builder, list, contaAgenciaBancaria.getConConta());
				break;
			case "C":
				processC(builder, list, contaAgenciaBancaria.getConConta());
				break;
			default:
				break;
			}
		}
	}

	private void processX(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(conta);
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			builder.append(_inteligent.getCredito() != null ? MrContadorUtil.toMoney(_inteligent.getCredito())
					: MrContadorUtil.toMoney(_inteligent.getDebito().negate()));
			builder.append("|");
			builder.append("|");
			builder.append(_inteligent.getNotafiscal() != null ? "53" : "20");
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processC(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		Extrato extrato = inteligents.get(0).getExtrato();
		builder.append("|6100|");
		builder.append(formatter.format(extrato.getExtDatalancamento()));
		builder.append("|");
		builder.append(conta);
		builder.append("|");
		if(extrato.getExtDebito()!=null) {
		builder.append(MrContadorUtil.toMoney(
				extrato.getExtDebito().signum() < 0 ? extrato.getExtDebito().negate() : extrato.getExtDebito()));
		}else {
			builder.append(MrContadorUtil.toMoney(extrato.getExtCredito()));
		}
		builder.append("|");
		builder.append(extrato.getExtHistorico());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			if (_inteligent.getDebito() != null) {
				builder.append(MrContadorUtil.toMoney(_inteligent.getDebito().negate()));
			} else {
				builder.append(MrContadorUtil.toMoney(_inteligent.getCredito()));
			}
			builder.append("|");
			builder.append("|");
			builder.append(codHistorico(_inteligent));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
		}
	}

	private void processD(StringBuilder builder, List<Inteligent> inteligents, Integer conta) {
		Extrato extrato = inteligents.get(0).getExtrato();
		builder.append("|6100|");
		builder.append(formatter.format(extrato.getExtDatalancamento()));
		builder.append("|");
		builder.append(conta);
		builder.append("|");
		if(extrato.getExtDebito()!=null) {
		builder.append(MrContadorUtil.toMoney(
				extrato.getExtDebito().signum() < 0 ? extrato.getExtDebito().negate() : extrato.getExtDebito()));
		}else {
			builder.append(MrContadorUtil.toMoney(extrato.getExtCredito()));
		}
		builder.append("|");
		builder.append(extrato.getExtHistorico());
		builder.append("||||");
		builder.append(System.getProperty("line.separator"));
		for (Inteligent _inteligent : inteligents) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(conta);
			builder.append("|");
			if (_inteligent.getDebito() != null) {
				builder.append(MrContadorUtil.toMoney(_inteligent.getDebito().negate()));
			} else {
				builder.append(MrContadorUtil.toMoney(_inteligent.getCredito()));
			}
			builder.append("|");
			builder.append(codHistorico(_inteligent));
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
