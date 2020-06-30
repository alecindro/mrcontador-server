package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.NotaservicoRepository;
import br.com.mrcontador.service.NotaservicoService;
import br.com.mrcontador.service.dto.NotaservicoDTO;
import br.com.mrcontador.service.mapper.NotaservicoMapper;
import br.com.mrcontador.service.dto.NotaservicoCriteria;
import br.com.mrcontador.service.NotaservicoQueryService;

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
 * Integration tests for the {@link NotaservicoResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotaservicoResourceIT {

    private static final Integer DEFAULT_NSE_NUMERO = 1;
    private static final Integer UPDATED_NSE_NUMERO = 2;
    private static final Integer SMALLER_NSE_NUMERO = 1 - 1;

    private static final String DEFAULT_NSE_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_NSE_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_NSE_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_NSE_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_NSE_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_NSE_EMPRESA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_NSE_DATASAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NSE_DATASAIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_NSE_DATASAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_NSE_VALORNOTA = new BigDecimal(1);
    private static final BigDecimal UPDATED_NSE_VALORNOTA = new BigDecimal(2);
    private static final BigDecimal SMALLER_NSE_VALORNOTA = new BigDecimal(1 - 1);

    private static final ZonedDateTime DEFAULT_NSE_DATAPARCELA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NSE_DATAPARCELA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_NSE_DATAPARCELA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_NSE_VALORPARCELA = new BigDecimal(1);
    private static final BigDecimal UPDATED_NSE_VALORPARCELA = new BigDecimal(2);
    private static final BigDecimal SMALLER_NSE_VALORPARCELA = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_TNO_CODIGO = 1;
    private static final Integer UPDATED_TNO_CODIGO = 2;
    private static final Integer SMALLER_TNO_CODIGO = 1 - 1;

    private static final String DEFAULT_NSE_PARCELA = "AAAAAAAAAA";
    private static final String UPDATED_NSE_PARCELA = "BBBBBBBBBB";

    @Autowired
    private NotaservicoRepository notaservicoRepository;

    @Autowired
    private NotaservicoMapper notaservicoMapper;

    @Autowired
    private NotaservicoService notaservicoService;

    @Autowired
    private NotaservicoQueryService notaservicoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotaservicoMockMvc;

    private Notaservico notaservico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notaservico createEntity(EntityManager em) {
        Notaservico notaservico = new Notaservico()
            .nse_numero(DEFAULT_NSE_NUMERO)
            .nse_descricao(DEFAULT_NSE_DESCRICAO)
            .nse_cnpj(DEFAULT_NSE_CNPJ)
            .nse_empresa(DEFAULT_NSE_EMPRESA)
            .nse_datasaida(DEFAULT_NSE_DATASAIDA)
            .nse_valornota(DEFAULT_NSE_VALORNOTA)
            .nse_dataparcela(DEFAULT_NSE_DATAPARCELA)
            .nse_valorparcela(DEFAULT_NSE_VALORPARCELA)
            .tno_codigo(DEFAULT_TNO_CODIGO)
            .nse_parcela(DEFAULT_NSE_PARCELA);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        notaservico.setParceiro(parceiro);
        return notaservico;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notaservico createUpdatedEntity(EntityManager em) {
        Notaservico notaservico = new Notaservico()
            .nse_numero(UPDATED_NSE_NUMERO)
            .nse_descricao(UPDATED_NSE_DESCRICAO)
            .nse_cnpj(UPDATED_NSE_CNPJ)
            .nse_empresa(UPDATED_NSE_EMPRESA)
            .nse_datasaida(UPDATED_NSE_DATASAIDA)
            .nse_valornota(UPDATED_NSE_VALORNOTA)
            .nse_dataparcela(UPDATED_NSE_DATAPARCELA)
            .nse_valorparcela(UPDATED_NSE_VALORPARCELA)
            .tno_codigo(UPDATED_TNO_CODIGO)
            .nse_parcela(UPDATED_NSE_PARCELA);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createUpdatedEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        notaservico.setParceiro(parceiro);
        return notaservico;
    }

    @BeforeEach
    public void initTest() {
        notaservico = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotaservico() throws Exception {
        int databaseSizeBeforeCreate = notaservicoRepository.findAll().size();
        // Create the Notaservico
        NotaservicoDTO notaservicoDTO = notaservicoMapper.toDto(notaservico);
        restNotaservicoMockMvc.perform(post("/api/notaservicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notaservicoDTO)))
            .andExpect(status().isCreated());

        // Validate the Notaservico in the database
        List<Notaservico> notaservicoList = notaservicoRepository.findAll();
        assertThat(notaservicoList).hasSize(databaseSizeBeforeCreate + 1);
        Notaservico testNotaservico = notaservicoList.get(notaservicoList.size() - 1);
        assertThat(testNotaservico.getNse_numero()).isEqualTo(DEFAULT_NSE_NUMERO);
        assertThat(testNotaservico.getNse_descricao()).isEqualTo(DEFAULT_NSE_DESCRICAO);
        assertThat(testNotaservico.getNse_cnpj()).isEqualTo(DEFAULT_NSE_CNPJ);
        assertThat(testNotaservico.getNse_empresa()).isEqualTo(DEFAULT_NSE_EMPRESA);
        assertThat(testNotaservico.getNse_datasaida()).isEqualTo(DEFAULT_NSE_DATASAIDA);
        assertThat(testNotaservico.getNse_valornota()).isEqualTo(DEFAULT_NSE_VALORNOTA);
        assertThat(testNotaservico.getNse_dataparcela()).isEqualTo(DEFAULT_NSE_DATAPARCELA);
        assertThat(testNotaservico.getNse_valorparcela()).isEqualTo(DEFAULT_NSE_VALORPARCELA);
        assertThat(testNotaservico.getTno_codigo()).isEqualTo(DEFAULT_TNO_CODIGO);
        assertThat(testNotaservico.getNse_parcela()).isEqualTo(DEFAULT_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void createNotaservicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notaservicoRepository.findAll().size();

        // Create the Notaservico with an existing ID
        notaservico.setId(1L);
        NotaservicoDTO notaservicoDTO = notaservicoMapper.toDto(notaservico);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaservicoMockMvc.perform(post("/api/notaservicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notaservicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notaservico in the database
        List<Notaservico> notaservicoList = notaservicoRepository.findAll();
        assertThat(notaservicoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotaservicos() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList
        restNotaservicoMockMvc.perform(get("/api/notaservicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaservico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nse_numero").value(hasItem(DEFAULT_NSE_NUMERO)))
            .andExpect(jsonPath("$.[*].nse_descricao").value(hasItem(DEFAULT_NSE_DESCRICAO)))
            .andExpect(jsonPath("$.[*].nse_cnpj").value(hasItem(DEFAULT_NSE_CNPJ)))
            .andExpect(jsonPath("$.[*].nse_empresa").value(hasItem(DEFAULT_NSE_EMPRESA)))
            .andExpect(jsonPath("$.[*].nse_datasaida").value(hasItem(sameInstant(DEFAULT_NSE_DATASAIDA))))
            .andExpect(jsonPath("$.[*].nse_valornota").value(hasItem(DEFAULT_NSE_VALORNOTA.intValue())))
            .andExpect(jsonPath("$.[*].nse_dataparcela").value(hasItem(sameInstant(DEFAULT_NSE_DATAPARCELA))))
            .andExpect(jsonPath("$.[*].nse_valorparcela").value(hasItem(DEFAULT_NSE_VALORPARCELA.intValue())))
            .andExpect(jsonPath("$.[*].tno_codigo").value(hasItem(DEFAULT_TNO_CODIGO)))
            .andExpect(jsonPath("$.[*].nse_parcela").value(hasItem(DEFAULT_NSE_PARCELA)));
    }
    
    @Test
    @Transactional
    public void getNotaservico() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get the notaservico
        restNotaservicoMockMvc.perform(get("/api/notaservicos/{id}", notaservico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notaservico.getId().intValue()))
            .andExpect(jsonPath("$.nse_numero").value(DEFAULT_NSE_NUMERO))
            .andExpect(jsonPath("$.nse_descricao").value(DEFAULT_NSE_DESCRICAO))
            .andExpect(jsonPath("$.nse_cnpj").value(DEFAULT_NSE_CNPJ))
            .andExpect(jsonPath("$.nse_empresa").value(DEFAULT_NSE_EMPRESA))
            .andExpect(jsonPath("$.nse_datasaida").value(sameInstant(DEFAULT_NSE_DATASAIDA)))
            .andExpect(jsonPath("$.nse_valornota").value(DEFAULT_NSE_VALORNOTA.intValue()))
            .andExpect(jsonPath("$.nse_dataparcela").value(sameInstant(DEFAULT_NSE_DATAPARCELA)))
            .andExpect(jsonPath("$.nse_valorparcela").value(DEFAULT_NSE_VALORPARCELA.intValue()))
            .andExpect(jsonPath("$.tno_codigo").value(DEFAULT_TNO_CODIGO))
            .andExpect(jsonPath("$.nse_parcela").value(DEFAULT_NSE_PARCELA));
    }


    @Test
    @Transactional
    public void getNotaservicosByIdFiltering() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        Long id = notaservico.getId();

        defaultNotaservicoShouldBeFound("id.equals=" + id);
        defaultNotaservicoShouldNotBeFound("id.notEquals=" + id);

        defaultNotaservicoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotaservicoShouldNotBeFound("id.greaterThan=" + id);

        defaultNotaservicoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotaservicoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero equals to DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.equals=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero equals to UPDATED_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.equals=" + UPDATED_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero not equals to DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.notEquals=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero not equals to UPDATED_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.notEquals=" + UPDATED_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero in DEFAULT_NSE_NUMERO or UPDATED_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.in=" + DEFAULT_NSE_NUMERO + "," + UPDATED_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero equals to UPDATED_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.in=" + UPDATED_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero is not null
        defaultNotaservicoShouldBeFound("nse_numero.specified=true");

        // Get all the notaservicoList where nse_numero is null
        defaultNotaservicoShouldNotBeFound("nse_numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero is greater than or equal to DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.greaterThanOrEqual=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero is greater than or equal to UPDATED_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.greaterThanOrEqual=" + UPDATED_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero is less than or equal to DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.lessThanOrEqual=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero is less than or equal to SMALLER_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.lessThanOrEqual=" + SMALLER_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero is less than DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.lessThan=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero is less than UPDATED_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.lessThan=" + UPDATED_NSE_NUMERO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_numeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_numero is greater than DEFAULT_NSE_NUMERO
        defaultNotaservicoShouldNotBeFound("nse_numero.greaterThan=" + DEFAULT_NSE_NUMERO);

        // Get all the notaservicoList where nse_numero is greater than SMALLER_NSE_NUMERO
        defaultNotaservicoShouldBeFound("nse_numero.greaterThan=" + SMALLER_NSE_NUMERO);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao equals to DEFAULT_NSE_DESCRICAO
        defaultNotaservicoShouldBeFound("nse_descricao.equals=" + DEFAULT_NSE_DESCRICAO);

        // Get all the notaservicoList where nse_descricao equals to UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldNotBeFound("nse_descricao.equals=" + UPDATED_NSE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao not equals to DEFAULT_NSE_DESCRICAO
        defaultNotaservicoShouldNotBeFound("nse_descricao.notEquals=" + DEFAULT_NSE_DESCRICAO);

        // Get all the notaservicoList where nse_descricao not equals to UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldBeFound("nse_descricao.notEquals=" + UPDATED_NSE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao in DEFAULT_NSE_DESCRICAO or UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldBeFound("nse_descricao.in=" + DEFAULT_NSE_DESCRICAO + "," + UPDATED_NSE_DESCRICAO);

        // Get all the notaservicoList where nse_descricao equals to UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldNotBeFound("nse_descricao.in=" + UPDATED_NSE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao is not null
        defaultNotaservicoShouldBeFound("nse_descricao.specified=true");

        // Get all the notaservicoList where nse_descricao is null
        defaultNotaservicoShouldNotBeFound("nse_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao contains DEFAULT_NSE_DESCRICAO
        defaultNotaservicoShouldBeFound("nse_descricao.contains=" + DEFAULT_NSE_DESCRICAO);

        // Get all the notaservicoList where nse_descricao contains UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldNotBeFound("nse_descricao.contains=" + UPDATED_NSE_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_descricao does not contain DEFAULT_NSE_DESCRICAO
        defaultNotaservicoShouldNotBeFound("nse_descricao.doesNotContain=" + DEFAULT_NSE_DESCRICAO);

        // Get all the notaservicoList where nse_descricao does not contain UPDATED_NSE_DESCRICAO
        defaultNotaservicoShouldBeFound("nse_descricao.doesNotContain=" + UPDATED_NSE_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj equals to DEFAULT_NSE_CNPJ
        defaultNotaservicoShouldBeFound("nse_cnpj.equals=" + DEFAULT_NSE_CNPJ);

        // Get all the notaservicoList where nse_cnpj equals to UPDATED_NSE_CNPJ
        defaultNotaservicoShouldNotBeFound("nse_cnpj.equals=" + UPDATED_NSE_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj not equals to DEFAULT_NSE_CNPJ
        defaultNotaservicoShouldNotBeFound("nse_cnpj.notEquals=" + DEFAULT_NSE_CNPJ);

        // Get all the notaservicoList where nse_cnpj not equals to UPDATED_NSE_CNPJ
        defaultNotaservicoShouldBeFound("nse_cnpj.notEquals=" + UPDATED_NSE_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj in DEFAULT_NSE_CNPJ or UPDATED_NSE_CNPJ
        defaultNotaservicoShouldBeFound("nse_cnpj.in=" + DEFAULT_NSE_CNPJ + "," + UPDATED_NSE_CNPJ);

        // Get all the notaservicoList where nse_cnpj equals to UPDATED_NSE_CNPJ
        defaultNotaservicoShouldNotBeFound("nse_cnpj.in=" + UPDATED_NSE_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj is not null
        defaultNotaservicoShouldBeFound("nse_cnpj.specified=true");

        // Get all the notaservicoList where nse_cnpj is null
        defaultNotaservicoShouldNotBeFound("nse_cnpj.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj contains DEFAULT_NSE_CNPJ
        defaultNotaservicoShouldBeFound("nse_cnpj.contains=" + DEFAULT_NSE_CNPJ);

        // Get all the notaservicoList where nse_cnpj contains UPDATED_NSE_CNPJ
        defaultNotaservicoShouldNotBeFound("nse_cnpj.contains=" + UPDATED_NSE_CNPJ);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_cnpjNotContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_cnpj does not contain DEFAULT_NSE_CNPJ
        defaultNotaservicoShouldNotBeFound("nse_cnpj.doesNotContain=" + DEFAULT_NSE_CNPJ);

        // Get all the notaservicoList where nse_cnpj does not contain UPDATED_NSE_CNPJ
        defaultNotaservicoShouldBeFound("nse_cnpj.doesNotContain=" + UPDATED_NSE_CNPJ);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa equals to DEFAULT_NSE_EMPRESA
        defaultNotaservicoShouldBeFound("nse_empresa.equals=" + DEFAULT_NSE_EMPRESA);

        // Get all the notaservicoList where nse_empresa equals to UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldNotBeFound("nse_empresa.equals=" + UPDATED_NSE_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa not equals to DEFAULT_NSE_EMPRESA
        defaultNotaservicoShouldNotBeFound("nse_empresa.notEquals=" + DEFAULT_NSE_EMPRESA);

        // Get all the notaservicoList where nse_empresa not equals to UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldBeFound("nse_empresa.notEquals=" + UPDATED_NSE_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa in DEFAULT_NSE_EMPRESA or UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldBeFound("nse_empresa.in=" + DEFAULT_NSE_EMPRESA + "," + UPDATED_NSE_EMPRESA);

        // Get all the notaservicoList where nse_empresa equals to UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldNotBeFound("nse_empresa.in=" + UPDATED_NSE_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa is not null
        defaultNotaservicoShouldBeFound("nse_empresa.specified=true");

        // Get all the notaservicoList where nse_empresa is null
        defaultNotaservicoShouldNotBeFound("nse_empresa.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa contains DEFAULT_NSE_EMPRESA
        defaultNotaservicoShouldBeFound("nse_empresa.contains=" + DEFAULT_NSE_EMPRESA);

        // Get all the notaservicoList where nse_empresa contains UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldNotBeFound("nse_empresa.contains=" + UPDATED_NSE_EMPRESA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_empresaNotContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_empresa does not contain DEFAULT_NSE_EMPRESA
        defaultNotaservicoShouldNotBeFound("nse_empresa.doesNotContain=" + DEFAULT_NSE_EMPRESA);

        // Get all the notaservicoList where nse_empresa does not contain UPDATED_NSE_EMPRESA
        defaultNotaservicoShouldBeFound("nse_empresa.doesNotContain=" + UPDATED_NSE_EMPRESA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida equals to DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.equals=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida equals to UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.equals=" + UPDATED_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida not equals to DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.notEquals=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida not equals to UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.notEquals=" + UPDATED_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida in DEFAULT_NSE_DATASAIDA or UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.in=" + DEFAULT_NSE_DATASAIDA + "," + UPDATED_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida equals to UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.in=" + UPDATED_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida is not null
        defaultNotaservicoShouldBeFound("nse_datasaida.specified=true");

        // Get all the notaservicoList where nse_datasaida is null
        defaultNotaservicoShouldNotBeFound("nse_datasaida.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida is greater than or equal to DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.greaterThanOrEqual=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida is greater than or equal to UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.greaterThanOrEqual=" + UPDATED_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida is less than or equal to DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.lessThanOrEqual=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida is less than or equal to SMALLER_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.lessThanOrEqual=" + SMALLER_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida is less than DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.lessThan=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida is less than UPDATED_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.lessThan=" + UPDATED_NSE_DATASAIDA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_datasaidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_datasaida is greater than DEFAULT_NSE_DATASAIDA
        defaultNotaservicoShouldNotBeFound("nse_datasaida.greaterThan=" + DEFAULT_NSE_DATASAIDA);

        // Get all the notaservicoList where nse_datasaida is greater than SMALLER_NSE_DATASAIDA
        defaultNotaservicoShouldBeFound("nse_datasaida.greaterThan=" + SMALLER_NSE_DATASAIDA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota equals to DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.equals=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota equals to UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.equals=" + UPDATED_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota not equals to DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.notEquals=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota not equals to UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.notEquals=" + UPDATED_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota in DEFAULT_NSE_VALORNOTA or UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.in=" + DEFAULT_NSE_VALORNOTA + "," + UPDATED_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota equals to UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.in=" + UPDATED_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota is not null
        defaultNotaservicoShouldBeFound("nse_valornota.specified=true");

        // Get all the notaservicoList where nse_valornota is null
        defaultNotaservicoShouldNotBeFound("nse_valornota.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota is greater than or equal to DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.greaterThanOrEqual=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota is greater than or equal to UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.greaterThanOrEqual=" + UPDATED_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota is less than or equal to DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.lessThanOrEqual=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota is less than or equal to SMALLER_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.lessThanOrEqual=" + SMALLER_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota is less than DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.lessThan=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota is less than UPDATED_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.lessThan=" + UPDATED_NSE_VALORNOTA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valornotaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valornota is greater than DEFAULT_NSE_VALORNOTA
        defaultNotaservicoShouldNotBeFound("nse_valornota.greaterThan=" + DEFAULT_NSE_VALORNOTA);

        // Get all the notaservicoList where nse_valornota is greater than SMALLER_NSE_VALORNOTA
        defaultNotaservicoShouldBeFound("nse_valornota.greaterThan=" + SMALLER_NSE_VALORNOTA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela equals to DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.equals=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela equals to UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.equals=" + UPDATED_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela not equals to DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.notEquals=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela not equals to UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.notEquals=" + UPDATED_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela in DEFAULT_NSE_DATAPARCELA or UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.in=" + DEFAULT_NSE_DATAPARCELA + "," + UPDATED_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela equals to UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.in=" + UPDATED_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela is not null
        defaultNotaservicoShouldBeFound("nse_dataparcela.specified=true");

        // Get all the notaservicoList where nse_dataparcela is null
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela is greater than or equal to DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.greaterThanOrEqual=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela is greater than or equal to UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.greaterThanOrEqual=" + UPDATED_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela is less than or equal to DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.lessThanOrEqual=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela is less than or equal to SMALLER_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.lessThanOrEqual=" + SMALLER_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela is less than DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.lessThan=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela is less than UPDATED_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.lessThan=" + UPDATED_NSE_DATAPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_dataparcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_dataparcela is greater than DEFAULT_NSE_DATAPARCELA
        defaultNotaservicoShouldNotBeFound("nse_dataparcela.greaterThan=" + DEFAULT_NSE_DATAPARCELA);

        // Get all the notaservicoList where nse_dataparcela is greater than SMALLER_NSE_DATAPARCELA
        defaultNotaservicoShouldBeFound("nse_dataparcela.greaterThan=" + SMALLER_NSE_DATAPARCELA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela equals to DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.equals=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela equals to UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.equals=" + UPDATED_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela not equals to DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.notEquals=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela not equals to UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.notEquals=" + UPDATED_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela in DEFAULT_NSE_VALORPARCELA or UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.in=" + DEFAULT_NSE_VALORPARCELA + "," + UPDATED_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela equals to UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.in=" + UPDATED_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela is not null
        defaultNotaservicoShouldBeFound("nse_valorparcela.specified=true");

        // Get all the notaservicoList where nse_valorparcela is null
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela is greater than or equal to DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.greaterThanOrEqual=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela is greater than or equal to UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.greaterThanOrEqual=" + UPDATED_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela is less than or equal to DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.lessThanOrEqual=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela is less than or equal to SMALLER_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.lessThanOrEqual=" + SMALLER_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela is less than DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.lessThan=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela is less than UPDATED_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.lessThan=" + UPDATED_NSE_VALORPARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_valorparcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_valorparcela is greater than DEFAULT_NSE_VALORPARCELA
        defaultNotaservicoShouldNotBeFound("nse_valorparcela.greaterThan=" + DEFAULT_NSE_VALORPARCELA);

        // Get all the notaservicoList where nse_valorparcela is greater than SMALLER_NSE_VALORPARCELA
        defaultNotaservicoShouldBeFound("nse_valorparcela.greaterThan=" + SMALLER_NSE_VALORPARCELA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo equals to DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.equals=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo equals to UPDATED_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.equals=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo not equals to DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.notEquals=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo not equals to UPDATED_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.notEquals=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo in DEFAULT_TNO_CODIGO or UPDATED_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.in=" + DEFAULT_TNO_CODIGO + "," + UPDATED_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo equals to UPDATED_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.in=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo is not null
        defaultNotaservicoShouldBeFound("tno_codigo.specified=true");

        // Get all the notaservicoList where tno_codigo is null
        defaultNotaservicoShouldNotBeFound("tno_codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo is greater than or equal to DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.greaterThanOrEqual=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo is greater than or equal to UPDATED_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.greaterThanOrEqual=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo is less than or equal to DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.lessThanOrEqual=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo is less than or equal to SMALLER_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.lessThanOrEqual=" + SMALLER_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo is less than DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.lessThan=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo is less than UPDATED_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.lessThan=" + UPDATED_TNO_CODIGO);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByTno_codigoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where tno_codigo is greater than DEFAULT_TNO_CODIGO
        defaultNotaservicoShouldNotBeFound("tno_codigo.greaterThan=" + DEFAULT_TNO_CODIGO);

        // Get all the notaservicoList where tno_codigo is greater than SMALLER_TNO_CODIGO
        defaultNotaservicoShouldBeFound("tno_codigo.greaterThan=" + SMALLER_TNO_CODIGO);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela equals to DEFAULT_NSE_PARCELA
        defaultNotaservicoShouldBeFound("nse_parcela.equals=" + DEFAULT_NSE_PARCELA);

        // Get all the notaservicoList where nse_parcela equals to UPDATED_NSE_PARCELA
        defaultNotaservicoShouldNotBeFound("nse_parcela.equals=" + UPDATED_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela not equals to DEFAULT_NSE_PARCELA
        defaultNotaservicoShouldNotBeFound("nse_parcela.notEquals=" + DEFAULT_NSE_PARCELA);

        // Get all the notaservicoList where nse_parcela not equals to UPDATED_NSE_PARCELA
        defaultNotaservicoShouldBeFound("nse_parcela.notEquals=" + UPDATED_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaIsInShouldWork() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela in DEFAULT_NSE_PARCELA or UPDATED_NSE_PARCELA
        defaultNotaservicoShouldBeFound("nse_parcela.in=" + DEFAULT_NSE_PARCELA + "," + UPDATED_NSE_PARCELA);

        // Get all the notaservicoList where nse_parcela equals to UPDATED_NSE_PARCELA
        defaultNotaservicoShouldNotBeFound("nse_parcela.in=" + UPDATED_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela is not null
        defaultNotaservicoShouldBeFound("nse_parcela.specified=true");

        // Get all the notaservicoList where nse_parcela is null
        defaultNotaservicoShouldNotBeFound("nse_parcela.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela contains DEFAULT_NSE_PARCELA
        defaultNotaservicoShouldBeFound("nse_parcela.contains=" + DEFAULT_NSE_PARCELA);

        // Get all the notaservicoList where nse_parcela contains UPDATED_NSE_PARCELA
        defaultNotaservicoShouldNotBeFound("nse_parcela.contains=" + UPDATED_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void getAllNotaservicosByNse_parcelaNotContainsSomething() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        // Get all the notaservicoList where nse_parcela does not contain DEFAULT_NSE_PARCELA
        defaultNotaservicoShouldNotBeFound("nse_parcela.doesNotContain=" + DEFAULT_NSE_PARCELA);

        // Get all the notaservicoList where nse_parcela does not contain UPDATED_NSE_PARCELA
        defaultNotaservicoShouldBeFound("nse_parcela.doesNotContain=" + UPDATED_NSE_PARCELA);
    }


    @Test
    @Transactional
    public void getAllNotaservicosByParceiroIsEqualToSomething() throws Exception {
        // Get already existing entity
        Parceiro parceiro = notaservico.getParceiro();
        notaservicoRepository.saveAndFlush(notaservico);
        Long parceiroId = parceiro.getId();

        // Get all the notaservicoList where parceiro equals to parceiroId
        defaultNotaservicoShouldBeFound("parceiroId.equals=" + parceiroId);

        // Get all the notaservicoList where parceiro equals to parceiroId + 1
        defaultNotaservicoShouldNotBeFound("parceiroId.equals=" + (parceiroId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotaservicoShouldBeFound(String filter) throws Exception {
        restNotaservicoMockMvc.perform(get("/api/notaservicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notaservico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nse_numero").value(hasItem(DEFAULT_NSE_NUMERO)))
            .andExpect(jsonPath("$.[*].nse_descricao").value(hasItem(DEFAULT_NSE_DESCRICAO)))
            .andExpect(jsonPath("$.[*].nse_cnpj").value(hasItem(DEFAULT_NSE_CNPJ)))
            .andExpect(jsonPath("$.[*].nse_empresa").value(hasItem(DEFAULT_NSE_EMPRESA)))
            .andExpect(jsonPath("$.[*].nse_datasaida").value(hasItem(sameInstant(DEFAULT_NSE_DATASAIDA))))
            .andExpect(jsonPath("$.[*].nse_valornota").value(hasItem(DEFAULT_NSE_VALORNOTA.intValue())))
            .andExpect(jsonPath("$.[*].nse_dataparcela").value(hasItem(sameInstant(DEFAULT_NSE_DATAPARCELA))))
            .andExpect(jsonPath("$.[*].nse_valorparcela").value(hasItem(DEFAULT_NSE_VALORPARCELA.intValue())))
            .andExpect(jsonPath("$.[*].tno_codigo").value(hasItem(DEFAULT_TNO_CODIGO)))
            .andExpect(jsonPath("$.[*].nse_parcela").value(hasItem(DEFAULT_NSE_PARCELA)));

        // Check, that the count call also returns 1
        restNotaservicoMockMvc.perform(get("/api/notaservicos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotaservicoShouldNotBeFound(String filter) throws Exception {
        restNotaservicoMockMvc.perform(get("/api/notaservicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotaservicoMockMvc.perform(get("/api/notaservicos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNotaservico() throws Exception {
        // Get the notaservico
        restNotaservicoMockMvc.perform(get("/api/notaservicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotaservico() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        int databaseSizeBeforeUpdate = notaservicoRepository.findAll().size();

        // Update the notaservico
        Notaservico updatedNotaservico = notaservicoRepository.findById(notaservico.getId()).get();
        // Disconnect from session so that the updates on updatedNotaservico are not directly saved in db
        em.detach(updatedNotaservico);
        updatedNotaservico
            .nse_numero(UPDATED_NSE_NUMERO)
            .nse_descricao(UPDATED_NSE_DESCRICAO)
            .nse_cnpj(UPDATED_NSE_CNPJ)
            .nse_empresa(UPDATED_NSE_EMPRESA)
            .nse_datasaida(UPDATED_NSE_DATASAIDA)
            .nse_valornota(UPDATED_NSE_VALORNOTA)
            .nse_dataparcela(UPDATED_NSE_DATAPARCELA)
            .nse_valorparcela(UPDATED_NSE_VALORPARCELA)
            .tno_codigo(UPDATED_TNO_CODIGO)
            .nse_parcela(UPDATED_NSE_PARCELA);
        NotaservicoDTO notaservicoDTO = notaservicoMapper.toDto(updatedNotaservico);

        restNotaservicoMockMvc.perform(put("/api/notaservicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notaservicoDTO)))
            .andExpect(status().isOk());

        // Validate the Notaservico in the database
        List<Notaservico> notaservicoList = notaservicoRepository.findAll();
        assertThat(notaservicoList).hasSize(databaseSizeBeforeUpdate);
        Notaservico testNotaservico = notaservicoList.get(notaservicoList.size() - 1);
        assertThat(testNotaservico.getNse_numero()).isEqualTo(UPDATED_NSE_NUMERO);
        assertThat(testNotaservico.getNse_descricao()).isEqualTo(UPDATED_NSE_DESCRICAO);
        assertThat(testNotaservico.getNse_cnpj()).isEqualTo(UPDATED_NSE_CNPJ);
        assertThat(testNotaservico.getNse_empresa()).isEqualTo(UPDATED_NSE_EMPRESA);
        assertThat(testNotaservico.getNse_datasaida()).isEqualTo(UPDATED_NSE_DATASAIDA);
        assertThat(testNotaservico.getNse_valornota()).isEqualTo(UPDATED_NSE_VALORNOTA);
        assertThat(testNotaservico.getNse_dataparcela()).isEqualTo(UPDATED_NSE_DATAPARCELA);
        assertThat(testNotaservico.getNse_valorparcela()).isEqualTo(UPDATED_NSE_VALORPARCELA);
        assertThat(testNotaservico.getTno_codigo()).isEqualTo(UPDATED_TNO_CODIGO);
        assertThat(testNotaservico.getNse_parcela()).isEqualTo(UPDATED_NSE_PARCELA);
    }

    @Test
    @Transactional
    public void updateNonExistingNotaservico() throws Exception {
        int databaseSizeBeforeUpdate = notaservicoRepository.findAll().size();

        // Create the Notaservico
        NotaservicoDTO notaservicoDTO = notaservicoMapper.toDto(notaservico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaservicoMockMvc.perform(put("/api/notaservicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notaservicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notaservico in the database
        List<Notaservico> notaservicoList = notaservicoRepository.findAll();
        assertThat(notaservicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotaservico() throws Exception {
        // Initialize the database
        notaservicoRepository.saveAndFlush(notaservico);

        int databaseSizeBeforeDelete = notaservicoRepository.findAll().size();

        // Delete the notaservico
        restNotaservicoMockMvc.perform(delete("/api/notaservicos/{id}", notaservico.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notaservico> notaservicoList = notaservicoRepository.findAll();
        assertThat(notaservicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
