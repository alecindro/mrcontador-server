package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

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
    private String par_descricao;

    @Size(max = 70)
    @Column(name = "par_razaosocial", length = 70)
    private String par_razaosocial;

    @Size(max = 1)
    @Column(name = "par_tipopessoa", length = 1)
    private String par_tipopessoa;

    @Size(max = 20)
    @Column(name = "par_cnpjcpf", length = 20)
    private String par_cnpjcpf;

    @Size(max = 20)
    @Column(name = "par_rgie", length = 20)
    private String par_rgie;

    @Size(max = 200)
    @Column(name = "par_obs", length = 200)
    private String par_obs;

    @Column(name = "par_datacadastro")
    private ZonedDateTime par_datacadastro;

    @Column(name = "spa_codigo")
    private Integer spa_codigo;

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
    private String area_atuacao;

    @NotNull
    @Column(name = "comercio", nullable = false)
    private Boolean comercio;

    @Column(name = "nfc_e")
    private Boolean nfc_e;

    @Column(name = "danfe")
    private Boolean danfe;

    @Column(name = "servico")
    private Boolean servico;

    @Column(name = "nfs_e")
    private Boolean nfs_e;

    @Column(name = "transportadora")
    private Boolean transportadora;

    @Column(name = "conhec_transporte")
    private Boolean conhec_transporte;

    @Column(name = "industria")
    private Boolean industria;

    @Column(name = "ct")
    private Boolean ct;

    @Column(name = "outras")
    private String outras;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPar_descricao() {
        return par_descricao;
    }

    public Parceiro par_descricao(String par_descricao) {
        this.par_descricao = par_descricao;
        return this;
    }

    public void setPar_descricao(String par_descricao) {
        this.par_descricao = par_descricao;
    }

    public String getPar_razaosocial() {
        return par_razaosocial;
    }

    public Parceiro par_razaosocial(String par_razaosocial) {
        this.par_razaosocial = par_razaosocial;
        return this;
    }

    public void setPar_razaosocial(String par_razaosocial) {
        this.par_razaosocial = par_razaosocial;
    }

    public String getPar_tipopessoa() {
        return par_tipopessoa;
    }

    public Parceiro par_tipopessoa(String par_tipopessoa) {
        this.par_tipopessoa = par_tipopessoa;
        return this;
    }

    public void setPar_tipopessoa(String par_tipopessoa) {
        this.par_tipopessoa = par_tipopessoa;
    }

    public String getPar_cnpjcpf() {
        return par_cnpjcpf;
    }

    public Parceiro par_cnpjcpf(String par_cnpjcpf) {
        this.par_cnpjcpf = par_cnpjcpf;
        return this;
    }

    public void setPar_cnpjcpf(String par_cnpjcpf) {
        this.par_cnpjcpf = par_cnpjcpf;
    }

    public String getPar_rgie() {
        return par_rgie;
    }

    public Parceiro par_rgie(String par_rgie) {
        this.par_rgie = par_rgie;
        return this;
    }

    public void setPar_rgie(String par_rgie) {
        this.par_rgie = par_rgie;
    }

    public String getPar_obs() {
        return par_obs;
    }

    public Parceiro par_obs(String par_obs) {
        this.par_obs = par_obs;
        return this;
    }

    public void setPar_obs(String par_obs) {
        this.par_obs = par_obs;
    }

    public ZonedDateTime getPar_datacadastro() {
        return par_datacadastro;
    }

    public Parceiro par_datacadastro(ZonedDateTime par_datacadastro) {
        this.par_datacadastro = par_datacadastro;
        return this;
    }

    public void setPar_datacadastro(ZonedDateTime par_datacadastro) {
        this.par_datacadastro = par_datacadastro;
    }

    public Integer getSpa_codigo() {
        return spa_codigo;
    }

    public Parceiro spa_codigo(Integer spa_codigo) {
        this.spa_codigo = spa_codigo;
        return this;
    }

    public void setSpa_codigo(Integer spa_codigo) {
        this.spa_codigo = spa_codigo;
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

    public String getArea_atuacao() {
        return area_atuacao;
    }

    public Parceiro area_atuacao(String area_atuacao) {
        this.area_atuacao = area_atuacao;
        return this;
    }

    public void setArea_atuacao(String area_atuacao) {
        this.area_atuacao = area_atuacao;
    }

    public Boolean isComercio() {
        return comercio;
    }

    public Parceiro comercio(Boolean comercio) {
        this.comercio = comercio;
        return this;
    }

    public void setComercio(Boolean comercio) {
        this.comercio = comercio;
    }

    public Boolean isNfc_e() {
        return nfc_e;
    }

    public Parceiro nfc_e(Boolean nfc_e) {
        this.nfc_e = nfc_e;
        return this;
    }

    public void setNfc_e(Boolean nfc_e) {
        this.nfc_e = nfc_e;
    }

    public Boolean isDanfe() {
        return danfe;
    }

    public Parceiro danfe(Boolean danfe) {
        this.danfe = danfe;
        return this;
    }

    public void setDanfe(Boolean danfe) {
        this.danfe = danfe;
    }

    public Boolean isServico() {
        return servico;
    }

    public Parceiro servico(Boolean servico) {
        this.servico = servico;
        return this;
    }

    public void setServico(Boolean servico) {
        this.servico = servico;
    }

    public Boolean isNfs_e() {
        return nfs_e;
    }

    public Parceiro nfs_e(Boolean nfs_e) {
        this.nfs_e = nfs_e;
        return this;
    }

    public void setNfs_e(Boolean nfs_e) {
        this.nfs_e = nfs_e;
    }

    public Boolean isTransportadora() {
        return transportadora;
    }

    public Parceiro transportadora(Boolean transportadora) {
        this.transportadora = transportadora;
        return this;
    }

    public void setTransportadora(Boolean transportadora) {
        this.transportadora = transportadora;
    }

    public Boolean isConhec_transporte() {
        return conhec_transporte;
    }

    public Parceiro conhec_transporte(Boolean conhec_transporte) {
        this.conhec_transporte = conhec_transporte;
        return this;
    }

    public void setConhec_transporte(Boolean conhec_transporte) {
        this.conhec_transporte = conhec_transporte;
    }

    public Boolean isIndustria() {
        return industria;
    }

    public Parceiro industria(Boolean industria) {
        this.industria = industria;
        return this;
    }

    public void setIndustria(Boolean industria) {
        this.industria = industria;
    }

    public Boolean isCt() {
        return ct;
    }

    public Parceiro ct(Boolean ct) {
        this.ct = ct;
        return this;
    }

    public void setCt(Boolean ct) {
        this.ct = ct;
    }

    public String getOutras() {
        return outras;
    }

    public Parceiro outras(String outras) {
        this.outras = outras;
        return this;
    }

    public void setOutras(String outras) {
        this.outras = outras;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "Parceiro{" +
            "id=" + getId() +
            ", par_descricao='" + getPar_descricao() + "'" +
            ", par_razaosocial='" + getPar_razaosocial() + "'" +
            ", par_tipopessoa='" + getPar_tipopessoa() + "'" +
            ", par_cnpjcpf='" + getPar_cnpjcpf() + "'" +
            ", par_rgie='" + getPar_rgie() + "'" +
            ", par_obs='" + getPar_obs() + "'" +
            ", par_datacadastro='" + getPar_datacadastro() + "'" +
            ", spa_codigo=" + getSpa_codigo() +
            ", logradouro='" + getLogradouro() + "'" +
            ", cep='" + getCep() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", area_atuacao='" + getArea_atuacao() + "'" +
            ", comercio='" + isComercio() + "'" +
            ", nfc_e='" + isNfc_e() + "'" +
            ", danfe='" + isDanfe() + "'" +
            ", servico='" + isServico() + "'" +
            ", nfs_e='" + isNfs_e() + "'" +
            ", transportadora='" + isTransportadora() + "'" +
            ", conhec_transporte='" + isConhec_transporte() + "'" +
            ", industria='" + isIndustria() + "'" +
            ", ct='" + isCt() + "'" +
            ", outras='" + getOutras() + "'" +
            "}";
    }
}
