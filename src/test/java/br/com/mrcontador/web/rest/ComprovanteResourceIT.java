package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.ComprovanteDTO;
import br.com.mrcontador.service.mapper.ComprovanteMapper;
import br.com.mrcontador.service.dto.ComprovanteCriteria;
import br.com.mrcontador.service.ComprovanteQueryService;

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
import java.math.BigDecimal;
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
 * Integration tests for the {@link ComprovanteResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ComprovanteResourceIT {

    private static final Integer DEFAULT_PAR_CODIGO = 1;
    private static final Integer UPDATED_PAR_CODIGO = 2;
    private static final Integer SMALLER_PAR_CODIGO = 1 - 1;

    private static final Integer DEFAULT_AGE_CODIGO = 1;
    private static final Integer UPDATED_AGE_CODIGO = 2;
    private static final Integer SMALLER_AGE_CODIGO = 1 - 1;

    private static final String DEFAULT_COM_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_COM_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_COM_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_COM_BENEFICIARIO = "BBBBBBBBBB";

    private static final String DEFAULT_COM_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COM_DOCUMENTO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_COM_DATAVENCIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COM_DATAVENCIMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COM_DATAVENCIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_COM_DATAPAGAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COM_DATAPAGAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COM_DATAPAGAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_COM_VALORDOCUMENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_COM_VALORDOCUMENTO = new BigDecimal(2);
    private static final BigDecimal SMALLER_COM_VALORDOCUMENTO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COM_VALORPAGAMENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_COM_VALORPAGAMENTO = new BigDecimal(2);
    private static final BigDecimal SMALLER_COM_VALORPAGAMENTO = new BigDecimal(1 - 1);

    private static final String DEFAULT_COM_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_COM_OBSERVACAO = "BBBBBBBBBB";

    @Autowired
    private ComprovanteRepository comprovanteRepository;

    @Autowired
    private ComprovanteMapper comprovanteMapper;

    @Autowired
    private ComprovanteService comprovanteService;

    @Autowired
    private ComprovanteQueryService comprovanteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComprovanteMockMvc;

    private Comprovante comprovante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprovante createEntity(EntityManager em) {
        Comprovante comprovante = new Comprovante()
            .par_codigo(DEFAULT_PAR_CODIGO)
            .age_codigo(DEFAULT_AGE_CODIGO)
            .com_cnpj(DEFAULT_COM_CNPJ)
            .com_beneficiario(DEFAULT_COM_BENEFICIARIO)
            .com_documento(DEFAULT_COM_DOCUMENTO)
            .com_datavencimento(DEFAULT_COM_DATAVENCIMENTO)
            .com_datapagamento(DEFAULT_COM_DATAPAGAMENTO)
            .com_valordocumento(DEFAULT_COM_VALORDOCUMENTO)
            .com_valorpagamento(DEFAULT_COM_VALORPAGAMENTO)
            .com_observacao(DEFAULT_COM_OBSERVACAO);
        return comprovante;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprovante createUpdatedEntity(EntityManager em) {
        Comprovante comprovante = new Comprovante()
            .par_codigo(UPDATED_PAR_CODIGO)
            .age_codigo(UPDATED_AGE_CODIGO)
            .com_cnpj(UPDATED_COM_CNPJ)
            .com_beneficiario(UPDATED_COM_BENEFICIARIO)
            .com_documento(UPDATED_COM_DOCUMENTO)
            .com_datavencimento(UPDATED_COM_DATAVENCIMENTO)
            .com_datapagamento(UPDATED_COM_DATAPAGAMENTO)
            .com_valordocumento(UPDATED_COM_VALORDOCUMENTO)
            .com_valorpagamento(UPDATED_COM_VALORPAGAMENTO)
            .com_observacao(UPDATED_COM_OBSERVACAO);
        return comprovante;
    }

    @BeforeEach
    public void initTest() {
        comprovante = createEntity(em);
    }

    @Test
    @Transactional
    public void createComprovante() throws Exception {
        int databaseSizeBeforeCreate = comprovanteRepository.findAll().size();
        // Create the Comprovante
        ComprovanteDTO comprovanteDTO = comprovanteMapper.toDto(comprovante);
        restComprovanteMockMvc.perform(post("/api/comprovantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comprovanteDTO)))
            .andExpect(status().isCreated());

        // Validate the Comprovante in the database
        List<Comprovante> comprovanteList = comprovanteRepository.findAll();
        assertThat(comprovanteList).hasSize(databaseSizeBeforeCreate + 1);
        Comprovante testComprovante = comprovanteList.get(comprovanteList.size() - 1);
        assertThat(testComprovante.getPar_codigo()).isEqualTo(DEFAULT_PAR_CODIGO);
        assertThat(testComprovante.getAge_codigo()).isEqualTo(DEFAULT_AGE_CODIGO);
        assertThat(testComprovante.getCom_cnpj()).isEqualTo(DEFAULT_COM_CNPJ);
        assertThat(testComprovante.getCom_beneficiario()).isEqualTo(DEFAULT_COM_BENEFICIARIO);
        assertThat(testComprovante.getCom_documento()).isEqualTo(DEFAULT_COM_DOCUMENTO);
        assertThat(testComprovante.getCom_datavencimento()).isEqualTo(DEFAULT_COM_DATAVENCIMENTO);
        assertThat(testComprovante.getCom_datapagamento()).isEqualTo(DEFAULT_COM_DATAPAGAMENTO);
        assertThat(testComprovante.getCom_valordocumento()).isEqualTo(DEFAULT_COM_VALORDOCUMENTO);
        assertThat(testComprovante.getCom_valorpagamento()).isEqualTo(DEFAULT_COM_VALORPAGAMENTO);
        assertThat(testComprovante.getCom_observacao()).isEqualTo(DEFAULT_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void createComprovanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comprovanteRepository.findAll().size();

        // Create the Comprovante with an existing ID
        comprovante.setId(1L);
        ComprovanteDTO comprovanteDTO = comprovanteMapper.toDto(comprovante);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComprovanteMockMvc.perform(post("/api/comprovantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comprovanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comprovante in the database
        List<Comprovante> comprovanteList = comprovanteRepository.findAll();
        assertThat(comprovanteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllComprovantes() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList
        restComprovanteMockMvc.perform(get("/api/comprovantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprovante.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_codigo").value(hasItem(DEFAULT_PAR_CODIGO)))
            .andExpect(jsonPath("$.[*].age_codigo").value(hasItem(DEFAULT_AGE_CODIGO)))
            .andExpect(jsonPath("$.[*].com_cnpj").value(hasItem(DEFAULT_COM_CNPJ)))
            .andExpect(jsonPath("$.[*].com_beneficiario").value(hasItem(DEFAULT_COM_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].com_documento").value(hasItem(DEFAULT_COM_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].com_datavencimento").value(hasItem(sameInstant(DEFAULT_COM_DATAVENCIMENTO))))
            .andExpect(jsonPath("$.[*].com_datapagamento").value(hasItem(sameInstant(DEFAULT_COM_DATAPAGAMENTO))))
            .andExpect(jsonPath("$.[*].com_valordocumento").value(hasItem(DEFAULT_COM_VALORDOCUMENTO.intValue())))
            .andExpect(jsonPath("$.[*].com_valorpagamento").value(hasItem(DEFAULT_COM_VALORPAGAMENTO.intValue())))
            .andExpect(jsonPath("$.[*].com_observacao").value(hasItem(DEFAULT_COM_OBSERVACAO)));
    }
    
    @Test
    @Transactional
    public void getComprovante() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get the comprovante
        restComprovanteMockMvc.perform(get("/api/comprovantes/{id}", comprovante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comprovante.getId().intValue()))
            .andExpect(jsonPath("$.par_codigo").value(DEFAULT_PAR_CODIGO))
            .andExpect(jsonPath("$.age_codigo").value(DEFAULT_AGE_CODIGO))
            .andExpect(jsonPath("$.com_cnpj").value(DEFAULT_COM_CNPJ))
            .andExpect(jsonPath("$.com_beneficiario").value(DEFAULT_COM_BENEFICIARIO))
            .andExpect(jsonPath("$.com_documento").value(DEFAULT_COM_DOCUMENTO))
            .andExpect(jsonPath("$.com_datavencimento").value(sameInstant(DEFAULT_COM_DATAVENCIMENTO)))
            .andExpect(jsonPath("$.com_datapagamento").value(sameInstant(DEFAULT_COM_DATAPAGAMENTO)))
            .andExpect(jsonPath("$.com_valordocumento").value(DEFAULT_COM_VALORDOCUMENTO.intValue()))
            .andExpect(jsonPath("$.com_valorpagamento").value(DEFAULT_COM_VALORPAGAMENTO.intValue()))
            .andExpect(jsonPath("$.com_observacao").value(DEFAULT_COM_OBSERVACAO));
    }


    @Test
    @Transactional
    public void getComprovantesByIdFiltering() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        Long id = comprovante.getId();

        defaultComprovanteShouldBeFound("id.equals=" + id);
        defaultComprovanteShouldNotBeFound("id.notEquals=" + id);

        defaultComprovanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComprovanteShouldNotBeFound("id.greaterThan=" + id);

        defaultComprovanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComprovanteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo equals to DEFAULT_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.equals=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo equals to UPDATED_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.equals=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo not equals to DEFAULT_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.notEquals=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo not equals to UPDATED_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.notEquals=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo in DEFAULT_PAR_CODIGO or UPDATED_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.in=" + DEFAULT_PAR_CODIGO + "," + UPDATED_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo equals to UPDATED_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.in=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo is not null
        defaultComprovanteShouldBeFound("par_codigo.specified=true");

        // Get all the comprovanteList where par_codigo is null
        defaultComprovanteShouldNotBeFound("par_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo is greater than or equal to DEFAULT_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.greaterThanOrEqual=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo is greater than or equal to UPDATED_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.greaterThanOrEqual=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo is less than or equal to DEFAULT_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.lessThanOrEqual=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo is less than or equal to SMALLER_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.lessThanOrEqual=" + SMALLER_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo is less than DEFAULT_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.lessThan=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo is less than UPDATED_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.lessThan=" + UPDATED_PAR_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByPar_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where par_codigo is greater than DEFAULT_PAR_CODIGO
        defaultComprovanteShouldNotBeFound("par_codigo.greaterThan=" + DEFAULT_PAR_CODIGO);

        // Get all the comprovanteList where par_codigo is greater than SMALLER_PAR_CODIGO
        defaultComprovanteShouldBeFound("par_codigo.greaterThan=" + SMALLER_PAR_CODIGO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo equals to DEFAULT_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.equals=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo equals to UPDATED_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.equals=" + UPDATED_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo not equals to DEFAULT_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.notEquals=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo not equals to UPDATED_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.notEquals=" + UPDATED_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo in DEFAULT_AGE_CODIGO or UPDATED_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.in=" + DEFAULT_AGE_CODIGO + "," + UPDATED_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo equals to UPDATED_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.in=" + UPDATED_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo is not null
        defaultComprovanteShouldBeFound("age_codigo.specified=true");

        // Get all the comprovanteList where age_codigo is null
        defaultComprovanteShouldNotBeFound("age_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo is greater than or equal to DEFAULT_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.greaterThanOrEqual=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo is greater than or equal to UPDATED_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.greaterThanOrEqual=" + UPDATED_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo is less than or equal to DEFAULT_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.lessThanOrEqual=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo is less than or equal to SMALLER_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.lessThanOrEqual=" + SMALLER_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo is less than DEFAULT_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.lessThan=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo is less than UPDATED_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.lessThan=" + UPDATED_AGE_CODIGO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByAge_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where age_codigo is greater than DEFAULT_AGE_CODIGO
        defaultComprovanteShouldNotBeFound("age_codigo.greaterThan=" + DEFAULT_AGE_CODIGO);

        // Get all the comprovanteList where age_codigo is greater than SMALLER_AGE_CODIGO
        defaultComprovanteShouldBeFound("age_codigo.greaterThan=" + SMALLER_AGE_CODIGO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj equals to DEFAULT_COM_CNPJ
        defaultComprovanteShouldBeFound("com_cnpj.equals=" + DEFAULT_COM_CNPJ);

        // Get all the comprovanteList where com_cnpj equals to UPDATED_COM_CNPJ
        defaultComprovanteShouldNotBeFound("com_cnpj.equals=" + UPDATED_COM_CNPJ);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj not equals to DEFAULT_COM_CNPJ
        defaultComprovanteShouldNotBeFound("com_cnpj.notEquals=" + DEFAULT_COM_CNPJ);

        // Get all the comprovanteList where com_cnpj not equals to UPDATED_COM_CNPJ
        defaultComprovanteShouldBeFound("com_cnpj.notEquals=" + UPDATED_COM_CNPJ);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj in DEFAULT_COM_CNPJ or UPDATED_COM_CNPJ
        defaultComprovanteShouldBeFound("com_cnpj.in=" + DEFAULT_COM_CNPJ + "," + UPDATED_COM_CNPJ);

        // Get all the comprovanteList where com_cnpj equals to UPDATED_COM_CNPJ
        defaultComprovanteShouldNotBeFound("com_cnpj.in=" + UPDATED_COM_CNPJ);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj is not null
        defaultComprovanteShouldBeFound("com_cnpj.specified=true");

        // Get all the comprovanteList where com_cnpj is null
        defaultComprovanteShouldNotBeFound("com_cnpj.specified=false");
    }
                @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj contains DEFAULT_COM_CNPJ
        defaultComprovanteShouldBeFound("com_cnpj.contains=" + DEFAULT_COM_CNPJ);

        // Get all the comprovanteList where com_cnpj contains UPDATED_COM_CNPJ
        defaultComprovanteShouldNotBeFound("com_cnpj.contains=" + UPDATED_COM_CNPJ);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_cnpjNotContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_cnpj does not contain DEFAULT_COM_CNPJ
        defaultComprovanteShouldNotBeFound("com_cnpj.doesNotContain=" + DEFAULT_COM_CNPJ);

        // Get all the comprovanteList where com_cnpj does not contain UPDATED_COM_CNPJ
        defaultComprovanteShouldBeFound("com_cnpj.doesNotContain=" + UPDATED_COM_CNPJ);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario equals to DEFAULT_COM_BENEFICIARIO
        defaultComprovanteShouldBeFound("com_beneficiario.equals=" + DEFAULT_COM_BENEFICIARIO);

        // Get all the comprovanteList where com_beneficiario equals to UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldNotBeFound("com_beneficiario.equals=" + UPDATED_COM_BENEFICIARIO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario not equals to DEFAULT_COM_BENEFICIARIO
        defaultComprovanteShouldNotBeFound("com_beneficiario.notEquals=" + DEFAULT_COM_BENEFICIARIO);

        // Get all the comprovanteList where com_beneficiario not equals to UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldBeFound("com_beneficiario.notEquals=" + UPDATED_COM_BENEFICIARIO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario in DEFAULT_COM_BENEFICIARIO or UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldBeFound("com_beneficiario.in=" + DEFAULT_COM_BENEFICIARIO + "," + UPDATED_COM_BENEFICIARIO);

        // Get all the comprovanteList where com_beneficiario equals to UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldNotBeFound("com_beneficiario.in=" + UPDATED_COM_BENEFICIARIO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario is not null
        defaultComprovanteShouldBeFound("com_beneficiario.specified=true");

        // Get all the comprovanteList where com_beneficiario is null
        defaultComprovanteShouldNotBeFound("com_beneficiario.specified=false");
    }
                @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario contains DEFAULT_COM_BENEFICIARIO
        defaultComprovanteShouldBeFound("com_beneficiario.contains=" + DEFAULT_COM_BENEFICIARIO);

        // Get all the comprovanteList where com_beneficiario contains UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldNotBeFound("com_beneficiario.contains=" + UPDATED_COM_BENEFICIARIO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_beneficiarioNotContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_beneficiario does not contain DEFAULT_COM_BENEFICIARIO
        defaultComprovanteShouldNotBeFound("com_beneficiario.doesNotContain=" + DEFAULT_COM_BENEFICIARIO);

        // Get all the comprovanteList where com_beneficiario does not contain UPDATED_COM_BENEFICIARIO
        defaultComprovanteShouldBeFound("com_beneficiario.doesNotContain=" + UPDATED_COM_BENEFICIARIO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_documentoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento equals to DEFAULT_COM_DOCUMENTO
        defaultComprovanteShouldBeFound("com_documento.equals=" + DEFAULT_COM_DOCUMENTO);

        // Get all the comprovanteList where com_documento equals to UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldNotBeFound("com_documento.equals=" + UPDATED_COM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_documentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento not equals to DEFAULT_COM_DOCUMENTO
        defaultComprovanteShouldNotBeFound("com_documento.notEquals=" + DEFAULT_COM_DOCUMENTO);

        // Get all the comprovanteList where com_documento not equals to UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldBeFound("com_documento.notEquals=" + UPDATED_COM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_documentoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento in DEFAULT_COM_DOCUMENTO or UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldBeFound("com_documento.in=" + DEFAULT_COM_DOCUMENTO + "," + UPDATED_COM_DOCUMENTO);

        // Get all the comprovanteList where com_documento equals to UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldNotBeFound("com_documento.in=" + UPDATED_COM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_documentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento is not null
        defaultComprovanteShouldBeFound("com_documento.specified=true");

        // Get all the comprovanteList where com_documento is null
        defaultComprovanteShouldNotBeFound("com_documento.specified=false");
    }
                @Test
    @Transactional
    public void getAllComprovantesByCom_documentoContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento contains DEFAULT_COM_DOCUMENTO
        defaultComprovanteShouldBeFound("com_documento.contains=" + DEFAULT_COM_DOCUMENTO);

        // Get all the comprovanteList where com_documento contains UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldNotBeFound("com_documento.contains=" + UPDATED_COM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_documentoNotContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_documento does not contain DEFAULT_COM_DOCUMENTO
        defaultComprovanteShouldNotBeFound("com_documento.doesNotContain=" + DEFAULT_COM_DOCUMENTO);

        // Get all the comprovanteList where com_documento does not contain UPDATED_COM_DOCUMENTO
        defaultComprovanteShouldBeFound("com_documento.doesNotContain=" + UPDATED_COM_DOCUMENTO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento equals to DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.equals=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento equals to UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.equals=" + UPDATED_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento not equals to DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.notEquals=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento not equals to UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.notEquals=" + UPDATED_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento in DEFAULT_COM_DATAVENCIMENTO or UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.in=" + DEFAULT_COM_DATAVENCIMENTO + "," + UPDATED_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento equals to UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.in=" + UPDATED_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento is not null
        defaultComprovanteShouldBeFound("com_datavencimento.specified=true");

        // Get all the comprovanteList where com_datavencimento is null
        defaultComprovanteShouldNotBeFound("com_datavencimento.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento is greater than or equal to DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.greaterThanOrEqual=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento is greater than or equal to UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.greaterThanOrEqual=" + UPDATED_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento is less than or equal to DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.lessThanOrEqual=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento is less than or equal to SMALLER_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.lessThanOrEqual=" + SMALLER_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento is less than DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.lessThan=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento is less than UPDATED_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.lessThan=" + UPDATED_COM_DATAVENCIMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datavencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datavencimento is greater than DEFAULT_COM_DATAVENCIMENTO
        defaultComprovanteShouldNotBeFound("com_datavencimento.greaterThan=" + DEFAULT_COM_DATAVENCIMENTO);

        // Get all the comprovanteList where com_datavencimento is greater than SMALLER_COM_DATAVENCIMENTO
        defaultComprovanteShouldBeFound("com_datavencimento.greaterThan=" + SMALLER_COM_DATAVENCIMENTO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento equals to DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.equals=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento equals to UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.equals=" + UPDATED_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento not equals to DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.notEquals=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento not equals to UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.notEquals=" + UPDATED_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento in DEFAULT_COM_DATAPAGAMENTO or UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.in=" + DEFAULT_COM_DATAPAGAMENTO + "," + UPDATED_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento equals to UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.in=" + UPDATED_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento is not null
        defaultComprovanteShouldBeFound("com_datapagamento.specified=true");

        // Get all the comprovanteList where com_datapagamento is null
        defaultComprovanteShouldNotBeFound("com_datapagamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento is greater than or equal to DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.greaterThanOrEqual=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento is greater than or equal to UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.greaterThanOrEqual=" + UPDATED_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento is less than or equal to DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.lessThanOrEqual=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento is less than or equal to SMALLER_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.lessThanOrEqual=" + SMALLER_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento is less than DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.lessThan=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento is less than UPDATED_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.lessThan=" + UPDATED_COM_DATAPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_datapagamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_datapagamento is greater than DEFAULT_COM_DATAPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_datapagamento.greaterThan=" + DEFAULT_COM_DATAPAGAMENTO);

        // Get all the comprovanteList where com_datapagamento is greater than SMALLER_COM_DATAPAGAMENTO
        defaultComprovanteShouldBeFound("com_datapagamento.greaterThan=" + SMALLER_COM_DATAPAGAMENTO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento equals to DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.equals=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento equals to UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.equals=" + UPDATED_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento not equals to DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.notEquals=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento not equals to UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.notEquals=" + UPDATED_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento in DEFAULT_COM_VALORDOCUMENTO or UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.in=" + DEFAULT_COM_VALORDOCUMENTO + "," + UPDATED_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento equals to UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.in=" + UPDATED_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento is not null
        defaultComprovanteShouldBeFound("com_valordocumento.specified=true");

        // Get all the comprovanteList where com_valordocumento is null
        defaultComprovanteShouldNotBeFound("com_valordocumento.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento is greater than or equal to DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.greaterThanOrEqual=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento is greater than or equal to UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.greaterThanOrEqual=" + UPDATED_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento is less than or equal to DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.lessThanOrEqual=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento is less than or equal to SMALLER_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.lessThanOrEqual=" + SMALLER_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento is less than DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.lessThan=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento is less than UPDATED_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.lessThan=" + UPDATED_COM_VALORDOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valordocumentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valordocumento is greater than DEFAULT_COM_VALORDOCUMENTO
        defaultComprovanteShouldNotBeFound("com_valordocumento.greaterThan=" + DEFAULT_COM_VALORDOCUMENTO);

        // Get all the comprovanteList where com_valordocumento is greater than SMALLER_COM_VALORDOCUMENTO
        defaultComprovanteShouldBeFound("com_valordocumento.greaterThan=" + SMALLER_COM_VALORDOCUMENTO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento equals to DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.equals=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento equals to UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.equals=" + UPDATED_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento not equals to DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.notEquals=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento not equals to UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.notEquals=" + UPDATED_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento in DEFAULT_COM_VALORPAGAMENTO or UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.in=" + DEFAULT_COM_VALORPAGAMENTO + "," + UPDATED_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento equals to UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.in=" + UPDATED_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento is not null
        defaultComprovanteShouldBeFound("com_valorpagamento.specified=true");

        // Get all the comprovanteList where com_valorpagamento is null
        defaultComprovanteShouldNotBeFound("com_valorpagamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento is greater than or equal to DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.greaterThanOrEqual=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento is greater than or equal to UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.greaterThanOrEqual=" + UPDATED_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento is less than or equal to DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.lessThanOrEqual=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento is less than or equal to SMALLER_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.lessThanOrEqual=" + SMALLER_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento is less than DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.lessThan=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento is less than UPDATED_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.lessThan=" + UPDATED_COM_VALORPAGAMENTO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_valorpagamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_valorpagamento is greater than DEFAULT_COM_VALORPAGAMENTO
        defaultComprovanteShouldNotBeFound("com_valorpagamento.greaterThan=" + DEFAULT_COM_VALORPAGAMENTO);

        // Get all the comprovanteList where com_valorpagamento is greater than SMALLER_COM_VALORPAGAMENTO
        defaultComprovanteShouldBeFound("com_valorpagamento.greaterThan=" + SMALLER_COM_VALORPAGAMENTO);
    }


    @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao equals to DEFAULT_COM_OBSERVACAO
        defaultComprovanteShouldBeFound("com_observacao.equals=" + DEFAULT_COM_OBSERVACAO);

        // Get all the comprovanteList where com_observacao equals to UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldNotBeFound("com_observacao.equals=" + UPDATED_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao not equals to DEFAULT_COM_OBSERVACAO
        defaultComprovanteShouldNotBeFound("com_observacao.notEquals=" + DEFAULT_COM_OBSERVACAO);

        // Get all the comprovanteList where com_observacao not equals to UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldBeFound("com_observacao.notEquals=" + UPDATED_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoIsInShouldWork() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao in DEFAULT_COM_OBSERVACAO or UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldBeFound("com_observacao.in=" + DEFAULT_COM_OBSERVACAO + "," + UPDATED_COM_OBSERVACAO);

        // Get all the comprovanteList where com_observacao equals to UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldNotBeFound("com_observacao.in=" + UPDATED_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao is not null
        defaultComprovanteShouldBeFound("com_observacao.specified=true");

        // Get all the comprovanteList where com_observacao is null
        defaultComprovanteShouldNotBeFound("com_observacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao contains DEFAULT_COM_OBSERVACAO
        defaultComprovanteShouldBeFound("com_observacao.contains=" + DEFAULT_COM_OBSERVACAO);

        // Get all the comprovanteList where com_observacao contains UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldNotBeFound("com_observacao.contains=" + UPDATED_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllComprovantesByCom_observacaoNotContainsSomething() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        // Get all the comprovanteList where com_observacao does not contain DEFAULT_COM_OBSERVACAO
        defaultComprovanteShouldNotBeFound("com_observacao.doesNotContain=" + DEFAULT_COM_OBSERVACAO);

        // Get all the comprovanteList where com_observacao does not contain UPDATED_COM_OBSERVACAO
        defaultComprovanteShouldBeFound("com_observacao.doesNotContain=" + UPDATED_COM_OBSERVACAO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComprovanteShouldBeFound(String filter) throws Exception {
        restComprovanteMockMvc.perform(get("/api/comprovantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprovante.getId().intValue())))
            .andExpect(jsonPath("$.[*].par_codigo").value(hasItem(DEFAULT_PAR_CODIGO)))
            .andExpect(jsonPath("$.[*].age_codigo").value(hasItem(DEFAULT_AGE_CODIGO)))
            .andExpect(jsonPath("$.[*].com_cnpj").value(hasItem(DEFAULT_COM_CNPJ)))
            .andExpect(jsonPath("$.[*].com_beneficiario").value(hasItem(DEFAULT_COM_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].com_documento").value(hasItem(DEFAULT_COM_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].com_datavencimento").value(hasItem(sameInstant(DEFAULT_COM_DATAVENCIMENTO))))
            .andExpect(jsonPath("$.[*].com_datapagamento").value(hasItem(sameInstant(DEFAULT_COM_DATAPAGAMENTO))))
            .andExpect(jsonPath("$.[*].com_valordocumento").value(hasItem(DEFAULT_COM_VALORDOCUMENTO.intValue())))
            .andExpect(jsonPath("$.[*].com_valorpagamento").value(hasItem(DEFAULT_COM_VALORPAGAMENTO.intValue())))
            .andExpect(jsonPath("$.[*].com_observacao").value(hasItem(DEFAULT_COM_OBSERVACAO)));

        // Check, that the count call also returns 1
        restComprovanteMockMvc.perform(get("/api/comprovantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComprovanteShouldNotBeFound(String filter) throws Exception {
        restComprovanteMockMvc.perform(get("/api/comprovantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComprovanteMockMvc.perform(get("/api/comprovantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingComprovante() throws Exception {
        // Get the comprovante
        restComprovanteMockMvc.perform(get("/api/comprovantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComprovante() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        int databaseSizeBeforeUpdate = comprovanteRepository.findAll().size();

        // Update the comprovante
        Comprovante updatedComprovante = comprovanteRepository.findById(comprovante.getId()).get();
        // Disconnect from session so that the updates on updatedComprovante are not directly saved in db
        em.detach(updatedComprovante);
        updatedComprovante
            .par_codigo(UPDATED_PAR_CODIGO)
            .age_codigo(UPDATED_AGE_CODIGO)
            .com_cnpj(UPDATED_COM_CNPJ)
            .com_beneficiario(UPDATED_COM_BENEFICIARIO)
            .com_documento(UPDATED_COM_DOCUMENTO)
            .com_datavencimento(UPDATED_COM_DATAVENCIMENTO)
            .com_datapagamento(UPDATED_COM_DATAPAGAMENTO)
            .com_valordocumento(UPDATED_COM_VALORDOCUMENTO)
            .com_valorpagamento(UPDATED_COM_VALORPAGAMENTO)
            .com_observacao(UPDATED_COM_OBSERVACAO);
        ComprovanteDTO comprovanteDTO = comprovanteMapper.toDto(updatedComprovante);

        restComprovanteMockMvc.perform(put("/api/comprovantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comprovanteDTO)))
            .andExpect(status().isOk());

        // Validate the Comprovante in the database
        List<Comprovante> comprovanteList = comprovanteRepository.findAll();
        assertThat(comprovanteList).hasSize(databaseSizeBeforeUpdate);
        Comprovante testComprovante = comprovanteList.get(comprovanteList.size() - 1);
        assertThat(testComprovante.getPar_codigo()).isEqualTo(UPDATED_PAR_CODIGO);
        assertThat(testComprovante.getAge_codigo()).isEqualTo(UPDATED_AGE_CODIGO);
        assertThat(testComprovante.getCom_cnpj()).isEqualTo(UPDATED_COM_CNPJ);
        assertThat(testComprovante.getCom_beneficiario()).isEqualTo(UPDATED_COM_BENEFICIARIO);
        assertThat(testComprovante.getCom_documento()).isEqualTo(UPDATED_COM_DOCUMENTO);
        assertThat(testComprovante.getCom_datavencimento()).isEqualTo(UPDATED_COM_DATAVENCIMENTO);
        assertThat(testComprovante.getCom_datapagamento()).isEqualTo(UPDATED_COM_DATAPAGAMENTO);
        assertThat(testComprovante.getCom_valordocumento()).isEqualTo(UPDATED_COM_VALORDOCUMENTO);
        assertThat(testComprovante.getCom_valorpagamento()).isEqualTo(UPDATED_COM_VALORPAGAMENTO);
        assertThat(testComprovante.getCom_observacao()).isEqualTo(UPDATED_COM_OBSERVACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingComprovante() throws Exception {
        int databaseSizeBeforeUpdate = comprovanteRepository.findAll().size();

        // Create the Comprovante
        ComprovanteDTO comprovanteDTO = comprovanteMapper.toDto(comprovante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprovanteMockMvc.perform(put("/api/comprovantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comprovanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comprovante in the database
        List<Comprovante> comprovanteList = comprovanteRepository.findAll();
        assertThat(comprovanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComprovante() throws Exception {
        // Initialize the database
        comprovanteRepository.saveAndFlush(comprovante);

        int databaseSizeBeforeDelete = comprovanteRepository.findAll().size();

        // Delete the comprovante
        restComprovanteMockMvc.perform(delete("/api/comprovantes/{id}", comprovante.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comprovante> comprovanteList = comprovanteRepository.findAll();
        assertThat(comprovanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
