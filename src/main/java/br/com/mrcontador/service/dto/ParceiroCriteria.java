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

    private StringFilter numero;

    private StringFilter bairro;

    private StringFilter porte;

    private StringFilter abertura;

    private StringFilter natureza_juridica;

    private StringFilter ultimaAtualizacao;

    private StringFilter status;

    private StringFilter tipo;

    private StringFilter complemento;

    private StringFilter email;

    private StringFilter telefone;

    private StringFilter data_situacao;

    private StringFilter efr;

    private StringFilter motivo_situacao;

    private StringFilter situacao_especial;

    private StringFilter data_situacao_especial;

    private StringFilter capital_social;

    private LongFilter atividadeId;

    private LongFilter socioId;

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
        this.numero = other.numero == null ? null : other.numero.copy();
        this.bairro = other.bairro == null ? null : other.bairro.copy();
        this.porte = other.porte == null ? null : other.porte.copy();
        this.abertura = other.abertura == null ? null : other.abertura.copy();
        this.natureza_juridica = other.natureza_juridica == null ? null : other.natureza_juridica.copy();
        this.ultimaAtualizacao = other.ultimaAtualizacao == null ? null : other.ultimaAtualizacao.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.complemento = other.complemento == null ? null : other.complemento.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.data_situacao = other.data_situacao == null ? null : other.data_situacao.copy();
        this.efr = other.efr == null ? null : other.efr.copy();
        this.motivo_situacao = other.motivo_situacao == null ? null : other.motivo_situacao.copy();
        this.situacao_especial = other.situacao_especial == null ? null : other.situacao_especial.copy();
        this.data_situacao_especial = other.data_situacao_especial == null ? null : other.data_situacao_especial.copy();
        this.capital_social = other.capital_social == null ? null : other.capital_social.copy();
        this.atividadeId = other.atividadeId == null ? null : other.atividadeId.copy();
        this.socioId = other.socioId == null ? null : other.socioId.copy();
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

    public StringFilter getNumero() {
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
    }

    public StringFilter getPorte() {
        return porte;
    }

    public void setPorte(StringFilter porte) {
        this.porte = porte;
    }

    public StringFilter getAbertura() {
        return abertura;
    }

    public void setAbertura(StringFilter abertura) {
        this.abertura = abertura;
    }

    public StringFilter getNatureza_juridica() {
        return natureza_juridica;
    }

    public void setNatureza_juridica(StringFilter natureza_juridica) {
        this.natureza_juridica = natureza_juridica;
    }

    public StringFilter getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(StringFilter ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getComplemento() {
        return complemento;
    }

    public void setComplemento(StringFilter complemento) {
        this.complemento = complemento;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getData_situacao() {
        return data_situacao;
    }

    public void setData_situacao(StringFilter data_situacao) {
        this.data_situacao = data_situacao;
    }

    public StringFilter getEfr() {
        return efr;
    }

    public void setEfr(StringFilter efr) {
        this.efr = efr;
    }

    public StringFilter getMotivo_situacao() {
        return motivo_situacao;
    }

    public void setMotivo_situacao(StringFilter motivo_situacao) {
        this.motivo_situacao = motivo_situacao;
    }

    public StringFilter getSituacao_especial() {
        return situacao_especial;
    }

    public void setSituacao_especial(StringFilter situacao_especial) {
        this.situacao_especial = situacao_especial;
    }

    public StringFilter getData_situacao_especial() {
        return data_situacao_especial;
    }

    public void setData_situacao_especial(StringFilter data_situacao_especial) {
        this.data_situacao_especial = data_situacao_especial;
    }

    public StringFilter getCapital_social() {
        return capital_social;
    }

    public void setCapital_social(StringFilter capital_social) {
        this.capital_social = capital_social;
    }

    public LongFilter getAtividadeId() {
        return atividadeId;
    }

    public void setAtividadeId(LongFilter atividadeId) {
        this.atividadeId = atividadeId;
    }

    public LongFilter getSocioId() {
        return socioId;
    }

    public void setSocioId(LongFilter socioId) {
        this.socioId = socioId;
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
            Objects.equals(numero, that.numero) &&
            Objects.equals(bairro, that.bairro) &&
            Objects.equals(porte, that.porte) &&
            Objects.equals(abertura, that.abertura) &&
            Objects.equals(natureza_juridica, that.natureza_juridica) &&
            Objects.equals(ultimaAtualizacao, that.ultimaAtualizacao) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(complemento, that.complemento) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(data_situacao, that.data_situacao) &&
            Objects.equals(efr, that.efr) &&
            Objects.equals(motivo_situacao, that.motivo_situacao) &&
            Objects.equals(situacao_especial, that.situacao_especial) &&
            Objects.equals(data_situacao_especial, that.data_situacao_especial) &&
            Objects.equals(capital_social, that.capital_social) &&
            Objects.equals(atividadeId, that.atividadeId) &&
            Objects.equals(socioId, that.socioId);
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
        numero,
        bairro,
        porte,
        abertura,
        natureza_juridica,
        ultimaAtualizacao,
        status,
        tipo,
        complemento,
        email,
        telefone,
        data_situacao,
        efr,
        motivo_situacao,
        situacao_especial,
        data_situacao_especial,
        capital_social,
        atividadeId,
        socioId
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
                (numero != null ? "numero=" + numero + ", " : "") +
                (bairro != null ? "bairro=" + bairro + ", " : "") +
                (porte != null ? "porte=" + porte + ", " : "") +
                (abertura != null ? "abertura=" + abertura + ", " : "") +
                (natureza_juridica != null ? "natureza_juridica=" + natureza_juridica + ", " : "") +
                (ultimaAtualizacao != null ? "ultimaAtualizacao=" + ultimaAtualizacao + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (complemento != null ? "complemento=" + complemento + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (telefone != null ? "telefone=" + telefone + ", " : "") +
                (data_situacao != null ? "data_situacao=" + data_situacao + ", " : "") +
                (efr != null ? "efr=" + efr + ", " : "") +
                (motivo_situacao != null ? "motivo_situacao=" + motivo_situacao + ", " : "") +
                (situacao_especial != null ? "situacao_especial=" + situacao_especial + ", " : "") +
                (data_situacao_especial != null ? "data_situacao_especial=" + data_situacao_especial + ", " : "") +
                (capital_social != null ? "capital_social=" + capital_social + ", " : "") +
                (atividadeId != null ? "atividadeId=" + atividadeId + ", " : "") +
                (socioId != null ? "socioId=" + socioId + ", " : "") +
            "}";
    }

}
