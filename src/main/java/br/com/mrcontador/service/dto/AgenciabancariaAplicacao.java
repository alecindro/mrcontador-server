package br.com.mrcontador.service.dto;

import java.io.Serializable;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Conta;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Agenciabancaria} entity.
 */
public class AgenciabancariaAplicacao implements Serializable {
    
	
	private static final long serialVersionUID = 1L;
	private Agenciabancaria agenciaBancaria;
    private boolean contemAplicacao;
    private Conta conta;
	public Agenciabancaria getAgenciaBancaria() {
		return agenciaBancaria;
	}
	public void setAgenciaBancaria(Agenciabancaria agenciaBancaria) {
		this.agenciaBancaria = agenciaBancaria;
	}
	public boolean isContemAplicacao() {
		return contemAplicacao;
	}
	public void setContemAplicacao(boolean contemAplicacao) {
		this.contemAplicacao = contemAplicacao;
	}
	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agenciaBancaria == null) ? 0 : agenciaBancaria.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + (contemAplicacao ? 1231 : 1237);
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
		AgenciabancariaAplicacao other = (AgenciabancariaAplicacao) obj;
		if (agenciaBancaria == null) {
			if (other.agenciaBancaria != null)
				return false;
		} else if (!agenciaBancaria.equals(other.agenciaBancaria))
			return false;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (contemAplicacao != other.contemAplicacao)
			return false;
		return true;
	}
    
    
    
}
