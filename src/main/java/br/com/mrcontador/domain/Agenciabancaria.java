package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Agenciabancaria.
 */
@Entity
@Table(name = "agenciabancaria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agenciabancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age_numero", length = 20)
    private String age_numero;

    @Column(name = "age_digito", length = 20)
    private String age_digito;

    @Column(name = "age_agencia", length = 6)
    private String age_agencia;

    @Column(name = "age_descricao", length = 30)
    private String age_descricao;

    @Column(name = "age_situacao")
    private Boolean age_situacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "agenciabancarias", allowSetters = true)
    private Banco banco;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "agenciabancarias", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAge_numero() {
        return age_numero;
    }

    public Agenciabancaria age_numero(String age_numero) {
        this.age_numero = age_numero;
        return this;
    }

    public void setAge_numero(String age_numero) {
        this.age_numero = age_numero;
    }

    public String getAge_digito() {
        return age_digito;
    }

    public Agenciabancaria age_digito(String age_digito) {
        this.age_digito = age_digito;
        return this;
    }

    public void setAge_digito(String age_digito) {
        this.age_digito = age_digito;
    }

    public String getAge_agencia() {
        return age_agencia;
    }

    public Agenciabancaria age_agencia(String age_agencia) {
        this.age_agencia = age_agencia;
        return this;
    }

    public void setAge_agencia(String age_agencia) {
        this.age_agencia = age_agencia;
    }

    public String getAge_descricao() {
        return age_descricao;
    }

    public Agenciabancaria age_descricao(String age_descricao) {
        this.age_descricao = age_descricao;
        return this;
    }

    public void setAge_descricao(String age_descricao) {
        this.age_descricao = age_descricao;
    }

    public Boolean isAge_situacao() {
        return age_situacao;
    }

    public Agenciabancaria age_situacao(Boolean age_situacao) {
        this.age_situacao = age_situacao;
        return this;
    }

    public void setAge_situacao(Boolean age_situacao) {
        this.age_situacao = age_situacao;
    }

    public Banco getBanco() {
        return banco;
    }

    public Agenciabancaria banco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Agenciabancaria parceiro(Parceiro parceiro) {
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
        if (!(o instanceof Agenciabancaria)) {
            return false;
        }
        return id != null && id.equals(((Agenciabancaria) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agenciabancaria{" +
            "id=" + getId() +
            ", age_numero='" + getAge_numero() + "'" +
            ", age_digito='" + getAge_digito() + "'" +
            ", age_agencia='" + getAge_agencia() + "'" +
            ", age_descricao='" + getAge_descricao() + "'" +
            ", age_situacao='" + isAge_situacao() + "'" +
            "}";
    }
}
