package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Regra} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.RegraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /regras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RegraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter parceiroId;

    private StringFilter regDescricao;

    private IntegerFilter regConta;

    private StringFilter regHistorico;

    private StringFilter regTodos;
    
    private ZonedDateTimeFilter dataCadastro;

    public RegraCriteria() {
    }

    public RegraCriteria(RegraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.regDescricao = other.regDescricao == null ? null : other.regDescricao.copy();
        this.regConta = other.regConta == null ? null : other.regConta.copy();
        this.regHistorico = other.regHistorico == null ? null : other.regHistorico.copy();
        this.regTodos = other.regTodos == null ? null : other.regTodos.copy();
        this.dataCadastro = other.dataCadastro == null ? null : other.dataCadastro.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public RegraCriteria copy() {
        return new RegraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRegDescricao() {
        return regDescricao;
    }

    public void setRegDescricao(StringFilter regDescricao) {
        this.regDescricao = regDescricao;
    }

    public IntegerFilter getRegConta() {
        return regConta;
    }

    public void setRegConta(IntegerFilter regConta) {
        this.regConta = regConta;
    }

    public StringFilter getRegHistorico() {
        return regHistorico;
    }

    public void setRegHistorico(StringFilter regHistorico) {
        this.regHistorico = regHistorico;
    }

    public StringFilter getRegTodos() {
        return regTodos;
    }

    public void setRegTodos(StringFilter regTodos) {
        this.regTodos = regTodos;
    }

    public LongFilter getParceiroId() {
		return parceiroId;
	}

	public void setParceiroId(LongFilter parceiroId) {
		this.parceiroId = parceiroId;
	}
	
	public ZonedDateTimeFilter getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(ZonedDateTimeFilter dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegraCriteria that = (RegraCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(regDescricao, that.regDescricao) &&
            Objects.equals(regConta, that.regConta) &&
            Objects.equals(regHistorico, that.regHistorico) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(dataCadastro, that.dataCadastro) &&
            Objects.equals(regTodos, that.regTodos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        regDescricao,
        regConta,
        regHistorico,
        regTodos,
        dataCadastro,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (regDescricao != null ? "regDescricao=" + regDescricao + ", " : "") +
                (regConta != null ? "regConta=" + regConta + ", " : "") +
                (regHistorico != null ? "regHistorico=" + regHistorico + ", " : "") +
                (regTodos != null ? "regTodos=" + regTodos + ", " : "") +
                (dataCadastro != null ? "dataCadastro=" + dataCadastro + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
