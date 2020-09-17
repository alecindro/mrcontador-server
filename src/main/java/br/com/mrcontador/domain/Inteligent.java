package br.com.mrcontador.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Inteligent.
 */
@Entity
@Table(name = "inteligent")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inteligent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "historico")
    private String historico;

    @Column(name = "historicofinal")
    private String historicofinal;

    @Column(name = "datalancamento")
    private ZonedDateTime datalancamento;

    @Column(name = "associado")
    private Boolean associado;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "debito", precision = 21, scale = 2)
    private BigDecimal debito;

    @Column(name = "credito", precision = 21, scale = 2)
    private BigDecimal credito;

    @Column(name = "datainicio")
    private ZonedDateTime datainicio;

    @Column(name = "datafim")
    private ZonedDateTime datafim;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "beneficiario")
    private String beneficiario;

    @Column(name = "numerocontrole")
    private String numerocontrole;

    @Column(name = "numerodocumento")
    private String numerodocumento;

    @OneToOne
    @JoinColumn(unique = true)
    private Comprovante comprovante;

    @OneToOne
    @JoinColumn(unique = true)
    private Notafiscal notafiscal;

    @OneToOne
    @JoinColumn(unique = true)
    private Notaservico notaservico;
    
    @OneToOne
    @JoinColumn(unique = true)
    private Conta conta;
    
    @OneToOne
    @JoinColumn(unique = true)
    private Extrato extrato;

    @ManyToOne
    @JsonIgnoreProperties(value = "inteligents", allowSetters = true)
    private Parceiro parceiro;

    @ManyToOne
    @JsonIgnoreProperties(value = "inteligents", allowSetters = true)
    private Agenciabancaria agenciabancaria;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistorico() {
        return historico;
    }

    public Inteligent historico(String historico) {
        this.historico = historico;
        return this;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getHistoricofinal() {
        return historicofinal;
    }

    public Inteligent historicofinal(String historicofinal) {
        this.historicofinal = historicofinal;
        return this;
    }

    public void setHistoricofinal(String historicofinal) {
        this.historicofinal = historicofinal;
    }

    public ZonedDateTime getDatalancamento() {
        return datalancamento;
    }

    public Inteligent datalancamento(ZonedDateTime datalancamento) {
        this.datalancamento = datalancamento;
        return this;
    }

    public void setDatalancamento(ZonedDateTime datalancamento) {
        this.datalancamento = datalancamento;
    }

    public Boolean isAssociado() {
        return associado;
    }

    public Inteligent associado(Boolean associado) {
        this.associado = associado;
        return this;
    }

    public void setAssociado(Boolean associado) {
        this.associado = associado;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Inteligent periodo(String periodo) {
        this.periodo = periodo;
        return this;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public Inteligent debito(BigDecimal debito) {
        this.debito = debito;
        return this;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public Inteligent credito(BigDecimal credito) {
        this.credito = credito;
        return this;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public ZonedDateTime getDatainicio() {
        return datainicio;
    }

    public Inteligent datainicio(ZonedDateTime datainicio) {
        this.datainicio = datainicio;
        return this;
    }

    public void setDatainicio(ZonedDateTime datainicio) {
        this.datainicio = datainicio;
    }

    public ZonedDateTime getDatafim() {
        return datafim;
    }

    public Inteligent datafim(ZonedDateTime datafim) {
        this.datafim = datafim;
        return this;
    }

    public void setDatafim(ZonedDateTime datafim) {
        this.datafim = datafim;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Inteligent cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public Inteligent beneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
        return this;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getNumerocontrole() {
        return numerocontrole;
    }

    public Inteligent numerocontrole(String numerocontrole) {
        this.numerocontrole = numerocontrole;
        return this;
    }

    public void setNumerocontrole(String numerocontrole) {
        this.numerocontrole = numerocontrole;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public Inteligent numerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
        return this;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public Comprovante getComprovante() {
        return comprovante;
    }

    public Inteligent comprovante(Comprovante comprovante) {
        this.comprovante = comprovante;
        return this;
    }

    public void setComprovante(Comprovante comprovante) {
        this.comprovante = comprovante;
    }

    public Notafiscal getNotafiscal() {
        return notafiscal;
    }

    public Inteligent notafiscal(Notafiscal notafiscal) {
        this.notafiscal = notafiscal;
        return this;
    }

    public void setNotafiscal(Notafiscal notafiscal) {
        this.notafiscal = notafiscal;
    }

    public Notaservico getNotaservico() {
        return notaservico;
    }

    public Inteligent notaservico(Notaservico notaservico) {
        this.notaservico = notaservico;
        return this;
    }

    public void setNotaservico(Notaservico notaservico) {
        this.notaservico = notaservico;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Inteligent parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }

    public Agenciabancaria getAgenciabancaria() {
        return agenciabancaria;
    }

    public Inteligent agenciabancaria(Agenciabancaria agenciabancaria) {
        this.agenciabancaria = agenciabancaria;
        return this;
    }

    public void setAgenciabancaria(Agenciabancaria agenciabancaria) {
        this.agenciabancaria = agenciabancaria;
    }
    
    public Conta getConta() {
        return conta;
    }

    public Inteligent conta(Conta conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }
    public Extrato getExtrato() {
        return extrato;
    }

    public Inteligent extrato(Extrato extrato) {
        this.extrato = extrato;
        return this;
    }

    public void setExtrato(Extrato extrato) {
        this.extrato = extrato;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inteligent)) {
            return false;
        }
        return id != null && id.equals(((Inteligent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inteligent{" +
            "id=" + getId() +
            ", historico='" + getHistorico() + "'" +
            ", historicofinal='" + getHistoricofinal() + "'" +
            ", datalancamento='" + getDatalancamento() + "'" +
            ", associado='" + isAssociado() + "'" +
            ", periodo='" + getPeriodo() + "'" +
            ", debito=" + getDebito() +
            ", credito=" + getCredito() +
            ", datainicio='" + getDatainicio() + "'" +
            ", datafim='" + getDatafim() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", beneficiario='" + getBeneficiario() + "'" +
            ", numerocontrole='" + getNumerocontrole() + "'" +
            ", numerodocumento='" + getNumerodocumento() + "'" +
            "}";
    }
}
