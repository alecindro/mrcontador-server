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
 * Criteria class for the {@link br.com.mrcontador.domain.Notaservico} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.NotaservicoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notaservicos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotaservicoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nseNumero;

    private StringFilter nseDescricao;

    private StringFilter nseCnpj;

    private StringFilter nseEmpresa;

    private LocalDateFilter nseDatasaida;

    private BigDecimalFilter nseValornota;

    private LocalDateFilter nseDataparcela;

    private BigDecimalFilter nseValorparcela;

    private IntegerFilter tnoCodigo;

    private StringFilter nseParcela;

    private LongFilter parceiroId;

    public NotaservicoCriteria() {
    }

    public NotaservicoCriteria(NotaservicoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nseNumero = other.nseNumero == null ? null : other.nseNumero.copy();
        this.nseDescricao = other.nseDescricao == null ? null : other.nseDescricao.copy();
        this.nseCnpj = other.nseCnpj == null ? null : other.nseCnpj.copy();
        this.nseEmpresa = other.nseEmpresa == null ? null : other.nseEmpresa.copy();
        this.nseDatasaida = other.nseDatasaida == null ? null : other.nseDatasaida.copy();
        this.nseValornota = other.nseValornota == null ? null : other.nseValornota.copy();
        this.nseDataparcela = other.nseDataparcela == null ? null : other.nseDataparcela.copy();
        this.nseValorparcela = other.nseValorparcela == null ? null : other.nseValorparcela.copy();
        this.tnoCodigo = other.tnoCodigo == null ? null : other.tnoCodigo.copy();
        this.nseParcela = other.nseParcela == null ? null : other.nseParcela.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public NotaservicoCriteria copy() {
        return new NotaservicoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

  
    public LongFilter getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(LongFilter parceiroId) {
        this.parceiroId = parceiroId;
    }

    public IntegerFilter getNseNumero() {
		return nseNumero;
	}

	public void setNseNumero(IntegerFilter nseNumero) {
		this.nseNumero = nseNumero;
	}

	public StringFilter getNseDescricao() {
		return nseDescricao;
	}

	public void setNseDescricao(StringFilter nseDescricao) {
		this.nseDescricao = nseDescricao;
	}

	public StringFilter getNseCnpj() {
		return nseCnpj;
	}

	public void setNseCnpj(StringFilter nseCnpj) {
		this.nseCnpj = nseCnpj;
	}

	public StringFilter getNseEmpresa() {
		return nseEmpresa;
	}

	public void setNseEmpresa(StringFilter nseEmpresa) {
		this.nseEmpresa = nseEmpresa;
	}

	public LocalDateFilter getNseDatasaida() {
		return nseDatasaida;
	}

	public void setNseDatasaida(LocalDateFilter nseDatasaida) {
		this.nseDatasaida = nseDatasaida;
	}

	public BigDecimalFilter getNseValornota() {
		return nseValornota;
	}

	public void setNseValornota(BigDecimalFilter nseValornota) {
		this.nseValornota = nseValornota;
	}

	public LocalDateFilter getNseDataparcela() {
		return nseDataparcela;
	}

	public void setNseDataparcela(LocalDateFilter nseDataparcela) {
		this.nseDataparcela = nseDataparcela;
	}

	public BigDecimalFilter getNseValorparcela() {
		return nseValorparcela;
	}

	public void setNseValorparcela(BigDecimalFilter nseValorparcela) {
		this.nseValorparcela = nseValorparcela;
	}

	public IntegerFilter getTnoCodigo() {
		return tnoCodigo;
	}

	public void setTnoCodigo(IntegerFilter tnoCodigo) {
		this.tnoCodigo = tnoCodigo;
	}

	public StringFilter getNseParcela() {
		return nseParcela;
	}

	public void setNseParcela(StringFilter nseParcela) {
		this.nseParcela = nseParcela;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotaservicoCriteria that = (NotaservicoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nseNumero, that.nseNumero) &&
            Objects.equals(nseDescricao, that.nseDescricao) &&
            Objects.equals(nseCnpj, that.nseCnpj) &&
            Objects.equals(nseEmpresa, that.nseEmpresa) &&
            Objects.equals(nseDatasaida, that.nseDatasaida) &&
            Objects.equals(nseValornota, that.nseValornota) &&
            Objects.equals(nseDataparcela, that.nseDataparcela) &&
            Objects.equals(nseValorparcela, that.nseValorparcela) &&
            Objects.equals(tnoCodigo, that.tnoCodigo) &&
            Objects.equals(nseParcela, that.nseParcela) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nseNumero,
        nseDescricao,
        nseCnpj,
        nseEmpresa,
        nseDatasaida,
        nseValornota,
        nseDataparcela,
        nseValorparcela,
        tnoCodigo,
        nseParcela,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaservicoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nseNumero != null ? "nse_numero=" + nseNumero + ", " : "") +
                (nseDescricao != null ? "nse_descricao=" + nseDescricao + ", " : "") +
                (nseCnpj != null ? "nse_cnpj=" + nseCnpj + ", " : "") +
                (nseEmpresa != null ? "nse_empresa=" + nseEmpresa + ", " : "") +
                (nseDatasaida != null ? "nse_datasaida=" + nseDatasaida + ", " : "") +
                (nseValornota != null ? "nse_valornota=" + nseValornota + ", " : "") +
                (nseDataparcela != null ? "nse_dataparcela=" + nseDataparcela + ", " : "") +
                (nseValorparcela != null ? "nse_valorparcela=" + nseValorparcela + ", " : "") +
                (tnoCodigo != null ? "tno_codigo=" + tnoCodigo + ", " : "") +
                (nseParcela != null ? "nse_parcela=" + nseParcela + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
