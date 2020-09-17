package br.com.mrcontador.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Regra} entity.
 */
public class RegraDTO implements Serializable {
    
    private Long id;

    private Integer par_codigo;

    private String reg_descricao;

    private Integer reg_conta;

    private String reg_historico;

    private String reg_todos;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPar_codigo() {
        return par_codigo;
    }

    public void setPar_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
    }

    public String getReg_descricao() {
        return reg_descricao;
    }

    public void setReg_descricao(String reg_descricao) {
        this.reg_descricao = reg_descricao;
    }

    public Integer getReg_conta() {
        return reg_conta;
    }

    public void setReg_conta(Integer reg_conta) {
        this.reg_conta = reg_conta;
    }

    public String getReg_historico() {
        return reg_historico;
    }

    public void setReg_historico(String reg_historico) {
        this.reg_historico = reg_historico;
    }

    public String getReg_todos() {
        return reg_todos;
    }

    public void setReg_todos(String reg_todos) {
        this.reg_todos = reg_todos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegraDTO)) {
            return false;
        }

        return id != null && id.equals(((RegraDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegraDTO{" +
            "id=" + getId() +
            ", par_codigo=" + getPar_codigo() +
            ", reg_descricao='" + getReg_descricao() + "'" +
            ", reg_conta=" + getReg_conta() +
            ", reg_historico='" + getReg_historico() + "'" +
            ", reg_todos='" + getReg_todos() + "'" +
            "}";
    }
}
