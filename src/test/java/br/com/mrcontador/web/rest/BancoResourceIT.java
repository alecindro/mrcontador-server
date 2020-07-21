package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.repository.BancoRepository;
import br.com.mrcontador.service.BancoService;
import br.com.mrcontador.service.dto.BancoDTO;
import br.com.mrcontador.service.mapper.BancoMapper;
import br.com.mrcontador.service.dto.BancoCriteria;
import br.com.mrcontador.service.BancoQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BancoResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BancoResourceIT {

    private static final String DEFAULT_BAN_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_BAN_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_BAN_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_BAN_SIGLA = "BBBBBBBBBB";

    private static final Integer DEFAULT_BAN_ISPB = 1;
    private static final Integer UPDATED_BAN_ISPB = 2;
    private static final Integer SMALLER_BAN_ISPB = 1 - 1;

    private static final String DEFAULT_BAN_CODIGOBANCARIO = "AAAAA";
    private static final String UPDATED_BAN_CODIGOBANCARIO = "BBBBB";

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private BancoMapper bancoMapper;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private BancoQueryService bancoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBancoMockMvc;

    private Banco banco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banco createEntity(EntityManager em) {
        Banco banco = new Banco()
            .ban_descricao(DEFAULT_BAN_DESCRICAO)
            .ban_sigla(DEFAULT_BAN_SIGLA)
            .ban_ispb(DEFAULT_BAN_ISPB)
            .ban_codigobancario(DEFAULT_BAN_CODIGOBANCARIO);
        return banco;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banco createUpdatedEntity(EntityManager em) {
        Banco banco = new Banco()
            .ban_descricao(UPDATED_BAN_DESCRICAO)
            .ban_sigla(UPDATED_BAN_SIGLA)
            .ban_ispb(UPDATED_BAN_ISPB)
            .ban_codigobancario(UPDATED_BAN_CODIGOBANCARIO);
        return banco;
    }

    @BeforeEach
    public void initTest() {
        banco = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanco() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();
        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate + 1);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getBan_descricao()).isEqualTo(DEFAULT_BAN_DESCRICAO);
        assertThat(testBanco.getBan_sigla()).isEqualTo(DEFAULT_BAN_SIGLA);
        assertThat(testBanco.getBan_ispb()).isEqualTo(DEFAULT_BAN_ISPB);
        assertThat(testBanco.getBan_codigobancario()).isEqualTo(DEFAULT_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void createBancoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();

        // Create the Banco with an existing ID
        banco.setId(1L);
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBan_codigobancarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        // set the field null
        banco.setBan_codigobancario(null);

        // Create the Banco, which fails.
        BancoDTO bancoDTO = bancoMapper.toDto(banco);


        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBancos() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList
        restBancoMockMvc.perform(get("/api/bancos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].ban_descricao").value(hasItem(DEFAULT_BAN_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ban_sigla").value(hasItem(DEFAULT_BAN_SIGLA)))
            .andExpect(jsonPath("$.[*].ban_ispb").value(hasItem(DEFAULT_BAN_ISPB)))
            .andExpect(jsonPath("$.[*].ban_codigobancario").value(hasItem(DEFAULT_BAN_CODIGOBANCARIO)));
    }
    
    @Test
    @Transactional
    public void getBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banco.getId().intValue()))
            .andExpect(jsonPath("$.ban_descricao").value(DEFAULT_BAN_DESCRICAO))
            .andExpect(jsonPath("$.ban_sigla").value(DEFAULT_BAN_SIGLA))
            .andExpect(jsonPath("$.ban_ispb").value(DEFAULT_BAN_ISPB))
            .andExpect(jsonPath("$.ban_codigobancario").value(DEFAULT_BAN_CODIGOBANCARIO));
    }


    @Test
    @Transactional
    public void getBancosByIdFiltering() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        Long id = banco.getId();

        defaultBancoShouldBeFound("id.equals=" + id);
        defaultBancoShouldNotBeFound("id.notEquals=" + id);

        defaultBancoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBancoShouldNotBeFound("id.greaterThan=" + id);

        defaultBancoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBancoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBancosByBan_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao equals to DEFAULT_BAN_DESCRICAO
        defaultBancoShouldBeFound("ban_descricao.equals=" + DEFAULT_BAN_DESCRICAO);

        // Get all the bancoList where ban_descricao equals to UPDATED_BAN_DESCRICAO
        defaultBancoShouldNotBeFound("ban_descricao.equals=" + UPDATED_BAN_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao not equals to DEFAULT_BAN_DESCRICAO
        defaultBancoShouldNotBeFound("ban_descricao.notEquals=" + DEFAULT_BAN_DESCRICAO);

        // Get all the bancoList where ban_descricao not equals to UPDATED_BAN_DESCRICAO
        defaultBancoShouldBeFound("ban_descricao.notEquals=" + UPDATED_BAN_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao in DEFAULT_BAN_DESCRICAO or UPDATED_BAN_DESCRICAO
        defaultBancoShouldBeFound("ban_descricao.in=" + DEFAULT_BAN_DESCRICAO + "," + UPDATED_BAN_DESCRICAO);

        // Get all the bancoList where ban_descricao equals to UPDATED_BAN_DESCRICAO
        defaultBancoShouldNotBeFound("ban_descricao.in=" + UPDATED_BAN_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao is not null
        defaultBancoShouldBeFound("ban_descricao.specified=true");

        // Get all the bancoList where ban_descricao is null
        defaultBancoShouldNotBeFound("ban_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllBancosByBan_descricaoContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao contains DEFAULT_BAN_DESCRICAO
        defaultBancoShouldBeFound("ban_descricao.contains=" + DEFAULT_BAN_DESCRICAO);

        // Get all the bancoList where ban_descricao contains UPDATED_BAN_DESCRICAO
        defaultBancoShouldNotBeFound("ban_descricao.contains=" + UPDATED_BAN_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_descricao does not contain DEFAULT_BAN_DESCRICAO
        defaultBancoShouldNotBeFound("ban_descricao.doesNotContain=" + DEFAULT_BAN_DESCRICAO);

        // Get all the bancoList where ban_descricao does not contain UPDATED_BAN_DESCRICAO
        defaultBancoShouldBeFound("ban_descricao.doesNotContain=" + UPDATED_BAN_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllBancosByBan_siglaIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla equals to DEFAULT_BAN_SIGLA
        defaultBancoShouldBeFound("ban_sigla.equals=" + DEFAULT_BAN_SIGLA);

        // Get all the bancoList where ban_sigla equals to UPDATED_BAN_SIGLA
        defaultBancoShouldNotBeFound("ban_sigla.equals=" + UPDATED_BAN_SIGLA);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_siglaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla not equals to DEFAULT_BAN_SIGLA
        defaultBancoShouldNotBeFound("ban_sigla.notEquals=" + DEFAULT_BAN_SIGLA);

        // Get all the bancoList where ban_sigla not equals to UPDATED_BAN_SIGLA
        defaultBancoShouldBeFound("ban_sigla.notEquals=" + UPDATED_BAN_SIGLA);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_siglaIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla in DEFAULT_BAN_SIGLA or UPDATED_BAN_SIGLA
        defaultBancoShouldBeFound("ban_sigla.in=" + DEFAULT_BAN_SIGLA + "," + UPDATED_BAN_SIGLA);

        // Get all the bancoList where ban_sigla equals to UPDATED_BAN_SIGLA
        defaultBancoShouldNotBeFound("ban_sigla.in=" + UPDATED_BAN_SIGLA);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_siglaIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla is not null
        defaultBancoShouldBeFound("ban_sigla.specified=true");

        // Get all the bancoList where ban_sigla is null
        defaultBancoShouldNotBeFound("ban_sigla.specified=false");
    }
                @Test
    @Transactional
    public void getAllBancosByBan_siglaContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla contains DEFAULT_BAN_SIGLA
        defaultBancoShouldBeFound("ban_sigla.contains=" + DEFAULT_BAN_SIGLA);

        // Get all the bancoList where ban_sigla contains UPDATED_BAN_SIGLA
        defaultBancoShouldNotBeFound("ban_sigla.contains=" + UPDATED_BAN_SIGLA);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_siglaNotContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_sigla does not contain DEFAULT_BAN_SIGLA
        defaultBancoShouldNotBeFound("ban_sigla.doesNotContain=" + DEFAULT_BAN_SIGLA);

        // Get all the bancoList where ban_sigla does not contain UPDATED_BAN_SIGLA
        defaultBancoShouldBeFound("ban_sigla.doesNotContain=" + UPDATED_BAN_SIGLA);
    }


    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb equals to DEFAULT_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.equals=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb equals to UPDATED_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.equals=" + UPDATED_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb not equals to DEFAULT_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.notEquals=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb not equals to UPDATED_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.notEquals=" + UPDATED_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb in DEFAULT_BAN_ISPB or UPDATED_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.in=" + DEFAULT_BAN_ISPB + "," + UPDATED_BAN_ISPB);

        // Get all the bancoList where ban_ispb equals to UPDATED_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.in=" + UPDATED_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb is not null
        defaultBancoShouldBeFound("ban_ispb.specified=true");

        // Get all the bancoList where ban_ispb is null
        defaultBancoShouldNotBeFound("ban_ispb.specified=false");
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb is greater than or equal to DEFAULT_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.greaterThanOrEqual=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb is greater than or equal to UPDATED_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.greaterThanOrEqual=" + UPDATED_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb is less than or equal to DEFAULT_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.lessThanOrEqual=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb is less than or equal to SMALLER_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.lessThanOrEqual=" + SMALLER_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsLessThanSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb is less than DEFAULT_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.lessThan=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb is less than UPDATED_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.lessThan=" + UPDATED_BAN_ISPB);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_ispbIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_ispb is greater than DEFAULT_BAN_ISPB
        defaultBancoShouldNotBeFound("ban_ispb.greaterThan=" + DEFAULT_BAN_ISPB);

        // Get all the bancoList where ban_ispb is greater than SMALLER_BAN_ISPB
        defaultBancoShouldBeFound("ban_ispb.greaterThan=" + SMALLER_BAN_ISPB);
    }


    @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioIsEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario equals to DEFAULT_BAN_CODIGOBANCARIO
        defaultBancoShouldBeFound("ban_codigobancario.equals=" + DEFAULT_BAN_CODIGOBANCARIO);

        // Get all the bancoList where ban_codigobancario equals to UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldNotBeFound("ban_codigobancario.equals=" + UPDATED_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario not equals to DEFAULT_BAN_CODIGOBANCARIO
        defaultBancoShouldNotBeFound("ban_codigobancario.notEquals=" + DEFAULT_BAN_CODIGOBANCARIO);

        // Get all the bancoList where ban_codigobancario not equals to UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldBeFound("ban_codigobancario.notEquals=" + UPDATED_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioIsInShouldWork() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario in DEFAULT_BAN_CODIGOBANCARIO or UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldBeFound("ban_codigobancario.in=" + DEFAULT_BAN_CODIGOBANCARIO + "," + UPDATED_BAN_CODIGOBANCARIO);

        // Get all the bancoList where ban_codigobancario equals to UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldNotBeFound("ban_codigobancario.in=" + UPDATED_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario is not null
        defaultBancoShouldBeFound("ban_codigobancario.specified=true");

        // Get all the bancoList where ban_codigobancario is null
        defaultBancoShouldNotBeFound("ban_codigobancario.specified=false");
    }
                @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario contains DEFAULT_BAN_CODIGOBANCARIO
        defaultBancoShouldBeFound("ban_codigobancario.contains=" + DEFAULT_BAN_CODIGOBANCARIO);

        // Get all the bancoList where ban_codigobancario contains UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldNotBeFound("ban_codigobancario.contains=" + UPDATED_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void getAllBancosByBan_codigobancarioNotContainsSomething() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList where ban_codigobancario does not contain DEFAULT_BAN_CODIGOBANCARIO
        defaultBancoShouldNotBeFound("ban_codigobancario.doesNotContain=" + DEFAULT_BAN_CODIGOBANCARIO);

        // Get all the bancoList where ban_codigobancario does not contain UPDATED_BAN_CODIGOBANCARIO
        defaultBancoShouldBeFound("ban_codigobancario.doesNotContain=" + UPDATED_BAN_CODIGOBANCARIO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBancoShouldBeFound(String filter) throws Exception {
        restBancoMockMvc.perform(get("/api/bancos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].ban_descricao").value(hasItem(DEFAULT_BAN_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ban_sigla").value(hasItem(DEFAULT_BAN_SIGLA)))
            .andExpect(jsonPath("$.[*].ban_ispb").value(hasItem(DEFAULT_BAN_ISPB)))
            .andExpect(jsonPath("$.[*].ban_codigobancario").value(hasItem(DEFAULT_BAN_CODIGOBANCARIO)));

        // Check, that the count call also returns 1
        restBancoMockMvc.perform(get("/api/bancos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBancoShouldNotBeFound(String filter) throws Exception {
        restBancoMockMvc.perform(get("/api/bancos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBancoMockMvc.perform(get("/api/bancos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBanco() throws Exception {
        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Update the banco
        Banco updatedBanco = bancoRepository.findById(banco.getId()).get();
        // Disconnect from session so that the updates on updatedBanco are not directly saved in db
        em.detach(updatedBanco);
        updatedBanco
            .ban_descricao(UPDATED_BAN_DESCRICAO)
            .ban_sigla(UPDATED_BAN_SIGLA)
            .ban_ispb(UPDATED_BAN_ISPB)
            .ban_codigobancario(UPDATED_BAN_CODIGOBANCARIO);
        BancoDTO bancoDTO = bancoMapper.toDto(updatedBanco);

        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getBan_descricao()).isEqualTo(UPDATED_BAN_DESCRICAO);
        assertThat(testBanco.getBan_sigla()).isEqualTo(UPDATED_BAN_SIGLA);
        assertThat(testBanco.getBan_ispb()).isEqualTo(UPDATED_BAN_ISPB);
        assertThat(testBanco.getBan_codigobancario()).isEqualTo(UPDATED_BAN_CODIGOBANCARIO);
    }

    @Test
    @Transactional
    public void updateNonExistingBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        int databaseSizeBeforeDelete = bancoRepository.findAll().size();

        // Delete the banco
        restBancoMockMvc.perform(delete("/api/bancos/{id}", banco.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
