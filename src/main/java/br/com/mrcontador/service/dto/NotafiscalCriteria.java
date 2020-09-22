package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Notafiscal} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.NotafiscalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notafiscals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotafiscalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notNumero;

    private StringFilter notDescricao;

    private StringFilter notCnpj;

    private StringFilter notEmpresa;

    private LocalDateFilter notDatasaida;

    private BigDecimalFilter notValornota;

    private LocalDateFilter notDataparcela;

    private BigDecimalFilter notValorparcela;

    private IntegerFilter tnoCodigo;

    private StringFilter notParcela;

    private LongFilter parceiroId;

    public NotafiscalCriteria() {
    }

    public NotafiscalCriteria(NotafiscalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notNumero = other.notNumero == null ? null : other.notNumero.copy();
        this.notDescricao = other.notDescricao == null ? null : other.notDescricao.copy();
        this.notCnpj = other.notCnpj == null ? null : other.notCnpj.copy();
        this.notEmpresa = other.notEmpresa == null ? null : other.notEmpresa.copy();
        this.notDatasaida = other.notDatasaida == null ? null : other.notDatasaida.copy();
        this.notValornota = other.notValornota == null ? null : other.notValornota.copy();
        this.notDataparcela = other.notDataparcela == null ? null : other.notDataparcela.copy();
        this.notValorparcela = other.notValorparcela == null ? null : other.notValorparcela.copy();
        this.tnoCodigo = other.tnoCodigo == null ? null : other.tnoCodigo.copy();
        this.notParcela = other.notParcela == null ? null : other.notParcela.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public NotafiscalCriteria copy() {
        return new NotafiscalCriteria(this);
    }




    public LongFilter getId() {
		return id;
	}

	public void setId(LongFilter id) {
		this.id = id;
	}

	public StringFilter getNotNumero() {
		return notNumero;
	}

	public void setNotNumero(StringFilter notNumero) {
		this.notNumero = notNumero;
	}

	public StringFilter getNotDescricao() {
		return notDescricao;
	}

	public void setNotDescricao(StringFilter notDescricao) {
		this.notDescricao = notDescricao;
	}

	public StringFilter getNotCnpj() {
		return notCnpj;
	}

	public void setNotCnpj(StringFilter notCnpj) {
		this.notCnpj = notCnpj;
	}

	public StringFilter getNotEmpresa() {
		return notEmpresa;
	}

	public void setNotEmpresa(StringFilter notEmpresa) {
		this.notEmpresa = notEmpresa;
	}

	public LocalDateFilter getNotDatasaida() {
		return notDatasaida;
	}

	public void setNotDatasaida(LocalDateFilter notDatasaida) {
		this.notDatasaida = notDatasaida;
	}

	public BigDecimalFilter getNotValornota() {
		return notValornota;
	}

	public void setNotValornota(BigDecimalFilter notValornota) {
		this.notValornota = notValornota;
	}

	public LocalDateFilter getNotDataparcela() {
		return notDataparcela;
	}

	public void setNotDataparcela(LocalDateFilter notDataparcela) {
		this.notDataparcela = notDataparcela;
	}

	public BigDecimalFilter getNotValorparcela() {
		return notValorparcela;
	}

	public void setNotValorparcela(BigDecimalFilter notValorparcela) {
		this.notValorparcela = notValorparcela;
	}

	public IntegerFilter getTnoCodigo() {
		return tnoCodigo;
	}

	public void setTnoCodigo(IntegerFilter tnoCodigo) {
		this.tnoCodigo = tnoCodigo;
	}

	public StringFilter getNotParcela() {
		return notParcela;
	}

	public void setNotParcela(StringFilter notParcela) {
		this.notParcela = notParcela;
	}

	public LongFilter getParceiroId() {
		return parceiroId;
	}

	public void setParceiroId(LongFilter parceiroId) {
		this.parceiroId = parceiroId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotafiscalCriteria that = (NotafiscalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(notNumero, that.notNumero) &&
            Objects.equals(notDescricao, that.notDescricao) &&
            Objects.equals(notCnpj, that.notCnpj) &&
            Objects.equals(notEmpresa, that.notEmpresa) &&
            Objects.equals(notDatasaida, that.notDatasaida) &&
            Objects.equals(notValornota, that.notValornota) &&
            Objects.equals(notDataparcela, that.notDataparcela) &&
            Objects.equals(notValorparcela, that.notValorparcela) &&
            Objects.equals(tnoCodigo, that.tnoCodigo) &&
            Objects.equals(notParcela, that.notParcela) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        notNumero,
        notDescricao,
        notCnpj,
        notEmpresa,
        notDatasaida,
        notValornota,
        notDataparcela,
        notValorparcela,
        tnoCodigo,
        notParcela,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotafiscalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (notNumero != null ? "not_numero=" + notNumero + ", " : "") +
                (notDescricao != null ? "not_descricao=" + notDescricao + ", " : "") +
                (notCnpj != null ? "not_cnpj=" + notCnpj + ", " : "") +
                (notEmpresa != null ? "not_empresa=" + notEmpresa + ", " : "") +
                (notDatasaida != null ? "not_datasaida=" + notDatasaida + ", " : "") +
                (notValornota != null ? "not_valornota=" + notValornota + ", " : "") +
                (notDataparcela != null ? "not_dataparcela=" + notDataparcela + ", " : "") +
                (notValorparcela != null ? "not_valorparcela=" + notValorparcela + ", " : "") +
                (tnoCodigo != null ? "tno_codigo=" + tnoCodigo + ", " : "") +
                (notParcela != null ? "not_parcela=" + notParcela + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
