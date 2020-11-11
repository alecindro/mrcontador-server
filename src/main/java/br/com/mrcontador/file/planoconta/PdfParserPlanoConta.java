package br.com.mrcontador.file.planoconta;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.PlanoContaMapper;
import br.com.mrcontador.util.MrContadorUtil;
import br.com.mrcontador.web.rest.errors.CnpjAlreadyExistException;

@Service
public class PdfParserPlanoConta {

	@Autowired
	private S3Service s3Service;
	@Autowired
	private ContaService contaService;
	@Autowired
	private ParceiroService parceiroService;

	public Parceiro process(FileDTO dto, SistemaPlanoConta sistemaPlanoConta, String parceiroCnpj) {
		PDDocument document;
		InputStream first = null;
		try {

			first = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			PlanoConta planoConta = parsePlanoConta(sistemaPlanoConta, document);
			if (parceiroCnpj != null) {
				if (!MrContadorUtil.onlyNumbers(parceiroCnpj)
						.equals(MrContadorUtil.onlyNumbers(planoConta.getCnpjCliente()))) {
					throw new MrContadorException("file.notparceiro", parceiroCnpj);
				}
			}
			Parceiro parceiro = parceiroService.saveByServiceCnpj(planoConta.getCnpjCliente());
			dto.setParceiro(parceiro);
			PlanoContaMapper mapper = new PlanoContaMapper();
			List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), parceiro, planoConta.getArquivo());
			contaService.save(contas);
			Arquivo arquivo = s3Service.uploadPlanoConta(dto);
			contaService.updateArquivo(arquivo.getId());
			return parceiro;
		} catch (CnpjAlreadyExistException e) {
			throw e;
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			s3Service.uploadErro(dto);
			throw new MrContadorException("planodeconta.parse.error", e.getMessage(), e);
		} finally {
			try {
				if (first != null) {
					first.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void update(FileDTO dto, SistemaPlanoConta sistemaPlanoConta, Long parceiroId) {
		PDDocument document;
		InputStream first = null;
		try {
			first = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			PlanoConta planoConta = parsePlanoConta(sistemaPlanoConta, document);
			Optional<Parceiro> _parceiro = parceiroService.findOne(parceiroId);
			if (_parceiro.isEmpty()) {
				throw new MrContadorException("file.parceirorequired");
			}
			Parceiro parceiro = _parceiro.get();
			if (parceiro.getId() != parceiroId) {
				throw new MrContadorException("file.notparceiro");
			}
			dto.setParceiro(parceiro);
			Arquivo arquivo = s3Service.uploadPlanoConta(dto);
			PlanoContaMapper mapper = new PlanoContaMapper();
			List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), parceiro, planoConta.getArquivo());
			contaService.update(contas, dto.getUsuario(), arquivo, parceiro);
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			s3Service.uploadErro(dto);
			throw new MrContadorException("planodeconta.parse.error", e.getMessage(), e);
		} finally {
			try {
				if (first != null) {
					first.close();
				}
			} catch (IOException e) {

			}
		}
	}

	private PlanoConta parsePlanoConta(SistemaPlanoConta sistemaPlanoConta, PDDocument document) throws IOException {
		PdfReader reader = getIdentifier(sistemaPlanoConta);
		Splitter splitter = new Splitter();
		List<PDDocument> pages = splitter.split(document);
		return reader.process(pages);
	}

	private PdfReader getIdentifier(SistemaPlanoConta sistemaPlanoConta) throws IOException {
		switch (sistemaPlanoConta) {
		case DOMINIO_SISTEMAS:
			return new PdfPlanoContaDominio();

		default:
			throw new RuntimeException("pdf.systemnotimplemented");
		}
	}

}
