package br.com.mrcontador.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Banco} entity.
 */
public class BancoDTO implements Serializable {
    
    private Long id;

    @Size(max = 200)
    private String ban_descricao;

    @Size(max = 100)
    private String ban_sigla;

    private Integer ban_ispb;

    @NotNull
    @Size(max = 5)
    private String ban_codigobancario;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBan_descricao() {
        return ban_descricao;
    }

    public void setBan_descricao(String ban_descricao) {
        this.ban_descricao = ban_descricao;
    }

    public String getBan_sigla() {
        return ban_sigla;
    }

    public void setBan_sigla(String ban_sigla) {
        this.ban_sigla = ban_sigla;
    }

    public Integer getBan_ispb() {
        return ban_ispb;
    }

    public void setBan_ispb(Integer ban_ispb) {
        this.ban_ispb = ban_ispb;
    }

    public String getBan_codigobancario() {
        return ban_codigobancario;
    }

    public void setBan_codigobancario(String ban_codigobancario) {
        this.ban_codigobancario = ban_codigobancario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BancoDTO)) {
            return false;
        }

        return id != null && id.equals(((BancoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BancoDTO{" +
            "id=" + getId() +
            ", ban_descricao='" + getBan_descricao() + "'" +
            ", ban_sigla='" + getBan_sigla() + "'" +
            ", ban_ispb=" + getBan_ispb() +
            ", ban_codigobancario='" + getBan_codigobancario() + "'" +
            "}";
    }
}
