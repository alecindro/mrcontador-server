package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Regra.
 */
@Entity
@Table(name = "regra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Regra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "par_codigo")
    private Integer par_codigo;

    @Column(name = "reg_descricao", length = 60)
    private String reg_descricao;

    @Column(name = "reg_conta")
    private Integer reg_conta;

    @Column(name = "reg_historico", length = 60)
    private String reg_historico;

    @Column(name = "reg_todos", length = 1)
    private String reg_todos;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPar_codigo() {
        return par_codigo;
    }

    public Regra par_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
        return this;
    }

    public void setPar_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
    }

    public String getReg_descricao() {
        return reg_descricao;
    }

    public Regra reg_descricao(String reg_descricao) {
        this.reg_descricao = reg_descricao;
        return this;
    }

    public void setReg_descricao(String reg_descricao) {
        this.reg_descricao = reg_descricao;
    }

    public Integer getReg_conta() {
        return reg_conta;
    }

    public Regra reg_conta(Integer reg_conta) {
        this.reg_conta = reg_conta;
        return this;
    }

    public void setReg_conta(Integer reg_conta) {
        this.reg_conta = reg_conta;
    }

    public String getReg_historico() {
        return reg_historico;
    }

    public Regra reg_historico(String reg_historico) {
        this.reg_historico = reg_historico;
        return this;
    }

    public void setReg_historico(String reg_historico) {
        this.reg_historico = reg_historico;
    }

    public String getReg_todos() {
        return reg_todos;
    }

    public Regra reg_todos(String reg_todos) {
        this.reg_todos = reg_todos;
        return this;
    }

    public void setReg_todos(String reg_todos) {
        this.reg_todos = reg_todos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regra)) {
            return false;
        }
        return id != null && id.equals(((Regra) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regra{" +
            "id=" + getId() +
            ", par_codigo=" + getPar_codigo() +
            ", reg_descricao='" + getReg_descricao() + "'" +
            ", reg_conta=" + getReg_conta() +
            ", reg_historico='" + getReg_historico() + "'" +
            ", reg_todos='" + getReg_todos() + "'" +
            "}";
    }
}
