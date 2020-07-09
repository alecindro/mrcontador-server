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

    private Integer con_conta;

    private String con_classificacao;

    private String con_tipo;

    private String con_descricao;

    private String con_cnpj;

    private Integer con_grau;


    private Long parceiroId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCon_conta() {
        return con_conta;
    }

    public void setCon_conta(Integer con_conta) {
        this.con_conta = con_conta;
    }

    public String getCon_classificacao() {
        return con_classificacao;
    }

    public void setCon_classificacao(String con_classificacao) {
        this.con_classificacao = con_classificacao;
    }

    public String getCon_tipo() {
        return con_tipo;
    }

    public void setCon_tipo(String con_tipo) {
        this.con_tipo = con_tipo;
    }

    public String getCon_descricao() {
        return con_descricao;
    }

    public void setCon_descricao(String con_descricao) {
        this.con_descricao = con_descricao;
    }

    public String getCon_cnpj() {
        return con_cnpj;
    }

    public void setCon_cnpj(String con_cnpj) {
        this.con_cnpj = con_cnpj;
    }

    public Integer getCon_grau() {
        return con_grau;
    }

    public void setCon_grau(Integer con_grau) {
        this.con_grau = con_grau;
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
            ", con_conta=" + getCon_conta() +
            ", con_classificacao='" + getCon_classificacao() + "'" +
            ", con_tipo='" + getCon_tipo() + "'" +
            ", con_descricao='" + getCon_descricao() + "'" +
            ", con_cnpj='" + getCon_cnpj() + "'" +
            ", con_grau=" + getCon_grau() +
            ", parceiroId=" + getParceiroId() +
            "}";
    }
}
