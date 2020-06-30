package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * A Conta.
 */
@Entity
@Table(name = "conta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "con_conta")
    private Integer con_conta;

    @Column(name = "con_classificacao", length = 20)
    private String con_classificacao;

    @Column(name = "con_tipo", length = 1)
    private String con_tipo;

    @Column(name = "con_descricao", length = 60)
    private String con_descricao;

    @Column(name = "con_cnpj", length = 18)
    private String con_cnpj;

    @Column(name = "con_grau")
    private Integer con_grau;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "contas", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCon_conta() {
        return con_conta;
    }

    public Conta con_conta(Integer con_conta) {
        this.con_conta = con_conta;
        return this;
    }

    public void setCon_conta(Integer con_conta) {
        this.con_conta = con_conta;
    }

    public String getCon_classificacao() {
        return con_classificacao;
    }

    public Conta con_classificacao(String con_classificacao) {
        this.con_classificacao = con_classificacao;
        return this;
    }

    public void setCon_classificacao(String con_classificacao) {
        this.con_classificacao = con_classificacao;
    }

    public String getCon_tipo() {
        return con_tipo;
    }

    public Conta con_tipo(String con_tipo) {
        this.con_tipo = con_tipo;
        return this;
    }

    public void setCon_tipo(String con_tipo) {
        this.con_tipo = con_tipo;
    }

    public String getCon_descricao() {
        return con_descricao;
    }

    public Conta con_descricao(String con_descricao) {
        this.con_descricao = con_descricao;
        return this;
    }

    public void setCon_descricao(String con_descricao) {
        this.con_descricao = con_descricao;
    }

    public String getCon_cnpj() {
        return con_cnpj;
    }

    public Conta con_cnpj(String con_cnpj) {
        this.con_cnpj = con_cnpj;
        return this;
    }

    public void setCon_cnpj(String con_cnpj) {
        this.con_cnpj = con_cnpj;
    }

    public Integer getCon_grau() {
        return con_grau;
    }

    public Conta con_grau(Integer con_grau) {
        this.con_grau = con_grau;
        return this;
    }

    public void setCon_grau(Integer con_grau) {
        this.con_grau = con_grau;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Conta parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conta)) {
            return false;
        }
        return id != null && id.equals(((Conta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conta{" +
            "id=" + getId() +
            ", con_conta=" + getCon_conta() +
            ", con_classificacao='" + getCon_classificacao() + "'" +
            ", con_tipo='" + getCon_tipo() + "'" +
            ", con_descricao='" + getCon_descricao() + "'" +
            ", con_cnpj='" + getCon_cnpj() + "'" +
            ", con_grau=" + getCon_grau() +
            "}";
    }
}
