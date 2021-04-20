package br.com.mrcontador.file.extrato.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.mrcontador.file.extrato.TipoEntrada;

public class OfxData {
	
	private String historico;
	private BigDecimal valor;
	private Date lancamento;
	private String controle;
	private String documento;
	private TipoEntrada tipoEntrada;
	private String agenciaOrigem;
	
	public String getHistorico() {
		return historico;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Date getLancamento() {
		return lancamento;
	}
	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}
	public String getControle() {
		return controle;
	}
	public void setControle(String controle) {
		this.controle = controle;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public TipoEntrada getTipoEntrada() {
		return tipoEntrada;
	}
	public void setTipoEntrada(TipoEntrada tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}
	public String getAgenciaOrigem() {
		return agenciaOrigem;
	}
	public void setAgenciaOrigem(String agenciaOrigem) {
		this.agenciaOrigem = agenciaOrigem;
	}
	@Override
	public String toString() {
		return "OfxData [historico=" + historico + ", valor=" + valor + ", lancamento=" + lancamento + ", controle="
				+ controle + ", documento=" + documento + ", tipoEntrada=" + tipoEntrada + ", agenciaOrigem="
				+ agenciaOrigem + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((historico == null) ? 0 : historico.hashCode());
		result = prime * result + ((lancamento == null) ? 0 : lancamento.hashCode());
		result = prime * result + ((tipoEntrada == null) ? 0 : tipoEntrada.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OfxData other = (OfxData) obj;
		if (historico == null) {
			if (other.historico != null)
				return false;
		} else if (!historico.equals(other.historico))
			return false;
		if (lancamento == null) {
			if (other.lancamento != null)
				return false;
		} else if (!lancamento.equals(other.lancamento))
			return false;
		if (tipoEntrada != other.tipoEntrada)
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
	
	
	
	
}
