package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Conta} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ContaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter conConta;

    private StringFilter conClassificacao;

    private StringFilter conTipo;

    private StringFilter conDescricao;

    private StringFilter conCnpj;

    private IntegerFilter conGrau;

    private LongFilter parceiroId;

    public ContaCriteria() {
    }

    public ContaCriteria(ContaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.conConta = other.conConta == null ? null : other.conConta.copy();
        this.conClassificacao = other.conClassificacao == null ? null : other.conClassificacao.copy();
        this.conTipo = other.conTipo == null ? null : other.conTipo.copy();
        this.conDescricao = other.conDescricao == null ? null : other.conDescricao.copy();
        this.conCnpj = other.conCnpj == null ? null : other.conCnpj.copy();
        this.conGrau = other.conGrau == null ? null : other.conGrau.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public ContaCriteria copy() {
        return new ContaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getConConta() {
		return conConta;
	}

	public void setConConta(IntegerFilter conConta) {
		this.conConta = conConta;
	}

	public StringFilter getConClassificacao() {
		return conClassificacao;
	}

	public void setConClassificacao(StringFilter conClassificacao) {
		this.conClassificacao = conClassificacao;
	}

	public StringFilter getConTipo() {
		return conTipo;
	}

	public void setConTipo(StringFilter conTipo) {
		this.conTipo = conTipo;
	}

	public StringFilter getConDescricao() {
		return conDescricao;
	}

	public void setConDescricao(StringFilter conDescricao) {
		this.conDescricao = conDescricao;
	}

	public StringFilter getConCnpj() {
		return conCnpj;
	}

	public void setConCnpj(StringFilter conCnpj) {
		this.conCnpj = conCnpj;
	}

	public IntegerFilter getConGrau() {
		return conGrau;
	}

	public void setConGrau(IntegerFilter conGrau) {
		this.conGrau = conGrau;
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
        final ContaCriteria that = (ContaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(conConta, that.conConta) &&
            Objects.equals(conClassificacao, that.conClassificacao) &&
            Objects.equals(conTipo, that.conTipo) &&
            Objects.equals(conDescricao, that.conDescricao) &&
            Objects.equals(conCnpj, that.conCnpj) &&
            Objects.equals(conGrau, that.conGrau) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        conConta,
        conClassificacao,
        conTipo,
        conDescricao,
        conCnpj,
        conGrau,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (conConta != null ? "conConta=" + conConta + ", " : "") +
                (conClassificacao != null ? "conClassificacao=" + conClassificacao + ", " : "") +
                (conTipo != null ? "conTipo=" + conTipo + ", " : "") +
                (conDescricao != null ? "conDescricao=" + conDescricao + ", " : "") +
                (conCnpj != null ? "conCnpj=" + conCnpj + ", " : "") +
                (conGrau != null ? "conGrau=" + conGrau + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
