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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Parceiro} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ParceiroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parceiros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParceiroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter par_descricao;

    private StringFilter par_razaosocial;

    private StringFilter par_tipopessoa;

    private StringFilter par_cnpjcpf;

    private StringFilter par_rgie;

    private StringFilter par_obs;

    private ZonedDateTimeFilter par_datacadastro;

    private IntegerFilter spa_codigo;

    private StringFilter logradouro;

    private StringFilter cep;

    private StringFilter cidade;

    private StringFilter estado;

    private StringFilter area_atuacao;

    private BooleanFilter comercio;

    private BooleanFilter nfc_e;

    private BooleanFilter danfe;

    private BooleanFilter servico;

    private BooleanFilter nfs_e;

    private BooleanFilter transportadora;

    private BooleanFilter conhec_transporte;

    private BooleanFilter industria;

    private BooleanFilter ct;

    private StringFilter outras;

    public ParceiroCriteria() {
    }

    public ParceiroCriteria(ParceiroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.par_descricao = other.par_descricao == null ? null : other.par_descricao.copy();
        this.par_razaosocial = other.par_razaosocial == null ? null : other.par_razaosocial.copy();
        this.par_tipopessoa = other.par_tipopessoa == null ? null : other.par_tipopessoa.copy();
        this.par_cnpjcpf = other.par_cnpjcpf == null ? null : other.par_cnpjcpf.copy();
        this.par_rgie = other.par_rgie == null ? null : other.par_rgie.copy();
        this.par_obs = other.par_obs == null ? null : other.par_obs.copy();
        this.par_datacadastro = other.par_datacadastro == null ? null : other.par_datacadastro.copy();
        this.spa_codigo = other.spa_codigo == null ? null : other.spa_codigo.copy();
        this.logradouro = other.logradouro == null ? null : other.logradouro.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.cidade = other.cidade == null ? null : other.cidade.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.area_atuacao = other.area_atuacao == null ? null : other.area_atuacao.copy();
        this.comercio = other.comercio == null ? null : other.comercio.copy();
        this.nfc_e = other.nfc_e == null ? null : other.nfc_e.copy();
        this.danfe = other.danfe == null ? null : other.danfe.copy();
        this.servico = other.servico == null ? null : other.servico.copy();
        this.nfs_e = other.nfs_e == null ? null : other.nfs_e.copy();
        this.transportadora = other.transportadora == null ? null : other.transportadora.copy();
        this.conhec_transporte = other.conhec_transporte == null ? null : other.conhec_transporte.copy();
        this.industria = other.industria == null ? null : other.industria.copy();
        this.ct = other.ct == null ? null : other.ct.copy();
        this.outras = other.outras == null ? null : other.outras.copy();
    }

    @Override
    public ParceiroCriteria copy() {
        return new ParceiroCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPar_descricao() {
        return par_descricao;
    }

    public void setPar_descricao(StringFilter par_descricao) {
        this.par_descricao = par_descricao;
    }

    public StringFilter getPar_razaosocial() {
        return par_razaosocial;
    }

    public void setPar_razaosocial(StringFilter par_razaosocial) {
        this.par_razaosocial = par_razaosocial;
    }

    public StringFilter getPar_tipopessoa() {
        return par_tipopessoa;
    }

    public void setPar_tipopessoa(StringFilter par_tipopessoa) {
        this.par_tipopessoa = par_tipopessoa;
    }

    public StringFilter getPar_cnpjcpf() {
        return par_cnpjcpf;
    }

    public void setPar_cnpjcpf(StringFilter par_cnpjcpf) {
        this.par_cnpjcpf = par_cnpjcpf;
    }

    public StringFilter getPar_rgie() {
        return par_rgie;
    }

    public void setPar_rgie(StringFilter par_rgie) {
        this.par_rgie = par_rgie;
    }

    public StringFilter getPar_obs() {
        return par_obs;
    }

    public void setPar_obs(StringFilter par_obs) {
        this.par_obs = par_obs;
    }

    public ZonedDateTimeFilter getPar_datacadastro() {
        return par_datacadastro;
    }

    public void setPar_datacadastro(ZonedDateTimeFilter par_datacadastro) {
        this.par_datacadastro = par_datacadastro;
    }

    public IntegerFilter getSpa_codigo() {
        return spa_codigo;
    }

    public void setSpa_codigo(IntegerFilter spa_codigo) {
        this.spa_codigo = spa_codigo;
    }

    public StringFilter getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(StringFilter logradouro) {
        this.logradouro = logradouro;
    }

    public StringFilter getCep() {
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getArea_atuacao() {
        return area_atuacao;
    }

    public void setArea_atuacao(StringFilter area_atuacao) {
        this.area_atuacao = area_atuacao;
    }

    public BooleanFilter getComercio() {
        return comercio;
    }

    public void setComercio(BooleanFilter comercio) {
        this.comercio = comercio;
    }

    public BooleanFilter getNfc_e() {
        return nfc_e;
    }

    public void setNfc_e(BooleanFilter nfc_e) {
        this.nfc_e = nfc_e;
    }

    public BooleanFilter getDanfe() {
        return danfe;
    }

    public void setDanfe(BooleanFilter danfe) {
        this.danfe = danfe;
    }

    public BooleanFilter getServico() {
        return servico;
    }

    public void setServico(BooleanFilter servico) {
        this.servico = servico;
    }

    public BooleanFilter getNfs_e() {
        return nfs_e;
    }

    public void setNfs_e(BooleanFilter nfs_e) {
        this.nfs_e = nfs_e;
    }

    public BooleanFilter getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(BooleanFilter transportadora) {
        this.transportadora = transportadora;
    }

    public BooleanFilter getConhec_transporte() {
        return conhec_transporte;
    }

    public void setConhec_transporte(BooleanFilter conhec_transporte) {
        this.conhec_transporte = conhec_transporte;
    }

    public BooleanFilter getIndustria() {
        return industria;
    }

    public void setIndustria(BooleanFilter industria) {
        this.industria = industria;
    }

    public BooleanFilter getCt() {
        return ct;
    }

    public void setCt(BooleanFilter ct) {
        this.ct = ct;
    }

    public StringFilter getOutras() {
        return outras;
    }

    public void setOutras(StringFilter outras) {
        this.outras = outras;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParceiroCriteria that = (ParceiroCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(par_descricao, that.par_descricao) &&
            Objects.equals(par_razaosocial, that.par_razaosocial) &&
            Objects.equals(par_tipopessoa, that.par_tipopessoa) &&
            Objects.equals(par_cnpjcpf, that.par_cnpjcpf) &&
            Objects.equals(par_rgie, that.par_rgie) &&
            Objects.equals(par_obs, that.par_obs) &&
            Objects.equals(par_datacadastro, that.par_datacadastro) &&
            Objects.equals(spa_codigo, that.spa_codigo) &&
            Objects.equals(logradouro, that.logradouro) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(area_atuacao, that.area_atuacao) &&
            Objects.equals(comercio, that.comercio) &&
            Objects.equals(nfc_e, that.nfc_e) &&
            Objects.equals(danfe, that.danfe) &&
            Objects.equals(servico, that.servico) &&
            Objects.equals(nfs_e, that.nfs_e) &&
            Objects.equals(transportadora, that.transportadora) &&
            Objects.equals(conhec_transporte, that.conhec_transporte) &&
            Objects.equals(industria, that.industria) &&
            Objects.equals(ct, that.ct) &&
            Objects.equals(outras, that.outras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        par_descricao,
        par_razaosocial,
        par_tipopessoa,
        par_cnpjcpf,
        par_rgie,
        par_obs,
        par_datacadastro,
        spa_codigo,
        logradouro,
        cep,
        cidade,
        estado,
        area_atuacao,
        comercio,
        nfc_e,
        danfe,
        servico,
        nfs_e,
        transportadora,
        conhec_transporte,
        industria,
        ct,
        outras
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParceiroCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (par_descricao != null ? "par_descricao=" + par_descricao + ", " : "") +
                (par_razaosocial != null ? "par_razaosocial=" + par_razaosocial + ", " : "") +
                (par_tipopessoa != null ? "par_tipopessoa=" + par_tipopessoa + ", " : "") +
                (par_cnpjcpf != null ? "par_cnpjcpf=" + par_cnpjcpf + ", " : "") +
                (par_rgie != null ? "par_rgie=" + par_rgie + ", " : "") +
                (par_obs != null ? "par_obs=" + par_obs + ", " : "") +
                (par_datacadastro != null ? "par_datacadastro=" + par_datacadastro + ", " : "") +
                (spa_codigo != null ? "spa_codigo=" + spa_codigo + ", " : "") +
                (logradouro != null ? "logradouro=" + logradouro + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (cidade != null ? "cidade=" + cidade + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (area_atuacao != null ? "area_atuacao=" + area_atuacao + ", " : "") +
                (comercio != null ? "comercio=" + comercio + ", " : "") +
                (nfc_e != null ? "nfc_e=" + nfc_e + ", " : "") +
                (danfe != null ? "danfe=" + danfe + ", " : "") +
                (servico != null ? "servico=" + servico + ", " : "") +
                (nfs_e != null ? "nfs_e=" + nfs_e + ", " : "") +
                (transportadora != null ? "transportadora=" + transportadora + ", " : "") +
                (conhec_transporte != null ? "conhec_transporte=" + conhec_transporte + ", " : "") +
                (industria != null ? "industria=" + industria + ", " : "") +
                (ct != null ? "ct=" + ct + ", " : "") +
                (outras != null ? "outras=" + outras + ", " : "") +
            "}";
    }

}
