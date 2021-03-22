package br.com.mrcontador.export;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.util.MrContadorUtil;

public class ExportDominio implements ExportLancamento{
	

	private String tipoNota = "05";
	private String tipoLayout= "0";
	private int seq = 0;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String IMPORTADO = "IMPORTADO";
	private DecimalFormat df;
	
	public String process(List<Inteligent> inteligents, Agenciabancaria agencia, String codigoEmpresa, String cnpjParceiro) {
		StringBuilder builder = new StringBuilder();
		process(builder, inteligents);
		/*StringBuilder builder = cabecalho(codigoEmpresa, datainicial, dataFinal, tipoNota, tipoLayout, cnpjParceiro);
		inteligents.forEach(inteligent ->{
			processLineHeader(builder, inteligent);
			processLineBody(builder, inteligent, agencia,codigoEmpresa);
			processLineFooter(builder, codigoEmpresa);
			
		});
		processFooter(builder);*/
		return builder.toString();
	}
	
	private StringBuilder cabecalho(String codigoEmpresa, LocalDate dataInicial,LocalDate dataFinal, String tipoNota, String tipoLayout, String cnpjParceiro) {
		StringBuilder builder = new StringBuilder();
		builder.append("01");
		int tamCodEmpresa = 7;
		builder.append(StringUtils.repeat("0", tamCodEmpresa-codigoEmpresa.length()));
		builder.append(codigoEmpresa);
		int tamCnpj = 14;
		builder.append(StringUtils.repeat("0", tamCnpj-cnpjParceiro.length()));
		builder.append(cnpjParceiro);
		builder.append(formatter.format(dataInicial));
		builder.append(formatter.format(dataFinal));
		builder.append("N");
		builder.append(tipoNota);
		builder.append("00000");
		builder.append(tipoLayout);
		builder.append("17");
		builder.append(System.getProperty("line.separator"));
		return builder;
	}
	private void processFirstLine(StringBuilder builder, Inteligent inteligent) {
		builder.append("|");
		builder.append("0000|");
		builder.append(inteligent.getParceiro().getParCnpjcpf());
		builder.append("|");
		builder.append(System.getProperty("line.separator"));	
	}
	
	private void process(StringBuilder builder, List<Inteligent> inteligents) {
		processFirstLine(builder, inteligents.get(0));
		Map<String, List<Inteligent>> mapByNumDoc = inteligents.stream().filter(inteligent -> inteligent.getDebito() != null).collect(Collectors.groupingBy(Inteligent::getNumerodocumento));
		for(String key : mapByNumDoc.keySet()) {
			List<Inteligent> list = mapByNumDoc.get(key);
			builder.append("|6000|");
			builder.append(list.get(0).getTipoInteligent());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
			for(Inteligent _inteligent : list) {
			builder.append("|6100|");
			builder.append(formatter.format(_inteligent.getDatalancamento()));
			builder.append("|");
			builder.append(_inteligent.getConta().getConConta());
			builder.append("|");
			builder.append(MrContadorUtil.toMoney(_inteligent.getDebito()));
			builder.append("|");
			builder.append(_inteligent.getHistoricofinal());
			builder.append("||||");
			builder.append(System.getProperty("line.separator"));
			}
			
		}
	}
	
	
	private void processLineHeader(StringBuilder builder, Inteligent inteligent) {
		builder.append("02");
		seq = seq + 1;
		int tamSeq = 7;
		builder.append(StringUtils.repeat("0", tamSeq-String.valueOf(tamSeq).length()));
		builder.append(tamSeq);
		builder.append(TipoExtratoDominio.find(inteligent).name());
		builder.append(formatter.format(inteligent.getDatalancamento()));
		builder.append(IMPORTADO);
		builder.append(System.getProperty("line.separator"));		
	}
	private void processLineBody(StringBuilder builder, Inteligent inteligent, Agenciabancaria agencia, String codigoEmpresa) {
		builder.append("03");
		seq = seq + 1;
		int tamSeq = 7;
		builder.append(StringUtils.repeat("0", tamSeq-String.valueOf(tamSeq).length()));
		builder.append(tamSeq);
		String contaAgencia = String.valueOf(agencia.getConta().getConConta());
		builder.append(StringUtils.repeat("0", tamSeq-contaAgencia.length()));
		builder.append(contaAgencia);
		String contaPagamento = String.valueOf(inteligent.getConta().getConConta());
		builder.append(StringUtils.repeat("0", tamSeq-contaPagamento.length()));
		builder.append(contaPagamento);
		int tamValor = 15;
		String valor = parseBigDecimal(inteligent.getDebito() != null ? inteligent.getDebito() : inteligent.getCredito());
		builder.append(StringUtils.repeat("0", tamValor-valor.length()));
		builder.append(valor);
		builder.append(StringUtils.repeat("0", tamSeq));
		String historico = StringUtils.left(inteligent.getHistoricofinal(), 512);
		builder.append(historico);		
		builder.append(System.getProperty("line.separator"));
	}
	private void processLineFooter(StringBuilder builder, String codigoEmpresa) {
		int tamSeq = 7;
		builder.append(StringUtils.repeat("0", tamSeq-codigoEmpresa.length()));
		builder.append(codigoEmpresa);
		builder.append(System.getProperty("line.separator"));
	}
	
	private void processFooter(StringBuilder builder) {
		builder.append(StringUtils.repeat("9", 100));
	}
	
	private String parseBigDecimal(BigDecimal value) {
		if(df == null) {
		df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);
		}
		return df.format(value);
	}
	
	

}
