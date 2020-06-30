package br.com.mrcontador.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.AgenciabancariaRepository;
import br.com.mrcontador.service.AgenciabancariaQueryService;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.dto.AgenciabancariaDTO;
import br.com.mrcontador.service.mapper.AgenciabancariaMapper;

/**
 * Integration tests for the {@link AgenciabancariaResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AgenciabancariaResourceIT {

    private static final String DEFAULT_AGE_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_AGE_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_AGE_DIGITO = "AAAAAAAAAA";
    private static final String UPDATED_AGE_DIGITO = "BBBBBBBBBB";

    private static final String DEFAULT_AGE_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_AGE_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_AGE_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_AGE_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AGE_SITUACAO = false;
    private static final Boolean UPDATED_AGE_SITUACAO = true;

    @Autowired
    private AgenciabancariaRepository agenciabancariaRepository;

    @Autowired
    private AgenciabancariaMapper agenciabancariaMapper;

    @Autowired
    private AgenciabancariaService agenciabancariaService;

    @Autowired
    private AgenciabancariaQueryService agenciabancariaQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgenciabancariaMockMvc;

    private Agenciabancaria agenciabancaria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenciabancaria createEntity(EntityManager em) {
        Agenciabancaria agenciabancaria = new Agenciabancaria()
            .age_numero(DEFAULT_AGE_NUMERO)
            .age_digito(DEFAULT_AGE_DIGITO)
            .age_agencia(DEFAULT_AGE_AGENCIA)
            .age_descricao(DEFAULT_AGE_DESCRICAO)
            .age_situacao(DEFAULT_AGE_SITUACAO);
        // Add required entity
        Banco banco;
        if (TestUtil.findAll(em, Banco.class).isEmpty()) {
            banco = BancoResourceIT.createEntity(em);
            em.persist(banco);
            em.flush();
        } else {
            banco = TestUtil.findAll(em, Banco.class).get(0);
        }
        agenciabancaria.setBanco(banco);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        agenciabancaria.setParceiro(parceiro);
        return agenciabancaria;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenciabancaria createUpdatedEntity(EntityManager em) {
        Agenciabancaria agenciabancaria = new Agenciabancaria()
            .age_numero(UPDATED_AGE_NUMERO)
            .age_digito(UPDATED_AGE_DIGITO)
            .age_agencia(UPDATED_AGE_AGENCIA)
            .age_descricao(UPDATED_AGE_DESCRICAO)
            .age_situacao(UPDATED_AGE_SITUACAO);
        // Add required entity
        Banco banco;
        if (TestUtil.findAll(em, Banco.class).isEmpty()) {
            banco = BancoResourceIT.createUpdatedEntity(em);
            em.persist(banco);
            em.flush();
        } else {
            banco = TestUtil.findAll(em, Banco.class).get(0);
        }
        agenciabancaria.setBanco(banco);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createUpdatedEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        agenciabancaria.setParceiro(parceiro);
        return agenciabancaria;
    }

    @BeforeEach
    public void initTest() {
        agenciabancaria = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgenciabancaria() throws Exception {
        int databaseSizeBeforeCreate = agenciabancariaRepository.findAll().size();
        // Create the Agenciabancaria
        AgenciabancariaDTO agenciabancariaDTO = agenciabancariaMapper.toDto(agenciabancaria);
        restAgenciabancariaMockMvc.perform(post("/api/agenciabancarias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agenciabancariaDTO)))
            .andExpect(status().isCreated());

        // Validate the Agenciabancaria in the database
        List<Agenciabancaria> agenciabancariaList = agenciabancariaRepository.findAll();
        assertThat(agenciabancariaList).hasSize(databaseSizeBeforeCreate + 1);
        Agenciabancaria testAgenciabancaria = agenciabancariaList.get(agenciabancariaList.size() - 1);
        assertThat(testAgenciabancaria.getAge_numero()).isEqualTo(DEFAULT_AGE_NUMERO);
        assertThat(testAgenciabancaria.getAge_digito()).isEqualTo(DEFAULT_AGE_DIGITO);
        assertThat(testAgenciabancaria.getAge_agencia()).isEqualTo(DEFAULT_AGE_AGENCIA);
        assertThat(testAgenciabancaria.getAge_descricao()).isEqualTo(DEFAULT_AGE_DESCRICAO);
        assertThat(testAgenciabancaria.isAge_situacao()).isEqualTo(DEFAULT_AGE_SITUACAO);
    }

    @Test
    @Transactional
    public void createAgenciabancariaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agenciabancariaRepository.findAll().size();

        // Create the Agenciabancaria with an existing ID
        agenciabancaria.setId(1L);
        AgenciabancariaDTO agenciabancariaDTO = agenciabancariaMapper.toDto(agenciabancaria);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgenciabancariaMockMvc.perform(post("/api/agenciabancarias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agenciabancariaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agenciabancaria in the database
        List<Agenciabancaria> agenciabancariaList = agenciabancariaRepository.findAll();
        assertThat(agenciabancariaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgenciabancarias() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenciabancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].age_numero").value(hasItem(DEFAULT_AGE_NUMERO)))
            .andExpect(jsonPath("$.[*].age_digito").value(hasItem(DEFAULT_AGE_DIGITO)))
            .andExpect(jsonPath("$.[*].age_agencia").value(hasItem(DEFAULT_AGE_AGENCIA)))
            .andExpect(jsonPath("$.[*].age_descricao").value(hasItem(DEFAULT_AGE_DESCRICAO)))
            .andExpect(jsonPath("$.[*].age_situacao").value(hasItem(DEFAULT_AGE_SITUACAO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAgenciabancaria() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get the agenciabancaria
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias/{id}", agenciabancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agenciabancaria.getId().intValue()))
            .andExpect(jsonPath("$.age_numero").value(DEFAULT_AGE_NUMERO))
            .andExpect(jsonPath("$.age_digito").value(DEFAULT_AGE_DIGITO))
            .andExpect(jsonPath("$.age_agencia").value(DEFAULT_AGE_AGENCIA))
            .andExpect(jsonPath("$.age_descricao").value(DEFAULT_AGE_DESCRICAO))
            .andExpect(jsonPath("$.age_situacao").value(DEFAULT_AGE_SITUACAO.booleanValue()));
    }


    @Test
    @Transactional
    public void getAgenciabancariasByIdFiltering() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        Long id = agenciabancaria.getId();

        defaultAgenciabancariaShouldBeFound("id.equals=" + id);
        defaultAgenciabancariaShouldNotBeFound("id.notEquals=" + id);

        defaultAgenciabancariaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgenciabancariaShouldNotBeFound("id.greaterThan=" + id);

        defaultAgenciabancariaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgenciabancariaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero equals to DEFAULT_AGE_NUMERO
        defaultAgenciabancariaShouldBeFound("age_numero.equals=" + DEFAULT_AGE_NUMERO);

        // Get all the agenciabancariaList where age_numero equals to UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldNotBeFound("age_numero.equals=" + UPDATED_AGE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero not equals to DEFAULT_AGE_NUMERO
        defaultAgenciabancariaShouldNotBeFound("age_numero.notEquals=" + DEFAULT_AGE_NUMERO);

        // Get all the agenciabancariaList where age_numero not equals to UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldBeFound("age_numero.notEquals=" + UPDATED_AGE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroIsInShouldWork() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero in DEFAULT_AGE_NUMERO or UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldBeFound("age_numero.in=" + DEFAULT_AGE_NUMERO + "," + UPDATED_AGE_NUMERO);

        // Get all the agenciabancariaList where age_numero equals to UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldNotBeFound("age_numero.in=" + UPDATED_AGE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero is not null
        defaultAgenciabancariaShouldBeFound("age_numero.specified=true");

        // Get all the agenciabancariaList where age_numero is null
        defaultAgenciabancariaShouldNotBeFound("age_numero.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero contains DEFAULT_AGE_NUMERO
        defaultAgenciabancariaShouldBeFound("age_numero.contains=" + DEFAULT_AGE_NUMERO);

        // Get all the agenciabancariaList where age_numero contains UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldNotBeFound("age_numero.contains=" + UPDATED_AGE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_numeroNotContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_numero does not contain DEFAULT_AGE_NUMERO
        defaultAgenciabancariaShouldNotBeFound("age_numero.doesNotContain=" + DEFAULT_AGE_NUMERO);

        // Get all the agenciabancariaList where age_numero does not contain UPDATED_AGE_NUMERO
        defaultAgenciabancariaShouldBeFound("age_numero.doesNotContain=" + UPDATED_AGE_NUMERO);
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito equals to DEFAULT_AGE_DIGITO
        defaultAgenciabancariaShouldBeFound("age_digito.equals=" + DEFAULT_AGE_DIGITO);

        // Get all the agenciabancariaList where age_digito equals to UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldNotBeFound("age_digito.equals=" + UPDATED_AGE_DIGITO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito not equals to DEFAULT_AGE_DIGITO
        defaultAgenciabancariaShouldNotBeFound("age_digito.notEquals=" + DEFAULT_AGE_DIGITO);

        // Get all the agenciabancariaList where age_digito not equals to UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldBeFound("age_digito.notEquals=" + UPDATED_AGE_DIGITO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoIsInShouldWork() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito in DEFAULT_AGE_DIGITO or UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldBeFound("age_digito.in=" + DEFAULT_AGE_DIGITO + "," + UPDATED_AGE_DIGITO);

        // Get all the agenciabancariaList where age_digito equals to UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldNotBeFound("age_digito.in=" + UPDATED_AGE_DIGITO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito is not null
        defaultAgenciabancariaShouldBeFound("age_digito.specified=true");

        // Get all the agenciabancariaList where age_digito is null
        defaultAgenciabancariaShouldNotBeFound("age_digito.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito contains DEFAULT_AGE_DIGITO
        defaultAgenciabancariaShouldBeFound("age_digito.contains=" + DEFAULT_AGE_DIGITO);

        // Get all the agenciabancariaList where age_digito contains UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldNotBeFound("age_digito.contains=" + UPDATED_AGE_DIGITO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_digitoNotContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_digito does not contain DEFAULT_AGE_DIGITO
        defaultAgenciabancariaShouldNotBeFound("age_digito.doesNotContain=" + DEFAULT_AGE_DIGITO);

        // Get all the agenciabancariaList where age_digito does not contain UPDATED_AGE_DIGITO
        defaultAgenciabancariaShouldBeFound("age_digito.doesNotContain=" + UPDATED_AGE_DIGITO);
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia equals to DEFAULT_AGE_AGENCIA
        defaultAgenciabancariaShouldBeFound("age_agencia.equals=" + DEFAULT_AGE_AGENCIA);

        // Get all the agenciabancariaList where age_agencia equals to UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldNotBeFound("age_agencia.equals=" + UPDATED_AGE_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia not equals to DEFAULT_AGE_AGENCIA
        defaultAgenciabancariaShouldNotBeFound("age_agencia.notEquals=" + DEFAULT_AGE_AGENCIA);

        // Get all the agenciabancariaList where age_agencia not equals to UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldBeFound("age_agencia.notEquals=" + UPDATED_AGE_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaIsInShouldWork() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia in DEFAULT_AGE_AGENCIA or UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldBeFound("age_agencia.in=" + DEFAULT_AGE_AGENCIA + "," + UPDATED_AGE_AGENCIA);

        // Get all the agenciabancariaList where age_agencia equals to UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldNotBeFound("age_agencia.in=" + UPDATED_AGE_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia is not null
        defaultAgenciabancariaShouldBeFound("age_agencia.specified=true");

        // Get all the agenciabancariaList where age_agencia is null
        defaultAgenciabancariaShouldNotBeFound("age_agencia.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia contains DEFAULT_AGE_AGENCIA
        defaultAgenciabancariaShouldBeFound("age_agencia.contains=" + DEFAULT_AGE_AGENCIA);

        // Get all the agenciabancariaList where age_agencia contains UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldNotBeFound("age_agencia.contains=" + UPDATED_AGE_AGENCIA);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_agenciaNotContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_agencia does not contain DEFAULT_AGE_AGENCIA
        defaultAgenciabancariaShouldNotBeFound("age_agencia.doesNotContain=" + DEFAULT_AGE_AGENCIA);

        // Get all the agenciabancariaList where age_agencia does not contain UPDATED_AGE_AGENCIA
        defaultAgenciabancariaShouldBeFound("age_agencia.doesNotContain=" + UPDATED_AGE_AGENCIA);
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao equals to DEFAULT_AGE_DESCRICAO
        defaultAgenciabancariaShouldBeFound("age_descricao.equals=" + DEFAULT_AGE_DESCRICAO);

        // Get all the agenciabancariaList where age_descricao equals to UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldNotBeFound("age_descricao.equals=" + UPDATED_AGE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao not equals to DEFAULT_AGE_DESCRICAO
        defaultAgenciabancariaShouldNotBeFound("age_descricao.notEquals=" + DEFAULT_AGE_DESCRICAO);

        // Get all the agenciabancariaList where age_descricao not equals to UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldBeFound("age_descricao.notEquals=" + UPDATED_AGE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao in DEFAULT_AGE_DESCRICAO or UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldBeFound("age_descricao.in=" + DEFAULT_AGE_DESCRICAO + "," + UPDATED_AGE_DESCRICAO);

        // Get all the agenciabancariaList where age_descricao equals to UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldNotBeFound("age_descricao.in=" + UPDATED_AGE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao is not null
        defaultAgenciabancariaShouldBeFound("age_descricao.specified=true");

        // Get all the agenciabancariaList where age_descricao is null
        defaultAgenciabancariaShouldNotBeFound("age_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao contains DEFAULT_AGE_DESCRICAO
        defaultAgenciabancariaShouldBeFound("age_descricao.contains=" + DEFAULT_AGE_DESCRICAO);

        // Get all the agenciabancariaList where age_descricao contains UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldNotBeFound("age_descricao.contains=" + UPDATED_AGE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_descricao does not contain DEFAULT_AGE_DESCRICAO
        defaultAgenciabancariaShouldNotBeFound("age_descricao.doesNotContain=" + DEFAULT_AGE_DESCRICAO);

        // Get all the agenciabancariaList where age_descricao does not contain UPDATED_AGE_DESCRICAO
        defaultAgenciabancariaShouldBeFound("age_descricao.doesNotContain=" + UPDATED_AGE_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_situacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_situacao equals to DEFAULT_AGE_SITUACAO
        defaultAgenciabancariaShouldBeFound("age_situacao.equals=" + DEFAULT_AGE_SITUACAO);

        // Get all the agenciabancariaList where age_situacao equals to UPDATED_AGE_SITUACAO
        defaultAgenciabancariaShouldNotBeFound("age_situacao.equals=" + UPDATED_AGE_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_situacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_situacao not equals to DEFAULT_AGE_SITUACAO
        defaultAgenciabancariaShouldNotBeFound("age_situacao.notEquals=" + DEFAULT_AGE_SITUACAO);

        // Get all the agenciabancariaList where age_situacao not equals to UPDATED_AGE_SITUACAO
        defaultAgenciabancariaShouldBeFound("age_situacao.notEquals=" + UPDATED_AGE_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_situacaoIsInShouldWork() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_situacao in DEFAULT_AGE_SITUACAO or UPDATED_AGE_SITUACAO
        defaultAgenciabancariaShouldBeFound("age_situacao.in=" + DEFAULT_AGE_SITUACAO + "," + UPDATED_AGE_SITUACAO);

        // Get all the agenciabancariaList where age_situacao equals to UPDATED_AGE_SITUACAO
        defaultAgenciabancariaShouldNotBeFound("age_situacao.in=" + UPDATED_AGE_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByAge_situacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        // Get all the agenciabancariaList where age_situacao is not null
        defaultAgenciabancariaShouldBeFound("age_situacao.specified=true");

        // Get all the agenciabancariaList where age_situacao is null
        defaultAgenciabancariaShouldNotBeFound("age_situacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgenciabancariasByBancoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Banco banco = agenciabancaria.getBanco();
        agenciabancariaRepository.saveAndFlush(agenciabancaria);
        Long bancoId = banco.getId();

        // Get all the agenciabancariaList where banco equals to bancoId
        defaultAgenciabancariaShouldBeFound("bancoId.equals=" + bancoId);

        // Get all the agenciabancariaList where banco equals to bancoId + 1
        defaultAgenciabancariaShouldNotBeFound("bancoId.equals=" + (bancoId + 1));
    }


    @Test
    @Transactional
    public void getAllAgenciabancariasByParceiroIsEqualToSomething() throws Exception {
        // Get already existing entity
        Parceiro parceiro = agenciabancaria.getParceiro();
        agenciabancariaRepository.saveAndFlush(agenciabancaria);
        Long parceiroId = parceiro.getId();

        // Get all the agenciabancariaList where parceiro equals to parceiroId
        defaultAgenciabancariaShouldBeFound("parceiroId.equals=" + parceiroId);

        // Get all the agenciabancariaList where parceiro equals to parceiroId + 1
        defaultAgenciabancariaShouldNotBeFound("parceiroId.equals=" + (parceiroId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgenciabancariaShouldBeFound(String filter) throws Exception {
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenciabancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].age_numero").value(hasItem(DEFAULT_AGE_NUMERO)))
            .andExpect(jsonPath("$.[*].age_digito").value(hasItem(DEFAULT_AGE_DIGITO)))
            .andExpect(jsonPath("$.[*].age_agencia").value(hasItem(DEFAULT_AGE_AGENCIA)))
            .andExpect(jsonPath("$.[*].age_descricao").value(hasItem(DEFAULT_AGE_DESCRICAO)))
            .andExpect(jsonPath("$.[*].age_situacao").value(hasItem(DEFAULT_AGE_SITUACAO.booleanValue())));

        // Check, that the count call also returns 1
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgenciabancariaShouldNotBeFound(String filter) throws Exception {
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAgenciabancaria() throws Exception {
        // Get the agenciabancaria
        restAgenciabancariaMockMvc.perform(get("/api/agenciabancarias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgenciabancaria() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        int databaseSizeBeforeUpdate = agenciabancariaRepository.findAll().size();

        // Update the agenciabancaria
        Agenciabancaria updatedAgenciabancaria = agenciabancariaRepository.findById(agenciabancaria.getId()).get();
        // Disconnect from session so that the updates on updatedAgenciabancaria are not directly saved in db
        em.detach(updatedAgenciabancaria);
        updatedAgenciabancaria
            .age_numero(UPDATED_AGE_NUMERO)
            .age_digito(UPDATED_AGE_DIGITO)
            .age_agencia(UPDATED_AGE_AGENCIA)
            .age_descricao(UPDATED_AGE_DESCRICAO)
            .age_situacao(UPDATED_AGE_SITUACAO);
        AgenciabancariaDTO agenciabancariaDTO = agenciabancariaMapper.toDto(updatedAgenciabancaria);

        restAgenciabancariaMockMvc.perform(put("/api/agenciabancarias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agenciabancariaDTO)))
            .andExpect(status().isOk());

        // Validate the Agenciabancaria in the database
        List<Agenciabancaria> agenciabancariaList = agenciabancariaRepository.findAll();
        assertThat(agenciabancariaList).hasSize(databaseSizeBeforeUpdate);
        Agenciabancaria testAgenciabancaria = agenciabancariaList.get(agenciabancariaList.size() - 1);
        assertThat(testAgenciabancaria.getAge_numero()).isEqualTo(UPDATED_AGE_NUMERO);
        assertThat(testAgenciabancaria.getAge_digito()).isEqualTo(UPDATED_AGE_DIGITO);
        assertThat(testAgenciabancaria.getAge_agencia()).isEqualTo(UPDATED_AGE_AGENCIA);
        assertThat(testAgenciabancaria.getAge_descricao()).isEqualTo(UPDATED_AGE_DESCRICAO);
        assertThat(testAgenciabancaria.isAge_situacao()).isEqualTo(UPDATED_AGE_SITUACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingAgenciabancaria() throws Exception {
        int databaseSizeBeforeUpdate = agenciabancariaRepository.findAll().size();

        // Create the Agenciabancaria
        AgenciabancariaDTO agenciabancariaDTO = agenciabancariaMapper.toDto(agenciabancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenciabancariaMockMvc.perform(put("/api/agenciabancarias")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(agenciabancariaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agenciabancaria in the database
        List<Agenciabancaria> agenciabancariaList = agenciabancariaRepository.findAll();
        assertThat(agenciabancariaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgenciabancaria() throws Exception {
        // Initialize the database
        agenciabancariaRepository.saveAndFlush(agenciabancaria);

        int databaseSizeBeforeDelete = agenciabancariaRepository.findAll().size();

        // Delete the agenciabancaria
        restAgenciabancariaMockMvc.perform(delete("/api/agenciabancarias/{id}", agenciabancaria.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agenciabancaria> agenciabancariaList = agenciabancariaRepository.findAll();
        assertThat(agenciabancariaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
