package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Extrato} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ExtratoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extratoes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtratoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    /**
     * 
     */
    private LocalDateFilter extDatalancamento;

    private StringFilter extHistorico;

    private StringFilter extNumerodocumento;

    private StringFilter extNumerocontrole;

    private BigDecimalFilter extDebito;

    private BigDecimalFilter extCredito;

    private StringFilter extDescricao;

    private LongFilter parceiroId;

    private LongFilter agenciabancariaId;
    
    private LongFilter arquivoId;

    public ExtratoCriteria() {
    }

    public ExtratoCriteria(ExtratoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.extDatalancamento = other.extDatalancamento == null ? null : other.extDatalancamento.copy();
        this.extHistorico = other.extHistorico == null ? null : other.extHistorico.copy();
        this.extNumerodocumento = other.extNumerodocumento == null ? null : other.extNumerodocumento.copy();
        this.extNumerocontrole = other.extNumerocontrole == null ? null : other.extNumerocontrole.copy();
        this.extDebito = other.extDebito == null ? null : other.extDebito.copy();
        this.extCredito = other.extCredito == null ? null : other.extCredito.copy();
        this.extDescricao = other.extDescricao == null ? null : other.extDescricao.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.agenciabancariaId = other.agenciabancariaId == null ? null : other.agenciabancariaId.copy();
        this.arquivoId = other.arquivoId == null ? null : other.arquivoId.copy();
    }

    @Override
    public ExtratoCriteria copy() {
        return new ExtratoCriteria(this);
    }

    
    public LongFilter getId() {
		return id;
	}

	public void setId(LongFilter id) {
		this.id = id;
	}

	public LocalDateFilter getExtDatalancamento() {
		return extDatalancamento;
	}

	public void setExtDatalancamento(LocalDateFilter extDatalancamento) {
		this.extDatalancamento = extDatalancamento;
	}

	public StringFilter getExtHistorico() {
		return extHistorico;
	}

	public void setExtHistorico(StringFilter extHistorico) {
		this.extHistorico = extHistorico;
	}

	public StringFilter getExtNumerodocumento() {
		return extNumerodocumento;
	}

	public void setExtNumerodocumento(StringFilter extNumerodocumento) {
		this.extNumerodocumento = extNumerodocumento;
	}

	public StringFilter getExtNumerocontrole() {
		return extNumerocontrole;
	}

	public void setExtNumerocontrole(StringFilter extNumerocontrole) {
		this.extNumerocontrole = extNumerocontrole;
	}

	public BigDecimalFilter getExtDebito() {
		return extDebito;
	}

	public void setExtDebito(BigDecimalFilter extDebito) {
		this.extDebito = extDebito;
	}

	public BigDecimalFilter getExtCredito() {
		return extCredito;
	}

	public void setExtCredito(BigDecimalFilter extCredito) {
		this.extCredito = extCredito;
	}

	public StringFilter getExtDescricao() {
		return extDescricao;
	}

	public void setExtDescricao(StringFilter extDescricao) {
		this.extDescricao = extDescricao;
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

	public LongFilter getArquivoId() {
		return arquivoId;
	}

	public void setArquivoId(LongFilter arquivoId) {
		this.arquivoId = arquivoId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExtratoCriteria that = (ExtratoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(extDatalancamento, that.extDatalancamento) &&
            Objects.equals(extHistorico, that.extHistorico) &&
            Objects.equals(extNumerodocumento, that.extNumerodocumento) &&
            Objects.equals(extNumerocontrole, that.extNumerocontrole) &&
            Objects.equals(extDebito, that.extDebito) &&
            Objects.equals(extCredito, that.extCredito) &&
            Objects.equals(extDescricao, that.extDescricao) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(agenciabancariaId, that.agenciabancariaId) &&
            Objects.equals(arquivoId, that.arquivoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        extDatalancamento,
        extHistorico,
        extNumerodocumento,
        extNumerocontrole,
        extDebito,
        extCredito,
        extDescricao,
        parceiroId,
        agenciabancariaId,
        arquivoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtratoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (extDatalancamento != null ? "ext_datalancamento=" + extDatalancamento + ", " : "") +
                (extHistorico != null ? "ext_historico=" + extHistorico + ", " : "") +
                (extNumerodocumento != null ? "ext_numerodocumento=" + extNumerodocumento + ", " : "") +
                (extNumerocontrole != null ? "ext_numerocontrole=" + extNumerocontrole + ", " : "") +
                (extDebito != null ? "ext_debito=" + extDebito + ", " : "") +
                (extCredito != null ? "ext_credito=" + extCredito + ", " : "") +
                (extDescricao != null ? "ext_descricao=" + extDescricao + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (agenciabancariaId != null ? "agenciabancariaId=" + agenciabancariaId + ", " : "") +
                (arquivoId != null ? "arquivoId=" + arquivoId + ", " : "") +
            "}";
    }

}
