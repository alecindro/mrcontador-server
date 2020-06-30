package br.com.mrcontador.web.rest;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.ExtratoDTO;
import br.com.mrcontador.service.mapper.ExtratoMapper;
import br.com.mrcontador.service.dto.ExtratoCriteria;
import br.com.mrcontador.service.ExtratoQueryService;

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
 * Integration tests for the {@link ExtratoResource} REST controller.
 */
@SpringBootTest(classes = MrcontadorServerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExtratoResourceIT {

    private static final ZonedDateTime DEFAULT_EXT_DATALANCAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXT_DATALANCAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXT_DATALANCAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_EXT_HISTORICO = "AAAAAAAAAA";
    private static final String UPDATED_EXT_HISTORICO = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_NUMERODOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_EXT_NUMERODOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_NUMEROCONTROLE = "AAAAAAAAAA";
    private static final String UPDATED_EXT_NUMEROCONTROLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXT_DEBITO = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXT_DEBITO = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXT_DEBITO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_EXT_CREDITO = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXT_CREDITO = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXT_CREDITO = new BigDecimal(1 - 1);

    private static final String DEFAULT_EXT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_EXT_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ExtratoRepository extratoRepository;

    @Autowired
    private ExtratoMapper extratoMapper;

    @Autowired
    private ExtratoService extratoService;

    @Autowired
    private ExtratoQueryService extratoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtratoMockMvc;

    private Extrato extrato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extrato createEntity(EntityManager em) {
        Extrato extrato = new Extrato()
            .ext_datalancamento(DEFAULT_EXT_DATALANCAMENTO)
            .ext_historico(DEFAULT_EXT_HISTORICO)
            .ext_numerodocumento(DEFAULT_EXT_NUMERODOCUMENTO)
            .ext_numerocontrole(DEFAULT_EXT_NUMEROCONTROLE)
            .ext_debito(DEFAULT_EXT_DEBITO)
            .ext_credito(DEFAULT_EXT_CREDITO)
            .ext_descricao(DEFAULT_EXT_DESCRICAO);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        extrato.setParceiro(parceiro);
        // Add required entity
        Agenciabancaria agenciabancaria;
        if (TestUtil.findAll(em, Agenciabancaria.class).isEmpty()) {
            agenciabancaria = AgenciabancariaResourceIT.createEntity(em);
            em.persist(agenciabancaria);
            em.flush();
        } else {
            agenciabancaria = TestUtil.findAll(em, Agenciabancaria.class).get(0);
        }
        extrato.setAgenciabancaria(agenciabancaria);
        return extrato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extrato createUpdatedEntity(EntityManager em) {
        Extrato extrato = new Extrato()
            .ext_datalancamento(UPDATED_EXT_DATALANCAMENTO)
            .ext_historico(UPDATED_EXT_HISTORICO)
            .ext_numerodocumento(UPDATED_EXT_NUMERODOCUMENTO)
            .ext_numerocontrole(UPDATED_EXT_NUMEROCONTROLE)
            .ext_debito(UPDATED_EXT_DEBITO)
            .ext_credito(UPDATED_EXT_CREDITO)
            .ext_descricao(UPDATED_EXT_DESCRICAO);
        // Add required entity
        Parceiro parceiro;
        if (TestUtil.findAll(em, Parceiro.class).isEmpty()) {
            parceiro = ParceiroResourceIT.createUpdatedEntity(em);
            em.persist(parceiro);
            em.flush();
        } else {
            parceiro = TestUtil.findAll(em, Parceiro.class).get(0);
        }
        extrato.setParceiro(parceiro);
        // Add required entity
        Agenciabancaria agenciabancaria;
        if (TestUtil.findAll(em, Agenciabancaria.class).isEmpty()) {
            agenciabancaria = AgenciabancariaResourceIT.createUpdatedEntity(em);
            em.persist(agenciabancaria);
            em.flush();
        } else {
            agenciabancaria = TestUtil.findAll(em, Agenciabancaria.class).get(0);
        }
        extrato.setAgenciabancaria(agenciabancaria);
        return extrato;
    }

    @BeforeEach
    public void initTest() {
        extrato = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtrato() throws Exception {
        int databaseSizeBeforeCreate = extratoRepository.findAll().size();
        // Create the Extrato
        ExtratoDTO extratoDTO = extratoMapper.toDto(extrato);
        restExtratoMockMvc.perform(post("/api/extratoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extratoDTO)))
            .andExpect(status().isCreated());

        // Validate the Extrato in the database
        List<Extrato> extratoList = extratoRepository.findAll();
        assertThat(extratoList).hasSize(databaseSizeBeforeCreate + 1);
        Extrato testExtrato = extratoList.get(extratoList.size() - 1);
        assertThat(testExtrato.getExt_datalancamento()).isEqualTo(DEFAULT_EXT_DATALANCAMENTO);
        assertThat(testExtrato.getExt_historico()).isEqualTo(DEFAULT_EXT_HISTORICO);
        assertThat(testExtrato.getExt_numerodocumento()).isEqualTo(DEFAULT_EXT_NUMERODOCUMENTO);
        assertThat(testExtrato.getExt_numerocontrole()).isEqualTo(DEFAULT_EXT_NUMEROCONTROLE);
        assertThat(testExtrato.getExt_debito()).isEqualTo(DEFAULT_EXT_DEBITO);
        assertThat(testExtrato.getExt_credito()).isEqualTo(DEFAULT_EXT_CREDITO);
        assertThat(testExtrato.getExt_descricao()).isEqualTo(DEFAULT_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createExtratoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extratoRepository.findAll().size();

        // Create the Extrato with an existing ID
        extrato.setId(1L);
        ExtratoDTO extratoDTO = extratoMapper.toDto(extrato);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtratoMockMvc.perform(post("/api/extratoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extratoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Extrato in the database
        List<Extrato> extratoList = extratoRepository.findAll();
        assertThat(extratoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExtratoes() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList
        restExtratoMockMvc.perform(get("/api/extratoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].ext_datalancamento").value(hasItem(sameInstant(DEFAULT_EXT_DATALANCAMENTO))))
            .andExpect(jsonPath("$.[*].ext_historico").value(hasItem(DEFAULT_EXT_HISTORICO)))
            .andExpect(jsonPath("$.[*].ext_numerodocumento").value(hasItem(DEFAULT_EXT_NUMERODOCUMENTO)))
            .andExpect(jsonPath("$.[*].ext_numerocontrole").value(hasItem(DEFAULT_EXT_NUMEROCONTROLE)))
            .andExpect(jsonPath("$.[*].ext_debito").value(hasItem(DEFAULT_EXT_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].ext_credito").value(hasItem(DEFAULT_EXT_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].ext_descricao").value(hasItem(DEFAULT_EXT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getExtrato() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get the extrato
        restExtratoMockMvc.perform(get("/api/extratoes/{id}", extrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extrato.getId().intValue()))
            .andExpect(jsonPath("$.ext_datalancamento").value(sameInstant(DEFAULT_EXT_DATALANCAMENTO)))
            .andExpect(jsonPath("$.ext_historico").value(DEFAULT_EXT_HISTORICO))
            .andExpect(jsonPath("$.ext_numerodocumento").value(DEFAULT_EXT_NUMERODOCUMENTO))
            .andExpect(jsonPath("$.ext_numerocontrole").value(DEFAULT_EXT_NUMEROCONTROLE))
            .andExpect(jsonPath("$.ext_debito").value(DEFAULT_EXT_DEBITO.intValue()))
            .andExpect(jsonPath("$.ext_credito").value(DEFAULT_EXT_CREDITO.intValue()))
            .andExpect(jsonPath("$.ext_descricao").value(DEFAULT_EXT_DESCRICAO));
    }


    @Test
    @Transactional
    public void getExtratoesByIdFiltering() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        Long id = extrato.getId();

        defaultExtratoShouldBeFound("id.equals=" + id);
        defaultExtratoShouldNotBeFound("id.notEquals=" + id);

        defaultExtratoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExtratoShouldNotBeFound("id.greaterThan=" + id);

        defaultExtratoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExtratoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento equals to DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.equals=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento equals to UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.equals=" + UPDATED_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento not equals to DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.notEquals=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento not equals to UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.notEquals=" + UPDATED_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento in DEFAULT_EXT_DATALANCAMENTO or UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.in=" + DEFAULT_EXT_DATALANCAMENTO + "," + UPDATED_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento equals to UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.in=" + UPDATED_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento is not null
        defaultExtratoShouldBeFound("ext_datalancamento.specified=true");

        // Get all the extratoList where ext_datalancamento is null
        defaultExtratoShouldNotBeFound("ext_datalancamento.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento is greater than or equal to DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.greaterThanOrEqual=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento is greater than or equal to UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.greaterThanOrEqual=" + UPDATED_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento is less than or equal to DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.lessThanOrEqual=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento is less than or equal to SMALLER_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.lessThanOrEqual=" + SMALLER_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento is less than DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.lessThan=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento is less than UPDATED_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.lessThan=" + UPDATED_EXT_DATALANCAMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_datalancamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_datalancamento is greater than DEFAULT_EXT_DATALANCAMENTO
        defaultExtratoShouldNotBeFound("ext_datalancamento.greaterThan=" + DEFAULT_EXT_DATALANCAMENTO);

        // Get all the extratoList where ext_datalancamento is greater than SMALLER_EXT_DATALANCAMENTO
        defaultExtratoShouldBeFound("ext_datalancamento.greaterThan=" + SMALLER_EXT_DATALANCAMENTO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_historicoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico equals to DEFAULT_EXT_HISTORICO
        defaultExtratoShouldBeFound("ext_historico.equals=" + DEFAULT_EXT_HISTORICO);

        // Get all the extratoList where ext_historico equals to UPDATED_EXT_HISTORICO
        defaultExtratoShouldNotBeFound("ext_historico.equals=" + UPDATED_EXT_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_historicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico not equals to DEFAULT_EXT_HISTORICO
        defaultExtratoShouldNotBeFound("ext_historico.notEquals=" + DEFAULT_EXT_HISTORICO);

        // Get all the extratoList where ext_historico not equals to UPDATED_EXT_HISTORICO
        defaultExtratoShouldBeFound("ext_historico.notEquals=" + UPDATED_EXT_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_historicoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico in DEFAULT_EXT_HISTORICO or UPDATED_EXT_HISTORICO
        defaultExtratoShouldBeFound("ext_historico.in=" + DEFAULT_EXT_HISTORICO + "," + UPDATED_EXT_HISTORICO);

        // Get all the extratoList where ext_historico equals to UPDATED_EXT_HISTORICO
        defaultExtratoShouldNotBeFound("ext_historico.in=" + UPDATED_EXT_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_historicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico is not null
        defaultExtratoShouldBeFound("ext_historico.specified=true");

        // Get all the extratoList where ext_historico is null
        defaultExtratoShouldNotBeFound("ext_historico.specified=false");
    }
                @Test
    @Transactional
    public void getAllExtratoesByExt_historicoContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico contains DEFAULT_EXT_HISTORICO
        defaultExtratoShouldBeFound("ext_historico.contains=" + DEFAULT_EXT_HISTORICO);

        // Get all the extratoList where ext_historico contains UPDATED_EXT_HISTORICO
        defaultExtratoShouldNotBeFound("ext_historico.contains=" + UPDATED_EXT_HISTORICO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_historicoNotContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_historico does not contain DEFAULT_EXT_HISTORICO
        defaultExtratoShouldNotBeFound("ext_historico.doesNotContain=" + DEFAULT_EXT_HISTORICO);

        // Get all the extratoList where ext_historico does not contain UPDATED_EXT_HISTORICO
        defaultExtratoShouldBeFound("ext_historico.doesNotContain=" + UPDATED_EXT_HISTORICO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento equals to DEFAULT_EXT_NUMERODOCUMENTO
        defaultExtratoShouldBeFound("ext_numerodocumento.equals=" + DEFAULT_EXT_NUMERODOCUMENTO);

        // Get all the extratoList where ext_numerodocumento equals to UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldNotBeFound("ext_numerodocumento.equals=" + UPDATED_EXT_NUMERODOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento not equals to DEFAULT_EXT_NUMERODOCUMENTO
        defaultExtratoShouldNotBeFound("ext_numerodocumento.notEquals=" + DEFAULT_EXT_NUMERODOCUMENTO);

        // Get all the extratoList where ext_numerodocumento not equals to UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldBeFound("ext_numerodocumento.notEquals=" + UPDATED_EXT_NUMERODOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento in DEFAULT_EXT_NUMERODOCUMENTO or UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldBeFound("ext_numerodocumento.in=" + DEFAULT_EXT_NUMERODOCUMENTO + "," + UPDATED_EXT_NUMERODOCUMENTO);

        // Get all the extratoList where ext_numerodocumento equals to UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldNotBeFound("ext_numerodocumento.in=" + UPDATED_EXT_NUMERODOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento is not null
        defaultExtratoShouldBeFound("ext_numerodocumento.specified=true");

        // Get all the extratoList where ext_numerodocumento is null
        defaultExtratoShouldNotBeFound("ext_numerodocumento.specified=false");
    }
                @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento contains DEFAULT_EXT_NUMERODOCUMENTO
        defaultExtratoShouldBeFound("ext_numerodocumento.contains=" + DEFAULT_EXT_NUMERODOCUMENTO);

        // Get all the extratoList where ext_numerodocumento contains UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldNotBeFound("ext_numerodocumento.contains=" + UPDATED_EXT_NUMERODOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerodocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerodocumento does not contain DEFAULT_EXT_NUMERODOCUMENTO
        defaultExtratoShouldNotBeFound("ext_numerodocumento.doesNotContain=" + DEFAULT_EXT_NUMERODOCUMENTO);

        // Get all the extratoList where ext_numerodocumento does not contain UPDATED_EXT_NUMERODOCUMENTO
        defaultExtratoShouldBeFound("ext_numerodocumento.doesNotContain=" + UPDATED_EXT_NUMERODOCUMENTO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole equals to DEFAULT_EXT_NUMEROCONTROLE
        defaultExtratoShouldBeFound("ext_numerocontrole.equals=" + DEFAULT_EXT_NUMEROCONTROLE);

        // Get all the extratoList where ext_numerocontrole equals to UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldNotBeFound("ext_numerocontrole.equals=" + UPDATED_EXT_NUMEROCONTROLE);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole not equals to DEFAULT_EXT_NUMEROCONTROLE
        defaultExtratoShouldNotBeFound("ext_numerocontrole.notEquals=" + DEFAULT_EXT_NUMEROCONTROLE);

        // Get all the extratoList where ext_numerocontrole not equals to UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldBeFound("ext_numerocontrole.notEquals=" + UPDATED_EXT_NUMEROCONTROLE);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole in DEFAULT_EXT_NUMEROCONTROLE or UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldBeFound("ext_numerocontrole.in=" + DEFAULT_EXT_NUMEROCONTROLE + "," + UPDATED_EXT_NUMEROCONTROLE);

        // Get all the extratoList where ext_numerocontrole equals to UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldNotBeFound("ext_numerocontrole.in=" + UPDATED_EXT_NUMEROCONTROLE);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole is not null
        defaultExtratoShouldBeFound("ext_numerocontrole.specified=true");

        // Get all the extratoList where ext_numerocontrole is null
        defaultExtratoShouldNotBeFound("ext_numerocontrole.specified=false");
    }
                @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole contains DEFAULT_EXT_NUMEROCONTROLE
        defaultExtratoShouldBeFound("ext_numerocontrole.contains=" + DEFAULT_EXT_NUMEROCONTROLE);

        // Get all the extratoList where ext_numerocontrole contains UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldNotBeFound("ext_numerocontrole.contains=" + UPDATED_EXT_NUMEROCONTROLE);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_numerocontroleNotContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_numerocontrole does not contain DEFAULT_EXT_NUMEROCONTROLE
        defaultExtratoShouldNotBeFound("ext_numerocontrole.doesNotContain=" + DEFAULT_EXT_NUMEROCONTROLE);

        // Get all the extratoList where ext_numerocontrole does not contain UPDATED_EXT_NUMEROCONTROLE
        defaultExtratoShouldBeFound("ext_numerocontrole.doesNotContain=" + UPDATED_EXT_NUMEROCONTROLE);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito equals to DEFAULT_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.equals=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito equals to UPDATED_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.equals=" + UPDATED_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito not equals to DEFAULT_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.notEquals=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito not equals to UPDATED_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.notEquals=" + UPDATED_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito in DEFAULT_EXT_DEBITO or UPDATED_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.in=" + DEFAULT_EXT_DEBITO + "," + UPDATED_EXT_DEBITO);

        // Get all the extratoList where ext_debito equals to UPDATED_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.in=" + UPDATED_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito is not null
        defaultExtratoShouldBeFound("ext_debito.specified=true");

        // Get all the extratoList where ext_debito is null
        defaultExtratoShouldNotBeFound("ext_debito.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito is greater than or equal to DEFAULT_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.greaterThanOrEqual=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito is greater than or equal to UPDATED_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.greaterThanOrEqual=" + UPDATED_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito is less than or equal to DEFAULT_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.lessThanOrEqual=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito is less than or equal to SMALLER_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.lessThanOrEqual=" + SMALLER_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsLessThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito is less than DEFAULT_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.lessThan=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito is less than UPDATED_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.lessThan=" + UPDATED_EXT_DEBITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_debitoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_debito is greater than DEFAULT_EXT_DEBITO
        defaultExtratoShouldNotBeFound("ext_debito.greaterThan=" + DEFAULT_EXT_DEBITO);

        // Get all the extratoList where ext_debito is greater than SMALLER_EXT_DEBITO
        defaultExtratoShouldBeFound("ext_debito.greaterThan=" + SMALLER_EXT_DEBITO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito equals to DEFAULT_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.equals=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito equals to UPDATED_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.equals=" + UPDATED_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito not equals to DEFAULT_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.notEquals=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito not equals to UPDATED_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.notEquals=" + UPDATED_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito in DEFAULT_EXT_CREDITO or UPDATED_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.in=" + DEFAULT_EXT_CREDITO + "," + UPDATED_EXT_CREDITO);

        // Get all the extratoList where ext_credito equals to UPDATED_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.in=" + UPDATED_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito is not null
        defaultExtratoShouldBeFound("ext_credito.specified=true");

        // Get all the extratoList where ext_credito is null
        defaultExtratoShouldNotBeFound("ext_credito.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito is greater than or equal to DEFAULT_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.greaterThanOrEqual=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito is greater than or equal to UPDATED_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.greaterThanOrEqual=" + UPDATED_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito is less than or equal to DEFAULT_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.lessThanOrEqual=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito is less than or equal to SMALLER_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.lessThanOrEqual=" + SMALLER_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsLessThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito is less than DEFAULT_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.lessThan=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito is less than UPDATED_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.lessThan=" + UPDATED_EXT_CREDITO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_creditoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_credito is greater than DEFAULT_EXT_CREDITO
        defaultExtratoShouldNotBeFound("ext_credito.greaterThan=" + DEFAULT_EXT_CREDITO);

        // Get all the extratoList where ext_credito is greater than SMALLER_EXT_CREDITO
        defaultExtratoShouldBeFound("ext_credito.greaterThan=" + SMALLER_EXT_CREDITO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao equals to DEFAULT_EXT_DESCRICAO
        defaultExtratoShouldBeFound("ext_descricao.equals=" + DEFAULT_EXT_DESCRICAO);

        // Get all the extratoList where ext_descricao equals to UPDATED_EXT_DESCRICAO
        defaultExtratoShouldNotBeFound("ext_descricao.equals=" + UPDATED_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao not equals to DEFAULT_EXT_DESCRICAO
        defaultExtratoShouldNotBeFound("ext_descricao.notEquals=" + DEFAULT_EXT_DESCRICAO);

        // Get all the extratoList where ext_descricao not equals to UPDATED_EXT_DESCRICAO
        defaultExtratoShouldBeFound("ext_descricao.notEquals=" + UPDATED_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoIsInShouldWork() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao in DEFAULT_EXT_DESCRICAO or UPDATED_EXT_DESCRICAO
        defaultExtratoShouldBeFound("ext_descricao.in=" + DEFAULT_EXT_DESCRICAO + "," + UPDATED_EXT_DESCRICAO);

        // Get all the extratoList where ext_descricao equals to UPDATED_EXT_DESCRICAO
        defaultExtratoShouldNotBeFound("ext_descricao.in=" + UPDATED_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao is not null
        defaultExtratoShouldBeFound("ext_descricao.specified=true");

        // Get all the extratoList where ext_descricao is null
        defaultExtratoShouldNotBeFound("ext_descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao contains DEFAULT_EXT_DESCRICAO
        defaultExtratoShouldBeFound("ext_descricao.contains=" + DEFAULT_EXT_DESCRICAO);

        // Get all the extratoList where ext_descricao contains UPDATED_EXT_DESCRICAO
        defaultExtratoShouldNotBeFound("ext_descricao.contains=" + UPDATED_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllExtratoesByExt_descricaoNotContainsSomething() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        // Get all the extratoList where ext_descricao does not contain DEFAULT_EXT_DESCRICAO
        defaultExtratoShouldNotBeFound("ext_descricao.doesNotContain=" + DEFAULT_EXT_DESCRICAO);

        // Get all the extratoList where ext_descricao does not contain UPDATED_EXT_DESCRICAO
        defaultExtratoShouldBeFound("ext_descricao.doesNotContain=" + UPDATED_EXT_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllExtratoesByParceiroIsEqualToSomething() throws Exception {
        // Get already existing entity
        Parceiro parceiro = extrato.getParceiro();
        extratoRepository.saveAndFlush(extrato);
        Long parceiroId = parceiro.getId();

        // Get all the extratoList where parceiro equals to parceiroId
        defaultExtratoShouldBeFound("parceiroId.equals=" + parceiroId);

        // Get all the extratoList where parceiro equals to parceiroId + 1
        defaultExtratoShouldNotBeFound("parceiroId.equals=" + (parceiroId + 1));
    }


    @Test
    @Transactional
    public void getAllExtratoesByAgenciabancariaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Agenciabancaria agenciabancaria = extrato.getAgenciabancaria();
        extratoRepository.saveAndFlush(extrato);
        Long agenciabancariaId = agenciabancaria.getId();

        // Get all the extratoList where agenciabancaria equals to agenciabancariaId
        defaultExtratoShouldBeFound("agenciabancariaId.equals=" + agenciabancariaId);

        // Get all the extratoList where agenciabancaria equals to agenciabancariaId + 1
        defaultExtratoShouldNotBeFound("agenciabancariaId.equals=" + (agenciabancariaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExtratoShouldBeFound(String filter) throws Exception {
        restExtratoMockMvc.perform(get("/api/extratoes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].ext_datalancamento").value(hasItem(sameInstant(DEFAULT_EXT_DATALANCAMENTO))))
            .andExpect(jsonPath("$.[*].ext_historico").value(hasItem(DEFAULT_EXT_HISTORICO)))
            .andExpect(jsonPath("$.[*].ext_numerodocumento").value(hasItem(DEFAULT_EXT_NUMERODOCUMENTO)))
            .andExpect(jsonPath("$.[*].ext_numerocontrole").value(hasItem(DEFAULT_EXT_NUMEROCONTROLE)))
            .andExpect(jsonPath("$.[*].ext_debito").value(hasItem(DEFAULT_EXT_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].ext_credito").value(hasItem(DEFAULT_EXT_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].ext_descricao").value(hasItem(DEFAULT_EXT_DESCRICAO)));

        // Check, that the count call also returns 1
        restExtratoMockMvc.perform(get("/api/extratoes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExtratoShouldNotBeFound(String filter) throws Exception {
        restExtratoMockMvc.perform(get("/api/extratoes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExtratoMockMvc.perform(get("/api/extratoes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingExtrato() throws Exception {
        // Get the extrato
        restExtratoMockMvc.perform(get("/api/extratoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtrato() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        int databaseSizeBeforeUpdate = extratoRepository.findAll().size();

        // Update the extrato
        Extrato updatedExtrato = extratoRepository.findById(extrato.getId()).get();
        // Disconnect from session so that the updates on updatedExtrato are not directly saved in db
        em.detach(updatedExtrato);
        updatedExtrato
            .ext_datalancamento(UPDATED_EXT_DATALANCAMENTO)
            .ext_historico(UPDATED_EXT_HISTORICO)
            .ext_numerodocumento(UPDATED_EXT_NUMERODOCUMENTO)
            .ext_numerocontrole(UPDATED_EXT_NUMEROCONTROLE)
            .ext_debito(UPDATED_EXT_DEBITO)
            .ext_credito(UPDATED_EXT_CREDITO)
            .ext_descricao(UPDATED_EXT_DESCRICAO);
        ExtratoDTO extratoDTO = extratoMapper.toDto(updatedExtrato);

        restExtratoMockMvc.perform(put("/api/extratoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extratoDTO)))
            .andExpect(status().isOk());

        // Validate the Extrato in the database
        List<Extrato> extratoList = extratoRepository.findAll();
        assertThat(extratoList).hasSize(databaseSizeBeforeUpdate);
        Extrato testExtrato = extratoList.get(extratoList.size() - 1);
        assertThat(testExtrato.getExt_datalancamento()).isEqualTo(UPDATED_EXT_DATALANCAMENTO);
        assertThat(testExtrato.getExt_historico()).isEqualTo(UPDATED_EXT_HISTORICO);
        assertThat(testExtrato.getExt_numerodocumento()).isEqualTo(UPDATED_EXT_NUMERODOCUMENTO);
        assertThat(testExtrato.getExt_numerocontrole()).isEqualTo(UPDATED_EXT_NUMEROCONTROLE);
        assertThat(testExtrato.getExt_debito()).isEqualTo(UPDATED_EXT_DEBITO);
        assertThat(testExtrato.getExt_credito()).isEqualTo(UPDATED_EXT_CREDITO);
        assertThat(testExtrato.getExt_descricao()).isEqualTo(UPDATED_EXT_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingExtrato() throws Exception {
        int databaseSizeBeforeUpdate = extratoRepository.findAll().size();

        // Create the Extrato
        ExtratoDTO extratoDTO = extratoMapper.toDto(extrato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtratoMockMvc.perform(put("/api/extratoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extratoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Extrato in the database
        List<Extrato> extratoList = extratoRepository.findAll();
        assertThat(extratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtrato() throws Exception {
        // Initialize the database
        extratoRepository.saveAndFlush(extrato);

        int databaseSizeBeforeDelete = extratoRepository.findAll().size();

        // Delete the extrato
        restExtratoMockMvc.perform(delete("/api/extratoes/{id}", extrato.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Extrato> extratoList = extratoRepository.findAll();
        assertThat(extratoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
