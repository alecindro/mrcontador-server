package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.ContaRepository;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.dto.ContaDTO;
import br.com.mrcontador.service.mapper.ContaMapper;
import br.com.mrcontador.service.dto.ContaCriteria;
import br.com.mrcontador.service.ContaQueryService;

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
 * Integration tests for the {@link ContaResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContaResourceIT {

    private static final Integer DEFAULT_CON_CONTA = 1;
    private static final Integer UPDATED_CON_CONTA = 2;
    private static final Integer SMALLER_CON_CONTA = 1 - 1;

    private static final String DEFAULT_CON_CLASSIFICACAO = "AAAAAAAAAA";
    private static final String UPDATED_CON_CLASSIFICACAO = "BBBBBBBBBB";

    private static final String DEFAULT_CON_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_CON_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_CON_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_CON_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CON_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CON_CNPJ = "BBBBBBBBBB";

    private static final Integer DEFAULT_CON_GRAU = 1;
    private static final Integer UPDATED_CON_GRAU = 2;
    private static final Integer SMALLER_CON_GRAU = 1 - 1;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaMapper contaMapper;

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaQueryService contaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaMockMvc;

    private Conta conta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conta createEntity(EntityManager em) {
        Conta conta = new Conta()
            .con_conta(DEFAULT_CON_CONTA)
            .con_classificacao(DEFAULT_CON_CLASSIFICACAO)
            .con_tipo(DEFAULT_CON_TIPO)
            .con_descricao(DEFAULT_CON_DESCRICAO)
            .con_cnpj(DEFAULT_CON_CNPJ)
            .con_grau(DEFAULT_CON_GRAU);
        return conta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conta createUpdatedEntity(EntityManager em) {
        Conta conta = new Conta()
            .con_conta(UPDATED_CON_CONTA)
            .con_classificacao(UPDATED_CON_CLASSIFICACAO)
            .con_tipo(UPDATED_CON_TIPO)
            .con_descricao(UPDATED_CON_DESCRICAO)
            .con_cnpj(UPDATED_CON_CNPJ)
            .con_grau(UPDATED_CON_GRAU);
        return conta;
    }

    @BeforeEach
    public void initTest() {
        conta = createEntity(em);
    }

    @Test
    @Transactional
    public void createConta() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();
        // Create the Conta
        ContaDTO contaDTO = contaMapper.toDto(conta);
        restContaMockMvc.perform(post("/api/contas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contaDTO)))
            .andExpect(status().isCreated());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate + 1);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getCon_conta()).isEqualTo(DEFAULT_CON_CONTA);
        assertThat(testConta.getCon_classificacao()).isEqualTo(DEFAULT_CON_CLASSIFICACAO);
        assertThat(testConta.getCon_tipo()).isEqualTo(DEFAULT_CON_TIPO);
        assertThat(testConta.getCon_descricao()).isEqualTo(DEFAULT_CON_DESCRICAO);
        assertThat(testConta.getCon_cnpj()).isEqualTo(DEFAULT_CON_CNPJ);
        assertThat(testConta.getCon_grau()).isEqualTo(DEFAULT_CON_GRAU);
    }

    @Test
    @Transactional
    public void createContaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contaRepository.findAll().size();

        // Create the Conta with an existing ID
        conta.setId(1L);
        ContaDTO contaDTO = contaMapper.toDto(conta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaMockMvc.perform(post("/api/contas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContas() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList
        restContaMockMvc.perform(get("/api/contas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conta.getId().intValue())))
            .andExpect(jsonPath("$.[*].con_conta").value(hasItem(DEFAULT_CON_CONTA)))
            .andExpect(jsonPath("$.[*].con_classificacao").value(hasItem(DEFAULT_CON_CLASSIFICACAO)))
            .andExpect(jsonPath("$.[*].con_tipo").value(hasItem(DEFAULT_CON_TIPO)))
            .andExpect(jsonPath("$.[*].con_descricao").value(hasItem(DEFAULT_CON_DESCRICAO)))
            .andExpect(jsonPath("$.[*].con_cnpj").value(hasItem(DEFAULT_CON_CNPJ)))
            .andExpect(jsonPath("$.[*].con_grau").value(hasItem(DEFAULT_CON_GRAU)));
    }
    
    @Test
    @Transactional
    public void getConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", conta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conta.getId().intValue()))
            .andExpect(jsonPath("$.con_conta").value(DEFAULT_CON_CONTA))
            .andExpect(jsonPath("$.con_classificacao").value(DEFAULT_CON_CLASSIFICACAO))
            .andExpect(jsonPath("$.con_tipo").value(DEFAULT_CON_TIPO))
            .andExpect(jsonPath("$.con_descricao").value(DEFAULT_CON_DESCRICAO))
            .andExpect(jsonPath("$.con_cnpj").value(DEFAULT_CON_CNPJ))
            .andExpect(jsonPath("$.con_grau").value(DEFAULT_CON_GRAU));
    }


    @Test
    @Transactional
    public void getContasByIdFiltering() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        Long id = conta.getId();

        defaultContaShouldBeFound("id.equals=" + id);
        defaultContaShouldNotBeFound("id.notEquals=" + id);

        defaultContaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContaShouldNotBeFound("id.greaterThan=" + id);

        defaultContaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContasByCon_contaIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta equals to DEFAULT_CON_CONTA
        defaultContaShouldBeFound("con_conta.equals=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta equals to UPDATED_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.equals=" + UPDATED_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta not equals to DEFAULT_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.notEquals=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta not equals to UPDATED_CON_CONTA
        defaultContaShouldBeFound("con_conta.notEquals=" + UPDATED_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta in DEFAULT_CON_CONTA or UPDATED_CON_CONTA
        defaultContaShouldBeFound("con_conta.in=" + DEFAULT_CON_CONTA + "," + UPDATED_CON_CONTA);

        // Get all the contaList where con_conta equals to UPDATED_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.in=" + UPDATED_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta is not null
        defaultContaShouldBeFound("con_conta.specified=true");

        // Get all the contaList where con_conta is null
        defaultContaShouldNotBeFound("con_conta.specified=false");
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta is greater than or equal to DEFAULT_CON_CONTA
        defaultContaShouldBeFound("con_conta.greaterThanOrEqual=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta is greater than or equal to UPDATED_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.greaterThanOrEqual=" + UPDATED_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta is less than or equal to DEFAULT_CON_CONTA
        defaultContaShouldBeFound("con_conta.lessThanOrEqual=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta is less than or equal to SMALLER_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.lessThanOrEqual=" + SMALLER_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta is less than DEFAULT_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.lessThan=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta is less than UPDATED_CON_CONTA
        defaultContaShouldBeFound("con_conta.lessThan=" + UPDATED_CON_CONTA);
    }

    @Test
    @Transactional
    public void getAllContasByCon_contaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_conta is greater than DEFAULT_CON_CONTA
        defaultContaShouldNotBeFound("con_conta.greaterThan=" + DEFAULT_CON_CONTA);

        // Get all the contaList where con_conta is greater than SMALLER_CON_CONTA
        defaultContaShouldBeFound("con_conta.greaterThan=" + SMALLER_CON_CONTA);
    }


    @Test
    @Transactional
    public void getAllContasByCon_classificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao equals to DEFAULT_CON_CLASSIFICACAO
        defaultContaShouldBeFound("con_classificacao.equals=" + DEFAULT_CON_CLASSIFICACAO);

        // Get all the contaList where con_classificacao equals to UPDATED_CON_CLASSIFICACAO
        defaultContaShouldNotBeFound("con_classificacao.equals=" + UPDATED_CON_CLASSIFICACAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_classificacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao not equals to DEFAULT_CON_CLASSIFICACAO
        defaultContaShouldNotBeFound("con_classificacao.notEquals=" + DEFAULT_CON_CLASSIFICACAO);

        // Get all the contaList where con_classificacao not equals to UPDATED_CON_CLASSIFICACAO
        defaultContaShouldBeFound("con_classificacao.notEquals=" + UPDATED_CON_CLASSIFICACAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_classificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao in DEFAULT_CON_CLASSIFICACAO or UPDATED_CON_CLASSIFICACAO
        defaultContaShouldBeFound("con_classificacao.in=" + DEFAULT_CON_CLASSIFICACAO + "," + UPDATED_CON_CLASSIFICACAO);

        // Get all the contaList where con_classificacao equals to UPDATED_CON_CLASSIFICACAO
        defaultContaShouldNotBeFound("con_classificacao.in=" + UPDATED_CON_CLASSIFICACAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_classificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao is not null
        defaultContaShouldBeFound("con_classificacao.specified=true");

        // Get all the contaList where con_classificacao is null
        defaultContaShouldNotBeFound("con_classificacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllContasByCon_classificacaoContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao contains DEFAULT_CON_CLASSIFICACAO
        defaultContaShouldBeFound("con_classificacao.contains=" + DEFAULT_CON_CLASSIFICACAO);

        // Get all the contaList where con_classificacao contains UPDATED_CON_CLASSIFICACAO
        defaultContaShouldNotBeFound("con_classificacao.contains=" + UPDATED_CON_CLASSIFICACAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_classificacaoNotContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_classificacao does not contain DEFAULT_CON_CLASSIFICACAO
        defaultContaShouldNotBeFound("con_classificacao.doesNotContain=" + DEFAULT_CON_CLASSIFICACAO);

        // Get all the contaList where con_classificacao does not contain UPDATED_CON_CLASSIFICACAO
        defaultContaShouldBeFound("con_classificacao.doesNotContain=" + UPDATED_CON_CLASSIFICACAO);
    }


    @Test
    @Transactional
    public void getAllContasByCon_tipoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo equals to DEFAULT_CON_TIPO
        defaultContaShouldBeFound("con_tipo.equals=" + DEFAULT_CON_TIPO);

        // Get all the contaList where con_tipo equals to UPDATED_CON_TIPO
        defaultContaShouldNotBeFound("con_tipo.equals=" + UPDATED_CON_TIPO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_tipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo not equals to DEFAULT_CON_TIPO
        defaultContaShouldNotBeFound("con_tipo.notEquals=" + DEFAULT_CON_TIPO);

        // Get all the contaList where con_tipo not equals to UPDATED_CON_TIPO
        defaultContaShouldBeFound("con_tipo.notEquals=" + UPDATED_CON_TIPO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_tipoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo in DEFAULT_CON_TIPO or UPDATED_CON_TIPO
        defaultContaShouldBeFound("con_tipo.in=" + DEFAULT_CON_TIPO + "," + UPDATED_CON_TIPO);

        // Get all the contaList where con_tipo equals to UPDATED_CON_TIPO
        defaultContaShouldNotBeFound("con_tipo.in=" + UPDATED_CON_TIPO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_tipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo is not null
        defaultContaShouldBeFound("con_tipo.specified=true");

        // Get all the contaList where con_tipo is null
        defaultContaShouldNotBeFound("con_tipo.specified=false");
    }
                @Test
    @Transactional
    public void getAllContasByCon_tipoContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo contains DEFAULT_CON_TIPO
        defaultContaShouldBeFound("con_tipo.contains=" + DEFAULT_CON_TIPO);

        // Get all the contaList where con_tipo contains UPDATED_CON_TIPO
        defaultContaShouldNotBeFound("con_tipo.contains=" + UPDATED_CON_TIPO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_tipoNotContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_tipo does not contain DEFAULT_CON_TIPO
        defaultContaShouldNotBeFound("con_tipo.doesNotContain=" + DEFAULT_CON_TIPO);

        // Get all the contaList where con_tipo does not contain UPDATED_CON_TIPO
        defaultContaShouldBeFound("con_tipo.doesNotContain=" + UPDATED_CON_TIPO);
    }


    @Test
    @Transactional
    public void getAllContasByCon_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao equals to DEFAULT_CON_DESCRICAO
        defaultContaShouldBeFound("con_descricao.equals=" + DEFAULT_CON_DESCRICAO);

        // Get all the contaList where con_descricao equals to UPDATED_CON_DESCRICAO
        defaultContaShouldNotBeFound("con_descricao.equals=" + UPDATED_CON_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao not equals to DEFAULT_CON_DESCRICAO
        defaultContaShouldNotBeFound("con_descricao.notEquals=" + DEFAULT_CON_DESCRICAO);

        // Get all the contaList where con_descricao not equals to UPDATED_CON_DESCRICAO
        defaultContaShouldBeFound("con_descricao.notEquals=" + UPDATED_CON_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao in DEFAULT_CON_DESCRICAO or UPDATED_CON_DESCRICAO
        defaultContaShouldBeFound("con_descricao.in=" + DEFAULT_CON_DESCRICAO + "," + UPDATED_CON_DESCRICAO);

        // Get all the contaList where con_descricao equals to UPDATED_CON_DESCRICAO
        defaultContaShouldNotBeFound("con_descricao.in=" + UPDATED_CON_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao is not null
        defaultContaShouldBeFound("con_descricao.specified=true");

        // Get all the contaList where con_descricao is null
        defaultContaShouldNotBeFound("con_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllContasByCon_descricaoContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao contains DEFAULT_CON_DESCRICAO
        defaultContaShouldBeFound("con_descricao.contains=" + DEFAULT_CON_DESCRICAO);

        // Get all the contaList where con_descricao contains UPDATED_CON_DESCRICAO
        defaultContaShouldNotBeFound("con_descricao.contains=" + UPDATED_CON_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllContasByCon_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_descricao does not contain DEFAULT_CON_DESCRICAO
        defaultContaShouldNotBeFound("con_descricao.doesNotContain=" + DEFAULT_CON_DESCRICAO);

        // Get all the contaList where con_descricao does not contain UPDATED_CON_DESCRICAO
        defaultContaShouldBeFound("con_descricao.doesNotContain=" + UPDATED_CON_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllContasByCon_cnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj equals to DEFAULT_CON_CNPJ
        defaultContaShouldBeFound("con_cnpj.equals=" + DEFAULT_CON_CNPJ);

        // Get all the contaList where con_cnpj equals to UPDATED_CON_CNPJ
        defaultContaShouldNotBeFound("con_cnpj.equals=" + UPDATED_CON_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContasByCon_cnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj not equals to DEFAULT_CON_CNPJ
        defaultContaShouldNotBeFound("con_cnpj.notEquals=" + DEFAULT_CON_CNPJ);

        // Get all the contaList where con_cnpj not equals to UPDATED_CON_CNPJ
        defaultContaShouldBeFound("con_cnpj.notEquals=" + UPDATED_CON_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContasByCon_cnpjIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj in DEFAULT_CON_CNPJ or UPDATED_CON_CNPJ
        defaultContaShouldBeFound("con_cnpj.in=" + DEFAULT_CON_CNPJ + "," + UPDATED_CON_CNPJ);

        // Get all the contaList where con_cnpj equals to UPDATED_CON_CNPJ
        defaultContaShouldNotBeFound("con_cnpj.in=" + UPDATED_CON_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContasByCon_cnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj is not null
        defaultContaShouldBeFound("con_cnpj.specified=true");

        // Get all the contaList where con_cnpj is null
        defaultContaShouldNotBeFound("con_cnpj.specified=false");
    }
                @Test
    @Transactional
    public void getAllContasByCon_cnpjContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj contains DEFAULT_CON_CNPJ
        defaultContaShouldBeFound("con_cnpj.contains=" + DEFAULT_CON_CNPJ);

        // Get all the contaList where con_cnpj contains UPDATED_CON_CNPJ
        defaultContaShouldNotBeFound("con_cnpj.contains=" + UPDATED_CON_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContasByCon_cnpjNotContainsSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_cnpj does not contain DEFAULT_CON_CNPJ
        defaultContaShouldNotBeFound("con_cnpj.doesNotContain=" + DEFAULT_CON_CNPJ);

        // Get all the contaList where con_cnpj does not contain UPDATED_CON_CNPJ
        defaultContaShouldBeFound("con_cnpj.doesNotContain=" + UPDATED_CON_CNPJ);
    }


    @Test
    @Transactional
    public void getAllContasByCon_grauIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau equals to DEFAULT_CON_GRAU
        defaultContaShouldBeFound("con_grau.equals=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau equals to UPDATED_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.equals=" + UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau not equals to DEFAULT_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.notEquals=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau not equals to UPDATED_CON_GRAU
        defaultContaShouldBeFound("con_grau.notEquals=" + UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsInShouldWork() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau in DEFAULT_CON_GRAU or UPDATED_CON_GRAU
        defaultContaShouldBeFound("con_grau.in=" + DEFAULT_CON_GRAU + "," + UPDATED_CON_GRAU);

        // Get all the contaList where con_grau equals to UPDATED_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.in=" + UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau is not null
        defaultContaShouldBeFound("con_grau.specified=true");

        // Get all the contaList where con_grau is null
        defaultContaShouldNotBeFound("con_grau.specified=false");
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau is greater than or equal to DEFAULT_CON_GRAU
        defaultContaShouldBeFound("con_grau.greaterThanOrEqual=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau is greater than or equal to UPDATED_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.greaterThanOrEqual=" + UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau is less than or equal to DEFAULT_CON_GRAU
        defaultContaShouldBeFound("con_grau.lessThanOrEqual=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau is less than or equal to SMALLER_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.lessThanOrEqual=" + SMALLER_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau is less than DEFAULT_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.lessThan=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau is less than UPDATED_CON_GRAU
        defaultContaShouldBeFound("con_grau.lessThan=" + UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void getAllContasByCon_grauIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        // Get all the contaList where con_grau is greater than DEFAULT_CON_GRAU
        defaultContaShouldNotBeFound("con_grau.greaterThan=" + DEFAULT_CON_GRAU);

        // Get all the contaList where con_grau is greater than SMALLER_CON_GRAU
        defaultContaShouldBeFound("con_grau.greaterThan=" + SMALLER_CON_GRAU);
    }


    @Test
    @Transactional
    public void getAllContasByParceiroIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);
        Parceiro parceiro = ParceiroResourceIT.createEntity(em);
        em.persist(parceiro);
        em.flush();
        conta.setParceiro(parceiro);
        contaRepository.saveAndFlush(conta);
        Long parceiroId = parceiro.getId();

        // Get all the contaList where parceiro equals to parceiroId
        defaultContaShouldBeFound("parceiroId.equals=" + parceiroId);

        // Get all the contaList where parceiro equals to parceiroId + 1
        defaultContaShouldNotBeFound("parceiroId.equals=" + (parceiroId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaShouldBeFound(String filter) throws Exception {
        restContaMockMvc.perform(get("/api/contas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conta.getId().intValue())))
            .andExpect(jsonPath("$.[*].con_conta").value(hasItem(DEFAULT_CON_CONTA)))
            .andExpect(jsonPath("$.[*].con_classificacao").value(hasItem(DEFAULT_CON_CLASSIFICACAO)))
            .andExpect(jsonPath("$.[*].con_tipo").value(hasItem(DEFAULT_CON_TIPO)))
            .andExpect(jsonPath("$.[*].con_descricao").value(hasItem(DEFAULT_CON_DESCRICAO)))
            .andExpect(jsonPath("$.[*].con_cnpj").value(hasItem(DEFAULT_CON_CNPJ)))
            .andExpect(jsonPath("$.[*].con_grau").value(hasItem(DEFAULT_CON_GRAU)));

        // Check, that the count call also returns 1
        restContaMockMvc.perform(get("/api/contas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaShouldNotBeFound(String filter) throws Exception {
        restContaMockMvc.perform(get("/api/contas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaMockMvc.perform(get("/api/contas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingConta() throws Exception {
        // Get the conta
        restContaMockMvc.perform(get("/api/contas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Update the conta
        Conta updatedConta = contaRepository.findById(conta.getId()).get();
        // Disconnect from session so that the updates on updatedConta are not directly saved in db
        em.detach(updatedConta);
        updatedConta
            .con_conta(UPDATED_CON_CONTA)
            .con_classificacao(UPDATED_CON_CLASSIFICACAO)
            .con_tipo(UPDATED_CON_TIPO)
            .con_descricao(UPDATED_CON_DESCRICAO)
            .con_cnpj(UPDATED_CON_CNPJ)
            .con_grau(UPDATED_CON_GRAU);
        ContaDTO contaDTO = contaMapper.toDto(updatedConta);

        restContaMockMvc.perform(put("/api/contas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contaDTO)))
            .andExpect(status().isOk());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate);
        Conta testConta = contaList.get(contaList.size() - 1);
        assertThat(testConta.getCon_conta()).isEqualTo(UPDATED_CON_CONTA);
        assertThat(testConta.getCon_classificacao()).isEqualTo(UPDATED_CON_CLASSIFICACAO);
        assertThat(testConta.getCon_tipo()).isEqualTo(UPDATED_CON_TIPO);
        assertThat(testConta.getCon_descricao()).isEqualTo(UPDATED_CON_DESCRICAO);
        assertThat(testConta.getCon_cnpj()).isEqualTo(UPDATED_CON_CNPJ);
        assertThat(testConta.getCon_grau()).isEqualTo(UPDATED_CON_GRAU);
    }

    @Test
    @Transactional
    public void updateNonExistingConta() throws Exception {
        int databaseSizeBeforeUpdate = contaRepository.findAll().size();

        // Create the Conta
        ContaDTO contaDTO = contaMapper.toDto(conta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaMockMvc.perform(put("/api/contas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conta in the database
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConta() throws Exception {
        // Initialize the database
        contaRepository.saveAndFlush(conta);

        int databaseSizeBeforeDelete = contaRepository.findAll().size();

        // Delete the conta
        restContaMockMvc.perform(delete("/api/contas/{id}", conta.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conta> contaList = contaRepository.findAll();
        assertThat(contaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
