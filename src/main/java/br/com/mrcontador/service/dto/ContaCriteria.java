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

    private IntegerFilter con_conta;

    private StringFilter con_classificacao;

    private StringFilter con_tipo;

    private StringFilter con_descricao;

    private StringFilter con_cnpj;

    private IntegerFilter con_grau;

    private LongFilter parceiroId;

    public ContaCriteria() {
    }

    public ContaCriteria(ContaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.con_conta = other.con_conta == null ? null : other.con_conta.copy();
        this.con_classificacao = other.con_classificacao == null ? null : other.con_classificacao.copy();
        this.con_tipo = other.con_tipo == null ? null : other.con_tipo.copy();
        this.con_descricao = other.con_descricao == null ? null : other.con_descricao.copy();
        this.con_cnpj = other.con_cnpj == null ? null : other.con_cnpj.copy();
        this.con_grau = other.con_grau == null ? null : other.con_grau.copy();
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

    public IntegerFilter getCon_conta() {
        return con_conta;
    }

    public void setCon_conta(IntegerFilter con_conta) {
        this.con_conta = con_conta;
    }

    public StringFilter getCon_classificacao() {
        return con_classificacao;
    }

    public void setCon_classificacao(StringFilter con_classificacao) {
        this.con_classificacao = con_classificacao;
    }

    public StringFilter getCon_tipo() {
        return con_tipo;
    }

    public void setCon_tipo(StringFilter con_tipo) {
        this.con_tipo = con_tipo;
    }

    public StringFilter getCon_descricao() {
        return con_descricao;
    }

    public void setCon_descricao(StringFilter con_descricao) {
        this.con_descricao = con_descricao;
    }

    public StringFilter getCon_cnpj() {
        return con_cnpj;
    }

    public void setCon_cnpj(StringFilter con_cnpj) {
        this.con_cnpj = con_cnpj;
    }

    public IntegerFilter getCon_grau() {
        return con_grau;
    }

    public void setCon_grau(IntegerFilter con_grau) {
        this.con_grau = con_grau;
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
            Objects.equals(con_conta, that.con_conta) &&
            Objects.equals(con_classificacao, that.con_classificacao) &&
            Objects.equals(con_tipo, that.con_tipo) &&
            Objects.equals(con_descricao, that.con_descricao) &&
            Objects.equals(con_cnpj, that.con_cnpj) &&
            Objects.equals(con_grau, that.con_grau) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        con_conta,
        con_classificacao,
        con_tipo,
        con_descricao,
        con_cnpj,
        con_grau,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (con_conta != null ? "con_conta=" + con_conta + ", " : "") +
                (con_classificacao != null ? "con_classificacao=" + con_classificacao + ", " : "") +
                (con_tipo != null ? "con_tipo=" + con_tipo + ", " : "") +
                (con_descricao != null ? "con_descricao=" + con_descricao + ", " : "") +
                (con_cnpj != null ? "con_cnpj=" + con_cnpj + ", " : "") +
                (con_grau != null ? "con_grau=" + con_grau + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
