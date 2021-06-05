package br.com.mrcontador.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Parceiro.
 */
@Entity
@Table(name = "parceiro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parceiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "par_descricao", length = 50)
    private String parDescricao;

    @Size(max = 70)
    @Column(name = "par_razaosocial", length = 70)
    private String parRazaosocial;

    @Size(max = 1)
    @Column(name = "par_tipopessoa", length = 1)
    private String parTipopessoa;

    @Size(max = 20)
    @Column(name = "par_cnpjcpf", length = 20)
    private String parCnpjcpf;

    @Size(max = 20)
    @Column(name = "par_rgie", length = 20)
    private String parRgie;

    @Size(max = 200)
    @Column(name = "par_obs", length = 200)
    private String parObs;

    @Column(name = "par_datacadastro")
    private ZonedDateTime parDatacadastro;

    @Column(name = "cadastro_status")
    private Integer cadastroStatus;
    
    @Column(name = "spa_codigo")
    private Integer spaCodigo;

    @Column(name = "logradouro")
    private String logradouro;

    @Size(max = 8)
    @Column(name = "cep", length = 8)
    private String cep;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "area_atuacao")
    private String areaAtuacao;

    @Column(name = "numero")
    private String numero;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "porte")
    private String porte;

    @Column(name = "abertura")
    private String abertura;

    @Column(name = "natureza_juridica")
    private String naturezaJuridica;

    @Column(name = "ultima_atualizacao")
    private String ultimaAtualizacao;

    @Column(name = "status")
    private String status;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "data_situacao")
    private String dataSituacao;

    @Column(name = "efr")
    private String efr;

    @Column(name = "motivo_situacao")
    private String motivoSituacao;

    @Column(name = "situacao_especial")
    private String situacaoEspecial;

    @Column(name = "data_situacao_especial")
    private String dataSituacaoEspecial;

    @Column(name = "capital_social")
    private String capitalSocial;

    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "cod_ext")
    private String codExt;
    

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Set<Atividade> atividades;

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Set<Socio> socios;
    
    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Set<Agenciabancaria> agenciabancarias;
    
    @OneToMany(mappedBy = "parceiro", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Set<Integracao> integracaos;
    
    
    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Set<PermissaoParceiro> permissaos;
    
    @ManyToOne
    @JoinColumn(name="despesa_juros")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta despesaJuros;
    
    @ManyToOne
    @JoinColumn(name="despesa_iof")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta despesaIof;
    
    @ManyToOne
    @JoinColumn(name="juros_ativos")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta jurosAtivos;
    
    @ManyToOne
    @JoinColumn(name="descontos_ativos")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta descontosAtivos;
    
    @ManyToOne
    @JoinColumn(name="caixa_conta")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta caixaConta;
    
    @ManyToOne
    @JoinColumn(name="despesas_bancarias")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta despesasBancarias;
    @ManyToOne
    @JoinColumn(name="despesa_tarifa")
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Conta despesaTarifa;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public Parceiro parDescricao(String parDescricao) {
        this.parDescricao = parDescricao;
        return this;
    }

    
    public Parceiro parRazaosocial(String parRazaosocial) {
        this.parRazaosocial = parRazaosocial;
        return this;
    }

    public Parceiro parTipopessoa(String parTipopessoa) {
        this.parTipopessoa = parTipopessoa;
        return this;
    }


    public Parceiro parCnpjcpf(String parCnpjcpf) {
        this.parCnpjcpf = parCnpjcpf;
        return this;
    }


    public Parceiro parRgie(String parRgie) {
        this.parRgie = parRgie;
        return this;
    }


    public Parceiro parObs(String parObs) {
        this.parObs = parObs;
        return this;
    }


    public Parceiro parDatacadastro(ZonedDateTime parDatacadastro) {
        this.parDatacadastro = parDatacadastro;
        return this;
    }


    public Parceiro spaCodigo(Integer spaCodigo) {
        this.spaCodigo = spaCodigo;
        return this;
    }


    public String getLogradouro() {
        return logradouro;
    }

    public Parceiro logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public Parceiro cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public Parceiro cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public Parceiro estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Parceiro areaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
        return this;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public String getNumero() {
        return numero;
    }

    public Parceiro numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public Parceiro bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPorte() {
        return porte;
    }

    public Parceiro porte(String porte) {
        this.porte = porte;
        return this;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getAbertura() {
        return abertura;
    }

    public Parceiro abertura(String abertura) {
        this.abertura = abertura;
        return this;
    }

    public void setAbertura(String abertura) {
        this.abertura = abertura;
    }

    
    public Parceiro naturezaJuridica(String naturezaJuridica) {
        this.naturezaJuridica = naturezaJuridica;
        return this;
    }

    
    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public Parceiro ultimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
        return this;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getStatus() {
        return status;
    }

    public Parceiro status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public Parceiro tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComplemento() {
        return complemento;
    }

    public Parceiro complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public Parceiro email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Parceiro telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Parceiro dataSituacao(String dataSituacao) {
        this.dataSituacao = dataSituacao;
        return this;
    }

    public void setDataSituacao(String dataSituacao) {
        this.dataSituacao = dataSituacao;
    }

    public String getEfr() {
        return efr;
    }

    public Parceiro efr(String efr) {
        this.efr = efr;
        return this;
    }

    public void setEfr(String efr) {
        this.efr = efr;
    }

    public String getMotivoSituacao() {
        return motivoSituacao;
    }

    public Parceiro motivoSituacao(String motivoSituacao) {
        this.motivoSituacao = motivoSituacao;
        return this;
    }

    public Parceiro situacaoEspecial(String situacaoEspecial) {
        this.situacaoEspecial = situacaoEspecial;
        return this;
    }

    
    public Parceiro dataSituacaoEspecial(String dataSituacaoEspecial) {
        this.dataSituacaoEspecial = dataSituacaoEspecial;
        return this;
    }

    public Parceiro capitalSocial(String capitalSocial) {
        this.capitalSocial = capitalSocial;
        return this;
    }

    public void setCapitalSocial(String capitalSocial) {
        this.capitalSocial = capitalSocial;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Parceiro enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Atividade> getAtividades() {
    	if(atividades == null) {
    		atividades =  new HashSet<>();
    	}
        return atividades;
    }

    public Parceiro atividades(Set<Atividade> atividades) {
        this.atividades = atividades;
        return this;
    }

    public Parceiro addAtividade(Atividade atividade) {
        this.getAtividades().add(atividade);
        atividade.setParceiro(this);
        return this;
    }

    public Parceiro removeAtividade(Atividade atividade) {
        this.getAtividades().remove(atividade);
        atividade.setParceiro(null);
        return this;
    }

    public void setAtividades(Set<Atividade> atividades) {
        this.atividades = atividades;
    }

    public Set<Socio> getSocios() {
    	if(socios == null) {
    		socios = new HashSet<>();
    	}
        return socios;
    }

    public Parceiro socios(Set<Socio> socios) {
        this.socios = socios;
        return this;
    }

    public Parceiro addSocio(Socio socio) {
        this.getSocios().add(socio);
        socio.setParceiro(this);
        return this;
    }

    public Parceiro removeSocio(Socio socio) {
        this.getSocios().remove(socio);
        socio.setParceiro(null);
        return this;
    }

    public void setSocios(Set<Socio> socios) {
        this.socios = socios;
    }
    
    public Set<Agenciabancaria> getAgenciabancarias() {
    	if(agenciabancarias == null) {
    		agenciabancarias =  new HashSet<>();
    	}
        return agenciabancarias;
    }

    public Parceiro agenciabancarias(Set<Agenciabancaria> agenciabancarias) {
        this.agenciabancarias = agenciabancarias;
        return this;
    }

    public Parceiro addAgenciabancaria(Agenciabancaria agenciabancaria) {
        this.getAgenciabancarias().add(agenciabancaria);
        agenciabancaria.setParceiro(this);
        return this;
    }
    
    public Set<Integracao> getIntegracaos() {
    	if(integracaos == null) {
    		integracaos =  new HashSet<>();
    	}
        return integracaos;
    }

    public Parceiro integracaos(Set<Integracao> integracaos) {
        this.integracaos = integracaos;
        return this;
    }

    public Parceiro addIntegracao(Integracao integracao) {
        this.getIntegracaos().add(integracao);
        integracao.setParceiro(this);
        return this;
    }
    
    public Set<PermissaoParceiro> getPermissaos() {
    	if(permissaos == null) {
    		permissaos =  new HashSet<>();
    	}
        return permissaos;
    }

    public Parceiro permissaos(Set<PermissaoParceiro> permissaos) {
        this.permissaos = permissaos;
        return this;
    }

    public Parceiro addPermissao(PermissaoParceiro permissao) {
        this.getPermissaos().add(permissao);
        permissao.setParceiro(this);
        return this;
    }

    public Parceiro removeAgenciabancaria(Agenciabancaria agenciabancaria) {
        this.getAgenciabancarias().remove(agenciabancaria);
        agenciabancaria.setParceiro(null);
        return this;
    }

    public void setAgenciabancarias(Set<Agenciabancaria> agenciabancarias) {
        this.agenciabancarias = agenciabancarias;
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

	public String getNaturezaJuridica() {
		return naturezaJuridica;
	}

	public void setNaturezaJuridica(String naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
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

	public String getAreaAtuacao() {
		return areaAtuacao;
	}

	public String getDataSituacao() {
		return dataSituacao;
	}

	public String getCapitalSocial() {
		return capitalSocial;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setMotivoSituacao(String motivoSituacao) {
		this.motivoSituacao = motivoSituacao;
	}
	
	public String getCodExt() {
		return codExt;
	}

	public void setCodExt(String codExt) {
		this.codExt = codExt;
	}
	
	public Conta getDespesaJuros() {
		return despesaJuros;
	}

	public void setDespesaJuros(Conta despesaJuros) {
		this.despesaJuros = despesaJuros;
	}

	public Conta getDespesaIof() {
		return despesaIof;
	}

	public void setDespesaIof(Conta despesaIof) {
		this.despesaIof = despesaIof;
	}

	public Conta getJurosAtivos() {
		return jurosAtivos;
	}

	public void setJurosAtivos(Conta jurosAtivos) {
		this.jurosAtivos = jurosAtivos;
	}

	public Conta getDescontosAtivos() {
		return descontosAtivos;
	}

	public void setDescontosAtivos(Conta descontosAtivos) {
		this.descontosAtivos = descontosAtivos;
	}
	
	public Conta getCaixaConta() {
		return caixaConta;
	}

	public void setCaixaConta(Conta caixaConta) {
		this.caixaConta = caixaConta;
	}

	public Conta getDespesasBancarias() {
		return despesasBancarias;
	}

	public void setDespesasBancarias(Conta despesasBancarias) {
		this.despesasBancarias = despesasBancarias;
	}

	public Conta getDespesaTarifa() {
		return despesaTarifa;
	}

	public void setDespesaTarifa(Conta despesaTarifa) {
		this.despesaTarifa = despesaTarifa;
	}
	
	public Integer getCadastroStatus() {
		return cadastroStatus;
	}

	public void setCadastroStatus(Integer cadastroStatus) {
		this.cadastroStatus = cadastroStatus;
	}
	
	
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	



	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parceiro)) {
            return false;
        }
        return id != null && id.equals(((Parceiro) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "Parceiro [id=" + id + ", parDescricao=" + parDescricao + ", parRazaosocial=" + parRazaosocial
				+ ", parTipopessoa=" + parTipopessoa + ", parCnpjcpf=" + parCnpjcpf + ", parRgie=" + parRgie
				+ ", parObs=" + parObs + ", parDatacadastro=" + parDatacadastro + ", spaCodigo=" + spaCodigo
				+ ", logradouro=" + logradouro + ", cep=" + cep + ", cidade=" + cidade + ", estado=" + estado
				+ ", areaAtuacao=" + areaAtuacao + ", numero=" + numero + ", bairro=" + bairro + ", porte=" + porte
				+ ", abertura=" + abertura + ", naturezaJuridica=" + naturezaJuridica + ", ultimaAtualizacao="
				+ ultimaAtualizacao + ", status=" + status + ", tipo=" + tipo + ", complemento=" + complemento
				+ ", email=" + email + ", telefone=" + telefone + ", dataSituacao=" + dataSituacao + ", efr=" + efr
				+ ", motivoSituacao=" + motivoSituacao + ", situacaoEspecial=" + situacaoEspecial
				+ ", dataSituacaoEspecial=" + dataSituacaoEspecial + ", capitalSocial=" + capitalSocial + ", enabled="
				+ enabled  + "]";
	}
    
    

    
}
