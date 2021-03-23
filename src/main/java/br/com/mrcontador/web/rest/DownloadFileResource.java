package br.com.mrcontador.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.export.ExportLancamento;
import br.com.mrcontador.export.ExportLancamentoFactory;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.InteligentQueryService;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.InteligentCriteria;
import br.com.mrcontador.service.file.S3Service;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

@RestController
@RequestMapping("/api")
public class DownloadFileResource {

	private final Logger log = LoggerFactory.getLogger(DownloadFileResource.class);

	private final S3Service s3Service;
	private final ComprovanteService comprovanteService;
	private final NotafiscalService notafiscalService;
	private final ExtratoService extratoService;
	private final InteligentQueryService inteligentQueryService;
	private final AgenciabancariaService agenciabancariaService;
	private final ParceiroService parceiroService;

	public DownloadFileResource(S3Service s3Service, ComprovanteService comprovanteService,
			NotafiscalService notafiscalService, ExtratoService extratoService,
			InteligentQueryService inteligentQueryService, AgenciabancariaService agenciabancariaService,
			ParceiroService parceiroService) {
		this.s3Service = s3Service;
		this.comprovanteService = comprovanteService;
		this.notafiscalService = notafiscalService;
		this.extratoService = extratoService;
		this.inteligentQueryService = inteligentQueryService;
		this.parceiroService = parceiroService;
		this.agenciabancariaService = agenciabancariaService;
	}

	@GetMapping("/downloadFile/comprovante/{id}")
	public ResponseEntity<Resource> downloadComprovante(@PathVariable Long id) {
		log.info("download comprovante de id {}", id);
		Optional<Comprovante> comprovante = comprovanteService.findOne(id);
		if (comprovante.isEmpty()) {
			log.error("comprovante não encontrado. ID: {} ", id);
			throw new MrContadorException("comprovante.notfound");
		}
		Arquivo arquivo = comprovante.get().getArquivo();
		if (arquivo == null) {
			log.error("arquivo não encontrado. Comprovante ID: {} ", id);
			throw new MrContadorException("arquivo.processing");
		}
		String key = arquivo.gets3Dir() + "/" + arquivo.getNome();
		byte[] file = s3Service.downloadByteArray(key);
		Resource resource = new ByteArrayResource(file);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(arquivo.getTipoArquivo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeOriginal() + "\"")
				.body(resource);
	}

	@GetMapping("/downloadFile/notafiscal/{id}")
	public ResponseEntity<Resource> downloadNotaFiscal(@PathVariable Long id) {
		log.info("download comprovante de id {}", id);
		Optional<Notafiscal> notafiscal = notafiscalService.findOne(id);
		if (notafiscal.isEmpty()) {
			log.error("nota fiscal não encontrado. ID: {} ", id);
			throw new MrContadorException("notafiscal.notfound");
		}
		Arquivo arquivo = notafiscal.get().getArquivoPDF();
		if (arquivo == null) {
			log.error("arquivo não encontrado. Nota fiscal ID: {} ", id);
			throw new MrContadorException("arquivo.processing");
		}
		String key = arquivo.gets3Dir() + "/" + arquivo.getNome();
		byte[] file = s3Service.downloadByteArray(key);
		Resource resource = new ByteArrayResource(file);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(arquivo.getTipoArquivo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeOriginal() + "\"")
				.body(resource);
	}

	@GetMapping("/downloadFile/extrato/{id}")
	public ResponseEntity<Resource> downloadExtrato(@PathVariable Long id) {
		log.info("download comprovante de id {}", id);
		Optional<Extrato> extrato = extratoService.findOne(id);
		if (extrato.isEmpty()) {
			log.error("nota fiscal não encontrado. ID: {} ", id);
			throw new MrContadorException("notafiscal.notfound");
		}
		Arquivo arquivo = extrato.get().getArquivo();
		if (arquivo == null) {
			log.error("arquivo não encontrado. Nota Extrato ID: {} ", id);
			throw new MrContadorException("arquivo.processing");
		}
		String key = arquivo.gets3Dir() + "/" + arquivo.getNome();
		byte[] file = s3Service.downloadByteArray(key);
		Resource resource = new ByteArrayResource(file);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(arquivo.getTipoArquivo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeOriginal() + "\"")
				.body(resource);
	}

	@GetMapping("/downloadFile/lancamento/{periodo}/{agenciabancariaId}/{parceiroId}/{cod_ext}")
	public ResponseEntity<Resource> downloadLancamento(@PathVariable String periodo,@PathVariable Long agenciabancariaId, @PathVariable Long parceiroId, @PathVariable String cod_ext) {
		InteligentCriteria criteria = new InteligentCriteria();
		LongFilter _parceiro = new LongFilter();
		_parceiro.setEquals(parceiroId);
		StringFilter _periodo = new StringFilter();
		_periodo.setEquals(periodo);
		LongFilter _agencia = new LongFilter();
		_agencia.setEquals(agenciabancariaId);
		BooleanFilter _associado = new BooleanFilter();
		_associado.setEquals(true);
		criteria.setParceiroId(_parceiro);
		criteria.setPeriodo(_periodo);
		criteria.setAssociado(_associado);
		criteria.setAgenciabancariaId(_agencia);
		List<Inteligent> inteligents = inteligentQueryService.findByCriteria(criteria);
		Agenciabancaria agencia = agenciabancariaService.findOne(agenciabancariaId).get();
		Parceiro parceiro = parceiroService.findOne(parceiroId).get();
		ExportLancamento lancamento = ExportLancamentoFactory.get(SistemaPlanoConta.DOMINIO_SISTEMAS);
		String result = lancamento.process(inteligents, agencia.getConta());
		Resource resource = new ByteArrayResource(result.getBytes());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"lancamento_" + org.apache.commons.lang3.StringUtils.deleteWhitespace(parceiro.getParRazaosocial())+"_"+periodo + ".txt\"")
				.body(resource);

	}

}
