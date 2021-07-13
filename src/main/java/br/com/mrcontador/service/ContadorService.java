package br.com.mrcontador.service;

import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.config.liquibase.LiquibaseMultiTenantcy;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Authority;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.domain.User;
import br.com.mrcontador.repository.AuthorityRepository;
import br.com.mrcontador.repository.ContadorRepository;
import br.com.mrcontador.security.AuthoritiesConstants;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.service.dto.UserDTO;
import br.com.mrcontador.service.dto.UserPermissaoDTO;
import br.com.mrcontador.service.mapper.ContadorMapper;
import liquibase.integration.spring.SpringLiquibase;

/**
 * Service Implementation for managing {@link Contador}.
 */
@Service
@Transactional
public class ContadorService {

    private final Logger log = LoggerFactory.getLogger(ContadorService.class);

    private final ContadorRepository contadorRepository;

    private final ContadorMapper contadorMapper;
    
    private final DataSource dataSource; 
    
    private final AuthorityRepository authorityRepository;
    
    private final UserService userService;
    
    private final MailService mailService;
    
    private final LiquibaseMultiTenantcy springLiquibase;

    public ContadorService(ContadorRepository contadorRepository, 
    		AuthorityRepository authorityRepository, 
    		UserService userService,
    		MailService mailService,
    		SpringLiquibase springLiquibase,
    		ContadorMapper contadorMapper, DataSource dataSource) {
        this.contadorRepository = contadorRepository;
        this.authorityRepository = authorityRepository;
        this.contadorMapper = contadorMapper;
        this.dataSource = dataSource;
        this.userService = userService;
        this.mailService = mailService;
        this.springLiquibase = (LiquibaseMultiTenantcy) springLiquibase;
    }

    /**
     * Save a contador.
     *
     * @param contadorDTO the entity to save.
     * @return the persisted entity.
     * @throws Exception 
     */
    @Transactional
    public ContadorDTO save(ContadorDTO contadorDTO) throws Exception {
    	log.debug("Request to save Contador : {}", contadorDTO);
        Contador contador = contadorMapper.toEntity(contadorDTO);
        String datasource = SecurityUtils.DS_PREFIX.concat(contador.getCnpj().replaceAll("\\D", ""));
  
        springLiquibase.createSchema(datasource, dataSource);
        contador.setDatasource(datasource);
        contador = contadorRepository.save(contador);
        authorityRepository.save(new Authority(contador.getDatasource()));
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(contador.getEmail());
        userDTO.setLogin(contador.getEmail());
        userDTO.setFirstName("administrador");
        userDTO.setLangKey("pt-br");
        userDTO.setDatasource(contador.getDatasource());
        userDTO.getAuthorities().add(contador.getDatasource());
        userDTO.getAuthorities().add(AuthoritiesConstants.ADMIN);
        userDTO.getAuthorities().add(AuthoritiesConstants.USER);
        UserPermissaoDTO UserPermissaoDTO = new UserPermissaoDTO(userDTO, null); 
        User user = userService.createUser(UserPermissaoDTO);
        mailService.sendCreationEmail(user);
        return contadorMapper.toDto(contador);
    }
    
    public ContadorDTO update(ContadorDTO contadorDTO) {
    	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
        log.debug("Request to save Contador : {}", contadorDTO);
        Contador contador = contadorMapper.toEntity(contadorDTO);
        contador = contadorRepository.save(contador);
        return contadorMapper.toDto(contador);
    }

    /**
     * Get all the contadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContadorDTO> findAll(Pageable pageable) {
    	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
        log.debug("Request to get all Contadors");
        return contadorRepository.findAll(pageable)
            .map(contadorMapper::toDto);
    }


    /**
     * Get one contador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContadorDTO> findOne(Long id) {
    	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
        log.debug("Request to get Contador : {}", id);
        return contadorRepository.findById(id)
            .map(contadorMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<ContadorDTO> findOneByDatasource(String datasource) {
    	log.debug("Request to get Contador : {}", datasource);
        return contadorRepository.findOneByDatasource(datasource)
            .map(contadorMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<Contador> findByDatasource(String datasource) {
    	log.debug("Request to get Contador : {}", datasource);
        return contadorRepository.findOneByDatasource(datasource);
    }

    /**
     * Delete the contador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
    	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
        log.debug("Request to delete Contador : {}", id);
        contadorRepository.deleteById(id);
    }
    
    public Optional<Contador> findOneByCnpjOrCrc(String cnpj, String crc){
    	return contadorRepository.findOneByCnpjOrCrc(cnpj, crc);
    }
}
