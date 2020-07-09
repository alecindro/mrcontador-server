package br.com.mrcontador.file.dto;

import java.util.ArrayList;
import java.util.List;

public class PlanoConta {

	private List<PlanoContaDetail> planoContaDetails;
	private String cnpjCliente;
	private String dataPlanoDeContas;

	public void addPlanoContaDetail(PlanoContaDetail planoContaDetail) {
		getPlanoContaDetails().add(planoContaDetail);
	}

	public List<PlanoContaDetail> getPlanoContaDetails() {
		if(planoContaDetails == null) {
			planoContaDetails = new ArrayList<PlanoContaDetail>();
		}
		return planoContaDetails;
	}
	public void setPlanoContaDetails(List<PlanoContaDetail> planoContaDetails) {
		this.planoContaDetails = planoContaDetails;
	}


	public String getCnpjCliente() {
		return cnpjCliente;
	}

	public void setCnpjCliente(String cnpjCliente) {
		this.cnpjCliente = cnpjCliente;
	}

	public String getDataPlanoDeContas() {
		return dataPlanoDeContas;
	}

	public void setDataPlanoDeContas(String dataPlanoDeContas) {
		this.dataPlanoDeContas = dataPlanoDeContas;
	}

}
