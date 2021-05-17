package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Integracao.
 */
@Entity
@Table(name = "integracao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Integracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integrador")
    @Enumerated(EnumType.STRING)
    private Integrador integrador;

    @Column(name = "tipo_integracao")
    @Enumerated(EnumType.STRING)
    private TipoIntegracao tipoIntegracao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "habilitado")
    private Boolean habilitado;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "integracaos", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integrador getIntegrador() {
        return integrador;
    }

    public Integracao integrador(Integrador integrador) {
        this.integrador = integrador;
        return this;
    }

    public void setIntegrador(Integrador integrador) {
        this.integrador = integrador;
    }

    public TipoIntegracao getTipoIntegracao() {
        return tipoIntegracao;
    }

    public Integracao tipoIntegracao(TipoIntegracao tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
        return this;
    }

    public void setTipoIntegracao(TipoIntegracao tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public Integracao dataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Integracao dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean isHabilitado() {
        return habilitado;
    }

    public Integracao habilitado(Boolean habilitado) {
        this.habilitado = habilitado;
        return this;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Integracao parceiro(Parceiro parceiro) {
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
        if (!(o instanceof Integracao)) {
            return false;
        }
        return id != null && id.equals(((Integracao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Integracao{" +
            "id=" + getId() +
            ", integrador='" + getIntegrador() + "'" +
            ", tipoIntegracao='" + getTipoIntegracao() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", habilitado='" + isHabilitado() + "'" +
            "}";
    }
}
