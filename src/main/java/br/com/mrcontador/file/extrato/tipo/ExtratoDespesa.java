package br.com.mrcontador.file.extrato.tipo;

public enum ExtratoDespesa {
	
	A("CADASTRO"),
	B1("2ª VIA CARTÃO DÉBITO"),
	B2("2ª via - CARTÃO POUPANÇA"),
	C1("EXCLUSÃO CCF"),
	C2("SUSTAÇÃO REVOGAÇÃO"),
	C3("FOLHACHEQUE"),
	C4("CHEQUE ADMINISTRATIVO"),
	C5("CHEQUE VISADO"),
	D1("SAQUE pessoal"),
	D2("SAQUE terminal"),
	D3("SAQUE Correspondente"),
	E1("DEPOSITO identificado"),
	F1("EXTRATO mês (P)"),
	F2("EXTRATO mês(E)"),
	F3("EXTRATO mês (C)"),
	G1("EXTRATO movimento (P)"),
	G2("EXTRATO movimento (E)"),
	G3("EXTRATO movimento (C)"),
	G4("MICROFILME"),
	H1("DOC/TED pessoal"),
	H2("DOC/TED eletrônico"),
	H3("DOC/TED internet"),
	I1("DOC/TED Agendado(P)"),
	I2("DOC/TED agendado (E)"),
	I3("DOC/TED agendado (I)"),
	J1("TRANSF.RECURSOS(P)"),
	J2("TRANSF.RECURSO (E/I)"),
	J3("ORDEM PAGAMENTO"),
	K1("ADIANT. DEPOSITANTE"),
	L1("Anuidade do Cartão Básico"),
	L2("Anuidade/Nacional"),
	M1("2ª via-CARTÃOCRÉDITO"),
	N1("RETIRADA-País"),
	N2("RETIRADA_Exterior"),
	O1("PAGAMENTOCONTAS"),
	P1("AVAL.EMERG CREDITO"),
	Q1("VENDACÂMBIOespécie"),
	Q2("VENDACÂMBIOcheque"),
	Q3("VENDACÂMBIOprépagoemi"),
	Q4("VENDACÂMBIOprépagorec"),
	R1("COMPRACÂMBIOespécie"),
	R2("COMPRACÂMBIOcheque"),
	R3("COMPRACÂMBIOprépago");
	
	private String descricao;
	
	private ExtratoDespesa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
