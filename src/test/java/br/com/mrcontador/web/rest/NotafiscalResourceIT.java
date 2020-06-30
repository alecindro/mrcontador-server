package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.NotafiscalRepository;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.dto.NotafiscalDTO;
import br.com.mrcontador.service.mapper.NotafiscalMapper;
import br.com.mrcontador.service.dto.NotafiscalCriteria;
import br.com.mrcontador.service.NotafiscalQueryService;

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
 * Integration tests for the {@link NotafiscalResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotafiscalResourceIT {

    private static final Integer DEFAULT_NOT_NUMERO = 1;
    private static final Integer UPDATED_NOT_NUMERO = 2;
    private static final Integer SMALLER_NOT_NUMERO = 1 - 1;

    private static final String DEFAULT_NOT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_NOT_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_NOT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_NOT_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_NOT_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_NOT_EMPRESA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_NOT_DATASAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NOT_DATASAIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_NOT_DATASAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_NOT_VALORNOTA = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOT_VALORNOTA = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOT_VALORNOTA = new BigDecimal(1 - 1);

    private static final ZonedDateTime DEFAULT_NOT_DATAPARCELA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NOT_DATAPARCELA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_NOT_DATAPARCELA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_NOT_VALORPARCELA = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOT_VALORPARCELA = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOT_VALORPARCELA = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_TNO_CODIGO = 1;
    private static final Integer UPDATED_TNO_CODIGO = 2;
    private static final Integer SMALLER_TNO_CODIGO = 1 - 1;

    private static final String DEFAULT_NOT_PARCELA = "AAAAAAAAAA";
    private static final String UPDATED_NOT_PARCELA = "BBBBBBBBBB";

    @Autowired
    private NotafiscalRepository notafiscalRepository;

    @Autowired
    private NotafiscalMapper notafiscalMapper;

    @Autowired
    private NotafiscalService notafiscalService;

    @Autowired
    private NotafiscalQueryService notafiscalQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotafiscalMockMvc;

    private Notafiscal notafiscal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notafiscal createEntity(EntityManager em) {
        Notafiscal notafiscal = new Notafiscal()
            .not_numero(DEFAULT_NOT_NUMERO)
            .not_descricao(DEFAULT_NOT_DESCRICAO)
            .not_cnpj(DEFAULT_NOT_CNPJ)
            .not_empresa(DEFAULT_NOT_EMPRESA)
            .not_datasaida(DEFAULT_NOT_DATASAIDA)
            .not_valornota(DEFAULT_NOT_VALORNOTA)
            .not_dataparcela(DEFAULT_NOT_DATAPARCELA)
            .not_valorparcela(DEFAULT_NOT_VALORPARCELA)
            .tno_codigo(DEFAULT_TNO_CODIGO)
            .not_parcela(DEFAULT_NOT_PARCELA);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        notafiscal.setParceiro(parceiro);
        return notafiscal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notafiscal createUpdatedEntity(EntityManager em) {
        Notafiscal notafiscal = new Notafiscal()
            .not_numero(UPDATED_NOT_NUMERO)
            .not_descricao(UPDATED_NOT_DESCRICAO)
            .not_cnpj(UPDATED_NOT_CNPJ)
            .not_empresa(UPDATED_NOT_EMPRESA)
            .not_datasaida(UPDATED_NOT_DATASAIDA)
            .not_valornota(UPDATED_NOT_VALORNOTA)
            .not_dataparcela(UPDATED_NOT_DATAPARCELA)
            .not_valorparcela(UPDATED_NOT_VALORPARCELA)
            .tno_codigo(UPDATED_TNO_CODIGO)
            .not_parcela(UPDATED_NOT_PARCELA);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createUpdatedEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        notafiscal.setParceiro(parceiro);
        return notafiscal;
    }

    @BeforeEach
    public void initTest() {
        notafiscal = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotafiscal() throws Exception {
        int databaseSizeBeforeCreate = notafiscalRepository.findAll().size();
        // Create the Notafiscal
        NotafiscalDTO notafiscalDTO = notafiscalMapper.toDto(notafiscal);
        restNotafiscalMockMvc.perform(post("/api/notafiscals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notafiscalDTO)))
            .andExpect(status().isCreated());

        // Validate the Notafiscal in the database
        List<Notafiscal> notafiscalList = notafiscalRepository.findAll();
        assertThat(notafiscalList).hasSize(databaseSizeBeforeCreate + 1);
        Notafiscal testNotafiscal = notafiscalList.get(notafiscalList.size() - 1);
        assertThat(testNotafiscal.getNot_numero()).isEqualTo(DEFAULT_NOT_NUMERO);
        assertThat(testNotafiscal.getNot_descricao()).isEqualTo(DEFAULT_NOT_DESCRICAO);
        assertThat(testNotafiscal.getNot_cnpj()).isEqualTo(DEFAULT_NOT_CNPJ);
        assertThat(testNotafiscal.getNot_empresa()).isEqualTo(DEFAULT_NOT_EMPRESA);
        assertThat(testNotafiscal.getNot_datasaida()).isEqualTo(DEFAULT_NOT_DATASAIDA);
        assertThat(testNotafiscal.getNot_valornota()).isEqualTo(DEFAULT_NOT_VALORNOTA);
        assertThat(testNotafiscal.getNot_dataparcela()).isEqualTo(DEFAULT_NOT_DATAPARCELA);
        assertThat(testNotafiscal.getNot_valorparcela()).isEqualTo(DEFAULT_NOT_VALORPARCELA);
        assertThat(testNotafiscal.getTno_codigo()).isEqualTo(DEFAULT_TNO_CODIGO);
        assertThat(testNotafiscal.getNot_parcela()).isEqualTo(DEFAULT_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void createNotafiscalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notafiscalRepository.findAll().size();

        // Create the Notafiscal with an existing ID
        notafiscal.setId(1L);
        NotafiscalDTO notafiscalDTO = notafiscalMapper.toDto(notafiscal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotafiscalMockMvc.perform(post("/api/notafiscals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notafiscalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notafiscal in the database
        List<Notafiscal> notafiscalList = notafiscalRepository.findAll();
        assertThat(notafiscalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotafiscals() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList
        restNotafiscalMockMvc.perform(get("/api/notafiscals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notafiscal.getId().intValue())))
            .andExpect(jsonPath("$.[*].not_numero").value(hasItem(DEFAULT_NOT_NUMERO)))
            .andExpect(jsonPath("$.[*].not_descricao").value(hasItem(DEFAULT_NOT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].not_cnpj").value(hasItem(DEFAULT_NOT_CNPJ)))
            .andExpect(jsonPath("$.[*].not_empresa").value(hasItem(DEFAULT_NOT_EMPRESA)))
            .andExpect(jsonPath("$.[*].not_datasaida").value(hasItem(sameInstant(DEFAULT_NOT_DATASAIDA))))
            .andExpect(jsonPath("$.[*].not_valornota").value(hasItem(DEFAULT_NOT_VALORNOTA.intValue())))
            .andExpect(jsonPath("$.[*].not_dataparcela").value(hasItem(sameInstant(DEFAULT_NOT_DATAPARCELA))))
            .andExpect(jsonPath("$.[*].not_valorparcela").value(hasItem(DEFAULT_NOT_VALORPARCELA.intValue())))
            .andExpect(jsonPath("$.[*].tno_codigo").value(hasItem(DEFAULT_TNO_CODIGO)))
            .andExpect(jsonPath("$.[*].not_parcela").value(hasItem(DEFAULT_NOT_PARCELA)));
    }
    
    @Test
    @Transactional
    public void getNotafiscal() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get the notafiscal
        restNotafiscalMockMvc.perform(get("/api/notafiscals/{id}", notafiscal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notafiscal.getId().intValue()))
            .andExpect(jsonPath("$.not_numero").value(DEFAULT_NOT_NUMERO))
            .andExpect(jsonPath("$.not_descricao").value(DEFAULT_NOT_DESCRICAO))
            .andExpect(jsonPath("$.not_cnpj").value(DEFAULT_NOT_CNPJ))
            .andExpect(jsonPath("$.not_empresa").value(DEFAULT_NOT_EMPRESA))
            .andExpect(jsonPath("$.not_datasaida").value(sameInstant(DEFAULT_NOT_DATASAIDA)))
            .andExpect(jsonPath("$.not_valornota").value(DEFAULT_NOT_VALORNOTA.intValue()))
            .andExpect(jsonPath("$.not_dataparcela").value(sameInstant(DEFAULT_NOT_DATAPARCELA)))
            .andExpect(jsonPath("$.not_valorparcela").value(DEFAULT_NOT_VALORPARCELA.intValue()))
            .andExpect(jsonPath("$.tno_codigo").value(DEFAULT_TNO_CODIGO))
            .andExpect(jsonPath("$.not_parcela").value(DEFAULT_NOT_PARCELA));
    }


    @Test
    @Transactional
    public void getNotafiscalsByIdFiltering() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        Long id = notafiscal.getId();

        defaultNotafiscalShouldBeFound("id.equals=" + id);
        defaultNotafiscalShouldNotBeFound("id.notEquals=" + id);

        defaultNotafiscalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotafiscalShouldNotBeFound("id.greaterThan=" + id);

        defaultNotafiscalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotafiscalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero equals to DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.equals=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero equals to UPDATED_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.equals=" + UPDATED_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero not equals to DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.notEquals=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero not equals to UPDATED_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.notEquals=" + UPDATED_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero in DEFAULT_NOT_NUMERO or UPDATED_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.in=" + DEFAULT_NOT_NUMERO + "," + UPDATED_NOT_NUMERO);

        // Get all the notafiscalList where not_numero equals to UPDATED_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.in=" + UPDATED_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero is not null
        defaultNotafiscalShouldBeFound("not_numero.specified=true");

        // Get all the notafiscalList where not_numero is null
        defaultNotafiscalShouldNotBeFound("not_numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero is greater than or equal to DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.greaterThanOrEqual=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero is greater than or equal to UPDATED_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.greaterThanOrEqual=" + UPDATED_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero is less than or equal to DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.lessThanOrEqual=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero is less than or equal to SMALLER_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.lessThanOrEqual=" + SMALLER_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero is less than DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.lessThan=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero is less than UPDATED_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.lessThan=" + UPDATED_NOT_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_numeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_numero is greater than DEFAULT_NOT_NUMERO
        defaultNotafiscalShouldNotBeFound("not_numero.greaterThan=" + DEFAULT_NOT_NUMERO);

        // Get all the notafiscalList where not_numero is greater than SMALLER_NOT_NUMERO
        defaultNotafiscalShouldBeFound("not_numero.greaterThan=" + SMALLER_NOT_NUMERO);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao equals to DEFAULT_NOT_DESCRICAO
        defaultNotafiscalShouldBeFound("not_descricao.equals=" + DEFAULT_NOT_DESCRICAO);

        // Get all the notafiscalList where not_descricao equals to UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldNotBeFound("not_descricao.equals=" + UPDATED_NOT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao not equals to DEFAULT_NOT_DESCRICAO
        defaultNotafiscalShouldNotBeFound("not_descricao.notEquals=" + DEFAULT_NOT_DESCRICAO);

        // Get all the notafiscalList where not_descricao not equals to UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldBeFound("not_descricao.notEquals=" + UPDATED_NOT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao in DEFAULT_NOT_DESCRICAO or UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldBeFound("not_descricao.in=" + DEFAULT_NOT_DESCRICAO + "," + UPDATED_NOT_DESCRICAO);

        // Get all the notafiscalList where not_descricao equals to UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldNotBeFound("not_descricao.in=" + UPDATED_NOT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao is not null
        defaultNotafiscalShouldBeFound("not_descricao.specified=true");

        // Get all the notafiscalList where not_descricao is null
        defaultNotafiscalShouldNotBeFound("not_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao contains DEFAULT_NOT_DESCRICAO
        defaultNotafiscalShouldBeFound("not_descricao.contains=" + DEFAULT_NOT_DESCRICAO);

        // Get all the notafiscalList where not_descricao contains UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldNotBeFound("not_descricao.contains=" + UPDATED_NOT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_descricao does not contain DEFAULT_NOT_DESCRICAO
        defaultNotafiscalShouldNotBeFound("not_descricao.doesNotContain=" + DEFAULT_NOT_DESCRICAO);

        // Get all the notafiscalList where not_descricao does not contain UPDATED_NOT_DESCRICAO
        defaultNotafiscalShouldBeFound("not_descricao.doesNotContain=" + UPDATED_NOT_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj equals to DEFAULT_NOT_CNPJ
        defaultNotafiscalShouldBeFound("not_cnpj.equals=" + DEFAULT_NOT_CNPJ);

        // Get all the notafiscalList where not_cnpj equals to UPDATED_NOT_CNPJ
        defaultNotafiscalShouldNotBeFound("not_cnpj.equals=" + UPDATED_NOT_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj not equals to DEFAULT_NOT_CNPJ
        defaultNotafiscalShouldNotBeFound("not_cnpj.notEquals=" + DEFAULT_NOT_CNPJ);

        // Get all the notafiscalList where not_cnpj not equals to UPDATED_NOT_CNPJ
        defaultNotafiscalShouldBeFound("not_cnpj.notEquals=" + UPDATED_NOT_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj in DEFAULT_NOT_CNPJ or UPDATED_NOT_CNPJ
        defaultNotafiscalShouldBeFound("not_cnpj.in=" + DEFAULT_NOT_CNPJ + "," + UPDATED_NOT_CNPJ);

        // Get all the notafiscalList where not_cnpj equals to UPDATED_NOT_CNPJ
        defaultNotafiscalShouldNotBeFound("not_cnpj.in=" + UPDATED_NOT_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj is not null
        defaultNotafiscalShouldBeFound("not_cnpj.specified=true");

        // Get all the notafiscalList where not_cnpj is null
        defaultNotafiscalShouldNotBeFound("not_cnpj.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj contains DEFAULT_NOT_CNPJ
        defaultNotafiscalShouldBeFound("not_cnpj.contains=" + DEFAULT_NOT_CNPJ);

        // Get all the notafiscalList where not_cnpj contains UPDATED_NOT_CNPJ
        defaultNotafiscalShouldNotBeFound("not_cnpj.contains=" + UPDATED_NOT_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_cnpjNotContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_cnpj does not contain DEFAULT_NOT_CNPJ
        defaultNotafiscalShouldNotBeFound("not_cnpj.doesNotContain=" + DEFAULT_NOT_CNPJ);

        // Get all the notafiscalList where not_cnpj does not contain UPDATED_NOT_CNPJ
        defaultNotafiscalShouldBeFound("not_cnpj.doesNotContain=" + UPDATED_NOT_CNPJ);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa equals to DEFAULT_NOT_EMPRESA
        defaultNotafiscalShouldBeFound("not_empresa.equals=" + DEFAULT_NOT_EMPRESA);

        // Get all the notafiscalList where not_empresa equals to UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldNotBeFound("not_empresa.equals=" + UPDATED_NOT_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa not equals to DEFAULT_NOT_EMPRESA
        defaultNotafiscalShouldNotBeFound("not_empresa.notEquals=" + DEFAULT_NOT_EMPRESA);

        // Get all the notafiscalList where not_empresa not equals to UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldBeFound("not_empresa.notEquals=" + UPDATED_NOT_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa in DEFAULT_NOT_EMPRESA or UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldBeFound("not_empresa.in=" + DEFAULT_NOT_EMPRESA + "," + UPDATED_NOT_EMPRESA);

        // Get all the notafiscalList where not_empresa equals to UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldNotBeFound("not_empresa.in=" + UPDATED_NOT_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa is not null
        defaultNotafiscalShouldBeFound("not_empresa.specified=true");

        // Get all the notafiscalList where not_empresa is null
        defaultNotafiscalShouldNotBeFound("not_empresa.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa contains DEFAULT_NOT_EMPRESA
        defaultNotafiscalShouldBeFound("not_empresa.contains=" + DEFAULT_NOT_EMPRESA);

        // Get all the notafiscalList where not_empresa contains UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldNotBeFound("not_empresa.contains=" + UPDATED_NOT_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_empresaNotContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_empresa does not contain DEFAULT_NOT_EMPRESA
        defaultNotafiscalShouldNotBeFound("not_empresa.doesNotContain=" + DEFAULT_NOT_EMPRESA);

        // Get all the notafiscalList where not_empresa does not contain UPDATED_NOT_EMPRESA
        defaultNotafiscalShouldBeFound("not_empresa.doesNotContain=" + UPDATED_NOT_EMPRESA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida equals to DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.equals=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida equals to UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.equals=" + UPDATED_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida not equals to DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.notEquals=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida not equals to UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.notEquals=" + UPDATED_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida in DEFAULT_NOT_DATASAIDA or UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.in=" + DEFAULT_NOT_DATASAIDA + "," + UPDATED_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida equals to UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.in=" + UPDATED_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida is not null
        defaultNotafiscalShouldBeFound("not_datasaida.specified=true");

        // Get all the notafiscalList where not_datasaida is null
        defaultNotafiscalShouldNotBeFound("not_datasaida.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida is greater than or equal to DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.greaterThanOrEqual=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida is greater than or equal to UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.greaterThanOrEqual=" + UPDATED_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida is less than or equal to DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.lessThanOrEqual=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida is less than or equal to SMALLER_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.lessThanOrEqual=" + SMALLER_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida is less than DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.lessThan=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida is less than UPDATED_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.lessThan=" + UPDATED_NOT_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_datasaidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_datasaida is greater than DEFAULT_NOT_DATASAIDA
        defaultNotafiscalShouldNotBeFound("not_datasaida.greaterThan=" + DEFAULT_NOT_DATASAIDA);

        // Get all the notafiscalList where not_datasaida is greater than SMALLER_NOT_DATASAIDA
        defaultNotafiscalShouldBeFound("not_datasaida.greaterThan=" + SMALLER_NOT_DATASAIDA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota equals to DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.equals=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota equals to UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.equals=" + UPDATED_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota not equals to DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.notEquals=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota not equals to UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.notEquals=" + UPDATED_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota in DEFAULT_NOT_VALORNOTA or UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.in=" + DEFAULT_NOT_VALORNOTA + "," + UPDATED_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota equals to UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.in=" + UPDATED_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota is not null
        defaultNotafiscalShouldBeFound("not_valornota.specified=true");

        // Get all the notafiscalList where not_valornota is null
        defaultNotafiscalShouldNotBeFound("not_valornota.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota is greater than or equal to DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.greaterThanOrEqual=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota is greater than or equal to UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.greaterThanOrEqual=" + UPDATED_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota is less than or equal to DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.lessThanOrEqual=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota is less than or equal to SMALLER_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.lessThanOrEqual=" + SMALLER_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota is less than DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.lessThan=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota is less than UPDATED_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.lessThan=" + UPDATED_NOT_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valornotaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valornota is greater than DEFAULT_NOT_VALORNOTA
        defaultNotafiscalShouldNotBeFound("not_valornota.greaterThan=" + DEFAULT_NOT_VALORNOTA);

        // Get all the notafiscalList where not_valornota is greater than SMALLER_NOT_VALORNOTA
        defaultNotafiscalShouldBeFound("not_valornota.greaterThan=" + SMALLER_NOT_VALORNOTA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela equals to DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.equals=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela equals to UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.equals=" + UPDATED_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela not equals to DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.notEquals=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela not equals to UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.notEquals=" + UPDATED_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela in DEFAULT_NOT_DATAPARCELA or UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.in=" + DEFAULT_NOT_DATAPARCELA + "," + UPDATED_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela equals to UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.in=" + UPDATED_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela is not null
        defaultNotafiscalShouldBeFound("not_dataparcela.specified=true");

        // Get all the notafiscalList where not_dataparcela is null
        defaultNotafiscalShouldNotBeFound("not_dataparcela.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela is greater than or equal to DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.greaterThanOrEqual=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela is greater than or equal to UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.greaterThanOrEqual=" + UPDATED_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela is less than or equal to DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.lessThanOrEqual=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela is less than or equal to SMALLER_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.lessThanOrEqual=" + SMALLER_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela is less than DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.lessThan=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela is less than UPDATED_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.lessThan=" + UPDATED_NOT_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_dataparcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_dataparcela is greater than DEFAULT_NOT_DATAPARCELA
        defaultNotafiscalShouldNotBeFound("not_dataparcela.greaterThan=" + DEFAULT_NOT_DATAPARCELA);

        // Get all the notafiscalList where not_dataparcela is greater than SMALLER_NOT_DATAPARCELA
        defaultNotafiscalShouldBeFound("not_dataparcela.greaterThan=" + SMALLER_NOT_DATAPARCELA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela equals to DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.equals=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela equals to UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.equals=" + UPDATED_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela not equals to DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.notEquals=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela not equals to UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.notEquals=" + UPDATED_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela in DEFAULT_NOT_VALORPARCELA or UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.in=" + DEFAULT_NOT_VALORPARCELA + "," + UPDATED_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela equals to UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.in=" + UPDATED_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela is not null
        defaultNotafiscalShouldBeFound("not_valorparcela.specified=true");

        // Get all the notafiscalList where not_valorparcela is null
        defaultNotafiscalShouldNotBeFound("not_valorparcela.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela is greater than or equal to DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.greaterThanOrEqual=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela is greater than or equal to UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.greaterThanOrEqual=" + UPDATED_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela is less than or equal to DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.lessThanOrEqual=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela is less than or equal to SMALLER_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.lessThanOrEqual=" + SMALLER_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela is less than DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.lessThan=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela is less than UPDATED_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.lessThan=" + UPDATED_NOT_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_valorparcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_valorparcela is greater than DEFAULT_NOT_VALORPARCELA
        defaultNotafiscalShouldNotBeFound("not_valorparcela.greaterThan=" + DEFAULT_NOT_VALORPARCELA);

        // Get all the notafiscalList where not_valorparcela is greater than SMALLER_NOT_VALORPARCELA
        defaultNotafiscalShouldBeFound("not_valorparcela.greaterThan=" + SMALLER_NOT_VALORPARCELA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo equals to DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.equals=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo equals to UPDATED_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.equals=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo not equals to DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.notEquals=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo not equals to UPDATED_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.notEquals=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo in DEFAULT_TNO_CODIGO or UPDATED_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.in=" + DEFAULT_TNO_CODIGO + "," + UPDATED_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo equals to UPDATED_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.in=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo is not null
        defaultNotafiscalShouldBeFound("tno_codigo.specified=true");

        // Get all the notafiscalList where tno_codigo is null
        defaultNotafiscalShouldNotBeFound("tno_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo is greater than or equal to DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.greaterThanOrEqual=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo is greater than or equal to UPDATED_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.greaterThanOrEqual=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo is less than or equal to DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.lessThanOrEqual=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo is less than or equal to SMALLER_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.lessThanOrEqual=" + SMALLER_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo is less than DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.lessThan=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo is less than UPDATED_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.lessThan=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByTno_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where tno_codigo is greater than DEFAULT_TNO_CODIGO
        defaultNotafiscalShouldNotBeFound("tno_codigo.greaterThan=" + DEFAULT_TNO_CODIGO);

        // Get all the notafiscalList where tno_codigo is greater than SMALLER_TNO_CODIGO
        defaultNotafiscalShouldBeFound("tno_codigo.greaterThan=" + SMALLER_TNO_CODIGO);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela equals to DEFAULT_NOT_PARCELA
        defaultNotafiscalShouldBeFound("not_parcela.equals=" + DEFAULT_NOT_PARCELA);

        // Get all the notafiscalList where not_parcela equals to UPDATED_NOT_PARCELA
        defaultNotafiscalShouldNotBeFound("not_parcela.equals=" + UPDATED_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela not equals to DEFAULT_NOT_PARCELA
        defaultNotafiscalShouldNotBeFound("not_parcela.notEquals=" + DEFAULT_NOT_PARCELA);

        // Get all the notafiscalList where not_parcela not equals to UPDATED_NOT_PARCELA
        defaultNotafiscalShouldBeFound("not_parcela.notEquals=" + UPDATED_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela in DEFAULT_NOT_PARCELA or UPDATED_NOT_PARCELA
        defaultNotafiscalShouldBeFound("not_parcela.in=" + DEFAULT_NOT_PARCELA + "," + UPDATED_NOT_PARCELA);

        // Get all the notafiscalList where not_parcela equals to UPDATED_NOT_PARCELA
        defaultNotafiscalShouldNotBeFound("not_parcela.in=" + UPDATED_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela is not null
        defaultNotafiscalShouldBeFound("not_parcela.specified=true");

        // Get all the notafiscalList where not_parcela is null
        defaultNotafiscalShouldNotBeFound("not_parcela.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela contains DEFAULT_NOT_PARCELA
        defaultNotafiscalShouldBeFound("not_parcela.contains=" + DEFAULT_NOT_PARCELA);

        // Get all the notafiscalList where not_parcela contains UPDATED_NOT_PARCELA
        defaultNotafiscalShouldNotBeFound("not_parcela.contains=" + UPDATED_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotafiscalsByNot_parcelaNotContainsSomething() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        // Get all the notafiscalList where not_parcela does not contain DEFAULT_NOT_PARCELA
        defaultNotafiscalShouldNotBeFound("not_parcela.doesNotContain=" + DEFAULT_NOT_PARCELA);

        // Get all the notafiscalList where not_parcela does not contain UPDATED_NOT_PARCELA
        defaultNotafiscalShouldBeFound("not_parcela.doesNotContain=" + UPDATED_NOT_PARCELA);
    }


    @Test
    @Transactional
    public void getAllNotafiscalsByParceiroIsEqualToSomething() throws Exception {
        // Get already existing entity
        Parceiro parceiro = notafiscal.getParceiro();
        notafiscalRepository.saveAndFlush(notafiscal);
        Long parceiroId = parceiro.getId();

        // Get all the notafiscalList where parceiro equals to parceiroId
        defaultNotafiscalShouldBeFound("parceiroId.equals=" + parceiroId);

        // Get all the notafiscalList where parceiro equals to parceiroId + 1
        defaultNotafiscalShouldNotBeFound("parceiroId.equals=" + (parceiroId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotafiscalShouldBeFound(String filter) throws Exception {
        restNotafiscalMockMvc.perform(get("/api/notafiscals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notafiscal.getId().intValue())))
            .andExpect(jsonPath("$.[*].not_numero").value(hasItem(DEFAULT_NOT_NUMERO)))
            .andExpect(jsonPath("$.[*].not_descricao").value(hasItem(DEFAULT_NOT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].not_cnpj").value(hasItem(DEFAULT_NOT_CNPJ)))
            .andExpect(jsonPath("$.[*].not_empresa").value(hasItem(DEFAULT_NOT_EMPRESA)))
            .andExpect(jsonPath("$.[*].not_datasaida").value(hasItem(sameInstant(DEFAULT_NOT_DATASAIDA))))
            .andExpect(jsonPath("$.[*].not_valornota").value(hasItem(DEFAULT_NOT_VALORNOTA.intValue())))
            .andExpect(jsonPath("$.[*].not_dataparcela").value(hasItem(sameInstant(DEFAULT_NOT_DATAPARCELA))))
            .andExpect(jsonPath("$.[*].not_valorparcela").value(hasItem(DEFAULT_NOT_VALORPARCELA.intValue())))
            .andExpect(jsonPath("$.[*].tno_codigo").value(hasItem(DEFAULT_TNO_CODIGO)))
            .andExpect(jsonPath("$.[*].not_parcela").value(hasItem(DEFAULT_NOT_PARCELA)));

        // Check, that the count call also returns 1
        restNotafiscalMockMvc.perform(get("/api/notafiscals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotafiscalShouldNotBeFound(String filter) throws Exception {
        restNotafiscalMockMvc.perform(get("/api/notafiscals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotafiscalMockMvc.perform(get("/api/notafiscals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNotafiscal() throws Exception {
        // Get the notafiscal
        restNotafiscalMockMvc.perform(get("/api/notafiscals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotafiscal() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        int databaseSizeBeforeUpdate = notafiscalRepository.findAll().size();

        // Update the notafiscal
        Notafiscal updatedNotafiscal = notafiscalRepository.findById(notafiscal.getId()).get();
        // Disconnect from session so that the updates on updatedNotafiscal are not directly saved in db
        em.detach(updatedNotafiscal);
        updatedNotafiscal
            .not_numero(UPDATED_NOT_NUMERO)
            .not_descricao(UPDATED_NOT_DESCRICAO)
            .not_cnpj(UPDATED_NOT_CNPJ)
            .not_empresa(UPDATED_NOT_EMPRESA)
            .not_datasaida(UPDATED_NOT_DATASAIDA)
            .not_valornota(UPDATED_NOT_VALORNOTA)
            .not_dataparcela(UPDATED_NOT_DATAPARCELA)
            .not_valorparcela(UPDATED_NOT_VALORPARCELA)
            .tno_codigo(UPDATED_TNO_CODIGO)
            .not_parcela(UPDATED_NOT_PARCELA);
        NotafiscalDTO notafiscalDTO = notafiscalMapper.toDto(updatedNotafiscal);

        restNotafiscalMockMvc.perform(put("/api/notafiscals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notafiscalDTO)))
            .andExpect(status().isOk());

        // Validate the Notafiscal in the database
        List<Notafiscal> notafiscalList = notafiscalRepository.findAll();
        assertThat(notafiscalList).hasSize(databaseSizeBeforeUpdate);
        Notafiscal testNotafiscal = notafiscalList.get(notafiscalList.size() - 1);
        assertThat(testNotafiscal.getNot_numero()).isEqualTo(UPDATED_NOT_NUMERO);
        assertThat(testNotafiscal.getNot_descricao()).isEqualTo(UPDATED_NOT_DESCRICAO);
        assertThat(testNotafiscal.getNot_cnpj()).isEqualTo(UPDATED_NOT_CNPJ);
        assertThat(testNotafiscal.getNot_empresa()).isEqualTo(UPDATED_NOT_EMPRESA);
        assertThat(testNotafiscal.getNot_datasaida()).isEqualTo(UPDATED_NOT_DATASAIDA);
        assertThat(testNotafiscal.getNot_valornota()).isEqualTo(UPDATED_NOT_VALORNOTA);
        assertThat(testNotafiscal.getNot_dataparcela()).isEqualTo(UPDATED_NOT_DATAPARCELA);
        assertThat(testNotafiscal.getNot_valorparcela()).isEqualTo(UPDATED_NOT_VALORPARCELA);
        assertThat(testNotafiscal.getTno_codigo()).isEqualTo(UPDATED_TNO_CODIGO);
        assertThat(testNotafiscal.getNot_parcela()).isEqualTo(UPDATED_NOT_PARCELA);
    }

    @Test
    @Transactional
    public void updateNonExistingNotafiscal() throws Exception {
        int databaseSizeBeforeUpdate = notafiscalRepository.findAll().size();

        // Create the Notafiscal
        NotafiscalDTO notafiscalDTO = notafiscalMapper.toDto(notafiscal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotafiscalMockMvc.perform(put("/api/notafiscals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notafiscalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notafiscal in the database
        List<Notafiscal> notafiscalList = notafiscalRepository.findAll();
        assertThat(notafiscalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotafiscal() throws Exception {
        // Initialize the database
        notafiscalRepository.saveAndFlush(notafiscal);

        int databaseSizeBeforeDelete = notafiscalRepository.findAll().size();

        // Delete the notafiscal
        restNotafiscalMockMvc.perform(delete("/api/notafiscals/{id}", notafiscal.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notafiscal> notafiscalList = notafiscalRepository.findAll();
        assertThat(notafiscalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
