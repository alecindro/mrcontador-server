package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.ParceiroRepository;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.ParceiroDTO;
import br.com.mrcontador.service.mapper.ParceiroMapper;
import br.com.mrcontador.service.dto.ParceiroCriteria;
import br.com.mrcontador.service.ParceiroQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.com.mrcontador.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParceiroResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParceiroResourceIT {

    private static final String DEFAULT_PAR_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_PAR_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_PAR_RAZAOSOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_PAR_RAZAOSOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_PAR_TIPOPESSOA = "A";
    private static final String UPDATED_PAR_TIPOPESSOA = "B";

    private static final String DEFAULT_PAR_CNPJCPF = "AAAAAAAAAA";
    private static final String UPDATED_PAR_CNPJCPF = "BBBBBBBBBB";

    private static final String DEFAULT_PAR_RGIE = "AAAAAAAAAA";
    private static final String UPDATED_PAR_RGIE = "BBBBBBBBBB";

    private static final String DEFAULT_PAR_OBS = "AAAAAAAAAA";
    private static final String UPDATED_PAR_OBS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PAR_DATACADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAR_DATACADASTRO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PAR_DATACADASTRO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_SPA_CODIGO = 1;
    private static final Integer UPDATED_SPA_CODIGO = 2;
    private static final Integer SMALLER_SPA_CODIGO = 1 - 1;

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_ATUACAO = "AAAAAAAAAA";
    private static final String UPDATED_AREA_ATUACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMERCIO = false;
    private static final Boolean UPDATED_COMERCIO = true;

    private static final Boolean DEFAULT_NFC_E = false;
    private static final Boolean UPDATED_NFC_E = true;

    private static final Boolean DEFAULT_DANFE = false;
    private static final Boolean UPDATED_DANFE = true;

    private static final Boolean DEFAULT_SERVICO = false;
    private static final Boolean UPDATED_SERVICO = true;

    private static final Boolean DEFAULT_NFS_E = false;
    private static final Boolean UPDATED_NFS_E = true;

    private static final Boolean DEFAULT_TRANSPORTADORA = false;
    private static final Boolean UPDATED_TRANSPORTADORA = true;

    private static final Boolean DEFAULT_CONHEC_TRANSPORTE = false;
    private static final Boolean UPDATED_CONHEC_TRANSPORTE = true;

    private static final Boolean DEFAULT_INDUSTRIA = false;
    private static final Boolean UPDATED_INDUSTRIA = true;

    private static final Boolean DEFAULT_CT = false;
    private static final Boolean UPDATED_CT = true;

    private static final String DEFAULT_OUTRAS = "AAAAAAAAAA";
    private static final String UPDATED_OUTRAS = "BBBBBBBBBB";

    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ParceiroMapper parceiroMapper;

    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private ParceiroQueryService parceiroQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParceiroMockMvc;

    private Parceiro parceiro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parceiro createEntity(EntityManager em) {
        Parceiro parceiro = new Parceiro()
            .par_descricao(DEFAULT_PAR_DESCRICAO)
            .par_razaosocial(DEFAULT_PAR_RAZAOSOCIAL)
            .par_tipopessoa(DEFAULT_PAR_TIPOPESSOA)
            .par_cnpjcpf(DEFAULT_PAR_CNPJCPF)
            .par_rgie(DEFAULT_PAR_RGIE)
            .par_obs(DEFAULT_PAR_OBS)
            .par_datacadastro(DEFAULT_PAR_DATACADASTRO)
            .spa_codigo(DEFAULT_SPA_CODIGO)
            .logradouro(DEFAULT_LOGRADOURO)
            .cep(DEFAULT_CEP)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .area_atuacao(DEFAULT_AREA_ATUACAO)
            .comercio(DEFAULT_COMERCIO)
            .nfc_e(DEFAULT_NFC_E)
            .danfe(DEFAULT_DANFE)
            .servico(DEFAULT_SERVICO)
            .nfs_e(DEFAULT_NFS_E)
            .transportadora(DEFAULT_TRANSPORTADORA)
            .conhec_transporte(DEFAULT_CONHEC_TRANSPORTE)
            .industria(DEFAULT_INDUSTRIA)
            .ct(DEFAULT_CT)
            .outras(DEFAULT_OUTRAS);
        return parceiro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parceiro createUpdatedEntity(EntityManager em) {
        Parceiro parceiro = new Parceiro()
            .par_descricao(UPDATED_PAR_DESCRICAO)
            .par_razaosocial(UPDATED_PAR_RAZAOSOCIAL)
            .par_tipopessoa(UPDATED_PAR_TIPOPESSOA)
            .par_cnpjcpf(UPDATED_PAR_CNPJCPF)
            .par_rgie(UPDATED_PAR_RGIE)
            .par_obs(UPDATED_PAR_OBS)
            .par_datacadastro(UPDATED_PAR_DATACADASTRO)
            .spa_codigo(UPDATED_SPA_CODIGO)
            .logradouro(UPDATED_LOGRADOURO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .area_atuacao(UPDATED_AREA_ATUACAO)
            .comercio(UPDATED_COMERCIO)
            .nfc_e(UPDATED_NFC_E)
            .danfe(UPDATED_DANFE)
            .servico(UPDATED_SERVICO)
            .nfs_e(UPDATED_NFS_E)
            .transportadora(UPDATED_TRANSPORTADORA)
            .conhec_transporte(UPDATED_CONHEC_TRANSPORTE)
            .industria(UPDATED_INDUSTRIA)
            .ct(UPDATED_CT)
            .outras(UPDATED_OUTRAS);
        return parceiro;
    }

    @BeforeEach
    public void initTest() {
        parceiro = createEntity(em);
    }

    @Test
    @Transactional
    public void createParceiro() throws Exception {
        int databaseSizeBeforeCreate = parceiroRepository.findAll().size();
        // Create the Parceiro
        ParceiroDTO parceiroDTO = parceiroMapper.toDto(parceiro);
        restParceiroMockMvc.perform(post("/api/parceiros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parceiroDTO)))
            .andExpect(status().isCreated());

        // Validate the Parceiro in the database
        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeCreate + 1);
        Parceiro testParceiro = parceiroList.get(parceiroList.size() - 1);
        assertThat(testParceiro.getPar_descricao()).isEqualTo(DEFAULT_PAR_DESCRICAO);
        assertThat(testParceiro.getPar_razaosocial()).isEqualTo(DEFAULT_PAR_RAZAOSOCIAL);
        assertThat(testParceiro.getPar_tipopessoa()).isEqualTo(DEFAULT_PAR_TIPOPESSOA);
        assertThat(testParceiro.getPar_cnpjcpf()).isEqualTo(DEFAULT_PAR_CNPJCPF);
        assertThat(testParceiro.getPar_rgie()).isEqualTo(DEFAULT_PAR_RGIE);
        assertThat(testParceiro.getPar_obs()).isEqualTo(DEFAULT_PAR_OBS);
        assertThat(testParceiro.getPar_datacadastro()).isEqualTo(DEFAULT_PAR_DATACADASTRO);
        assertThat(testParceiro.getSpa_codigo()).isEqualTo(DEFAULT_SPA_CODIGO);
        assertThat(testParceiro.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testParceiro.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testParceiro.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testParceiro.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testParceiro.getArea_atuacao()).isEqualTo(DEFAULT_AREA_ATUACAO);
        assertThat(testParceiro.isComercio()).isEqualTo(DEFAULT_COMERCIO);
        assertThat(testParceiro.isNfc_e()).isEqualTo(DEFAULT_NFC_E);
        assertThat(testParceiro.isDanfe()).isEqualTo(DEFAULT_DANFE);
        assertThat(testParceiro.isServico()).isEqualTo(DEFAULT_SERVICO);
        assertThat(testParceiro.isNfs_e()).isEqualTo(DEFAULT_NFS_E);
        assertThat(testParceiro.isTransportadora()).isEqualTo(DEFAULT_TRANSPORTADORA);
        assertThat(testParceiro.isConhec_transporte()).isEqualTo(DEFAULT_CONHEC_TRANSPORTE);
        assertThat(testParceiro.isIndustria()).isEqualTo(DEFAULT_INDUSTRIA);
        assertThat(testParceiro.isCt()).isEqualTo(DEFAULT_CT);
        assertThat(testParceiro.getOutras()).isEqualTo(DEFAULT_OUTRAS);
    }

    @Test
    @Transactional
    public void createParceiroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parceiroRepository.findAll().size();

        // Create the Parceiro with an existing ID
        parceiro.setId(1L);
        ParceiroDTO parceiroDTO = parceiroMapper.toDto(parceiro);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParceiroMockMvc.perform(post("/api/parceiros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parceiroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parceiro in the database
        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkComercioIsRequired() throws Exception {
        int databaseSizeBeforeTest = parceiroRepository.findAll().size();
        // set the field null
        parceiro.setComercio(null);

        // Create the Parceiro, which fails.
        ParceiroDTO parceiroDTO = parceiroMapper.toDto(parceiro);


        restParceiroMockMvc.perform(post("/api/parceiros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parceiroDTO)))
            .andExpect(status().isBadRequest());

        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParceiros() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList
        restParceiroMockMvc.perform(get("/api/parceiros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parceiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_descricao").value(hasItem(DEFAULT_PAR_DESCRICAO)))
            .andExpect(jsonPath("$.[*].par_razaosocial").value(hasItem(DEFAULT_PAR_RAZAOSOCIAL)))
            .andExpect(jsonPath("$.[*].par_tipopessoa").value(hasItem(DEFAULT_PAR_TIPOPESSOA)))
            .andExpect(jsonPath("$.[*].par_cnpjcpf").value(hasItem(DEFAULT_PAR_CNPJCPF)))
            .andExpect(jsonPath("$.[*].par_rgie").value(hasItem(DEFAULT_PAR_RGIE)))
            .andExpect(jsonPath("$.[*].par_obs").value(hasItem(DEFAULT_PAR_OBS)))
            .andExpect(jsonPath("$.[*].par_datacadastro").value(hasItem(sameInstant(DEFAULT_PAR_DATACADASTRO))))
            .andExpect(jsonPath("$.[*].spa_codigo").value(hasItem(DEFAULT_SPA_CODIGO)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].area_atuacao").value(hasItem(DEFAULT_AREA_ATUACAO)))
            .andExpect(jsonPath("$.[*].comercio").value(hasItem(DEFAULT_COMERCIO.booleanValue())))
            .andExpect(jsonPath("$.[*].nfc_e").value(hasItem(DEFAULT_NFC_E.booleanValue())))
            .andExpect(jsonPath("$.[*].danfe").value(hasItem(DEFAULT_DANFE.booleanValue())))
            .andExpect(jsonPath("$.[*].servico").value(hasItem(DEFAULT_SERVICO.booleanValue())))
            .andExpect(jsonPath("$.[*].nfs_e").value(hasItem(DEFAULT_NFS_E.booleanValue())))
            .andExpect(jsonPath("$.[*].transportadora").value(hasItem(DEFAULT_TRANSPORTADORA.booleanValue())))
            .andExpect(jsonPath("$.[*].conhec_transporte").value(hasItem(DEFAULT_CONHEC_TRANSPORTE.booleanValue())))
            .andExpect(jsonPath("$.[*].industria").value(hasItem(DEFAULT_INDUSTRIA.booleanValue())))
            .andExpect(jsonPath("$.[*].ct").value(hasItem(DEFAULT_CT.booleanValue())))
            .andExpect(jsonPath("$.[*].outras").value(hasItem(DEFAULT_OUTRAS)));
    }
    
    @Test
    @Transactional
    public void getParceiro() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get the parceiro
        restParceiroMockMvc.perform(get("/api/parceiros/{id}", parceiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parceiro.getId().intValue()))
            .andExpect(jsonPath("$.par_descricao").value(DEFAULT_PAR_DESCRICAO))
            .andExpect(jsonPath("$.par_razaosocial").value(DEFAULT_PAR_RAZAOSOCIAL))
            .andExpect(jsonPath("$.par_tipopessoa").value(DEFAULT_PAR_TIPOPESSOA))
            .andExpect(jsonPath("$.par_cnpjcpf").value(DEFAULT_PAR_CNPJCPF))
            .andExpect(jsonPath("$.par_rgie").value(DEFAULT_PAR_RGIE))
            .andExpect(jsonPath("$.par_obs").value(DEFAULT_PAR_OBS))
            .andExpect(jsonPath("$.par_datacadastro").value(sameInstant(DEFAULT_PAR_DATACADASTRO)))
            .andExpect(jsonPath("$.spa_codigo").value(DEFAULT_SPA_CODIGO))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.area_atuacao").value(DEFAULT_AREA_ATUACAO))
            .andExpect(jsonPath("$.comercio").value(DEFAULT_COMERCIO.booleanValue()))
            .andExpect(jsonPath("$.nfc_e").value(DEFAULT_NFC_E.booleanValue()))
            .andExpect(jsonPath("$.danfe").value(DEFAULT_DANFE.booleanValue()))
            .andExpect(jsonPath("$.servico").value(DEFAULT_SERVICO.booleanValue()))
            .andExpect(jsonPath("$.nfs_e").value(DEFAULT_NFS_E.booleanValue()))
            .andExpect(jsonPath("$.transportadora").value(DEFAULT_TRANSPORTADORA.booleanValue()))
            .andExpect(jsonPath("$.conhec_transporte").value(DEFAULT_CONHEC_TRANSPORTE.booleanValue()))
            .andExpect(jsonPath("$.industria").value(DEFAULT_INDUSTRIA.booleanValue()))
            .andExpect(jsonPath("$.ct").value(DEFAULT_CT.booleanValue()))
            .andExpect(jsonPath("$.outras").value(DEFAULT_OUTRAS));
    }


    @Test
    @Transactional
    public void getParceirosByIdFiltering() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        Long id = parceiro.getId();

        defaultParceiroShouldBeFound("id.equals=" + id);
        defaultParceiroShouldNotBeFound("id.notEquals=" + id);

        defaultParceiroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParceiroShouldNotBeFound("id.greaterThan=" + id);

        defaultParceiroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParceiroShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao equals to DEFAULT_PAR_DESCRICAO
        defaultParceiroShouldBeFound("par_descricao.equals=" + DEFAULT_PAR_DESCRICAO);

        // Get all the parceiroList where par_descricao equals to UPDATED_PAR_DESCRICAO
        defaultParceiroShouldNotBeFound("par_descricao.equals=" + UPDATED_PAR_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao not equals to DEFAULT_PAR_DESCRICAO
        defaultParceiroShouldNotBeFound("par_descricao.notEquals=" + DEFAULT_PAR_DESCRICAO);

        // Get all the parceiroList where par_descricao not equals to UPDATED_PAR_DESCRICAO
        defaultParceiroShouldBeFound("par_descricao.notEquals=" + UPDATED_PAR_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao in DEFAULT_PAR_DESCRICAO or UPDATED_PAR_DESCRICAO
        defaultParceiroShouldBeFound("par_descricao.in=" + DEFAULT_PAR_DESCRICAO + "," + UPDATED_PAR_DESCRICAO);

        // Get all the parceiroList where par_descricao equals to UPDATED_PAR_DESCRICAO
        defaultParceiroShouldNotBeFound("par_descricao.in=" + UPDATED_PAR_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao is not null
        defaultParceiroShouldBeFound("par_descricao.specified=true");

        // Get all the parceiroList where par_descricao is null
        defaultParceiroShouldNotBeFound("par_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_descricaoContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao contains DEFAULT_PAR_DESCRICAO
        defaultParceiroShouldBeFound("par_descricao.contains=" + DEFAULT_PAR_DESCRICAO);

        // Get all the parceiroList where par_descricao contains UPDATED_PAR_DESCRICAO
        defaultParceiroShouldNotBeFound("par_descricao.contains=" + UPDATED_PAR_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_descricao does not contain DEFAULT_PAR_DESCRICAO
        defaultParceiroShouldNotBeFound("par_descricao.doesNotContain=" + DEFAULT_PAR_DESCRICAO);

        // Get all the parceiroList where par_descricao does not contain UPDATED_PAR_DESCRICAO
        defaultParceiroShouldBeFound("par_descricao.doesNotContain=" + UPDATED_PAR_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial equals to DEFAULT_PAR_RAZAOSOCIAL
        defaultParceiroShouldBeFound("par_razaosocial.equals=" + DEFAULT_PAR_RAZAOSOCIAL);

        // Get all the parceiroList where par_razaosocial equals to UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldNotBeFound("par_razaosocial.equals=" + UPDATED_PAR_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial not equals to DEFAULT_PAR_RAZAOSOCIAL
        defaultParceiroShouldNotBeFound("par_razaosocial.notEquals=" + DEFAULT_PAR_RAZAOSOCIAL);

        // Get all the parceiroList where par_razaosocial not equals to UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldBeFound("par_razaosocial.notEquals=" + UPDATED_PAR_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial in DEFAULT_PAR_RAZAOSOCIAL or UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldBeFound("par_razaosocial.in=" + DEFAULT_PAR_RAZAOSOCIAL + "," + UPDATED_PAR_RAZAOSOCIAL);

        // Get all the parceiroList where par_razaosocial equals to UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldNotBeFound("par_razaosocial.in=" + UPDATED_PAR_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial is not null
        defaultParceiroShouldBeFound("par_razaosocial.specified=true");

        // Get all the parceiroList where par_razaosocial is null
        defaultParceiroShouldNotBeFound("par_razaosocial.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial contains DEFAULT_PAR_RAZAOSOCIAL
        defaultParceiroShouldBeFound("par_razaosocial.contains=" + DEFAULT_PAR_RAZAOSOCIAL);

        // Get all the parceiroList where par_razaosocial contains UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldNotBeFound("par_razaosocial.contains=" + UPDATED_PAR_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_razaosocialNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_razaosocial does not contain DEFAULT_PAR_RAZAOSOCIAL
        defaultParceiroShouldNotBeFound("par_razaosocial.doesNotContain=" + DEFAULT_PAR_RAZAOSOCIAL);

        // Get all the parceiroList where par_razaosocial does not contain UPDATED_PAR_RAZAOSOCIAL
        defaultParceiroShouldBeFound("par_razaosocial.doesNotContain=" + UPDATED_PAR_RAZAOSOCIAL);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa equals to DEFAULT_PAR_TIPOPESSOA
        defaultParceiroShouldBeFound("par_tipopessoa.equals=" + DEFAULT_PAR_TIPOPESSOA);

        // Get all the parceiroList where par_tipopessoa equals to UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldNotBeFound("par_tipopessoa.equals=" + UPDATED_PAR_TIPOPESSOA);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa not equals to DEFAULT_PAR_TIPOPESSOA
        defaultParceiroShouldNotBeFound("par_tipopessoa.notEquals=" + DEFAULT_PAR_TIPOPESSOA);

        // Get all the parceiroList where par_tipopessoa not equals to UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldBeFound("par_tipopessoa.notEquals=" + UPDATED_PAR_TIPOPESSOA);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa in DEFAULT_PAR_TIPOPESSOA or UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldBeFound("par_tipopessoa.in=" + DEFAULT_PAR_TIPOPESSOA + "," + UPDATED_PAR_TIPOPESSOA);

        // Get all the parceiroList where par_tipopessoa equals to UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldNotBeFound("par_tipopessoa.in=" + UPDATED_PAR_TIPOPESSOA);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa is not null
        defaultParceiroShouldBeFound("par_tipopessoa.specified=true");

        // Get all the parceiroList where par_tipopessoa is null
        defaultParceiroShouldNotBeFound("par_tipopessoa.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa contains DEFAULT_PAR_TIPOPESSOA
        defaultParceiroShouldBeFound("par_tipopessoa.contains=" + DEFAULT_PAR_TIPOPESSOA);

        // Get all the parceiroList where par_tipopessoa contains UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldNotBeFound("par_tipopessoa.contains=" + UPDATED_PAR_TIPOPESSOA);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_tipopessoaNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_tipopessoa does not contain DEFAULT_PAR_TIPOPESSOA
        defaultParceiroShouldNotBeFound("par_tipopessoa.doesNotContain=" + DEFAULT_PAR_TIPOPESSOA);

        // Get all the parceiroList where par_tipopessoa does not contain UPDATED_PAR_TIPOPESSOA
        defaultParceiroShouldBeFound("par_tipopessoa.doesNotContain=" + UPDATED_PAR_TIPOPESSOA);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf equals to DEFAULT_PAR_CNPJCPF
        defaultParceiroShouldBeFound("par_cnpjcpf.equals=" + DEFAULT_PAR_CNPJCPF);

        // Get all the parceiroList where par_cnpjcpf equals to UPDATED_PAR_CNPJCPF
        defaultParceiroShouldNotBeFound("par_cnpjcpf.equals=" + UPDATED_PAR_CNPJCPF);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf not equals to DEFAULT_PAR_CNPJCPF
        defaultParceiroShouldNotBeFound("par_cnpjcpf.notEquals=" + DEFAULT_PAR_CNPJCPF);

        // Get all the parceiroList where par_cnpjcpf not equals to UPDATED_PAR_CNPJCPF
        defaultParceiroShouldBeFound("par_cnpjcpf.notEquals=" + UPDATED_PAR_CNPJCPF);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf in DEFAULT_PAR_CNPJCPF or UPDATED_PAR_CNPJCPF
        defaultParceiroShouldBeFound("par_cnpjcpf.in=" + DEFAULT_PAR_CNPJCPF + "," + UPDATED_PAR_CNPJCPF);

        // Get all the parceiroList where par_cnpjcpf equals to UPDATED_PAR_CNPJCPF
        defaultParceiroShouldNotBeFound("par_cnpjcpf.in=" + UPDATED_PAR_CNPJCPF);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf is not null
        defaultParceiroShouldBeFound("par_cnpjcpf.specified=true");

        // Get all the parceiroList where par_cnpjcpf is null
        defaultParceiroShouldNotBeFound("par_cnpjcpf.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf contains DEFAULT_PAR_CNPJCPF
        defaultParceiroShouldBeFound("par_cnpjcpf.contains=" + DEFAULT_PAR_CNPJCPF);

        // Get all the parceiroList where par_cnpjcpf contains UPDATED_PAR_CNPJCPF
        defaultParceiroShouldNotBeFound("par_cnpjcpf.contains=" + UPDATED_PAR_CNPJCPF);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_cnpjcpfNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_cnpjcpf does not contain DEFAULT_PAR_CNPJCPF
        defaultParceiroShouldNotBeFound("par_cnpjcpf.doesNotContain=" + DEFAULT_PAR_CNPJCPF);

        // Get all the parceiroList where par_cnpjcpf does not contain UPDATED_PAR_CNPJCPF
        defaultParceiroShouldBeFound("par_cnpjcpf.doesNotContain=" + UPDATED_PAR_CNPJCPF);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_rgieIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie equals to DEFAULT_PAR_RGIE
        defaultParceiroShouldBeFound("par_rgie.equals=" + DEFAULT_PAR_RGIE);

        // Get all the parceiroList where par_rgie equals to UPDATED_PAR_RGIE
        defaultParceiroShouldNotBeFound("par_rgie.equals=" + UPDATED_PAR_RGIE);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_rgieIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie not equals to DEFAULT_PAR_RGIE
        defaultParceiroShouldNotBeFound("par_rgie.notEquals=" + DEFAULT_PAR_RGIE);

        // Get all the parceiroList where par_rgie not equals to UPDATED_PAR_RGIE
        defaultParceiroShouldBeFound("par_rgie.notEquals=" + UPDATED_PAR_RGIE);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_rgieIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie in DEFAULT_PAR_RGIE or UPDATED_PAR_RGIE
        defaultParceiroShouldBeFound("par_rgie.in=" + DEFAULT_PAR_RGIE + "," + UPDATED_PAR_RGIE);

        // Get all the parceiroList where par_rgie equals to UPDATED_PAR_RGIE
        defaultParceiroShouldNotBeFound("par_rgie.in=" + UPDATED_PAR_RGIE);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_rgieIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie is not null
        defaultParceiroShouldBeFound("par_rgie.specified=true");

        // Get all the parceiroList where par_rgie is null
        defaultParceiroShouldNotBeFound("par_rgie.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_rgieContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie contains DEFAULT_PAR_RGIE
        defaultParceiroShouldBeFound("par_rgie.contains=" + DEFAULT_PAR_RGIE);

        // Get all the parceiroList where par_rgie contains UPDATED_PAR_RGIE
        defaultParceiroShouldNotBeFound("par_rgie.contains=" + UPDATED_PAR_RGIE);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_rgieNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_rgie does not contain DEFAULT_PAR_RGIE
        defaultParceiroShouldNotBeFound("par_rgie.doesNotContain=" + DEFAULT_PAR_RGIE);

        // Get all the parceiroList where par_rgie does not contain UPDATED_PAR_RGIE
        defaultParceiroShouldBeFound("par_rgie.doesNotContain=" + UPDATED_PAR_RGIE);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_obsIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs equals to DEFAULT_PAR_OBS
        defaultParceiroShouldBeFound("par_obs.equals=" + DEFAULT_PAR_OBS);

        // Get all the parceiroList where par_obs equals to UPDATED_PAR_OBS
        defaultParceiroShouldNotBeFound("par_obs.equals=" + UPDATED_PAR_OBS);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_obsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs not equals to DEFAULT_PAR_OBS
        defaultParceiroShouldNotBeFound("par_obs.notEquals=" + DEFAULT_PAR_OBS);

        // Get all the parceiroList where par_obs not equals to UPDATED_PAR_OBS
        defaultParceiroShouldBeFound("par_obs.notEquals=" + UPDATED_PAR_OBS);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_obsIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs in DEFAULT_PAR_OBS or UPDATED_PAR_OBS
        defaultParceiroShouldBeFound("par_obs.in=" + DEFAULT_PAR_OBS + "," + UPDATED_PAR_OBS);

        // Get all the parceiroList where par_obs equals to UPDATED_PAR_OBS
        defaultParceiroShouldNotBeFound("par_obs.in=" + UPDATED_PAR_OBS);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_obsIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs is not null
        defaultParceiroShouldBeFound("par_obs.specified=true");

        // Get all the parceiroList where par_obs is null
        defaultParceiroShouldNotBeFound("par_obs.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByPar_obsContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs contains DEFAULT_PAR_OBS
        defaultParceiroShouldBeFound("par_obs.contains=" + DEFAULT_PAR_OBS);

        // Get all the parceiroList where par_obs contains UPDATED_PAR_OBS
        defaultParceiroShouldNotBeFound("par_obs.contains=" + UPDATED_PAR_OBS);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_obsNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_obs does not contain DEFAULT_PAR_OBS
        defaultParceiroShouldNotBeFound("par_obs.doesNotContain=" + DEFAULT_PAR_OBS);

        // Get all the parceiroList where par_obs does not contain UPDATED_PAR_OBS
        defaultParceiroShouldBeFound("par_obs.doesNotContain=" + UPDATED_PAR_OBS);
    }


    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro equals to DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.equals=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro equals to UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.equals=" + UPDATED_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro not equals to DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.notEquals=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro not equals to UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.notEquals=" + UPDATED_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro in DEFAULT_PAR_DATACADASTRO or UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.in=" + DEFAULT_PAR_DATACADASTRO + "," + UPDATED_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro equals to UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.in=" + UPDATED_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro is not null
        defaultParceiroShouldBeFound("par_datacadastro.specified=true");

        // Get all the parceiroList where par_datacadastro is null
        defaultParceiroShouldNotBeFound("par_datacadastro.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro is greater than or equal to DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.greaterThanOrEqual=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro is greater than or equal to UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.greaterThanOrEqual=" + UPDATED_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro is less than or equal to DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.lessThanOrEqual=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro is less than or equal to SMALLER_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.lessThanOrEqual=" + SMALLER_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsLessThanSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro is less than DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.lessThan=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro is less than UPDATED_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.lessThan=" + UPDATED_PAR_DATACADASTRO);
    }

    @Test
    @Transactional
    public void getAllParceirosByPar_datacadastroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where par_datacadastro is greater than DEFAULT_PAR_DATACADASTRO
        defaultParceiroShouldNotBeFound("par_datacadastro.greaterThan=" + DEFAULT_PAR_DATACADASTRO);

        // Get all the parceiroList where par_datacadastro is greater than SMALLER_PAR_DATACADASTRO
        defaultParceiroShouldBeFound("par_datacadastro.greaterThan=" + SMALLER_PAR_DATACADASTRO);
    }


    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo equals to DEFAULT_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.equals=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo equals to UPDATED_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.equals=" + UPDATED_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo not equals to DEFAULT_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.notEquals=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo not equals to UPDATED_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.notEquals=" + UPDATED_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo in DEFAULT_SPA_CODIGO or UPDATED_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.in=" + DEFAULT_SPA_CODIGO + "," + UPDATED_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo equals to UPDATED_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.in=" + UPDATED_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo is not null
        defaultParceiroShouldBeFound("spa_codigo.specified=true");

        // Get all the parceiroList where spa_codigo is null
        defaultParceiroShouldNotBeFound("spa_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo is greater than or equal to DEFAULT_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.greaterThanOrEqual=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo is greater than or equal to UPDATED_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.greaterThanOrEqual=" + UPDATED_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo is less than or equal to DEFAULT_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.lessThanOrEqual=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo is less than or equal to SMALLER_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.lessThanOrEqual=" + SMALLER_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo is less than DEFAULT_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.lessThan=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo is less than UPDATED_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.lessThan=" + UPDATED_SPA_CODIGO);
    }

    @Test
    @Transactional
    public void getAllParceirosBySpa_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where spa_codigo is greater than DEFAULT_SPA_CODIGO
        defaultParceiroShouldNotBeFound("spa_codigo.greaterThan=" + DEFAULT_SPA_CODIGO);

        // Get all the parceiroList where spa_codigo is greater than SMALLER_SPA_CODIGO
        defaultParceiroShouldBeFound("spa_codigo.greaterThan=" + SMALLER_SPA_CODIGO);
    }


    @Test
    @Transactional
    public void getAllParceirosByLogradouroIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro equals to DEFAULT_LOGRADOURO
        defaultParceiroShouldBeFound("logradouro.equals=" + DEFAULT_LOGRADOURO);

        // Get all the parceiroList where logradouro equals to UPDATED_LOGRADOURO
        defaultParceiroShouldNotBeFound("logradouro.equals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    public void getAllParceirosByLogradouroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro not equals to DEFAULT_LOGRADOURO
        defaultParceiroShouldNotBeFound("logradouro.notEquals=" + DEFAULT_LOGRADOURO);

        // Get all the parceiroList where logradouro not equals to UPDATED_LOGRADOURO
        defaultParceiroShouldBeFound("logradouro.notEquals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    public void getAllParceirosByLogradouroIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro in DEFAULT_LOGRADOURO or UPDATED_LOGRADOURO
        defaultParceiroShouldBeFound("logradouro.in=" + DEFAULT_LOGRADOURO + "," + UPDATED_LOGRADOURO);

        // Get all the parceiroList where logradouro equals to UPDATED_LOGRADOURO
        defaultParceiroShouldNotBeFound("logradouro.in=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    public void getAllParceirosByLogradouroIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro is not null
        defaultParceiroShouldBeFound("logradouro.specified=true");

        // Get all the parceiroList where logradouro is null
        defaultParceiroShouldNotBeFound("logradouro.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByLogradouroContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro contains DEFAULT_LOGRADOURO
        defaultParceiroShouldBeFound("logradouro.contains=" + DEFAULT_LOGRADOURO);

        // Get all the parceiroList where logradouro contains UPDATED_LOGRADOURO
        defaultParceiroShouldNotBeFound("logradouro.contains=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    public void getAllParceirosByLogradouroNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where logradouro does not contain DEFAULT_LOGRADOURO
        defaultParceiroShouldNotBeFound("logradouro.doesNotContain=" + DEFAULT_LOGRADOURO);

        // Get all the parceiroList where logradouro does not contain UPDATED_LOGRADOURO
        defaultParceiroShouldBeFound("logradouro.doesNotContain=" + UPDATED_LOGRADOURO);
    }


    @Test
    @Transactional
    public void getAllParceirosByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep equals to DEFAULT_CEP
        defaultParceiroShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the parceiroList where cep equals to UPDATED_CEP
        defaultParceiroShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllParceirosByCepIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep not equals to DEFAULT_CEP
        defaultParceiroShouldNotBeFound("cep.notEquals=" + DEFAULT_CEP);

        // Get all the parceiroList where cep not equals to UPDATED_CEP
        defaultParceiroShouldBeFound("cep.notEquals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllParceirosByCepIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultParceiroShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the parceiroList where cep equals to UPDATED_CEP
        defaultParceiroShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllParceirosByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep is not null
        defaultParceiroShouldBeFound("cep.specified=true");

        // Get all the parceiroList where cep is null
        defaultParceiroShouldNotBeFound("cep.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByCepContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep contains DEFAULT_CEP
        defaultParceiroShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the parceiroList where cep contains UPDATED_CEP
        defaultParceiroShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllParceirosByCepNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cep does not contain DEFAULT_CEP
        defaultParceiroShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the parceiroList where cep does not contain UPDATED_CEP
        defaultParceiroShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }


    @Test
    @Transactional
    public void getAllParceirosByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade equals to DEFAULT_CIDADE
        defaultParceiroShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the parceiroList where cidade equals to UPDATED_CIDADE
        defaultParceiroShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllParceirosByCidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade not equals to DEFAULT_CIDADE
        defaultParceiroShouldNotBeFound("cidade.notEquals=" + DEFAULT_CIDADE);

        // Get all the parceiroList where cidade not equals to UPDATED_CIDADE
        defaultParceiroShouldBeFound("cidade.notEquals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllParceirosByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultParceiroShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the parceiroList where cidade equals to UPDATED_CIDADE
        defaultParceiroShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllParceirosByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade is not null
        defaultParceiroShouldBeFound("cidade.specified=true");

        // Get all the parceiroList where cidade is null
        defaultParceiroShouldNotBeFound("cidade.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByCidadeContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade contains DEFAULT_CIDADE
        defaultParceiroShouldBeFound("cidade.contains=" + DEFAULT_CIDADE);

        // Get all the parceiroList where cidade contains UPDATED_CIDADE
        defaultParceiroShouldNotBeFound("cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllParceirosByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where cidade does not contain DEFAULT_CIDADE
        defaultParceiroShouldNotBeFound("cidade.doesNotContain=" + DEFAULT_CIDADE);

        // Get all the parceiroList where cidade does not contain UPDATED_CIDADE
        defaultParceiroShouldBeFound("cidade.doesNotContain=" + UPDATED_CIDADE);
    }


    @Test
    @Transactional
    public void getAllParceirosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado equals to DEFAULT_ESTADO
        defaultParceiroShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the parceiroList where estado equals to UPDATED_ESTADO
        defaultParceiroShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllParceirosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado not equals to DEFAULT_ESTADO
        defaultParceiroShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the parceiroList where estado not equals to UPDATED_ESTADO
        defaultParceiroShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllParceirosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultParceiroShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the parceiroList where estado equals to UPDATED_ESTADO
        defaultParceiroShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllParceirosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado is not null
        defaultParceiroShouldBeFound("estado.specified=true");

        // Get all the parceiroList where estado is null
        defaultParceiroShouldNotBeFound("estado.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado contains DEFAULT_ESTADO
        defaultParceiroShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the parceiroList where estado contains UPDATED_ESTADO
        defaultParceiroShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllParceirosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where estado does not contain DEFAULT_ESTADO
        defaultParceiroShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the parceiroList where estado does not contain UPDATED_ESTADO
        defaultParceiroShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }


    @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao equals to DEFAULT_AREA_ATUACAO
        defaultParceiroShouldBeFound("area_atuacao.equals=" + DEFAULT_AREA_ATUACAO);

        // Get all the parceiroList where area_atuacao equals to UPDATED_AREA_ATUACAO
        defaultParceiroShouldNotBeFound("area_atuacao.equals=" + UPDATED_AREA_ATUACAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao not equals to DEFAULT_AREA_ATUACAO
        defaultParceiroShouldNotBeFound("area_atuacao.notEquals=" + DEFAULT_AREA_ATUACAO);

        // Get all the parceiroList where area_atuacao not equals to UPDATED_AREA_ATUACAO
        defaultParceiroShouldBeFound("area_atuacao.notEquals=" + UPDATED_AREA_ATUACAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao in DEFAULT_AREA_ATUACAO or UPDATED_AREA_ATUACAO
        defaultParceiroShouldBeFound("area_atuacao.in=" + DEFAULT_AREA_ATUACAO + "," + UPDATED_AREA_ATUACAO);

        // Get all the parceiroList where area_atuacao equals to UPDATED_AREA_ATUACAO
        defaultParceiroShouldNotBeFound("area_atuacao.in=" + UPDATED_AREA_ATUACAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao is not null
        defaultParceiroShouldBeFound("area_atuacao.specified=true");

        // Get all the parceiroList where area_atuacao is null
        defaultParceiroShouldNotBeFound("area_atuacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao contains DEFAULT_AREA_ATUACAO
        defaultParceiroShouldBeFound("area_atuacao.contains=" + DEFAULT_AREA_ATUACAO);

        // Get all the parceiroList where area_atuacao contains UPDATED_AREA_ATUACAO
        defaultParceiroShouldNotBeFound("area_atuacao.contains=" + UPDATED_AREA_ATUACAO);
    }

    @Test
    @Transactional
    public void getAllParceirosByArea_atuacaoNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where area_atuacao does not contain DEFAULT_AREA_ATUACAO
        defaultParceiroShouldNotBeFound("area_atuacao.doesNotContain=" + DEFAULT_AREA_ATUACAO);

        // Get all the parceiroList where area_atuacao does not contain UPDATED_AREA_ATUACAO
        defaultParceiroShouldBeFound("area_atuacao.doesNotContain=" + UPDATED_AREA_ATUACAO);
    }


    @Test
    @Transactional
    public void getAllParceirosByComercioIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where comercio equals to DEFAULT_COMERCIO
        defaultParceiroShouldBeFound("comercio.equals=" + DEFAULT_COMERCIO);

        // Get all the parceiroList where comercio equals to UPDATED_COMERCIO
        defaultParceiroShouldNotBeFound("comercio.equals=" + UPDATED_COMERCIO);
    }

    @Test
    @Transactional
    public void getAllParceirosByComercioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where comercio not equals to DEFAULT_COMERCIO
        defaultParceiroShouldNotBeFound("comercio.notEquals=" + DEFAULT_COMERCIO);

        // Get all the parceiroList where comercio not equals to UPDATED_COMERCIO
        defaultParceiroShouldBeFound("comercio.notEquals=" + UPDATED_COMERCIO);
    }

    @Test
    @Transactional
    public void getAllParceirosByComercioIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where comercio in DEFAULT_COMERCIO or UPDATED_COMERCIO
        defaultParceiroShouldBeFound("comercio.in=" + DEFAULT_COMERCIO + "," + UPDATED_COMERCIO);

        // Get all the parceiroList where comercio equals to UPDATED_COMERCIO
        defaultParceiroShouldNotBeFound("comercio.in=" + UPDATED_COMERCIO);
    }

    @Test
    @Transactional
    public void getAllParceirosByComercioIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where comercio is not null
        defaultParceiroShouldBeFound("comercio.specified=true");

        // Get all the parceiroList where comercio is null
        defaultParceiroShouldNotBeFound("comercio.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByNfc_eIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfc_e equals to DEFAULT_NFC_E
        defaultParceiroShouldBeFound("nfc_e.equals=" + DEFAULT_NFC_E);

        // Get all the parceiroList where nfc_e equals to UPDATED_NFC_E
        defaultParceiroShouldNotBeFound("nfc_e.equals=" + UPDATED_NFC_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfc_eIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfc_e not equals to DEFAULT_NFC_E
        defaultParceiroShouldNotBeFound("nfc_e.notEquals=" + DEFAULT_NFC_E);

        // Get all the parceiroList where nfc_e not equals to UPDATED_NFC_E
        defaultParceiroShouldBeFound("nfc_e.notEquals=" + UPDATED_NFC_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfc_eIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfc_e in DEFAULT_NFC_E or UPDATED_NFC_E
        defaultParceiroShouldBeFound("nfc_e.in=" + DEFAULT_NFC_E + "," + UPDATED_NFC_E);

        // Get all the parceiroList where nfc_e equals to UPDATED_NFC_E
        defaultParceiroShouldNotBeFound("nfc_e.in=" + UPDATED_NFC_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfc_eIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfc_e is not null
        defaultParceiroShouldBeFound("nfc_e.specified=true");

        // Get all the parceiroList where nfc_e is null
        defaultParceiroShouldNotBeFound("nfc_e.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByDanfeIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where danfe equals to DEFAULT_DANFE
        defaultParceiroShouldBeFound("danfe.equals=" + DEFAULT_DANFE);

        // Get all the parceiroList where danfe equals to UPDATED_DANFE
        defaultParceiroShouldNotBeFound("danfe.equals=" + UPDATED_DANFE);
    }

    @Test
    @Transactional
    public void getAllParceirosByDanfeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where danfe not equals to DEFAULT_DANFE
        defaultParceiroShouldNotBeFound("danfe.notEquals=" + DEFAULT_DANFE);

        // Get all the parceiroList where danfe not equals to UPDATED_DANFE
        defaultParceiroShouldBeFound("danfe.notEquals=" + UPDATED_DANFE);
    }

    @Test
    @Transactional
    public void getAllParceirosByDanfeIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where danfe in DEFAULT_DANFE or UPDATED_DANFE
        defaultParceiroShouldBeFound("danfe.in=" + DEFAULT_DANFE + "," + UPDATED_DANFE);

        // Get all the parceiroList where danfe equals to UPDATED_DANFE
        defaultParceiroShouldNotBeFound("danfe.in=" + UPDATED_DANFE);
    }

    @Test
    @Transactional
    public void getAllParceirosByDanfeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where danfe is not null
        defaultParceiroShouldBeFound("danfe.specified=true");

        // Get all the parceiroList where danfe is null
        defaultParceiroShouldNotBeFound("danfe.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByServicoIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where servico equals to DEFAULT_SERVICO
        defaultParceiroShouldBeFound("servico.equals=" + DEFAULT_SERVICO);

        // Get all the parceiroList where servico equals to UPDATED_SERVICO
        defaultParceiroShouldNotBeFound("servico.equals=" + UPDATED_SERVICO);
    }

    @Test
    @Transactional
    public void getAllParceirosByServicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where servico not equals to DEFAULT_SERVICO
        defaultParceiroShouldNotBeFound("servico.notEquals=" + DEFAULT_SERVICO);

        // Get all the parceiroList where servico not equals to UPDATED_SERVICO
        defaultParceiroShouldBeFound("servico.notEquals=" + UPDATED_SERVICO);
    }

    @Test
    @Transactional
    public void getAllParceirosByServicoIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where servico in DEFAULT_SERVICO or UPDATED_SERVICO
        defaultParceiroShouldBeFound("servico.in=" + DEFAULT_SERVICO + "," + UPDATED_SERVICO);

        // Get all the parceiroList where servico equals to UPDATED_SERVICO
        defaultParceiroShouldNotBeFound("servico.in=" + UPDATED_SERVICO);
    }

    @Test
    @Transactional
    public void getAllParceirosByServicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where servico is not null
        defaultParceiroShouldBeFound("servico.specified=true");

        // Get all the parceiroList where servico is null
        defaultParceiroShouldNotBeFound("servico.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByNfs_eIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfs_e equals to DEFAULT_NFS_E
        defaultParceiroShouldBeFound("nfs_e.equals=" + DEFAULT_NFS_E);

        // Get all the parceiroList where nfs_e equals to UPDATED_NFS_E
        defaultParceiroShouldNotBeFound("nfs_e.equals=" + UPDATED_NFS_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfs_eIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfs_e not equals to DEFAULT_NFS_E
        defaultParceiroShouldNotBeFound("nfs_e.notEquals=" + DEFAULT_NFS_E);

        // Get all the parceiroList where nfs_e not equals to UPDATED_NFS_E
        defaultParceiroShouldBeFound("nfs_e.notEquals=" + UPDATED_NFS_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfs_eIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfs_e in DEFAULT_NFS_E or UPDATED_NFS_E
        defaultParceiroShouldBeFound("nfs_e.in=" + DEFAULT_NFS_E + "," + UPDATED_NFS_E);

        // Get all the parceiroList where nfs_e equals to UPDATED_NFS_E
        defaultParceiroShouldNotBeFound("nfs_e.in=" + UPDATED_NFS_E);
    }

    @Test
    @Transactional
    public void getAllParceirosByNfs_eIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where nfs_e is not null
        defaultParceiroShouldBeFound("nfs_e.specified=true");

        // Get all the parceiroList where nfs_e is null
        defaultParceiroShouldNotBeFound("nfs_e.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByTransportadoraIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where transportadora equals to DEFAULT_TRANSPORTADORA
        defaultParceiroShouldBeFound("transportadora.equals=" + DEFAULT_TRANSPORTADORA);

        // Get all the parceiroList where transportadora equals to UPDATED_TRANSPORTADORA
        defaultParceiroShouldNotBeFound("transportadora.equals=" + UPDATED_TRANSPORTADORA);
    }

    @Test
    @Transactional
    public void getAllParceirosByTransportadoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where transportadora not equals to DEFAULT_TRANSPORTADORA
        defaultParceiroShouldNotBeFound("transportadora.notEquals=" + DEFAULT_TRANSPORTADORA);

        // Get all the parceiroList where transportadora not equals to UPDATED_TRANSPORTADORA
        defaultParceiroShouldBeFound("transportadora.notEquals=" + UPDATED_TRANSPORTADORA);
    }

    @Test
    @Transactional
    public void getAllParceirosByTransportadoraIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where transportadora in DEFAULT_TRANSPORTADORA or UPDATED_TRANSPORTADORA
        defaultParceiroShouldBeFound("transportadora.in=" + DEFAULT_TRANSPORTADORA + "," + UPDATED_TRANSPORTADORA);

        // Get all the parceiroList where transportadora equals to UPDATED_TRANSPORTADORA
        defaultParceiroShouldNotBeFound("transportadora.in=" + UPDATED_TRANSPORTADORA);
    }

    @Test
    @Transactional
    public void getAllParceirosByTransportadoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where transportadora is not null
        defaultParceiroShouldBeFound("transportadora.specified=true");

        // Get all the parceiroList where transportadora is null
        defaultParceiroShouldNotBeFound("transportadora.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByConhec_transporteIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where conhec_transporte equals to DEFAULT_CONHEC_TRANSPORTE
        defaultParceiroShouldBeFound("conhec_transporte.equals=" + DEFAULT_CONHEC_TRANSPORTE);

        // Get all the parceiroList where conhec_transporte equals to UPDATED_CONHEC_TRANSPORTE
        defaultParceiroShouldNotBeFound("conhec_transporte.equals=" + UPDATED_CONHEC_TRANSPORTE);
    }

    @Test
    @Transactional
    public void getAllParceirosByConhec_transporteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where conhec_transporte not equals to DEFAULT_CONHEC_TRANSPORTE
        defaultParceiroShouldNotBeFound("conhec_transporte.notEquals=" + DEFAULT_CONHEC_TRANSPORTE);

        // Get all the parceiroList where conhec_transporte not equals to UPDATED_CONHEC_TRANSPORTE
        defaultParceiroShouldBeFound("conhec_transporte.notEquals=" + UPDATED_CONHEC_TRANSPORTE);
    }

    @Test
    @Transactional
    public void getAllParceirosByConhec_transporteIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where conhec_transporte in DEFAULT_CONHEC_TRANSPORTE or UPDATED_CONHEC_TRANSPORTE
        defaultParceiroShouldBeFound("conhec_transporte.in=" + DEFAULT_CONHEC_TRANSPORTE + "," + UPDATED_CONHEC_TRANSPORTE);

        // Get all the parceiroList where conhec_transporte equals to UPDATED_CONHEC_TRANSPORTE
        defaultParceiroShouldNotBeFound("conhec_transporte.in=" + UPDATED_CONHEC_TRANSPORTE);
    }

    @Test
    @Transactional
    public void getAllParceirosByConhec_transporteIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where conhec_transporte is not null
        defaultParceiroShouldBeFound("conhec_transporte.specified=true");

        // Get all the parceiroList where conhec_transporte is null
        defaultParceiroShouldNotBeFound("conhec_transporte.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByIndustriaIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where industria equals to DEFAULT_INDUSTRIA
        defaultParceiroShouldBeFound("industria.equals=" + DEFAULT_INDUSTRIA);

        // Get all the parceiroList where industria equals to UPDATED_INDUSTRIA
        defaultParceiroShouldNotBeFound("industria.equals=" + UPDATED_INDUSTRIA);
    }

    @Test
    @Transactional
    public void getAllParceirosByIndustriaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where industria not equals to DEFAULT_INDUSTRIA
        defaultParceiroShouldNotBeFound("industria.notEquals=" + DEFAULT_INDUSTRIA);

        // Get all the parceiroList where industria not equals to UPDATED_INDUSTRIA
        defaultParceiroShouldBeFound("industria.notEquals=" + UPDATED_INDUSTRIA);
    }

    @Test
    @Transactional
    public void getAllParceirosByIndustriaIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where industria in DEFAULT_INDUSTRIA or UPDATED_INDUSTRIA
        defaultParceiroShouldBeFound("industria.in=" + DEFAULT_INDUSTRIA + "," + UPDATED_INDUSTRIA);

        // Get all the parceiroList where industria equals to UPDATED_INDUSTRIA
        defaultParceiroShouldNotBeFound("industria.in=" + UPDATED_INDUSTRIA);
    }

    @Test
    @Transactional
    public void getAllParceirosByIndustriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where industria is not null
        defaultParceiroShouldBeFound("industria.specified=true");

        // Get all the parceiroList where industria is null
        defaultParceiroShouldNotBeFound("industria.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByCtIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where ct equals to DEFAULT_CT
        defaultParceiroShouldBeFound("ct.equals=" + DEFAULT_CT);

        // Get all the parceiroList where ct equals to UPDATED_CT
        defaultParceiroShouldNotBeFound("ct.equals=" + UPDATED_CT);
    }

    @Test
    @Transactional
    public void getAllParceirosByCtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where ct not equals to DEFAULT_CT
        defaultParceiroShouldNotBeFound("ct.notEquals=" + DEFAULT_CT);

        // Get all the parceiroList where ct not equals to UPDATED_CT
        defaultParceiroShouldBeFound("ct.notEquals=" + UPDATED_CT);
    }

    @Test
    @Transactional
    public void getAllParceirosByCtIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where ct in DEFAULT_CT or UPDATED_CT
        defaultParceiroShouldBeFound("ct.in=" + DEFAULT_CT + "," + UPDATED_CT);

        // Get all the parceiroList where ct equals to UPDATED_CT
        defaultParceiroShouldNotBeFound("ct.in=" + UPDATED_CT);
    }

    @Test
    @Transactional
    public void getAllParceirosByCtIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where ct is not null
        defaultParceiroShouldBeFound("ct.specified=true");

        // Get all the parceiroList where ct is null
        defaultParceiroShouldNotBeFound("ct.specified=false");
    }

    @Test
    @Transactional
    public void getAllParceirosByOutrasIsEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras equals to DEFAULT_OUTRAS
        defaultParceiroShouldBeFound("outras.equals=" + DEFAULT_OUTRAS);

        // Get all the parceiroList where outras equals to UPDATED_OUTRAS
        defaultParceiroShouldNotBeFound("outras.equals=" + UPDATED_OUTRAS);
    }

    @Test
    @Transactional
    public void getAllParceirosByOutrasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras not equals to DEFAULT_OUTRAS
        defaultParceiroShouldNotBeFound("outras.notEquals=" + DEFAULT_OUTRAS);

        // Get all the parceiroList where outras not equals to UPDATED_OUTRAS
        defaultParceiroShouldBeFound("outras.notEquals=" + UPDATED_OUTRAS);
    }

    @Test
    @Transactional
    public void getAllParceirosByOutrasIsInShouldWork() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras in DEFAULT_OUTRAS or UPDATED_OUTRAS
        defaultParceiroShouldBeFound("outras.in=" + DEFAULT_OUTRAS + "," + UPDATED_OUTRAS);

        // Get all the parceiroList where outras equals to UPDATED_OUTRAS
        defaultParceiroShouldNotBeFound("outras.in=" + UPDATED_OUTRAS);
    }

    @Test
    @Transactional
    public void getAllParceirosByOutrasIsNullOrNotNull() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras is not null
        defaultParceiroShouldBeFound("outras.specified=true");

        // Get all the parceiroList where outras is null
        defaultParceiroShouldNotBeFound("outras.specified=false");
    }
                @Test
    @Transactional
    public void getAllParceirosByOutrasContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras contains DEFAULT_OUTRAS
        defaultParceiroShouldBeFound("outras.contains=" + DEFAULT_OUTRAS);

        // Get all the parceiroList where outras contains UPDATED_OUTRAS
        defaultParceiroShouldNotBeFound("outras.contains=" + UPDATED_OUTRAS);
    }

    @Test
    @Transactional
    public void getAllParceirosByOutrasNotContainsSomething() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        // Get all the parceiroList where outras does not contain DEFAULT_OUTRAS
        defaultParceiroShouldNotBeFound("outras.doesNotContain=" + DEFAULT_OUTRAS);

        // Get all the parceiroList where outras does not contain UPDATED_OUTRAS
        defaultParceiroShouldBeFound("outras.doesNotContain=" + UPDATED_OUTRAS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParceiroShouldBeFound(String filter) throws Exception {
        restParceiroMockMvc.perform(get("/api/parceiros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parceiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_descricao").value(hasItem(DEFAULT_PAR_DESCRICAO)))
            .andExpect(jsonPath("$.[*].par_razaosocial").value(hasItem(DEFAULT_PAR_RAZAOSOCIAL)))
            .andExpect(jsonPath("$.[*].par_tipopessoa").value(hasItem(DEFAULT_PAR_TIPOPESSOA)))
            .andExpect(jsonPath("$.[*].par_cnpjcpf").value(hasItem(DEFAULT_PAR_CNPJCPF)))
            .andExpect(jsonPath("$.[*].par_rgie").value(hasItem(DEFAULT_PAR_RGIE)))
            .andExpect(jsonPath("$.[*].par_obs").value(hasItem(DEFAULT_PAR_OBS)))
            .andExpect(jsonPath("$.[*].par_datacadastro").value(hasItem(sameInstant(DEFAULT_PAR_DATACADASTRO))))
            .andExpect(jsonPath("$.[*].spa_codigo").value(hasItem(DEFAULT_SPA_CODIGO)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].area_atuacao").value(hasItem(DEFAULT_AREA_ATUACAO)))
            .andExpect(jsonPath("$.[*].comercio").value(hasItem(DEFAULT_COMERCIO.booleanValue())))
            .andExpect(jsonPath("$.[*].nfc_e").value(hasItem(DEFAULT_NFC_E.booleanValue())))
            .andExpect(jsonPath("$.[*].danfe").value(hasItem(DEFAULT_DANFE.booleanValue())))
            .andExpect(jsonPath("$.[*].servico").value(hasItem(DEFAULT_SERVICO.booleanValue())))
            .andExpect(jsonPath("$.[*].nfs_e").value(hasItem(DEFAULT_NFS_E.booleanValue())))
            .andExpect(jsonPath("$.[*].transportadora").value(hasItem(DEFAULT_TRANSPORTADORA.booleanValue())))
            .andExpect(jsonPath("$.[*].conhec_transporte").value(hasItem(DEFAULT_CONHEC_TRANSPORTE.booleanValue())))
            .andExpect(jsonPath("$.[*].industria").value(hasItem(DEFAULT_INDUSTRIA.booleanValue())))
            .andExpect(jsonPath("$.[*].ct").value(hasItem(DEFAULT_CT.booleanValue())))
            .andExpect(jsonPath("$.[*].outras").value(hasItem(DEFAULT_OUTRAS)));

        // Check, that the count call also returns 1
        restParceiroMockMvc.perform(get("/api/parceiros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParceiroShouldNotBeFound(String filter) throws Exception {
        restParceiroMockMvc.perform(get("/api/parceiros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParceiroMockMvc.perform(get("/api/parceiros/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingParceiro() throws Exception {
        // Get the parceiro
        restParceiroMockMvc.perform(get("/api/parceiros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParceiro() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        int databaseSizeBeforeUpdate = parceiroRepository.findAll().size();

        // Update the parceiro
        Parceiro updatedParceiro = parceiroRepository.findById(parceiro.getId()).get();
        // Disconnect from session so that the updates on updatedParceiro are not directly saved in db
        em.detach(updatedParceiro);
        updatedParceiro
            .par_descricao(UPDATED_PAR_DESCRICAO)
            .par_razaosocial(UPDATED_PAR_RAZAOSOCIAL)
            .par_tipopessoa(UPDATED_PAR_TIPOPESSOA)
            .par_cnpjcpf(UPDATED_PAR_CNPJCPF)
            .par_rgie(UPDATED_PAR_RGIE)
            .par_obs(UPDATED_PAR_OBS)
            .par_datacadastro(UPDATED_PAR_DATACADASTRO)
            .spa_codigo(UPDATED_SPA_CODIGO)
            .logradouro(UPDATED_LOGRADOURO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .area_atuacao(UPDATED_AREA_ATUACAO)
            .comercio(UPDATED_COMERCIO)
            .nfc_e(UPDATED_NFC_E)
            .danfe(UPDATED_DANFE)
            .servico(UPDATED_SERVICO)
            .nfs_e(UPDATED_NFS_E)
            .transportadora(UPDATED_TRANSPORTADORA)
            .conhec_transporte(UPDATED_CONHEC_TRANSPORTE)
            .industria(UPDATED_INDUSTRIA)
            .ct(UPDATED_CT)
            .outras(UPDATED_OUTRAS);
        ParceiroDTO parceiroDTO = parceiroMapper.toDto(updatedParceiro);

        restParceiroMockMvc.perform(put("/api/parceiros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parceiroDTO)))
            .andExpect(status().isOk());

        // Validate the Parceiro in the database
        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeUpdate);
        Parceiro testParceiro = parceiroList.get(parceiroList.size() - 1);
        assertThat(testParceiro.getPar_descricao()).isEqualTo(UPDATED_PAR_DESCRICAO);
        assertThat(testParceiro.getPar_razaosocial()).isEqualTo(UPDATED_PAR_RAZAOSOCIAL);
        assertThat(testParceiro.getPar_tipopessoa()).isEqualTo(UPDATED_PAR_TIPOPESSOA);
        assertThat(testParceiro.getPar_cnpjcpf()).isEqualTo(UPDATED_PAR_CNPJCPF);
        assertThat(testParceiro.getPar_rgie()).isEqualTo(UPDATED_PAR_RGIE);
        assertThat(testParceiro.getPar_obs()).isEqualTo(UPDATED_PAR_OBS);
        assertThat(testParceiro.getPar_datacadastro()).isEqualTo(UPDATED_PAR_DATACADASTRO);
        assertThat(testParceiro.getSpa_codigo()).isEqualTo(UPDATED_SPA_CODIGO);
        assertThat(testParceiro.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testParceiro.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testParceiro.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testParceiro.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testParceiro.getArea_atuacao()).isEqualTo(UPDATED_AREA_ATUACAO);
        assertThat(testParceiro.isComercio()).isEqualTo(UPDATED_COMERCIO);
        assertThat(testParceiro.isNfc_e()).isEqualTo(UPDATED_NFC_E);
        assertThat(testParceiro.isDanfe()).isEqualTo(UPDATED_DANFE);
        assertThat(testParceiro.isServico()).isEqualTo(UPDATED_SERVICO);
        assertThat(testParceiro.isNfs_e()).isEqualTo(UPDATED_NFS_E);
        assertThat(testParceiro.isTransportadora()).isEqualTo(UPDATED_TRANSPORTADORA);
        assertThat(testParceiro.isConhec_transporte()).isEqualTo(UPDATED_CONHEC_TRANSPORTE);
        assertThat(testParceiro.isIndustria()).isEqualTo(UPDATED_INDUSTRIA);
        assertThat(testParceiro.isCt()).isEqualTo(UPDATED_CT);
        assertThat(testParceiro.getOutras()).isEqualTo(UPDATED_OUTRAS);
    }

    @Test
    @Transactional
    public void updateNonExistingParceiro() throws Exception {
        int databaseSizeBeforeUpdate = parceiroRepository.findAll().size();

        // Create the Parceiro
        ParceiroDTO parceiroDTO = parceiroMapper.toDto(parceiro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParceiroMockMvc.perform(put("/api/parceiros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parceiroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parceiro in the database
        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParceiro() throws Exception {
        // Initialize the database
        parceiroRepository.saveAndFlush(parceiro);

        int databaseSizeBeforeDelete = parceiroRepository.findAll().size();

        // Delete the parceiro
        restParceiroMockMvc.perform(delete("/api/parceiros/{id}", parceiro.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parceiro> parceiroList = parceiroRepository.findAll();
        assertThat(parceiroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
