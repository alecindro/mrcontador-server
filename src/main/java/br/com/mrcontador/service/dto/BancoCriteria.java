package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Banco} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.BancoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bancos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BancoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter banDescricao;

    private StringFilter banSigla;

    private IntegerFilter banIspb;

    private StringFilter banCodigobancario;

    public BancoCriteria() {
    }

    public BancoCriteria(BancoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.banDescricao = other.banDescricao == null ? null : other.banDescricao.copy();
        this.banSigla = other.banSigla == null ? null : other.banSigla.copy();
        this.banIspb = other.banIspb == null ? null : other.banIspb.copy();
        this.banCodigobancario = other.banCodigobancario == null ? null : other.banCodigobancario.copy();
    }

    @Override
    public BancoCriteria copy() {
        return new BancoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBanDescricao() {
		return banDescricao;
	}

	public void setBanDescricao(StringFilter banDescricao) {
		this.banDescricao = banDescricao;
	}

	public StringFilter getBanSigla() {
		return banSigla;
	}

	public void setBanSigla(StringFilter banSigla) {
		this.banSigla = banSigla;
	}

	public IntegerFilter getBanIspb() {
		return banIspb;
	}

	public void setBanIspb(IntegerFilter banIspb) {
		this.banIspb = banIspb;
	}

	public StringFilter getBanCodigobancario() {
		return banCodigobancario;
	}

	public void setBanCodigobancario(StringFilter banCodigobancario) {
		this.banCodigobancario = banCodigobancario;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BancoCriteria that = (BancoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(banDescricao, that.banDescricao) &&
            Objects.equals(banSigla, that.banSigla) &&
            Objects.equals(banIspb, that.banIspb) &&
            Objects.equals(banCodigobancario, that.banCodigobancario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        banDescricao,
        banSigla,
        banIspb,
        banCodigobancario
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BancoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (banDescricao != null ? "ban_descricao=" + banDescricao + ", " : "") +
                (banSigla != null ? "ban_sigla=" + banSigla + ", " : "") +
                (banIspb != null ? "ban_ispb=" + banIspb + ", " : "") +
                (banCodigobancario != null ? "ban_codigobancario=" + banCodigobancario + ", " : "") +
            "}";
    }

}
