package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Inteligent} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.InteligentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inteligents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InteligentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter historico;

    private StringFilter historicofinal;
    
    private StringFilter numerocontrole;
    
    private StringFilter numerodocumento;

    private ZonedDateTimeFilter datalancamento;

    private BooleanFilter associado;

    private StringFilter periodo;

    private BigDecimalFilter debito;

    private BigDecimalFilter credito;

    private ZonedDateTimeFilter datainicio;

    private ZonedDateTimeFilter datafim;

    private StringFilter cnpj;

    private StringFilter beneficiario;

    private LongFilter comprovanteId;

    private LongFilter parceiroId;

    private LongFilter notafiscalId;

    private LongFilter notaservicoId;

    private LongFilter agenciabancariaId;
    
    private LongFilter contaId;
    
    private LongFilter extratoId;

    public InteligentCriteria() {
    }

    public InteligentCriteria(InteligentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.historico = other.historico == null ? null : other.historico.copy();
        this.historicofinal = other.historicofinal == null ? null : other.historicofinal.copy();
        this.numerocontrole = other.numerocontrole == null ? null : other.numerocontrole.copy();
        this.numerodocumento = other.numerodocumento == null ? null : other.numerodocumento.copy();
        this.datalancamento = other.datalancamento == null ? null : other.datalancamento.copy();
        this.associado = other.associado == null ? null : other.associado.copy();
        this.periodo = other.periodo == null ? null : other.periodo.copy();
        this.debito = other.debito == null ? null : other.debito.copy();
        this.credito = other.credito == null ? null : other.credito.copy();
        this.datainicio = other.datainicio == null ? null : other.datainicio.copy();
        this.datafim = other.datafim == null ? null : other.datafim.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.beneficiario = other.beneficiario == null ? null : other.beneficiario.copy();
        this.comprovanteId = other.comprovanteId == null ? null : other.comprovanteId.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.notafiscalId = other.notafiscalId == null ? null : other.notafiscalId.copy();
        this.notaservicoId = other.notaservicoId == null ? null : other.notaservicoId.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.agenciabancariaId = other.agenciabancariaId == null ? null : other.agenciabancariaId.copy();
        this.contaId = other.contaId == null ? null : other.contaId.copy();
        this.extratoId = other.extratoId == null ? null : other.extratoId.copy();
    }

    @Override
    public InteligentCriteria copy() {
        return new InteligentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getHistorico() {
        return historico;
    }

    public void setHistorico(StringFilter historico) {
        this.historico = historico;
    }

    public StringFilter getHistoricofinal() {
        return historicofinal;
    }

    public void setHistoricofinal(StringFilter historicofinal) {
        this.historicofinal = historicofinal;
    }

    public ZonedDateTimeFilter getDatalancamento() {
        return datalancamento;
    }

    public void setDatalancamento(ZonedDateTimeFilter datalancamento) {
        this.datalancamento = datalancamento;
    }

    public BooleanFilter getAssociado() {
        return associado;
    }

    public void setAssociado(BooleanFilter associado) {
        this.associado = associado;
    }

    public StringFilter getPeriodo() {
        return periodo;
    }

    public void setPeriodo(StringFilter periodo) {
        this.periodo = periodo;
    }

    public BigDecimalFilter getDebito() {
        return debito;
    }

    public void setDebito(BigDecimalFilter debito) {
        this.debito = debito;
    }

    public BigDecimalFilter getCredito() {
        return credito;
    }

    public void setCredito(BigDecimalFilter credito) {
        this.credito = credito;
    }

    public ZonedDateTimeFilter getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(ZonedDateTimeFilter datainicio) {
        this.datainicio = datainicio;
    }

    public ZonedDateTimeFilter getDatafim() {
        return datafim;
    }

    public void setDatafim(ZonedDateTimeFilter datafim) {
        this.datafim = datafim;
    }

    public StringFilter getCnpj() {
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(StringFilter beneficiario) {
        this.beneficiario = beneficiario;
    }

    public LongFilter getComprovanteId() {
        return comprovanteId;
    }

    public void setComprovanteId(LongFilter comprovanteId) {
        this.comprovanteId = comprovanteId;
    }

    public LongFilter getNotafiscalId() {
        return notafiscalId;
    }

    public void setNotafiscalId(LongFilter notafiscalId) {
        this.notafiscalId = notafiscalId;
    }

    public LongFilter getNotaservicoId() {
        return notaservicoId;
    }

    public void setNotaservicoId(LongFilter notaservicoId) {
        this.notaservicoId = notaservicoId;
    }

    public LongFilter getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(LongFilter parceiroId) {
        this.parceiroId = parceiroId;
    }

    public LongFilter getAgenciabancariaId() {
        return agenciabancariaId;
    }

    public void setAgenciabancariaId(LongFilter agenciabancariaId) {
        this.agenciabancariaId = agenciabancariaId;
    }
    
    public LongFilter getContaId() {
		return contaId;
	}

	public void setContaId(LongFilter contaId) {
		this.contaId = contaId;
	}

	public LongFilter getExtratoId() {
		return extratoId;
	}

	public void setExtratoId(LongFilter extratoId) {
		this.extratoId = extratoId;
	}

	public StringFilter getNumerocontrole() {
		return numerocontrole;
	}

	public void setNumerocontrole(StringFilter numerocontrole) {
		this.numerocontrole = numerocontrole;
	}

	public StringFilter getNumerodocumento() {
		return numerodocumento;
	}

	public void setNumerodocumento(StringFilter numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InteligentCriteria that = (InteligentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(historico, that.historico) &&
            Objects.equals(historicofinal, that.historicofinal) &&
            Objects.equals(datalancamento, that.datalancamento) &&
            Objects.equals(associado, that.associado) &&
            Objects.equals(numerocontrole, that.numerocontrole) &&
            Objects.equals(numerodocumento, that.numerodocumento) &&
            Objects.equals(periodo, that.periodo) &&
            Objects.equals(debito, that.debito) &&
            Objects.equals(credito, that.credito) &&
            Objects.equals(datainicio, that.datainicio) &&
            Objects.equals(datafim, that.datafim) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(beneficiario, that.beneficiario) &&
            Objects.equals(comprovanteId, that.comprovanteId) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(notafiscalId, that.notafiscalId) &&
            Objects.equals(notaservicoId, that.notaservicoId) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(agenciabancariaId, that.agenciabancariaId) &&
            Objects.equals(contaId, that.contaId) &&
            Objects.equals(extratoId, that.extratoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        historico,
        historicofinal,
        datalancamento,
        associado,
        numerocontrole,
        numerodocumento,
        periodo,
        debito,
        credito,
        datainicio,
        datafim,
        cnpj,
        beneficiario,
        comprovanteId,
        parceiroId,
        notafiscalId,
        notaservicoId,
        parceiroId,
        agenciabancariaId,
        contaId,
        extratoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InteligentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (historico != null ? "historico=" + historico + ", " : "") +
                (historicofinal != null ? "historicofinal=" + historicofinal + ", " : "") +
                (datalancamento != null ? "datalancamento=" + datalancamento + ", " : "") +
                (associado != null ? "associado=" + associado + ", " : "") +
                (numerocontrole != null ? "numerocontrole=" + numerocontrole + ", " : "") +
                (numerodocumento != null ? "numerodocumento=" + numerodocumento + ", " : "") +
                (periodo != null ? "periodo=" + periodo + ", " : "") +
                (debito != null ? "debito=" + debito + ", " : "") +
                (credito != null ? "credito=" + credito + ", " : "") +
                (datainicio != null ? "datainicio=" + datainicio + ", " : "") +
                (datafim != null ? "datafim=" + datafim + ", " : "") +
                (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
                (beneficiario != null ? "beneficiario=" + beneficiario + ", " : "") +
                (comprovanteId != null ? "comprovanteId=" + comprovanteId + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (notafiscalId != null ? "notafiscalId=" + notafiscalId + ", " : "") +
                (notaservicoId != null ? "notaservicoId=" + notaservicoId + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (agenciabancariaId != null ? "agenciabancariaId=" + agenciabancariaId + ", " : "") +
                (contaId != null ? "contaId=" + contaId + ", " : "") +
                (extratoId != null ? "extratoId=" + extratoId + ", " : "") +
            "}";
    }

}
