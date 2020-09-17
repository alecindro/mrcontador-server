package br.com.mrcontador.file.extrato.dto;

import java.util.ArrayList;
import java.util.List;

public class OfxDTO {
	
	private List<OfxData> dataList;
	private String banco;
	private String conta;
	private String agencia;
	
	
	public List<OfxData> getDataList() {
		if(dataList == null) {
			dataList = new ArrayList<>();
		}
		return dataList;
	}
	public void setDataList(List<OfxData> dataList) {
		this.dataList = dataList;
	}
	@Override
	public String toString() {
		return "OfxDTO [banco=" + banco + ", conta=" + conta + ", agencia=" + agencia + "]";
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	
}
