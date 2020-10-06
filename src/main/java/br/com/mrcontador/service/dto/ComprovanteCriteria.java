package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Comprovante} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ComprovanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comprovantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComprovanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comCnpj;

    private StringFilter comBeneficiario;

    private StringFilter comDocumento;

    private LocalDateFilter comDatavencimento;

    private LocalDateFilter comDatapagamento;

    private BigDecimalFilter comValordocumento;

    private BigDecimalFilter comValorpagamento;

    private StringFilter comObservacao;
    
    private LongFilter parceiroId;
    
    private LongFilter agenciabancariaId;
    
    private BooleanFilter processado;

    public ComprovanteCriteria() {
    }

    public ComprovanteCriteria(ComprovanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.comCnpj = other.comCnpj == null ? null : other.comCnpj.copy();
        this.comBeneficiario = other.comBeneficiario == null ? null : other.comBeneficiario.copy();
        this.comDocumento = other.comDocumento == null ? null : other.comDocumento.copy();
        this.comDatavencimento = other.comDatavencimento == null ? null : other.comDatavencimento.copy();
        this.comDatapagamento = other.comDatapagamento == null ? null : other.comDatapagamento.copy();
        this.comValordocumento = other.comValordocumento == null ? null : other.comValordocumento.copy();
        this.comValorpagamento = other.comValorpagamento == null ? null : other.comValorpagamento.copy();
        this.comObservacao = other.comObservacao == null ? null : other.comObservacao.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.agenciabancariaId = other.agenciabancariaId == null ? null : other.agenciabancariaId.copy();
        this.processado = other.processado == null ? null : other.processado.copy();
    }

    @Override
    public ComprovanteCriteria copy() {
        return new ComprovanteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public LongFilter getAgenciabancariaId() {
        return agenciabancariaId;
    }

    public void setAgenciabancariaId(LongFilter agenciabancariaId) {
        this.agenciabancariaId = agenciabancariaId;
    }

	public StringFilter getComCnpj() {
		return comCnpj;
	}

	public void setComCnpj(StringFilter comCnpj) {
		this.comCnpj = comCnpj;
	}

	public StringFilter getComBeneficiario() {
		return comBeneficiario;
	}

	public void setComBeneficiario(StringFilter comBeneficiario) {
		this.comBeneficiario = comBeneficiario;
	}

	public StringFilter getComDocumento() {
		return comDocumento;
	}

	public void setComDocumento(StringFilter comDocumento) {
		this.comDocumento = comDocumento;
	}

	public LocalDateFilter getComDatavencimento() {
		return comDatavencimento;
	}

	public void setComDatavencimento(LocalDateFilter comDatavencimento) {
		this.comDatavencimento = comDatavencimento;
	}

	public LocalDateFilter getComDatapagamento() {
		return comDatapagamento;
	}

	public void setComDatapagamento(LocalDateFilter comDatapagamento) {
		this.comDatapagamento = comDatapagamento;
	}

	public BigDecimalFilter getComValordocumento() {
		return comValordocumento;
	}

	public void setComValordocumento(BigDecimalFilter comValordocumento) {
		this.comValordocumento = comValordocumento;
	}

	public BigDecimalFilter getComValorpagamento() {
		return comValorpagamento;
	}

	public void setComValorpagamento(BigDecimalFilter comValorpagamento) {
		this.comValorpagamento = comValorpagamento;
	}

	public StringFilter getComObservacao() {
		return comObservacao;
	}

	public void setComObservacao(StringFilter comObservacao) {
		this.comObservacao = comObservacao;
	}
	
	public LongFilter getParceiroId() {
		return parceiroId;
	}

	public void setParceiroId(LongFilter parceiroId) {
		this.parceiroId = parceiroId;
	}
	
	public BooleanFilter getProcessado() {
		return processado;
	}

	public void setProcessado(BooleanFilter processado) {
		this.processado = processado;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ComprovanteCriteria that = (ComprovanteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(comCnpj, that.comCnpj) &&
            Objects.equals(comBeneficiario, that.comBeneficiario) &&
            Objects.equals(comDocumento, that.comDocumento) &&
            Objects.equals(comDatavencimento, that.comDatavencimento) &&
            Objects.equals(comDatapagamento, that.comDatapagamento) &&
            Objects.equals(comValordocumento, that.comValordocumento) &&
            Objects.equals(comValorpagamento, that.comValorpagamento) &&
            Objects.equals(comObservacao, that.comObservacao) &&
            Objects.equals(parceiroId, that.parceiroId)&&
        Objects.equals(agenciabancariaId, that.agenciabancariaId)&&
        Objects.equals(processado, that.processado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        comCnpj,
        comBeneficiario,
        comDocumento,
        comDatavencimento,
        comDatapagamento,
        comValordocumento,
        comValorpagamento,
        comObservacao,
        parceiroId,
        agenciabancariaId,
        processado
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComprovanteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (comCnpj != null ? "com_cnpj=" + comCnpj + ", " : "") +
                (comBeneficiario != null ? "com_beneficiario=" + comBeneficiario + ", " : "") +
                (comDocumento != null ? "com_documento=" + comDocumento + ", " : "") +
                (comDatavencimento != null ? "com_datavencimento=" + comDatavencimento + ", " : "") +
                (comDatapagamento != null ? "com_datapagamento=" + comDatapagamento + ", " : "") +
                (comValordocumento != null ? "com_valordocumento=" + comValordocumento + ", " : "") +
                (comValorpagamento != null ? "com_valorpagamento=" + comValorpagamento + ", " : "") +
                (comObservacao != null ? "com_observacao=" + comObservacao + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (agenciabancariaId != null ? "agenciabancariaId=" + agenciabancariaId + ", " : "") +
                (processado != null ? "processado=" + processado + ", " : "") +
            "}";
    }

}
