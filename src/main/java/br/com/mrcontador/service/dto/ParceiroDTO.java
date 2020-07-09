package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.Size;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Parceiro} entity.
 */
public class ParceiroDTO implements Serializable {
 
	private static final long serialVersionUID = 1L;

	private Long id;

    @Size(max = 50)
    private String parDescricao;

    @Size(max = 70)
    private String parRazaosocial;

    @Size(max = 1)
    private String parTipopessoa;

    @Size(max = 20)
    private String parCnpjcpf;

    @Size(max = 20)
    private String parRgie;

    @Size(max = 200)
    private String parObs;

    private ZonedDateTime parDatacadastro;

    private Integer spaCodigo;

    private String logradouro;

    @Size(max = 8)
    private String cep;

    private String cidade;

    private String estado;

    private String areAtuacao;

    private String numero;

    private String bairro;

    private String porte;

    private String abertura;

    private String naturezaJuridica;

    private String ultimaAtualizacao;

    private String status;

    private String tipo;

    private String complemento;

    private String email;

    private String telefone;

    private String dataSituacao;

    private String efr;

    private String motivoSituacao;

    private String situacaoEspecial;

    private String dataSituacaoEspecial;

    private String capitalSocial;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getAbertura() {
        return abertura;
    }

    public void setAbertura(String abertura) {
        this.abertura = abertura;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

     public String getEfr() {
        return efr;
    }

    public void setEfr(String efr) {
        this.efr = efr;
    }
     
    public String getParDescricao() {
		return parDescricao;
	}

	public void setParDescricao(String parDescricao) {
		this.parDescricao = parDescricao;
	}

	public String getParRazaosocial() {
		return parRazaosocial;
	}

	public void setParRazaosocial(String parRazaosocial) {
		this.parRazaosocial = parRazaosocial;
	}

	public String getParTipopessoa() {
		return parTipopessoa;
	}

	public void setParTipopessoa(String parTipopessoa) {
		this.parTipopessoa = parTipopessoa;
	}

	public String getParCnpjcpf() {
		return parCnpjcpf;
	}

	public void setParCnpjcpf(String parCnpjcpf) {
		this.parCnpjcpf = parCnpjcpf;
	}

	public String getParRgie() {
		return parRgie;
	}

	public void setParRgie(String parRgie) {
		this.parRgie = parRgie;
	}

	public String getParObs() {
		return parObs;
	}

	public void setParObs(String parObs) {
		this.parObs = parObs;
	}

	public ZonedDateTime getParDatacadastro() {
		return parDatacadastro;
	}

	public void setParDatacadastro(ZonedDateTime parDatacadastro) {
		this.parDatacadastro = parDatacadastro;
	}

	public Integer getSpaCodigo() {
		return spaCodigo;
	}

	public void setSpaCodigo(Integer spaCodigo) {
		this.spaCodigo = spaCodigo;
	}

	public String getAreAtuacao() {
		return areAtuacao;
	}

	public void setAreAtuacao(String areAtuacao) {
		this.areAtuacao = areAtuacao;
	}

	public String getNaturezaJuridica() {
		return naturezaJuridica;
	}

	public void setNaturezaJuridica(String naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
	}

	public String getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(String dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	public String getMotivoSituacao() {
		return motivoSituacao;
	}

	public void setMotivoSituacao(String motivoSituacao) {
		this.motivoSituacao = motivoSituacao;
	}

	public String getSituacaoEspecial() {
		return situacaoEspecial;
	}

	public void setSituacaoEspecial(String situacaoEspecial) {
		this.situacaoEspecial = situacaoEspecial;
	}

	public String getDataSituacaoEspecial() {
		return dataSituacaoEspecial;
	}

	public void setDataSituacaoEspecial(String dataSituacaoEspecial) {
		this.dataSituacaoEspecial = dataSituacaoEspecial;
	}

	public String getCapitalSocial() {
		return capitalSocial;
	}

	public void setCapitalSocial(String capitalSocial) {
		this.capitalSocial = capitalSocial;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParceiroDTO)) {
            return false;
        }

        return id != null && id.equals(((ParceiroDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "ParceiroDTO [id=" + id + ", parDescricao=" + parDescricao + ", parRazaosocial=" + parRazaosocial
				+ ", parTipopessoa=" + parTipopessoa + ", parCnpjcpf=" + parCnpjcpf + ", parRgie=" + parRgie
				+ ", parObs=" + parObs + ", parDatacadastro=" + parDatacadastro + ", spaCodigo=" + spaCodigo
				+ ", logradouro=" + logradouro + ", cep=" + cep + ", cidade=" + cidade + ", estado=" + estado
				+ ", areAtuacao=" + areAtuacao + ", numero=" + numero + ", bairro=" + bairro + ", porte=" + porte
				+ ", abertura=" + abertura + ", naturezaJuridica=" + naturezaJuridica + ", ultimaAtualizacao="
				+ ultimaAtualizacao + ", status=" + status + ", tipo=" + tipo + ", complemento=" + complemento
				+ ", email=" + email + ", telefone=" + telefone + ", dataSituacao=" + dataSituacao + ", efr=" + efr
				+ ", motivoSituacao=" + motivoSituacao + ", situacaoEspecial=" + situacaoEspecial
				+ ", dataSituacaoEspecial=" + dataSituacaoEspecial + ", capitalSocial=" + capitalSocial + "]";
	}

  
}
