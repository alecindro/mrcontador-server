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

    private IntegerFilter par_codigo;

    private StringFilter reg_descricao;

    private IntegerFilter reg_conta;

    private StringFilter reg_historico;

    private StringFilter reg_todos;

    public RegraCriteria() {
    }

    public RegraCriteria(RegraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.par_codigo = other.par_codigo == null ? null : other.par_codigo.copy();
        this.reg_descricao = other.reg_descricao == null ? null : other.reg_descricao.copy();
        this.reg_conta = other.reg_conta == null ? null : other.reg_conta.copy();
        this.reg_historico = other.reg_historico == null ? null : other.reg_historico.copy();
        this.reg_todos = other.reg_todos == null ? null : other.reg_todos.copy();
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

    public IntegerFilter getPar_codigo() {
        return par_codigo;
    }

    public void setPar_codigo(IntegerFilter par_codigo) {
        this.par_codigo = par_codigo;
    }

    public StringFilter getReg_descricao() {
        return reg_descricao;
    }

    public void setReg_descricao(StringFilter reg_descricao) {
        this.reg_descricao = reg_descricao;
    }

    public IntegerFilter getReg_conta() {
        return reg_conta;
    }

    public void setReg_conta(IntegerFilter reg_conta) {
        this.reg_conta = reg_conta;
    }

    public StringFilter getReg_historico() {
        return reg_historico;
    }

    public void setReg_historico(StringFilter reg_historico) {
        this.reg_historico = reg_historico;
    }

    public StringFilter getReg_todos() {
        return reg_todos;
    }

    public void setReg_todos(StringFilter reg_todos) {
        this.reg_todos = reg_todos;
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
            Objects.equals(par_codigo, that.par_codigo) &&
            Objects.equals(reg_descricao, that.reg_descricao) &&
            Objects.equals(reg_conta, that.reg_conta) &&
            Objects.equals(reg_historico, that.reg_historico) &&
            Objects.equals(reg_todos, that.reg_todos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        par_codigo,
        reg_descricao,
        reg_conta,
        reg_historico,
        reg_todos
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (par_codigo != null ? "par_codigo=" + par_codigo + ", " : "") +
                (reg_descricao != null ? "reg_descricao=" + reg_descricao + ", " : "") +
                (reg_conta != null ? "reg_conta=" + reg_conta + ", " : "") +
                (reg_historico != null ? "reg_historico=" + reg_historico + ", " : "") +
                (reg_todos != null ? "reg_todos=" + reg_todos + ", " : "") +
            "}";
    }

}
