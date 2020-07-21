package br.com.mrcontador.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Agenciabancaria} entity.
 */
public class AgenciabancariaDTO implements Serializable {
    
    private Long id;

    @Size(max = 20)
    private String age_numero;

    @Size(max = 20)
    private String age_digito;

    @Size(max = 6)
    private String age_agencia;

    @Size(max = 30)
    private String age_descricao;

    private Boolean age_situacao;

    @NotNull
    @Size(max = 5)
    private String ban_codigobancario;


    private Long bancoId;

    private Long parceiroId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAge_numero() {
        return age_numero;
    }

    public void setAge_numero(String age_numero) {
        this.age_numero = age_numero;
    }

    public String getAge_digito() {
        return age_digito;
    }

    public void setAge_digito(String age_digito) {
        this.age_digito = age_digito;
    }

    public String getAge_agencia() {
        return age_agencia;
    }

    public void setAge_agencia(String age_agencia) {
        this.age_agencia = age_agencia;
    }

    public String getAge_descricao() {
        return age_descricao;
    }

    public void setAge_descricao(String age_descricao) {
        this.age_descricao = age_descricao;
    }

    public Boolean isAge_situacao() {
        return age_situacao;
    }

    public void setAge_situacao(Boolean age_situacao) {
        this.age_situacao = age_situacao;
    }

    public String getBan_codigobancario() {
        return ban_codigobancario;
    }

    public void setBan_codigobancario(String ban_codigobancario) {
        this.ban_codigobancario = ban_codigobancario;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public Long getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(Long parceiroId) {
        this.parceiroId = parceiroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgenciabancariaDTO)) {
            return false;
        }

        return id != null && id.equals(((AgenciabancariaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgenciabancariaDTO{" +
            "id=" + getId() +
            ", age_numero='" + getAge_numero() + "'" +
            ", age_digito='" + getAge_digito() + "'" +
            ", age_agencia='" + getAge_agencia() + "'" +
            ", age_descricao='" + getAge_descricao() + "'" +
            ", age_situacao='" + isAge_situacao() + "'" +
            ", ban_codigobancario='" + getBan_codigobancario() + "'" +
            ", bancoId=" + getBancoId() +
            ", parceiroId=" + getParceiroId() +
            "}";
    }
}
