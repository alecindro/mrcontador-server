package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A Comprovante.
 */
@Entity
@Table(name = "comprovante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comprovante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "par_codigo")
    private Integer par_codigo;

    @Column(name = "age_codigo")
    private Integer age_codigo;

    @Column(name = "com_cnpj", length = 18)
    private String com_cnpj;

    @Column(name = "com_beneficiario", length = 60)
    private String com_beneficiario;

    @Column(name = "com_documento", length = 25)
    private String com_documento;

    @Column(name = "com_datavencimento")
    private ZonedDateTime com_datavencimento;

    @Column(name = "com_datapagamento")
    private ZonedDateTime com_datapagamento;

    @Column(name = "com_valordocumento", precision = 21, scale = 2)
    private BigDecimal com_valordocumento;

    @Column(name = "com_valorpagamento", precision = 21, scale = 2)
    private BigDecimal com_valorpagamento;

    @Column(name = "com_observacao", length = 90)
    private String com_observacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPar_codigo() {
        return par_codigo;
    }

    public Comprovante par_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
        return this;
    }

    public void setPar_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
    }

    public Integer getAge_codigo() {
        return age_codigo;
    }

    public Comprovante age_codigo(Integer age_codigo) {
        this.age_codigo = age_codigo;
        return this;
    }

    public void setAge_codigo(Integer age_codigo) {
        this.age_codigo = age_codigo;
    }

    public String getCom_cnpj() {
        return com_cnpj;
    }

    public Comprovante com_cnpj(String com_cnpj) {
        this.com_cnpj = com_cnpj;
        return this;
    }

    public void setCom_cnpj(String com_cnpj) {
        this.com_cnpj = com_cnpj;
    }

    public String getCom_beneficiario() {
        return com_beneficiario;
    }

    public Comprovante com_beneficiario(String com_beneficiario) {
        this.com_beneficiario = com_beneficiario;
        return this;
    }

    public void setCom_beneficiario(String com_beneficiario) {
        this.com_beneficiario = com_beneficiario;
    }

    public String getCom_documento() {
        return com_documento;
    }

    public Comprovante com_documento(String com_documento) {
        this.com_documento = com_documento;
        return this;
    }

    public void setCom_documento(String com_documento) {
        this.com_documento = com_documento;
    }

    public ZonedDateTime getCom_datavencimento() {
        return com_datavencimento;
    }

    public Comprovante com_datavencimento(ZonedDateTime com_datavencimento) {
        this.com_datavencimento = com_datavencimento;
        return this;
    }

    public void setCom_datavencimento(ZonedDateTime com_datavencimento) {
        this.com_datavencimento = com_datavencimento;
    }

    public ZonedDateTime getCom_datapagamento() {
        return com_datapagamento;
    }

    public Comprovante com_datapagamento(ZonedDateTime com_datapagamento) {
        this.com_datapagamento = com_datapagamento;
        return this;
    }

    public void setCom_datapagamento(ZonedDateTime com_datapagamento) {
        this.com_datapagamento = com_datapagamento;
    }

    public BigDecimal getCom_valordocumento() {
        return com_valordocumento;
    }

    public Comprovante com_valordocumento(BigDecimal com_valordocumento) {
        this.com_valordocumento = com_valordocumento;
        return this;
    }

    public void setCom_valordocumento(BigDecimal com_valordocumento) {
        this.com_valordocumento = com_valordocumento;
    }

    public BigDecimal getCom_valorpagamento() {
        return com_valorpagamento;
    }

    public Comprovante com_valorpagamento(BigDecimal com_valorpagamento) {
        this.com_valorpagamento = com_valorpagamento;
        return this;
    }

    public void setCom_valorpagamento(BigDecimal com_valorpagamento) {
        this.com_valorpagamento = com_valorpagamento;
    }

    public String getCom_observacao() {
        return com_observacao;
    }

    public Comprovante com_observacao(String com_observacao) {
        this.com_observacao = com_observacao;
        return this;
    }

    public void setCom_observacao(String com_observacao) {
        this.com_observacao = com_observacao;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comprovante)) {
            return false;
        }
        return id != null && id.equals(((Comprovante) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comprovante{" +
            "id=" + getId() +
            ", par_codigo=" + getPar_codigo() +
            ", age_codigo=" + getAge_codigo() +
            ", com_cnpj='" + getCom_cnpj() + "'" +
            ", com_beneficiario='" + getCom_beneficiario() + "'" +
            ", com_documento='" + getCom_documento() + "'" +
            ", com_datavencimento='" + getCom_datavencimento() + "'" +
            ", com_datapagamento='" + getCom_datapagamento() + "'" +
            ", com_valordocumento=" + getCom_valordocumento() +
            ", com_valorpagamento=" + getCom_valorpagamento() +
            ", com_observacao='" + getCom_observacao() + "'" +
            "}";
    }
}
