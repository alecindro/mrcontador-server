package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.repository.ContadorRepository;
import br.com.mrcontador.service.ContadorService;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.service.mapper.ContadorMapper;
import br.com.mrcontador.service.dto.ContadorCriteria;
import br.com.mrcontador.service.ContadorQueryService;

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
 * Integration tests for the {@link ContadorResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContadorResourceIT {

    private static final String DEFAULT_RAZAO = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO = "BBBBBBBBBB";

    private static final String DEFAULT_FANTASIA = "AAAAAAAAAA";
    private static final String UPDATED_FANTASIA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONES = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONES = "BBBBBBBBBB";

    private static final String DEFAULT_DATASOURCE = "AAAAAAAAAA";
    private static final String UPDATED_DATASOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ContadorRepository contadorRepository;

    @Autowired
    private ContadorMapper contadorMapper;

    @Autowired
    private ContadorService contadorService;

    @Autowired
    private ContadorQueryService contadorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContadorMockMvc;

    private Contador contador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contador createEntity(EntityManager em) {
        Contador contador = new Contador()
            .razao(DEFAULT_RAZAO)
            .fantasia(DEFAULT_FANTASIA)
            .telefones(DEFAULT_TELEFONES)
            .datasource(DEFAULT_DATASOURCE)
            .cnpj(DEFAULT_CNPJ)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP)
            .email(DEFAULT_EMAIL);
        return contador;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contador createUpdatedEntity(EntityManager em) {
        Contador contador = new Contador()
            .razao(UPDATED_RAZAO)
            .fantasia(UPDATED_FANTASIA)
            .telefones(UPDATED_TELEFONES)
            .datasource(UPDATED_DATASOURCE)
            .cnpj(UPDATED_CNPJ)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .email(UPDATED_EMAIL);
        return contador;
    }

    @BeforeEach
    public void initTest() {
        contador = createEntity(em);
    }

    @Test
    @Transactional
    public void createContador() throws Exception {
        int databaseSizeBeforeCreate = contadorRepository.findAll().size();
        // Create the Contador
        ContadorDTO contadorDTO = contadorMapper.toDto(contador);
        restContadorMockMvc.perform(post("/api/contadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contadorDTO)))
            .andExpect(status().isCreated());

        // Validate the Contador in the database
        List<Contador> contadorList = contadorRepository.findAll();
        assertThat(contadorList).hasSize(databaseSizeBeforeCreate + 1);
        Contador testContador = contadorList.get(contadorList.size() - 1);
        assertThat(testContador.getRazao()).isEqualTo(DEFAULT_RAZAO);
        assertThat(testContador.getFantasia()).isEqualTo(DEFAULT_FANTASIA);
        assertThat(testContador.getTelefones()).isEqualTo(DEFAULT_TELEFONES);
        assertThat(testContador.getDatasource()).isEqualTo(DEFAULT_DATASOURCE);
        assertThat(testContador.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testContador.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testContador.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testContador.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testContador.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createContadorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contadorRepository.findAll().size();

        // Create the Contador with an existing ID
        contador.setId(1L);
        ContadorDTO contadorDTO = contadorMapper.toDto(contador);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContadorMockMvc.perform(post("/api/contadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        List<Contador> contadorList = contadorRepository.findAll();
        assertThat(contadorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContadors() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList
        restContadorMockMvc.perform(get("/api/contadors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contador.getId().intValue())))
            .andExpect(jsonPath("$.[*].razao").value(hasItem(DEFAULT_RAZAO)))
            .andExpect(jsonPath("$.[*].fantasia").value(hasItem(DEFAULT_FANTASIA)))
            .andExpect(jsonPath("$.[*].telefones").value(hasItem(DEFAULT_TELEFONES)))
            .andExpect(jsonPath("$.[*].datasource").value(hasItem(DEFAULT_DATASOURCE)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getContador() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get the contador
        restContadorMockMvc.perform(get("/api/contadors/{id}", contador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contador.getId().intValue()))
            .andExpect(jsonPath("$.razao").value(DEFAULT_RAZAO))
            .andExpect(jsonPath("$.fantasia").value(DEFAULT_FANTASIA))
            .andExpect(jsonPath("$.telefones").value(DEFAULT_TELEFONES))
            .andExpect(jsonPath("$.datasource").value(DEFAULT_DATASOURCE))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }


    @Test
    @Transactional
    public void getContadorsByIdFiltering() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        Long id = contador.getId();

        defaultContadorShouldBeFound("id.equals=" + id);
        defaultContadorShouldNotBeFound("id.notEquals=" + id);

        defaultContadorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContadorShouldNotBeFound("id.greaterThan=" + id);

        defaultContadorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContadorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContadorsByRazaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao equals to DEFAULT_RAZAO
        defaultContadorShouldBeFound("razao.equals=" + DEFAULT_RAZAO);

        // Get all the contadorList where razao equals to UPDATED_RAZAO
        defaultContadorShouldNotBeFound("razao.equals=" + UPDATED_RAZAO);
    }

    @Test
    @Transactional
    public void getAllContadorsByRazaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao not equals to DEFAULT_RAZAO
        defaultContadorShouldNotBeFound("razao.notEquals=" + DEFAULT_RAZAO);

        // Get all the contadorList where razao not equals to UPDATED_RAZAO
        defaultContadorShouldBeFound("razao.notEquals=" + UPDATED_RAZAO);
    }

    @Test
    @Transactional
    public void getAllContadorsByRazaoIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao in DEFAULT_RAZAO or UPDATED_RAZAO
        defaultContadorShouldBeFound("razao.in=" + DEFAULT_RAZAO + "," + UPDATED_RAZAO);

        // Get all the contadorList where razao equals to UPDATED_RAZAO
        defaultContadorShouldNotBeFound("razao.in=" + UPDATED_RAZAO);
    }

    @Test
    @Transactional
    public void getAllContadorsByRazaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao is not null
        defaultContadorShouldBeFound("razao.specified=true");

        // Get all the contadorList where razao is null
        defaultContadorShouldNotBeFound("razao.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByRazaoContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao contains DEFAULT_RAZAO
        defaultContadorShouldBeFound("razao.contains=" + DEFAULT_RAZAO);

        // Get all the contadorList where razao contains UPDATED_RAZAO
        defaultContadorShouldNotBeFound("razao.contains=" + UPDATED_RAZAO);
    }

    @Test
    @Transactional
    public void getAllContadorsByRazaoNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where razao does not contain DEFAULT_RAZAO
        defaultContadorShouldNotBeFound("razao.doesNotContain=" + DEFAULT_RAZAO);

        // Get all the contadorList where razao does not contain UPDATED_RAZAO
        defaultContadorShouldBeFound("razao.doesNotContain=" + UPDATED_RAZAO);
    }


    @Test
    @Transactional
    public void getAllContadorsByFantasiaIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia equals to DEFAULT_FANTASIA
        defaultContadorShouldBeFound("fantasia.equals=" + DEFAULT_FANTASIA);

        // Get all the contadorList where fantasia equals to UPDATED_FANTASIA
        defaultContadorShouldNotBeFound("fantasia.equals=" + UPDATED_FANTASIA);
    }

    @Test
    @Transactional
    public void getAllContadorsByFantasiaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia not equals to DEFAULT_FANTASIA
        defaultContadorShouldNotBeFound("fantasia.notEquals=" + DEFAULT_FANTASIA);

        // Get all the contadorList where fantasia not equals to UPDATED_FANTASIA
        defaultContadorShouldBeFound("fantasia.notEquals=" + UPDATED_FANTASIA);
    }

    @Test
    @Transactional
    public void getAllContadorsByFantasiaIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia in DEFAULT_FANTASIA or UPDATED_FANTASIA
        defaultContadorShouldBeFound("fantasia.in=" + DEFAULT_FANTASIA + "," + UPDATED_FANTASIA);

        // Get all the contadorList where fantasia equals to UPDATED_FANTASIA
        defaultContadorShouldNotBeFound("fantasia.in=" + UPDATED_FANTASIA);
    }

    @Test
    @Transactional
    public void getAllContadorsByFantasiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia is not null
        defaultContadorShouldBeFound("fantasia.specified=true");

        // Get all the contadorList where fantasia is null
        defaultContadorShouldNotBeFound("fantasia.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByFantasiaContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia contains DEFAULT_FANTASIA
        defaultContadorShouldBeFound("fantasia.contains=" + DEFAULT_FANTASIA);

        // Get all the contadorList where fantasia contains UPDATED_FANTASIA
        defaultContadorShouldNotBeFound("fantasia.contains=" + UPDATED_FANTASIA);
    }

    @Test
    @Transactional
    public void getAllContadorsByFantasiaNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where fantasia does not contain DEFAULT_FANTASIA
        defaultContadorShouldNotBeFound("fantasia.doesNotContain=" + DEFAULT_FANTASIA);

        // Get all the contadorList where fantasia does not contain UPDATED_FANTASIA
        defaultContadorShouldBeFound("fantasia.doesNotContain=" + UPDATED_FANTASIA);
    }


    @Test
    @Transactional
    public void getAllContadorsByTelefonesIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones equals to DEFAULT_TELEFONES
        defaultContadorShouldBeFound("telefones.equals=" + DEFAULT_TELEFONES);

        // Get all the contadorList where telefones equals to UPDATED_TELEFONES
        defaultContadorShouldNotBeFound("telefones.equals=" + UPDATED_TELEFONES);
    }

    @Test
    @Transactional
    public void getAllContadorsByTelefonesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones not equals to DEFAULT_TELEFONES
        defaultContadorShouldNotBeFound("telefones.notEquals=" + DEFAULT_TELEFONES);

        // Get all the contadorList where telefones not equals to UPDATED_TELEFONES
        defaultContadorShouldBeFound("telefones.notEquals=" + UPDATED_TELEFONES);
    }

    @Test
    @Transactional
    public void getAllContadorsByTelefonesIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones in DEFAULT_TELEFONES or UPDATED_TELEFONES
        defaultContadorShouldBeFound("telefones.in=" + DEFAULT_TELEFONES + "," + UPDATED_TELEFONES);

        // Get all the contadorList where telefones equals to UPDATED_TELEFONES
        defaultContadorShouldNotBeFound("telefones.in=" + UPDATED_TELEFONES);
    }

    @Test
    @Transactional
    public void getAllContadorsByTelefonesIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones is not null
        defaultContadorShouldBeFound("telefones.specified=true");

        // Get all the contadorList where telefones is null
        defaultContadorShouldNotBeFound("telefones.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByTelefonesContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones contains DEFAULT_TELEFONES
        defaultContadorShouldBeFound("telefones.contains=" + DEFAULT_TELEFONES);

        // Get all the contadorList where telefones contains UPDATED_TELEFONES
        defaultContadorShouldNotBeFound("telefones.contains=" + UPDATED_TELEFONES);
    }

    @Test
    @Transactional
    public void getAllContadorsByTelefonesNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where telefones does not contain DEFAULT_TELEFONES
        defaultContadorShouldNotBeFound("telefones.doesNotContain=" + DEFAULT_TELEFONES);

        // Get all the contadorList where telefones does not contain UPDATED_TELEFONES
        defaultContadorShouldBeFound("telefones.doesNotContain=" + UPDATED_TELEFONES);
    }


    @Test
    @Transactional
    public void getAllContadorsByDatasourceIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource equals to DEFAULT_DATASOURCE
        defaultContadorShouldBeFound("datasource.equals=" + DEFAULT_DATASOURCE);

        // Get all the contadorList where datasource equals to UPDATED_DATASOURCE
        defaultContadorShouldNotBeFound("datasource.equals=" + UPDATED_DATASOURCE);
    }

    @Test
    @Transactional
    public void getAllContadorsByDatasourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource not equals to DEFAULT_DATASOURCE
        defaultContadorShouldNotBeFound("datasource.notEquals=" + DEFAULT_DATASOURCE);

        // Get all the contadorList where datasource not equals to UPDATED_DATASOURCE
        defaultContadorShouldBeFound("datasource.notEquals=" + UPDATED_DATASOURCE);
    }

    @Test
    @Transactional
    public void getAllContadorsByDatasourceIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource in DEFAULT_DATASOURCE or UPDATED_DATASOURCE
        defaultContadorShouldBeFound("datasource.in=" + DEFAULT_DATASOURCE + "," + UPDATED_DATASOURCE);

        // Get all the contadorList where datasource equals to UPDATED_DATASOURCE
        defaultContadorShouldNotBeFound("datasource.in=" + UPDATED_DATASOURCE);
    }

    @Test
    @Transactional
    public void getAllContadorsByDatasourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource is not null
        defaultContadorShouldBeFound("datasource.specified=true");

        // Get all the contadorList where datasource is null
        defaultContadorShouldNotBeFound("datasource.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByDatasourceContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource contains DEFAULT_DATASOURCE
        defaultContadorShouldBeFound("datasource.contains=" + DEFAULT_DATASOURCE);

        // Get all the contadorList where datasource contains UPDATED_DATASOURCE
        defaultContadorShouldNotBeFound("datasource.contains=" + UPDATED_DATASOURCE);
    }

    @Test
    @Transactional
    public void getAllContadorsByDatasourceNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where datasource does not contain DEFAULT_DATASOURCE
        defaultContadorShouldNotBeFound("datasource.doesNotContain=" + DEFAULT_DATASOURCE);

        // Get all the contadorList where datasource does not contain UPDATED_DATASOURCE
        defaultContadorShouldBeFound("datasource.doesNotContain=" + UPDATED_DATASOURCE);
    }


    @Test
    @Transactional
    public void getAllContadorsByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj equals to DEFAULT_CNPJ
        defaultContadorShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the contadorList where cnpj equals to UPDATED_CNPJ
        defaultContadorShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContadorsByCnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj not equals to DEFAULT_CNPJ
        defaultContadorShouldNotBeFound("cnpj.notEquals=" + DEFAULT_CNPJ);

        // Get all the contadorList where cnpj not equals to UPDATED_CNPJ
        defaultContadorShouldBeFound("cnpj.notEquals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContadorsByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultContadorShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the contadorList where cnpj equals to UPDATED_CNPJ
        defaultContadorShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContadorsByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj is not null
        defaultContadorShouldBeFound("cnpj.specified=true");

        // Get all the contadorList where cnpj is null
        defaultContadorShouldNotBeFound("cnpj.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByCnpjContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj contains DEFAULT_CNPJ
        defaultContadorShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the contadorList where cnpj contains UPDATED_CNPJ
        defaultContadorShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    public void getAllContadorsByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cnpj does not contain DEFAULT_CNPJ
        defaultContadorShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the contadorList where cnpj does not contain UPDATED_CNPJ
        defaultContadorShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }


    @Test
    @Transactional
    public void getAllContadorsByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade equals to DEFAULT_CIDADE
        defaultContadorShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the contadorList where cidade equals to UPDATED_CIDADE
        defaultContadorShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllContadorsByCidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade not equals to DEFAULT_CIDADE
        defaultContadorShouldNotBeFound("cidade.notEquals=" + DEFAULT_CIDADE);

        // Get all the contadorList where cidade not equals to UPDATED_CIDADE
        defaultContadorShouldBeFound("cidade.notEquals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllContadorsByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultContadorShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the contadorList where cidade equals to UPDATED_CIDADE
        defaultContadorShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllContadorsByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade is not null
        defaultContadorShouldBeFound("cidade.specified=true");

        // Get all the contadorList where cidade is null
        defaultContadorShouldNotBeFound("cidade.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByCidadeContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade contains DEFAULT_CIDADE
        defaultContadorShouldBeFound("cidade.contains=" + DEFAULT_CIDADE);

        // Get all the contadorList where cidade contains UPDATED_CIDADE
        defaultContadorShouldNotBeFound("cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllContadorsByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cidade does not contain DEFAULT_CIDADE
        defaultContadorShouldNotBeFound("cidade.doesNotContain=" + DEFAULT_CIDADE);

        // Get all the contadorList where cidade does not contain UPDATED_CIDADE
        defaultContadorShouldBeFound("cidade.doesNotContain=" + UPDATED_CIDADE);
    }


    @Test
    @Transactional
    public void getAllContadorsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado equals to DEFAULT_ESTADO
        defaultContadorShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the contadorList where estado equals to UPDATED_ESTADO
        defaultContadorShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllContadorsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado not equals to DEFAULT_ESTADO
        defaultContadorShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the contadorList where estado not equals to UPDATED_ESTADO
        defaultContadorShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllContadorsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultContadorShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the contadorList where estado equals to UPDATED_ESTADO
        defaultContadorShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllContadorsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado is not null
        defaultContadorShouldBeFound("estado.specified=true");

        // Get all the contadorList where estado is null
        defaultContadorShouldNotBeFound("estado.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByEstadoContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado contains DEFAULT_ESTADO
        defaultContadorShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the contadorList where estado contains UPDATED_ESTADO
        defaultContadorShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllContadorsByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where estado does not contain DEFAULT_ESTADO
        defaultContadorShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the contadorList where estado does not contain UPDATED_ESTADO
        defaultContadorShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }


    @Test
    @Transactional
    public void getAllContadorsByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep equals to DEFAULT_CEP
        defaultContadorShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the contadorList where cep equals to UPDATED_CEP
        defaultContadorShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllContadorsByCepIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep not equals to DEFAULT_CEP
        defaultContadorShouldNotBeFound("cep.notEquals=" + DEFAULT_CEP);

        // Get all the contadorList where cep not equals to UPDATED_CEP
        defaultContadorShouldBeFound("cep.notEquals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllContadorsByCepIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultContadorShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the contadorList where cep equals to UPDATED_CEP
        defaultContadorShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllContadorsByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep is not null
        defaultContadorShouldBeFound("cep.specified=true");

        // Get all the contadorList where cep is null
        defaultContadorShouldNotBeFound("cep.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByCepContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep contains DEFAULT_CEP
        defaultContadorShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the contadorList where cep contains UPDATED_CEP
        defaultContadorShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllContadorsByCepNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where cep does not contain DEFAULT_CEP
        defaultContadorShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the contadorList where cep does not contain UPDATED_CEP
        defaultContadorShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }


    @Test
    @Transactional
    public void getAllContadorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email equals to DEFAULT_EMAIL
        defaultContadorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the contadorList where email equals to UPDATED_EMAIL
        defaultContadorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContadorsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email not equals to DEFAULT_EMAIL
        defaultContadorShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the contadorList where email not equals to UPDATED_EMAIL
        defaultContadorShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContadorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultContadorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the contadorList where email equals to UPDATED_EMAIL
        defaultContadorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContadorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email is not null
        defaultContadorShouldBeFound("email.specified=true");

        // Get all the contadorList where email is null
        defaultContadorShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllContadorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email contains DEFAULT_EMAIL
        defaultContadorShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the contadorList where email contains UPDATED_EMAIL
        defaultContadorShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContadorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        // Get all the contadorList where email does not contain DEFAULT_EMAIL
        defaultContadorShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the contadorList where email does not contain UPDATED_EMAIL
        defaultContadorShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContadorShouldBeFound(String filter) throws Exception {
        restContadorMockMvc.perform(get("/api/contadors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contador.getId().intValue())))
            .andExpect(jsonPath("$.[*].razao").value(hasItem(DEFAULT_RAZAO)))
            .andExpect(jsonPath("$.[*].fantasia").value(hasItem(DEFAULT_FANTASIA)))
            .andExpect(jsonPath("$.[*].telefones").value(hasItem(DEFAULT_TELEFONES)))
            .andExpect(jsonPath("$.[*].datasource").value(hasItem(DEFAULT_DATASOURCE)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restContadorMockMvc.perform(get("/api/contadors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContadorShouldNotBeFound(String filter) throws Exception {
        restContadorMockMvc.perform(get("/api/contadors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContadorMockMvc.perform(get("/api/contadors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContador() throws Exception {
        // Get the contador
        restContadorMockMvc.perform(get("/api/contadors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContador() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        int databaseSizeBeforeUpdate = contadorRepository.findAll().size();

        // Update the contador
        Contador updatedContador = contadorRepository.findById(contador.getId()).get();
        // Disconnect from session so that the updates on updatedContador are not directly saved in db
        em.detach(updatedContador);
        updatedContador
            .razao(UPDATED_RAZAO)
            .fantasia(UPDATED_FANTASIA)
            .telefones(UPDATED_TELEFONES)
            .datasource(UPDATED_DATASOURCE)
            .cnpj(UPDATED_CNPJ)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .email(UPDATED_EMAIL);
        ContadorDTO contadorDTO = contadorMapper.toDto(updatedContador);

        restContadorMockMvc.perform(put("/api/contadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contadorDTO)))
            .andExpect(status().isOk());

        // Validate the Contador in the database
        List<Contador> contadorList = contadorRepository.findAll();
        assertThat(contadorList).hasSize(databaseSizeBeforeUpdate);
        Contador testContador = contadorList.get(contadorList.size() - 1);
        assertThat(testContador.getRazao()).isEqualTo(UPDATED_RAZAO);
        assertThat(testContador.getFantasia()).isEqualTo(UPDATED_FANTASIA);
        assertThat(testContador.getTelefones()).isEqualTo(UPDATED_TELEFONES);
        assertThat(testContador.getDatasource()).isEqualTo(UPDATED_DATASOURCE);
        assertThat(testContador.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testContador.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testContador.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testContador.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testContador.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingContador() throws Exception {
        int databaseSizeBeforeUpdate = contadorRepository.findAll().size();

        // Create the Contador
        ContadorDTO contadorDTO = contadorMapper.toDto(contador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContadorMockMvc.perform(put("/api/contadors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        List<Contador> contadorList = contadorRepository.findAll();
        assertThat(contadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContador() throws Exception {
        // Initialize the database
        contadorRepository.saveAndFlush(contador);

        int databaseSizeBeforeDelete = contadorRepository.findAll().size();

        // Delete the contador
        restContadorMockMvc.perform(delete("/api/contadors/{id}", contador.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contador> contadorList = contadorRepository.findAll();
        assertThat(contadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
