package br.com.mrcontador.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Parceiro} entity.
 */
public class ParceiroDTO implements Serializable {
    
    private Long id;

    @Size(max = 50)
    private String par_descricao;

    @Size(max = 70)
    private String par_razaosocial;

    @Size(max = 1)
    private String par_tipopessoa;

    @Size(max = 20)
    private String par_cnpjcpf;

    @Size(max = 20)
    private String par_rgie;

    @Size(max = 200)
    private String par_obs;

    private ZonedDateTime par_datacadastro;

    private Integer spa_codigo;

    private String logradouro;

    @Size(max = 8)
    private String cep;

    private String cidade;

    private String estado;

    private String area_atuacao;

    @NotNull
    private Boolean comercio;

    private Boolean nfc_e;

    private Boolean danfe;

    private Boolean servico;

    private Boolean nfs_e;

    private Boolean transportadora;

    private Boolean conhec_transporte;

    private Boolean industria;

    private Boolean ct;

    private String outras;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPar_descricao() {
        return par_descricao;
    }

    public void setPar_descricao(String par_descricao) {
        this.par_descricao = par_descricao;
    }

    public String getPar_razaosocial() {
        return par_razaosocial;
    }

    public void setPar_razaosocial(String par_razaosocial) {
        this.par_razaosocial = par_razaosocial;
    }

    public String getPar_tipopessoa() {
        return par_tipopessoa;
    }

    public void setPar_tipopessoa(String par_tipopessoa) {
        this.par_tipopessoa = par_tipopessoa;
    }

    public String getPar_cnpjcpf() {
        return par_cnpjcpf;
    }

    public void setPar_cnpjcpf(String par_cnpjcpf) {
        this.par_cnpjcpf = par_cnpjcpf;
    }

    public String getPar_rgie() {
        return par_rgie;
    }

    public void setPar_rgie(String par_rgie) {
        this.par_rgie = par_rgie;
    }

    public String getPar_obs() {
        return par_obs;
    }

    public void setPar_obs(String par_obs) {
        this.par_obs = par_obs;
    }

    public ZonedDateTime getPar_datacadastro() {
        return par_datacadastro;
    }

    public void setPar_datacadastro(ZonedDateTime par_datacadastro) {
        this.par_datacadastro = par_datacadastro;
    }

    public Integer getSpa_codigo() {
        return spa_codigo;
    }

    public void setSpa_codigo(Integer spa_codigo) {
        this.spa_codigo = spa_codigo;
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

    public String getArea_atuacao() {
        return area_atuacao;
    }

    public void setArea_atuacao(String area_atuacao) {
        this.area_atuacao = area_atuacao;
    }

    public Boolean isComercio() {
        return comercio;
    }

    public void setComercio(Boolean comercio) {
        this.comercio = comercio;
    }

    public Boolean isNfc_e() {
        return nfc_e;
    }

    public void setNfc_e(Boolean nfc_e) {
        this.nfc_e = nfc_e;
    }

    public Boolean isDanfe() {
        return danfe;
    }

    public void setDanfe(Boolean danfe) {
        this.danfe = danfe;
    }

    public Boolean isServico() {
        return servico;
    }

    public void setServico(Boolean servico) {
        this.servico = servico;
    }

    public Boolean isNfs_e() {
        return nfs_e;
    }

    public void setNfs_e(Boolean nfs_e) {
        this.nfs_e = nfs_e;
    }

    public Boolean isTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Boolean transportadora) {
        this.transportadora = transportadora;
    }

    public Boolean isConhec_transporte() {
        return conhec_transporte;
    }

    public void setConhec_transporte(Boolean conhec_transporte) {
        this.conhec_transporte = conhec_transporte;
    }

    public Boolean isIndustria() {
        return industria;
    }

    public void setIndustria(Boolean industria) {
        this.industria = industria;
    }

    public Boolean isCt() {
        return ct;
    }

    public void setCt(Boolean ct) {
        this.ct = ct;
    }

    public String getOutras() {
        return outras;
    }

    public void setOutras(String outras) {
        this.outras = outras;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "ParceiroDTO{" +
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
