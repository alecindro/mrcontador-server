package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.repository.RegraRepository;
import br.com.mrcontador.service.RegraService;
import br.com.mrcontador.service.dto.RegraDTO;
import br.com.mrcontador.service.mapper.RegraMapper;
import br.com.mrcontador.service.dto.RegraCriteria;
import br.com.mrcontador.service.RegraQueryService;

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
 * Integration tests for the {@link RegraResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegraResourceIT {

    private static final Integer DEFAULT_PAR_CODIGO = 1;
    private static final Integer UPDATED_PAR_CODIGO = 2;
    private static final Integer SMALLER_PAR_CODIGO = 1 - 1;

    private static final String DEFAULT_REG_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_REG_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_REG_CONTA = 1;
    private static final Integer UPDATED_REG_CONTA = 2;
    private static final Integer SMALLER_REG_CONTA = 1 - 1;

    private static final String DEFAULT_REG_HISTORICO = "AAAAAAAAAA";
    private static final String UPDATED_REG_HISTORICO = "BBBBBBBBBB";

    private static final String DEFAULT_REG_TODOS = "AAAAAAAAAA";
    private static final String UPDATED_REG_TODOS = "BBBBBBBBBB";

    @Autowired
    private RegraRepository regraRepository;

    @Autowired
    private RegraMapper regraMapper;

    @Autowired
    private RegraService regraService;

    @Autowired
    private RegraQueryService regraQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegraMockMvc;

    private Regra regra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regra createEntity(EntityManager em) {
        Regra regra = new Regra()
            .par_codigo(DEFAULT_PAR_CODIGO)
            .reg_descricao(DEFAULT_REG_DESCRICAO)
            .reg_conta(DEFAULT_REG_CONTA)
            .reg_historico(DEFAULT_REG_HISTORICO)
            .reg_todos(DEFAULT_REG_TODOS);
        return regra;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regra createUpdatedEntity(EntityManager em) {
        Regra regra = new Regra()
            .par_codigo(UPDATED_PAR_CODIGO)
            .reg_descricao(UPDATED_REG_DESCRICAO)
            .reg_conta(UPDATED_REG_CONTA)
            .reg_historico(UPDATED_REG_HISTORICO)
            .reg_todos(UPDATED_REG_TODOS);
        return regra;
    }

    @BeforeEach
    public void initTest() {
        regra = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegra() throws Exception {
        int databaseSizeBeforeCreate = regraRepository.findAll().size();
        // Create the Regra
        RegraDTO regraDTO = regraMapper.toDto(regra);
        restRegraMockMvc.perform(post("/api/regras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regraDTO)))
            .andExpect(status().isCreated());

        // Validate the Regra in the database
        List<Regra> regraList = regraRepository.findAll();
        assertThat(regraList).hasSize(databaseSizeBeforeCreate + 1);
        Regra testRegra = regraList.get(regraList.size() - 1);
        assertThat(testRegra.getPar_codigo()).isEqualTo(DEFAULT_PAR_CODIGO);
        assertThat(testRegra.getReg_descricao()).isEqualTo(DEFAULT_REG_DESCRICAO);
        assertThat(testRegra.getReg_conta()).isEqualTo(DEFAULT_REG_CONTA);
        assertThat(testRegra.getReg_historico()).isEqualTo(DEFAULT_REG_HISTORICO);
        assertThat(testRegra.getReg_todos()).isEqualTo(DEFAULT_REG_TODOS);
    }

    @Test
    @Transactional
    public void createRegraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regraRepository.findAll().size();

        // Create the Regra with an existing ID
        regra.setId(1L);
        RegraDTO regraDTO = regraMapper.toDto(regra);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegraMockMvc.perform(post("/api/regras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regra in the database
        List<Regra> regraList = regraRepository.findAll();
        assertThat(regraList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRegras() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList
        restRegraMockMvc.perform(get("/api/regras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regra.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_codigo").value(hasItem(DEFAULT_PAR_CODIGO)))
            .andExpect(jsonPath("$.[*].reg_descricao").value(hasItem(DEFAULT_REG_DESCRICAO)))
            .andExpect(jsonPath("$.[*].reg_conta").value(hasItem(DEFAULT_REG_CONTA)))
            .andExpect(jsonPath("$.[*].reg_historico").value(hasItem(DEFAULT_REG_HISTORICO)))
            .andExpect(jsonPath("$.[*].reg_todos").value(hasItem(DEFAULT_REG_TODOS)));
    }
    
    @Test
    @Transactional
    public void getRegra() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get the regra
        restRegraMockMvc.perform(get("/api/regras/{id}", regra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regra.getId().intValue()))
            .andExpect(jsonPath("$.par_codigo").value(DEFAULT_PAR_CODIGO))
            .andExpect(jsonPath("$.reg_descricao").value(DEFAULT_REG_DESCRICAO))
            .andExpect(jsonPath("$.reg_conta").value(DEFAULT_REG_CONTA))
            .andExpect(jsonPath("$.reg_historico").value(DEFAULT_REG_HISTORICO))
            .andExpect(jsonPath("$.reg_todos").value(DEFAULT_REG_TODOS));
    }


    @Test
    @Transactional
    public void getRegrasByIdFiltering() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        Long id = regra.getId();

        defaultRegraShouldBeFound("id.equals=" + id);
        defaultRegraShouldNotBeFound("id.notEquals=" + id);

        defaultRegraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegraShouldNotBeFound("id.greaterThan=" + id);

        defaultRegraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegraShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo equals to DEFAULT_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.equals=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo equals to UPDATED_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.equals=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo not equals to DEFAULT_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.notEquals=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo not equals to UPDATED_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.notEquals=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo in DEFAULT_PAR_CODIGO or UPDATED_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.in=" + DEFAULT_PAR_CODIGO + "," + UPDATED_PAR_CODIGO);

        // Get all the regraList where par_codigo equals to UPDATED_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.in=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo is not null
        defaultRegraShouldBeFound("par_codigo.specified=true");

        // Get all the regraList where par_codigo is null
        defaultRegraShouldNotBeFound("par_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo is greater than or equal to DEFAULT_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.greaterThanOrEqual=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo is greater than or equal to UPDATED_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.greaterThanOrEqual=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo is less than or equal to DEFAULT_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.lessThanOrEqual=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo is less than or equal to SMALLER_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.lessThanOrEqual=" + SMALLER_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo is less than DEFAULT_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.lessThan=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo is less than UPDATED_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.lessThan=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllRegrasByPar_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where par_codigo is greater than DEFAULT_PAR_CODIGO
        defaultRegraShouldNotBeFound("par_codigo.greaterThan=" + DEFAULT_PAR_CODIGO);

        // Get all the regraList where par_codigo is greater than SMALLER_PAR_CODIGO
        defaultRegraShouldBeFound("par_codigo.greaterThan=" + SMALLER_PAR_CODIGO);
    }


    @Test
    @Transactional
    public void getAllRegrasByReg_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao equals to DEFAULT_REG_DESCRICAO
        defaultRegraShouldBeFound("reg_descricao.equals=" + DEFAULT_REG_DESCRICAO);

        // Get all the regraList where reg_descricao equals to UPDATED_REG_DESCRICAO
        defaultRegraShouldNotBeFound("reg_descricao.equals=" + UPDATED_REG_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao not equals to DEFAULT_REG_DESCRICAO
        defaultRegraShouldNotBeFound("reg_descricao.notEquals=" + DEFAULT_REG_DESCRICAO);

        // Get all the regraList where reg_descricao not equals to UPDATED_REG_DESCRICAO
        defaultRegraShouldBeFound("reg_descricao.notEquals=" + UPDATED_REG_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao in DEFAULT_REG_DESCRICAO or UPDATED_REG_DESCRICAO
        defaultRegraShouldBeFound("reg_descricao.in=" + DEFAULT_REG_DESCRICAO + "," + UPDATED_REG_DESCRICAO);

        // Get all the regraList where reg_descricao equals to UPDATED_REG_DESCRICAO
        defaultRegraShouldNotBeFound("reg_descricao.in=" + UPDATED_REG_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao is not null
        defaultRegraShouldBeFound("reg_descricao.specified=true");

        // Get all the regraList where reg_descricao is null
        defaultRegraShouldNotBeFound("reg_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegrasByReg_descricaoContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao contains DEFAULT_REG_DESCRICAO
        defaultRegraShouldBeFound("reg_descricao.contains=" + DEFAULT_REG_DESCRICAO);

        // Get all the regraList where reg_descricao contains UPDATED_REG_DESCRICAO
        defaultRegraShouldNotBeFound("reg_descricao.contains=" + UPDATED_REG_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_descricao does not contain DEFAULT_REG_DESCRICAO
        defaultRegraShouldNotBeFound("reg_descricao.doesNotContain=" + DEFAULT_REG_DESCRICAO);

        // Get all the regraList where reg_descricao does not contain UPDATED_REG_DESCRICAO
        defaultRegraShouldBeFound("reg_descricao.doesNotContain=" + UPDATED_REG_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta equals to DEFAULT_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.equals=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta equals to UPDATED_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.equals=" + UPDATED_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta not equals to DEFAULT_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.notEquals=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta not equals to UPDATED_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.notEquals=" + UPDATED_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsInShouldWork() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta in DEFAULT_REG_CONTA or UPDATED_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.in=" + DEFAULT_REG_CONTA + "," + UPDATED_REG_CONTA);

        // Get all the regraList where reg_conta equals to UPDATED_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.in=" + UPDATED_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsNullOrNotNull() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta is not null
        defaultRegraShouldBeFound("reg_conta.specified=true");

        // Get all the regraList where reg_conta is null
        defaultRegraShouldNotBeFound("reg_conta.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta is greater than or equal to DEFAULT_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.greaterThanOrEqual=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta is greater than or equal to UPDATED_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.greaterThanOrEqual=" + UPDATED_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta is less than or equal to DEFAULT_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.lessThanOrEqual=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta is less than or equal to SMALLER_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.lessThanOrEqual=" + SMALLER_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsLessThanSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta is less than DEFAULT_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.lessThan=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta is less than UPDATED_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.lessThan=" + UPDATED_REG_CONTA);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_contaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_conta is greater than DEFAULT_REG_CONTA
        defaultRegraShouldNotBeFound("reg_conta.greaterThan=" + DEFAULT_REG_CONTA);

        // Get all the regraList where reg_conta is greater than SMALLER_REG_CONTA
        defaultRegraShouldBeFound("reg_conta.greaterThan=" + SMALLER_REG_CONTA);
    }


    @Test
    @Transactional
    public void getAllRegrasByReg_historicoIsEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico equals to DEFAULT_REG_HISTORICO
        defaultRegraShouldBeFound("reg_historico.equals=" + DEFAULT_REG_HISTORICO);

        // Get all the regraList where reg_historico equals to UPDATED_REG_HISTORICO
        defaultRegraShouldNotBeFound("reg_historico.equals=" + UPDATED_REG_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_historicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico not equals to DEFAULT_REG_HISTORICO
        defaultRegraShouldNotBeFound("reg_historico.notEquals=" + DEFAULT_REG_HISTORICO);

        // Get all the regraList where reg_historico not equals to UPDATED_REG_HISTORICO
        defaultRegraShouldBeFound("reg_historico.notEquals=" + UPDATED_REG_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_historicoIsInShouldWork() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico in DEFAULT_REG_HISTORICO or UPDATED_REG_HISTORICO
        defaultRegraShouldBeFound("reg_historico.in=" + DEFAULT_REG_HISTORICO + "," + UPDATED_REG_HISTORICO);

        // Get all the regraList where reg_historico equals to UPDATED_REG_HISTORICO
        defaultRegraShouldNotBeFound("reg_historico.in=" + UPDATED_REG_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_historicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico is not null
        defaultRegraShouldBeFound("reg_historico.specified=true");

        // Get all the regraList where reg_historico is null
        defaultRegraShouldNotBeFound("reg_historico.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegrasByReg_historicoContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico contains DEFAULT_REG_HISTORICO
        defaultRegraShouldBeFound("reg_historico.contains=" + DEFAULT_REG_HISTORICO);

        // Get all the regraList where reg_historico contains UPDATED_REG_HISTORICO
        defaultRegraShouldNotBeFound("reg_historico.contains=" + UPDATED_REG_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_historicoNotContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_historico does not contain DEFAULT_REG_HISTORICO
        defaultRegraShouldNotBeFound("reg_historico.doesNotContain=" + DEFAULT_REG_HISTORICO);

        // Get all the regraList where reg_historico does not contain UPDATED_REG_HISTORICO
        defaultRegraShouldBeFound("reg_historico.doesNotContain=" + UPDATED_REG_HISTORICO);
    }


    @Test
    @Transactional
    public void getAllRegrasByReg_todosIsEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos equals to DEFAULT_REG_TODOS
        defaultRegraShouldBeFound("reg_todos.equals=" + DEFAULT_REG_TODOS);

        // Get all the regraList where reg_todos equals to UPDATED_REG_TODOS
        defaultRegraShouldNotBeFound("reg_todos.equals=" + UPDATED_REG_TODOS);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_todosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos not equals to DEFAULT_REG_TODOS
        defaultRegraShouldNotBeFound("reg_todos.notEquals=" + DEFAULT_REG_TODOS);

        // Get all the regraList where reg_todos not equals to UPDATED_REG_TODOS
        defaultRegraShouldBeFound("reg_todos.notEquals=" + UPDATED_REG_TODOS);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_todosIsInShouldWork() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos in DEFAULT_REG_TODOS or UPDATED_REG_TODOS
        defaultRegraShouldBeFound("reg_todos.in=" + DEFAULT_REG_TODOS + "," + UPDATED_REG_TODOS);

        // Get all the regraList where reg_todos equals to UPDATED_REG_TODOS
        defaultRegraShouldNotBeFound("reg_todos.in=" + UPDATED_REG_TODOS);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_todosIsNullOrNotNull() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos is not null
        defaultRegraShouldBeFound("reg_todos.specified=true");

        // Get all the regraList where reg_todos is null
        defaultRegraShouldNotBeFound("reg_todos.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegrasByReg_todosContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos contains DEFAULT_REG_TODOS
        defaultRegraShouldBeFound("reg_todos.contains=" + DEFAULT_REG_TODOS);

        // Get all the regraList where reg_todos contains UPDATED_REG_TODOS
        defaultRegraShouldNotBeFound("reg_todos.contains=" + UPDATED_REG_TODOS);
    }

    @Test
    @Transactional
    public void getAllRegrasByReg_todosNotContainsSomething() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        // Get all the regraList where reg_todos does not contain DEFAULT_REG_TODOS
        defaultRegraShouldNotBeFound("reg_todos.doesNotContain=" + DEFAULT_REG_TODOS);

        // Get all the regraList where reg_todos does not contain UPDATED_REG_TODOS
        defaultRegraShouldBeFound("reg_todos.doesNotContain=" + UPDATED_REG_TODOS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegraShouldBeFound(String filter) throws Exception {
        restRegraMockMvc.perform(get("/api/regras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regra.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_codigo").value(hasItem(DEFAULT_PAR_CODIGO)))
            .andExpect(jsonPath("$.[*].reg_descricao").value(hasItem(DEFAULT_REG_DESCRICAO)))
            .andExpect(jsonPath("$.[*].reg_conta").value(hasItem(DEFAULT_REG_CONTA)))
            .andExpect(jsonPath("$.[*].reg_historico").value(hasItem(DEFAULT_REG_HISTORICO)))
            .andExpect(jsonPath("$.[*].reg_todos").value(hasItem(DEFAULT_REG_TODOS)));

        // Check, that the count call also returns 1
        restRegraMockMvc.perform(get("/api/regras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegraShouldNotBeFound(String filter) throws Exception {
        restRegraMockMvc.perform(get("/api/regras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegraMockMvc.perform(get("/api/regras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRegra() throws Exception {
        // Get the regra
        restRegraMockMvc.perform(get("/api/regras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegra() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        int databaseSizeBeforeUpdate = regraRepository.findAll().size();

        // Update the regra
        Regra updatedRegra = regraRepository.findById(regra.getId()).get();
        // Disconnect from session so that the updates on updatedRegra are not directly saved in db
        em.detach(updatedRegra);
        updatedRegra
            .par_codigo(UPDATED_PAR_CODIGO)
            .reg_descricao(UPDATED_REG_DESCRICAO)
            .reg_conta(UPDATED_REG_CONTA)
            .reg_historico(UPDATED_REG_HISTORICO)
            .reg_todos(UPDATED_REG_TODOS);
        RegraDTO regraDTO = regraMapper.toDto(updatedRegra);

        restRegraMockMvc.perform(put("/api/regras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regraDTO)))
            .andExpect(status().isOk());

        // Validate the Regra in the database
        List<Regra> regraList = regraRepository.findAll();
        assertThat(regraList).hasSize(databaseSizeBeforeUpdate);
        Regra testRegra = regraList.get(regraList.size() - 1);
        assertThat(testRegra.getPar_codigo()).isEqualTo(UPDATED_PAR_CODIGO);
        assertThat(testRegra.getReg_descricao()).isEqualTo(UPDATED_REG_DESCRICAO);
        assertThat(testRegra.getReg_conta()).isEqualTo(UPDATED_REG_CONTA);
        assertThat(testRegra.getReg_historico()).isEqualTo(UPDATED_REG_HISTORICO);
        assertThat(testRegra.getReg_todos()).isEqualTo(UPDATED_REG_TODOS);
    }

    @Test
    @Transactional
    public void updateNonExistingRegra() throws Exception {
        int databaseSizeBeforeUpdate = regraRepository.findAll().size();

        // Create the Regra
        RegraDTO regraDTO = regraMapper.toDto(regra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegraMockMvc.perform(put("/api/regras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regra in the database
        List<Regra> regraList = regraRepository.findAll();
        assertThat(regraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegra() throws Exception {
        // Initialize the database
        regraRepository.saveAndFlush(regra);

        int databaseSizeBeforeDelete = regraRepository.findAll().size();

        // Delete the regra
        restRegraMockMvc.perform(delete("/api/regras/{id}", regra.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regra> regraList = regraRepository.findAll();
        assertThat(regraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
