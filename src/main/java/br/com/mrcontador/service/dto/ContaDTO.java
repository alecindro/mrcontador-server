package br.com.mrcontador.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Conta} entity.
 */
public class ContaDTO implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer conConta;

    private String conClassificacao;

    private String conTipo;

    private String conDescricao;

    private String conCnpj;

    private Integer conGrau;


    private Long parceiroId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getConConta() {
		return conConta;
	}

	public void setConConta(Integer conConta) {
		this.conConta = conConta;
	}

	public String getConClassificacao() {
		return conClassificacao;
	}

	public void setConClassificacao(String conClassificacao) {
		this.conClassificacao = conClassificacao;
	}

	public String getConTipo() {
		return conTipo;
	}

	public void setConTipo(String conTipo) {
		this.conTipo = conTipo;
	}

	public String getConDescricao() {
		return conDescricao;
	}

	public void setConDescricao(String conDescricao) {
		this.conDescricao = conDescricao;
	}

	public String getConCnpj() {
		return conCnpj;
	}

	public void setConCnpj(String conCnpj) {
		this.conCnpj = conCnpj;
	}

	public Integer getConGrau() {
		return conGrau;
	}

	public void setConGrau(Integer conGrau) {
		this.conGrau = conGrau;
	}

	public Long getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(Long parceiroId) {
        this.parceiroId = parceiroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaDTO)) {
            return false;
        }

        return id != null && id.equals(((ContaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaDTO{" +
            "id=" + getId() +
            ", con_conta=" + getConConta() +
            ", con_classificacao='" + getConClassificacao() + "'" +
            ", con_tipo='" + getConTipo() + "'" +
            ", con_descricao='" + getConDescricao() + "'" +
            ", con_cnpj='" + getConCnpj() + "'" +
            ", con_grau=" + getConGrau() +
            ", parceiroId=" + getParceiroId() +
            "}";
    }
}
