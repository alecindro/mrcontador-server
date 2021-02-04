package br.com.mrcontador.web.rest;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class UploadFileResource {

	private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

	private final FileService fileService;
	private final ParceiroService parceiroService;
	private final AgenciabancariaService agenciabancariaService;
	private final ContaService contaService;

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	public UploadFileResource(FileService fileService, ParceiroService parceiroService,
			AgenciabancariaService agenciabancariaService, ContaService contaService) {
		this.parceiroService = parceiroService;
		this.fileService = fileService;
		this.agenciabancariaService = agenciabancariaService;
		this.contaService = contaService;
	}

	@PostMapping("/upload/planoconta")
	public ResponseEntity<Void> uploadPlanoConta(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "parceiroId") Long parceiroId) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> oParceiro = parceiroService.findOne(parceiroId);
		if(oParceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notfound");
		}
		try {
			FileDTO dto = fileService.getFileDTO(file.getContentType(), file.getOriginalFilename(), file.getSize(), file.getInputStream(), SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), oParceiro.get(),TipoDocumento.PLANO_DE_CONTA);
			Optional<Conta> oConta = contaService.findFirstByParceiro(oParceiro.get());
			if(oConta.isEmpty()) {
			fileService.processPlanoConta(dto, SistemaPlanoConta.DOMINIO_SISTEMAS);
			} else {
				fileService.updatePlanoConta(dto, SistemaPlanoConta.DOMINIO_SISTEMAS);
			}
			return ResponseEntity
					.created(new URI("/api/upload/planoconta/")).headers(HeaderUtil
							.createEntityCreationAlert(applicationName, true, "uploadPlanoConta", file.getName()))
					.build();
		} catch (MrContadorException e) {
			throw e;
		}
	}

	@PutMapping("/upload/planoconta")
	public ResponseEntity<Void> updatePlanoConta(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "parceiroId") Long parceiroId) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> oParceiro = parceiroService.findOne(parceiroId);
		if(oParceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notfound");
		}
		try {
			FileDTO dto = fileService.getFileDTO(file.getContentType(), file.getOriginalFilename(), file.getSize(), file.getInputStream(), SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), oParceiro.get(),TipoDocumento.PLANO_DE_CONTA );
			fileService.updatePlanoConta(dto, SistemaPlanoConta.DOMINIO_SISTEMAS);
			return ResponseEntity
					.created(new URI("/api/upload/planoconta/")).headers(HeaderUtil
							.createEntityCreationAlert(applicationName, true, "uploadPlanoConta", file.getName()))
					.build();
		} catch (MrContadorException e) {
			throw e;
		}
	}

	@PostMapping("/upload/extrato")
	public ResponseEntity<String> uploadExtrato(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro,
			@RequestParam(required = true, name = "idAgenciabancaria") Long idAgencia) throws Exception {
		log.info("Processando Extrato: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> oParceiro = parceiroService.findOne(idParceiro);
		if(oParceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notfound");
		}
		Optional<Agenciabancaria> agencia = agenciabancariaService.findOne(idAgencia);
		if(agencia.isEmpty()) {
			throw new MrContadorException("agencia.notfound");
		}
		try {
			String contentType = file.getContentType();

			FileDTO fileDTO = fileService.getFileDTO(file.getContentType(), file.getOriginalFilename(), file.getSize(), file.getInputStream(), SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), oParceiro.get(),TipoDocumento.EXTRATO);
			fileDTO.setIdAgencia(idAgencia);	
			String periodo = fileService.processExtrato(fileDTO, agencia.get(), contentType);
			return ResponseEntity.created(new URI("/api/upload/extrato/")).body(periodo);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new MrContadorException("extrato.imported");
		}
	}

	@PostMapping("/upload/comprovante")
	public ResponseEntity<String> uploadComprovante(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro,
			@RequestParam(required = true, name = "idAgenciabancaria") Long idAgencia) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> parceiro = parceiroService.findOne(idParceiro);
		if (parceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notfound");
		}
		Optional<Agenciabancaria> agencia = agenciabancariaService.findOne(idAgencia);
		if (agencia.isEmpty()) {
			throw new MrContadorException("agencia.notfound");
		}
		
			FileDTO fileDTO = fileService.getFileDTO(file.getContentType(), file.getOriginalFilename(), file.getSize(), file.getInputStream(), SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), parceiro.get(),TipoDocumento.COMPROVANTE);
			fileDTO.setIdAgencia(idAgencia);			
			String periodo = fileService.processComprovante(fileDTO, agencia.get());
			return ResponseEntity
					.created(new URI("/api/upload/comprovante/")).headers(HeaderUtil
							.createEntityCreationAlert(applicationName, true, "uploadComprovante", file.getName())).body(periodo);					
		
	}

	@PostMapping("/upload/nf")
	public ResponseEntity<String> uploadNF(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro) throws Exception {
		log.info("Processando arquivo: {}. Contador: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> parceiro = parceiroService.findOne(idParceiro);
		if (parceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notfound");
		}
		try {
			FileDTO dto = fileService.getFileDTO(file.getContentType(), file.getOriginalFilename(), file.getSize(), file.getInputStream(), SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), parceiro.get(),TipoDocumento.NOTA);
			String periodo = fileService.processNFE(dto);
			return ResponseEntity.created(new URI("/api/uploadplanoconta/"))
					.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "uploadNF", file.getName())).body(periodo);
		} catch (MrContadorException e) {
			throw e;
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new MrContadorException("nfe.imported");
		}
	}

	@PostMapping("/upload/ns")
	public ResponseEntity<Void> uploadNS(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		throw new MrContadorException("error.notimplemented", "parametro");
	}

	@GetMapping("/upload/teste")
	public ResponseEntity<Void> uploadTeste() throws Exception {
		log.info("testando erro");
		// BadRequestAlertException
		throw new MrContadorException("error.notimplemented", "parametro");

	}

}
