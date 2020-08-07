package br.com.mrcontador.web.rest;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ParceiroService;
import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class UploadFileResource {

	private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

	private final FileService fileService;
	private final ParceiroService parceiroService;
	private final AgenciabancariaService agenciabancariaService; 

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	public UploadFileResource(FileService fileService, ParceiroService parceiroService, AgenciabancariaService agenciabancariaService) {
		this.parceiroService = parceiroService;
		this.fileService = fileService;
		this.agenciabancariaService = agenciabancariaService;
	}

	@PostMapping("/upload/planoconta")
	public ResponseEntity<Parceiro> uploadPlanoConta(@RequestParam("file") MultipartFile file,
			@RequestParam(required = false, name = "parceiroCNPJ") String parceiroCnpj) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		try {
			Parceiro parceiro = fileService.processPlanoConta(file, SecurityUtils.getCurrentUserLogin(),
					SecurityUtils.getCurrentTenantHeader(), parceiroCnpj, SistemaPlanoConta.DOMINIO_SISTEMAS);
			return ResponseEntity
					.created(new URI("/api/upload/planoconta/")).headers(HeaderUtil
							.createEntityCreationAlert(applicationName, true, "uploadPlanoConta", file.getName()))
					.body(parceiro);
		} catch (MrContadorException e) {
			throw e;
		}
	}

	@PostMapping("/upload/extrato")
	public ResponseEntity<Void> uploadExtrato(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro,
			@RequestParam(required = true, name = "idAgenciabancaria") Long idAgencia) throws Exception {
		log.info("Processando Extrato: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> parceiro = parceiroService.findOne(idParceiro);
		Optional<Agenciabancaria> agencia = agenciabancariaService.findOne(idAgencia);
		fileService.processExtrato(file, SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentTenantHeader(), parceiro, agencia);
		return ResponseEntity
				.created(new URI("/api/upload/extrato/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, "uploadExtrato", file.getName()))
				.build();
	}

	@PostMapping("/upload/comprovante")
	public ResponseEntity<Void> uploadComprovante(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro,
			@RequestParam(required = true, name = "idAgenciabancaria") Long idAgencia) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> parceiro = parceiroService.findOne(idParceiro);
		Optional<Agenciabancaria> agencia = agenciabancariaService.findOne(idAgencia);
		fileService.processComprovante(file, SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentTenantHeader(), parceiro, agencia);
		return ResponseEntity
				.created(new URI("/api/upload/comprovante/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, "uploadComprovante", file.getName()))
				.build();
	}

	@PostMapping("/upload/nf")
	public ResponseEntity<Void> uploadNF(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro) throws Exception {
		log.info("Processando arquivo: {}. Contador: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		Optional<Parceiro> parceiro = parceiroService.findOne(idParceiro);
		try {
			fileService.processNFE(file, SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentTenantHeader(), parceiro);
			return ResponseEntity
					.created(new URI("/api/uploadplanoconta/")).headers(HeaderUtil
							.createEntityCreationAlert(applicationName, true, "uploadPlanoConta", file.getName()))
					.build();
		} catch (MrContadorException e) {
			throw e;
		} catch(org.springframework.dao.DataIntegrityViolationException e) {
			throw new MrContadorException("nfe.imported");
		}
	}

	@PostMapping("/upload/ns")
	public ResponseEntity<Void> uploadNS(@RequestParam("file") MultipartFile file,
			@RequestParam(required = true, name = "idParceiro") Long idParceiro) throws Exception {
		log.info("Processando arquivo: {}. Cliente: {}", file.getName(), SecurityUtils.getCurrentTenantHeader());
		throw new MrContadorException("error.notimplemented");
	}

}
